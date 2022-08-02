package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAnounceInfo;
import me.zhengjie.mapper.TZhsqAnounceInfoMapper;
import me.zhengjie.service.ITZhsqAnounceInfoService;
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
public class TZhsqAnounceInfoServiceImpl extends ServiceImpl<TZhsqAnounceInfoMapper, TZhsqAnounceInfo> implements ITZhsqAnounceInfoService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TZhsqAnounceInfoMapper tZhsqAnounceInfoMapper;

    @Override
    public TZhsqAnounceInfo getTZhsqAnounceInfoById(String id) {
        TZhsqAnounceInfo tZhsqAnounceInfo = tZhsqAnounceInfoMapper.selectById(id);
        return tZhsqAnounceInfo;
    }

    @Override
    public Result<Object> queryTZhsqAnounceInfoListByPage(TZhsqAnounceInfo tZhsqAnounceInfo, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqAnounceInfo> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqAnounceInfo> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnounceInfo != null) {
            queryWrapper = LikeAllFeild(tZhsqAnounceInfo, searchVo);
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc("t_zhsq_anounce_info." + pageVo.getSort());
            } else {
                queryWrapper.orderByDesc("t_zhsq_anounce_info." + pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("t_zhsq_anounce_info.create_time");
        }
        IPage<TZhsqAnounceInfo> result = tZhsqAnounceInfoMapper.selectByMyWrapper(queryWrapper,pageData);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqAnounceInfo tZhsqAnounceInfo, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqAnounceInfo> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnounceInfo != null) {
            queryWrapper = LikeAllFeild(tZhsqAnounceInfo, null);
        }
        List<TZhsqAnounceInfo> list = tZhsqAnounceInfoMapper.selectByMyWrapper(queryWrapper);
        for (TZhsqAnounceInfo re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("公告类型", re.getInfoType());

            map.put("公告排序", re.getInfoSort());
            map.put("公告标题", re.getInfoTitle());
            map.put("发布日期", re.getCreateTime());
            map.put("发布单位", re.getInfoAuthor());
            map.put("发布内容", re.getInfoContent());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqAnounceInfo 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqAnounceInfo> LikeAllFeild(TZhsqAnounceInfo tZhsqAnounceInfo, SearchVo searchVo) {
        QueryWrapper<TZhsqAnounceInfo> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnounceInfo.getId() != null) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.id", tZhsqAnounceInfo.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoType())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_type", tZhsqAnounceInfo.getInfoType()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoSort())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_sort", tZhsqAnounceInfo.getInfoSort()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoTitle())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_title", tZhsqAnounceInfo.getInfoTitle()));
        }
        if (tZhsqAnounceInfo.getCreateTime() != null) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.create_time", tZhsqAnounceInfo.getCreateTime()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoAuthor())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_author", tZhsqAnounceInfo.getInfoAuthor()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoContent())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_content", tZhsqAnounceInfo.getInfoContent()));
        }
        if (StringUtils.isNotBlank(tZhsqAnounceInfo.getInfoShow())) {
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.info_show", tZhsqAnounceInfo.getInfoShow()));
        }
        if(tZhsqAnounceInfo.getCreateId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.create_id", tZhsqAnounceInfo.getCreateId()));
        }
        if(tZhsqAnounceInfo.getCreateTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.create_time", tZhsqAnounceInfo.getCreateTime()));
        }
        if(tZhsqAnounceInfo.getUpdateId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.update_id", tZhsqAnounceInfo.getUpdateId()));
        }
        if(tZhsqAnounceInfo.getUpdateTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.update_time", tZhsqAnounceInfo.getUpdateTime()));
        }
        if(tZhsqAnounceInfo.getDeleteId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.delete_id", tZhsqAnounceInfo.getDeleteId()));
        }
        if(tZhsqAnounceInfo.getDeleteTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_anounce_info.delete_time", tZhsqAnounceInfo.getDeleteTime()));
        }
        if (searchVo != null) {
            if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
                queryWrapper.and(i -> i.between("t_zhsq_anounce_info.create_time", searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.and(i -> i.eq("t_zhsq_anounce_info.is_delete", 0));
        return queryWrapper;
    }
}