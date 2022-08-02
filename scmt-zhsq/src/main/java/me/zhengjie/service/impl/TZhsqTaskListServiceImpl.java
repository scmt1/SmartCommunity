package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.TZhsqTaskListMapper;
import me.zhengjie.entity.TZhsqTaskList;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqTaskListService;
import me.zhengjie.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.LinkedHashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
/**
 *@author
 **/
@Service
@AllArgsConstructor
public class TZhsqTaskListServiceImpl extends ServiceImpl<TZhsqTaskListMapper, TZhsqTaskList> implements ITZhsqTaskListService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqTaskListMapper tZhsqTaskListMapper;

    @Override
    public Result<Object> getTZhsqTaskListById(String id){
        TZhsqTaskList tZhsqTaskList = tZhsqTaskListMapper.selectById(id);
        if(tZhsqTaskList!=null){
            return  ResultUtil.data(tZhsqTaskList);
        }
        return  ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTZhsqTaskListListByPage(TZhsqTaskList  tZhsqTaskList, SearchVo searchVo, PageVo pageVo){
        int page = 1;
        int limit = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
        }
        Page<TZhsqTaskList> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqTaskList> queryWrapper = new QueryWrapper<>();
        if (tZhsqTaskList !=null) {
            queryWrapper = LikeAllFeild(tZhsqTaskList,searchVo);
        }
        if(pageVo.getSort()!=null){
            if(pageVo.getSort().equals("asc")){
                queryWrapper.orderByAsc(pageVo.getSort());
            }
            else{
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        }
        else{
            queryWrapper.orderByDesc("create_time");
        }
        IPage<TZhsqTaskList> result = tZhsqTaskListMapper.selectPage(pageData, queryWrapper);
        return  ResultUtil.data(result);
    }
    @Override
    public void download(TZhsqTaskList tZhsqTaskList, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqTaskList> queryWrapper = new QueryWrapper<>();
        if (tZhsqTaskList !=null) {
            queryWrapper = LikeAllFeild(tZhsqTaskList,null);
        }
        List<TZhsqTaskList> list = tZhsqTaskListMapper.selectList(queryWrapper);
        for (TZhsqTaskList re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务分类", re.getTaskSort());
            map.put("任务名称", re.getTaskName());
            map.put("所属社区", re.getCommunity());
            map.put("所属网格", re.getGrid());
            map.put("发起人", re.getSponsor());
            map.put("任务开始时间", re.getTaskStartingTime());
            map.put("任务结束时间", re.getTaskEndTime());
            map.put("执行人", re.getExecutor());
            map.put("任务状态", re.getTaskStatus());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     * @param tZhsqTaskList 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqTaskList>  LikeAllFeild(TZhsqTaskList  tZhsqTaskList, SearchVo searchVo) {
        QueryWrapper<TZhsqTaskList> queryWrapper = new QueryWrapper<>();
        if(tZhsqTaskList.getId() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getId, tZhsqTaskList.getId()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getTaskSort())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getTaskSort, tZhsqTaskList.getTaskSort()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getTaskName())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getTaskName, tZhsqTaskList.getTaskName()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getCommunity())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getCommunity, tZhsqTaskList.getCommunity()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getGrid())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getGrid, tZhsqTaskList.getGrid()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getSponsor())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getSponsor, tZhsqTaskList.getSponsor()));
        }
        if(tZhsqTaskList.getTaskStartingTime() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getTaskStartingTime, tZhsqTaskList.getTaskStartingTime()));
        }
        if(tZhsqTaskList.getTaskEndTime() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getTaskEndTime, tZhsqTaskList.getTaskEndTime()));
        }
        if(StringUtils.isNotBlank(tZhsqTaskList.getExecutor())){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getExecutor, tZhsqTaskList.getExecutor()));
        }
        if(tZhsqTaskList.getTaskStatus() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getTaskStatus, tZhsqTaskList.getTaskStatus()));
        }
        if(tZhsqTaskList.getCreateTime() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getCreateTime, tZhsqTaskList.getCreateTime()));
        }
        if(tZhsqTaskList.getCreateId() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getCreateId, tZhsqTaskList.getCreateId()));
        }
        if(tZhsqTaskList.getUpdateTime() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getUpdateTime, tZhsqTaskList.getUpdateTime()));
        }
        if(tZhsqTaskList.getUpdateId() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getUpdateId, tZhsqTaskList.getUpdateId()));
        }
        if(tZhsqTaskList.getIsDelete() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getIsDelete, tZhsqTaskList.getIsDelete()));
        }
        if(tZhsqTaskList.getPosition() != null){
            queryWrapper.lambda().and(i -> i.like(TZhsqTaskList::getPosition, tZhsqTaskList.getPosition()));
        }
        if(searchVo!=null){//查询结束时间段
            if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                queryWrapper.lambda().and(i -> i.between(TZhsqTaskList::getTaskEndTime, searchVo.getStartDate(),searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqTaskList::getIsDelete, 0));
        return queryWrapper;

    }
}
