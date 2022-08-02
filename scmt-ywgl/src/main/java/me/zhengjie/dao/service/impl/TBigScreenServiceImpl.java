package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TBigScreen;
import me.zhengjie.dao.mapper.TBigScreenMapper;
import me.zhengjie.dao.service.ITBigScreenService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 **/
@Service
public class TBigScreenServiceImpl extends ServiceImpl<TBigScreenMapper, TBigScreen> implements ITBigScreenService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TBigScreenMapper tBigScreenMapper;

    public Result<Object> getTBigScreenById(String id) {
        TBigScreen tBigScreen = tBigScreenMapper.selectById(id);
        if (tBigScreen != null) {
            return ResultUtil.data(tBigScreen);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTBigScreenListByPage(TBigScreen tBigScreen, SearchVo searchVo, PageVo pageVo) {
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
        Page<TBigScreen> pageData = new Page<>(page, limit);
        QueryWrapper<TBigScreen> queryWrapper = new QueryWrapper<>();
        if (tBigScreen != null) {
            queryWrapper = LikeAllFeild(tBigScreen, searchVo);
        }
        IPage<TBigScreen> result = tBigScreenMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TBigScreen tBigScreen, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TBigScreen> queryWrapper = new QueryWrapper<>();
        if (tBigScreen != null) {
            queryWrapper = LikeAllFeild(tBigScreen, null);
        }
        List<TBigScreen> list = tBigScreenMapper.selectList(queryWrapper);
        for (TBigScreen re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("大屏名称", re.getName());
            map.put("大屏描述", re.getDetails());
            map.put("大屏url地址", re.getUrl());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tBigScreen 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TBigScreen> LikeAllFeild(TBigScreen tBigScreen, SearchVo searchVo) {
        QueryWrapper<TBigScreen> queryWrapper = new QueryWrapper<>();
        if (tBigScreen.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getId, tBigScreen.getId()));
        }
        if (StringUtils.isNotBlank(tBigScreen.getName())) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getName, tBigScreen.getName()));
        }
        if (StringUtils.isNotBlank(tBigScreen.getDetails())) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getDetails, tBigScreen.getDetails()));
        }
        if (StringUtils.isNotBlank(tBigScreen.getUrl())) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getUrl, tBigScreen.getUrl()));
        }
        if (StringUtils.isNotBlank(tBigScreen.getConfigUrl())) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getConfigUrl, tBigScreen.getConfigUrl()));
        }
        if (tBigScreen.getImg() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getImg, tBigScreen.getImg()));
        }
        if (tBigScreen.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getIsDelete, tBigScreen.getIsDelete()));
        }
        if (tBigScreen.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getCreateId, tBigScreen.getCreateId()));
        }
        if (tBigScreen.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getCreateTime, tBigScreen.getCreateTime()));
        }
        if (tBigScreen.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getUpdateId, tBigScreen.getUpdateId()));
        }
        if (tBigScreen.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getUpdateTime, tBigScreen.getUpdateTime()));
        }
        if (tBigScreen.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getDeleteId, tBigScreen.getDeleteId()));
        }
        if (tBigScreen.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getDeleteTime, tBigScreen.getDeleteTime()));
        }
        if (tBigScreen.getType() != null) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getType, tBigScreen.getType()));
        }
        if (StringUtils.isNotBlank(tBigScreen.getState())) {
            queryWrapper.lambda().and(i -> i.like(TBigScreen::getState, tBigScreen.getState()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TBigScreen::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TBigScreen::getIsDelete, 0));
        return queryWrapper;

    }
}