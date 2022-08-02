package me.zhengjie.service.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.mapper.task.*;
import me.zhengjie.entity.task.TaskCategory;
import me.zhengjie.entity.task.TaskManager;
import me.zhengjie.entity.task.TaskScoreDetails;
import me.zhengjie.entity.task.TaskTimelyScore;
import me.zhengjie.service.task.ITaskCategoryService;
import me.zhengjie.service.task.ITaskScoreService;
import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljj
 */
@Service
@AllArgsConstructor
public class TaskScoreServiceImpl implements ITaskScoreService {

    private final TaskScoreMapper taskScoreMapper;

    private final TaskScoreDetailsMapper taskScoreDetailsMapper;
    
    private final TaskTimelyScoreMapper taskTimelyScoreMapper;

    private final TaskCategoryMapper taskCategoryMapper;

    private final ITaskCategoryService taskCategoryService;


    @Override
    public void modify(TaskCategory taskCategory) {
        List<TaskScoreDetails> taskScoreDetails = taskCategory.getTaskScoreDetails();
        // 先删除
        taskScoreDetailsMapper.delete(new QueryWrapper<TaskScoreDetails>().eq("master_id",taskCategory.getId()));
        if (!taskScoreDetails.isEmpty()){
            for (TaskScoreDetails taskScoreDetail : taskScoreDetails) {
                taskScoreDetail.setMasterId(taskCategory.getId());
                taskScoreDetailsMapper.insert(taskScoreDetail);
            }
        }
        taskCategoryMapper.updateById(taskCategory);
    }

    @Override
    public TaskCategory loadOne(Integer taskCategoryId) {
        TaskCategory taskCategory = taskCategoryMapper.selectById(taskCategoryId);
        List<TaskScoreDetails> taskScoreDetails = taskScoreDetailsMapper.selectList(new QueryWrapper<TaskScoreDetails>().eq("master_id", taskCategoryId));
        taskCategory.setTaskScoreDetails(taskScoreDetails);
        return taskCategory;
    }

    @Override
    public IPage<Map<String,Object>> loadAllByQuery(JSONObject query) {
        Page<Map<String,Object>> page = new Page<>(query.getLongValue("pageNum"),query.getLongValue("pageSize"));
        return taskScoreMapper.selectByQuery(page,query);
    }

    @Override
    public List<TaskScoreDetails> loadAllDetailsByMaster(Integer masterId){
        QueryWrapper<TaskScoreDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskScoreDetails::getMasterId,masterId);
        return taskScoreDetailsMapper.selectList(queryWrapper);
    }

    @Override
    public JSONObject loadAllDetailsByCode(JSONObject param) {
        TaskCategory taskCategory = null;
        if (null != param.get("categoryId")){
            taskCategory = taskCategoryMapper.selectById(param.getInteger("categoryId"));
        }else {
            QueryWrapper<TaskCategory> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.lambda().eq(TaskCategory::getCode, param.getString("code"));
            categoryQueryWrapper.lambda().eq(TaskCategory::getGridId,param.getInteger("gridId"));
            taskCategory = taskCategoryMapper.selectOne(categoryQueryWrapper);
        }
        if (taskCategory == null){
            throw new BusinessErrorException("请到后台管理系统设置该工单类型的明细分值");
        }
        QueryWrapper<TaskScoreDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskScoreDetails::getMasterId,taskCategory.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("countScore",taskCategory.getCountScore());
        jsonObject.put("details",taskScoreDetailsMapper.selectList(queryWrapper));
        return jsonObject;
    }

    @Override
    public void deleteDetails(Integer taskScoreDetailsIs){
        taskScoreDetailsMapper.deleteById(taskScoreDetailsIs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateDetails(List<TaskScoreDetails> taskScoreDetails) {
        taskScoreDetails.forEach(o ->{
            if (null == o.getId()){
                taskScoreDetailsMapper.insert(o);
            }else {
                taskScoreDetailsMapper.updateById(o);
            }
        });
    }

    @Override
    public Map<String,Object> loadTimely() {
        TaskTimelyScore taskTimelyScore = taskTimelyScoreMapper.selectById(1);
        Map<String,Object> result = new HashMap<>();
        if (null != taskTimelyScore.getId()){
            result.put("id",taskTimelyScore.getId());
        }

        Map<String,Object> grabOrderFirstModel = new HashMap<>();
        grabOrderFirstModel.put("time",taskTimelyScore.getGrabOrderFirst());
        grabOrderFirstModel.put("score",taskTimelyScore.getGrabOrderFirstScore());
        grabOrderFirstModel.put("status",taskTimelyScore.getGrabOrderFirstStatus());
        grabOrderFirstModel.put("num",taskTimelyScore.getGrabOrderFirstNum());
        Map<String,Object> grabOrderLastModel = new HashMap<>();
        grabOrderLastModel.put("time",taskTimelyScore.getGrabOrderLast());
        grabOrderLastModel.put("score",taskTimelyScore.getGrabOrderLastScore());
        grabOrderLastModel.put("status",taskTimelyScore.getGrabOrderLastStatus());
        grabOrderLastModel.put("num",taskTimelyScore.getGrabOrderLastNum());
        Map<String,Object> completeOrderModel = new HashMap<>();
        completeOrderModel.put("time",taskTimelyScore.getCompleteOrder());
        completeOrderModel.put("score",taskTimelyScore.getCompleteOrderScore());
        completeOrderModel.put("status",taskTimelyScore.getCompleteOrderStatus());
        completeOrderModel.put("num",taskTimelyScore.getCompleteOrderNum());
        result.put("grabOrderFirst",grabOrderFirstModel);
        result.put("grabOrderLast",grabOrderLastModel);
        result.put("completeOrder",completeOrderModel);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateTimely(TaskTimelyScore taskTimelyScore) {

        if(taskTimelyScore.getGrabOrderFirstScore().compareTo(taskTimelyScore.getGrabOrderLastScore()) !=1 ){
            throw new BusinessErrorException("最迟接单分值要比及时接单分值低哦");
        }else if(taskTimelyScore.getGrabOrderFirst().compareTo(taskTimelyScore.getGrabOrderLast()) != -1 ){
            throw new BusinessErrorException("最迟接单时间不能比及时接单时间低哦");
        }
        Double systemTotalScore = 0.0;
        // 先判断首次接单状态
        if (taskTimelyScore.getGrabOrderFirstStatus() == 1){
            systemTotalScore += taskTimelyScore.getGrabOrderFirstScore();
        }else if (taskTimelyScore.getGrabOrderLastStatus() == 1){ // 再判断最后接单状态
            systemTotalScore += taskTimelyScore.getGrabOrderLastScore();
        }
        if (taskTimelyScore.getCompleteOrderStatus() == 1){
            systemTotalScore += taskTimelyScore.getCompleteOrderScore();
        }

        List<TaskCategory> taskCategories = taskCategoryMapper.selectList(new QueryWrapper<>());
        for (TaskCategory taskCategory : taskCategories) {
            Double detailsTotalScore = 0.0;
            List<TaskScoreDetails> taskScoreDetails = taskScoreDetailsMapper.selectList(new QueryWrapper<TaskScoreDetails>().lambda().eq(TaskScoreDetails::getMasterId, taskCategory.getId()));
            if (taskScoreDetails.isEmpty()){
                continue;
            }
            for (TaskScoreDetails taskScoreDetail : taskScoreDetails) {
                detailsTotalScore += taskScoreDetail.getScore();
            }
            taskCategory.setCountScore(systemTotalScore+detailsTotalScore);
        }
        taskCategoryService.updateBatchById(taskCategories);
        if (null != taskTimelyScore.getId()){
            taskTimelyScoreMapper.updateById(taskTimelyScore);
        }else {
            taskTimelyScoreMapper.insert(taskTimelyScore);
        }
    }

}
