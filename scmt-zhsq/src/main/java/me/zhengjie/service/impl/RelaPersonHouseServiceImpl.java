package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.mapper.RelaPersonHouseMapper;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.RelaPersonHouse;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.IRelaPersonHouseService;
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
public class RelaPersonHouseServiceImpl extends ServiceImpl<RelaPersonHouseMapper, RelaPersonHouse> implements IRelaPersonHouseService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final RelaPersonHouseMapper relaPersonHouseMapper;


    public List<BasicHousing> getRelaPersonHouseByPid(String pid){
        return relaPersonHouseMapper.getRelaPersonHouseByPid(pid);
    }

    public Result<Object> getRelaPersonHouseById(String id){
        RelaPersonHouse relaPersonHouse = relaPersonHouseMapper.selectById(id);
        if(relaPersonHouse!=null){
            return  ResultUtil.data(relaPersonHouse);
        }
        return  ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryRelaPersonHouseListByPage(RelaPersonHouse  relaPersonHouse, SearchVo searchVo, PageVo pageVo){
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
        Page<RelaPersonHouse> pageData = new Page<>(page, limit);
        QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
        if (relaPersonHouse !=null) {
            queryWrapper = LikeAllFeild(relaPersonHouse,searchVo);
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
        IPage<RelaPersonHouse> result = relaPersonHouseMapper.selectPage(pageData, queryWrapper);
        return  ResultUtil.data(result);
    }
    @Override
    public void download(RelaPersonHouse relaPersonHouse, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
        if (relaPersonHouse !=null) {
            queryWrapper = LikeAllFeild(relaPersonHouse,null);
        }
        List<RelaPersonHouse> list = relaPersonHouseMapper.selectList(queryWrapper);
        for (RelaPersonHouse re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("人员Id", re.getPersonId());
            map.put("房屋id", re.getHouseId());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     * @param relaPersonHouse 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<RelaPersonHouse>  LikeAllFeild(RelaPersonHouse  relaPersonHouse, SearchVo searchVo) {
        QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
        if(relaPersonHouse.getId() != null){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getId, relaPersonHouse.getId()));
        }
        if(StringUtils.isNotBlank(relaPersonHouse.getPersonId())){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getPersonId, relaPersonHouse.getPersonId()));
        }
        if(StringUtils.isNotBlank(relaPersonHouse.getHouseId())){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getHouseId, relaPersonHouse.getHouseId()));
        }
        if(relaPersonHouse.getCreateId() != null){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getCreateId, relaPersonHouse.getCreateId()));
        }
        if(relaPersonHouse.getCreateTime() != null){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getCreateTime, relaPersonHouse.getCreateTime()));
        }
        if(relaPersonHouse.getUpdateId() != null){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getUpdateId, relaPersonHouse.getUpdateId()));
        }
        if(relaPersonHouse.getUpdateTime() != null){
            queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getUpdateTime, relaPersonHouse.getUpdateTime()));
        }
        if(searchVo!=null){
            if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                queryWrapper.lambda().and(i -> i.between(RelaPersonHouse::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
            }
        }
        return queryWrapper;

    }
}
