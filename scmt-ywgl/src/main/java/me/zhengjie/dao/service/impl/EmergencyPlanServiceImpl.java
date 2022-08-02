package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.EmergencyPlan;
import me.zhengjie.dao.mapper.EmergencyPlanMapper;
import me.zhengjie.dao.service.IEmergencyPlanService;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.FileUtil;
import javax.servlet.http.HttpServletResponse;
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
public class EmergencyPlanServiceImpl extends ServiceImpl<EmergencyPlanMapper, EmergencyPlan> implements IEmergencyPlanService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private EmergencyPlanMapper emergencyPlanMapper;

    public Result<Object> getEmergencyPlanById(String id){
        EmergencyPlan emergencyPlan = emergencyPlanMapper.selectById(id);
        if(emergencyPlan!=null){
            return  ResultUtil.data(emergencyPlan);
        }
        return  ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryEmergencyPlanListByPage(String search, SearchVo searchVo, PageVo pageVo){
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
        Page<EmergencyPlan> pageData = new Page<>(page, limit);
        QueryWrapper<EmergencyPlan> queryWrapper = new QueryWrapper<>();
        //应急预案分类查找
        queryWrapper = LikeAllFeild(search);


        IPage<EmergencyPlan> result = emergencyPlanMapper.selectPage(pageData, queryWrapper);
        return  ResultUtil.data(result);
    }
    @Override
    public void download(String likeValue, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<EmergencyPlan> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(likeValue)) {
            queryWrapper = LikeAllFeild(likeValue);
        }
        List<EmergencyPlan> list = emergencyPlanMapper.selectList(queryWrapper);
        for (EmergencyPlan re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("预案名称", re.getEmergName());
            map.put("预案制定日期", re.getEmergMakeDate());
            map.put("预案方案坐标点", re.getEmergMap());
            map.put("预案类型", re.getEmergType());
            map.put("预案等级", re.getEmergLevel());
            map.put("预案制定单位", re.getEmergMakeDept());
            map.put("预案执行部门", re.getEmergImplDept());
            map.put("预案制定人员", re.getEmergMakePerson());
            map.put("方案描述", re.getEmergDescription());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     * @param likeValue 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<EmergencyPlan>  LikeAllFeild(String likeValue) {
        QueryWrapper<EmergencyPlan> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getId, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergName, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMakeDate, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMap, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergType, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergLevel, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMakeDept, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMakeDeptid, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergImplDept, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergImplDeptid, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMakePerson, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergMakePersonid, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getCreateId, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getCreateTime, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getUpdateId, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getUpdateTime, likeValue));
        queryWrapper.lambda().or(i -> i.like(EmergencyPlan::getEmergDescription, likeValue));
        queryWrapper.lambda().and(i -> i.eq(EmergencyPlan::getDelFlag, 1));
        return queryWrapper;
    }
}
