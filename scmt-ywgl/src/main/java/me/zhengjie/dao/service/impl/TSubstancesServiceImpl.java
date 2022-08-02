package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TSubstances;
import me.zhengjie.dao.mapper.TSubstancesMapper;
import me.zhengjie.dao.service.ITSubstancesService;
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
 * @author
 **/
@Service
public class TSubstancesServiceImpl extends ServiceImpl<TSubstancesMapper, TSubstances> implements ITSubstancesService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TSubstancesMapper tSubstancesMapper;

    public Result<Object> getTSubstancesById(String id) {
        TSubstances tSubstances = tSubstancesMapper.selectById(id);
        if (tSubstances != null) {
            return ResultUtil.data(tSubstances);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTSubstancesListByPage(TSubstances tSubstances, SearchVo searchVo, PageVo pageVo) {
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
        Page<TSubstances> pageData = new Page<>(page, limit);
        QueryWrapper<TSubstances> queryWrapper = new QueryWrapper<>();
        if (tSubstances != null) {
            queryWrapper = LikeAllFeild(tSubstances,searchVo);
        }
        IPage<TSubstances> result = tSubstancesMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TSubstances tSubstances, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TSubstances> queryWrapper = new QueryWrapper<>();
        if (tSubstances != null) {
            queryWrapper = LikeAllFeild(tSubstances,null);
        }

        List<TSubstances> list = tSubstancesMapper.selectList(queryWrapper);
        for (TSubstances re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("物质名称", re.getName());
            map.put("规格型号", re.getSpecificationModel());
            map.put("标准", re.getStandard());
            map.put("数量", re.getNumber());
            map.put("单位", re.getUnit());
            map.put("所属单位", re.getAffiliatedUnit());
            map.put("存放位置", re.getParkingPosition());
            map.put("地图位置", re.getMapLocation());
            map.put("备注", re.getRemark());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tSubstances 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TSubstances> LikeAllFeild(TSubstances tSubstances, SearchVo searchVo) {
        QueryWrapper<TSubstances> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tSubstances.getId())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getId, tSubstances.getId()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getCode())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getCode, tSubstances.getCode()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getSpecificationModel())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getSpecificationModel, tSubstances.getSpecificationModel()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getStandard())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getStandard, tSubstances.getStandard()));
        }
        if (tSubstances.getNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getNumber, tSubstances.getNumber()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getUnit())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getUnit, tSubstances.getUnit()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getAffiliatedUnit())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getAffiliatedUnit, tSubstances.getAffiliatedUnit()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getParkingPosition())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getParkingPosition, tSubstances.getParkingPosition()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getMapLocation())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getMapLocation, tSubstances.getMapLocation()));
        }
        if (StringUtils.isNotEmpty(tSubstances.getName())) {
            queryWrapper.lambda().and(i -> i.like(TSubstances::getName, tSubstances.getName()));
        }
        if(searchVo!=null){
            if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                queryWrapper.lambda().and(i -> i.between(TSubstances::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TSubstances::getIsdelete, 1));

        return queryWrapper;

    }
}
