package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAnnounceType;
import me.zhengjie.mapper.TZhsqAnnounceTypeMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.zhengjie.service.ITZhsqAnnounceTypeService;

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
public class TZhsqAnnounceTypeServiceImpl extends ServiceImpl<TZhsqAnnounceTypeMapper, TZhsqAnnounceType> implements ITZhsqAnnounceTypeService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TZhsqAnnounceTypeMapper tZhsqAnnounceTypeMapper;

    @Override
    public TZhsqAnnounceType getTZhsqAnnounceTypeById(String id) {
        TZhsqAnnounceType tZhsqAnnounceType = tZhsqAnnounceTypeMapper.selectById(id);
        return tZhsqAnnounceType;
    }

    @Override
    public Result<Object> queryTZhsqAnnounceTypeListByPage(TZhsqAnnounceType tZhsqAnnounceType, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqAnnounceType> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqAnnounceType> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnnounceType != null) {
            queryWrapper = LikeAllFeild(tZhsqAnnounceType, searchVo);
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc("t_zhsq_announce_type." + pageVo.getSort());
            } else {
                queryWrapper.orderByDesc("t_zhsq_announce_type." + pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("t_zhsq_announce_type.create_time");
        }
        IPage<TZhsqAnnounceType> result = tZhsqAnnounceTypeMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqAnnounceType tZhsqAnnounceType, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqAnnounceType> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnnounceType != null) {
            queryWrapper = LikeAllFeild(tZhsqAnnounceType, null);
        }
        List<TZhsqAnnounceType> list = tZhsqAnnounceTypeMapper.selectList(queryWrapper);
        for (TZhsqAnnounceType re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("是否显示", re.getAnnounceShow());
            map.put("创建时间", re.getCreateTime());
            map.put("排序", re.getAnnounceSort());
            map.put("类型名称", re.getTypeName());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }


    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqAnnounceType 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqAnnounceType> LikeAllFeild(TZhsqAnnounceType tZhsqAnnounceType, SearchVo searchVo) {
        QueryWrapper<TZhsqAnnounceType> queryWrapper = new QueryWrapper<>();
        if (tZhsqAnnounceType.getId() != null) {
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.id", tZhsqAnnounceType.getId()));
        }
        if (tZhsqAnnounceType.getAnnounceShow() != null) {
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.announce_show", tZhsqAnnounceType.getAnnounceShow()));
        }
        if(tZhsqAnnounceType.getCreateId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.create_id", tZhsqAnnounceType.getCreateId()));
        }
        if(tZhsqAnnounceType.getCreateTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.create_time", tZhsqAnnounceType.getCreateTime()));
        }
        if(tZhsqAnnounceType.getUpdateId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.update_id", tZhsqAnnounceType.getUpdateId()));
        }
        if(tZhsqAnnounceType.getUpdateTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.update_time", tZhsqAnnounceType.getUpdateTime()));
        }
        if(tZhsqAnnounceType.getDeleteId() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.delete_id", tZhsqAnnounceType.getDeleteId()));
        }
        if(tZhsqAnnounceType.getDeleteTime() != null){
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.delete_time", tZhsqAnnounceType.getDeleteTime()));
        }
        if (StringUtils.isNotBlank(tZhsqAnnounceType.getAnnounceSort())) {
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.announce_sort", tZhsqAnnounceType.getAnnounceSort()));
        }
        if (StringUtils.isNotBlank(tZhsqAnnounceType.getTypeName())) {
            queryWrapper.and(i -> i.like("t_zhsq_announce_type.type_name", tZhsqAnnounceType.getTypeName()));
        }
        if (searchVo != null) {
            if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
                queryWrapper.and(i -> i.between("t_zhsq_announce_type.create_time", searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.and(i -> i.eq("t_zhsq_announce_type.is_delete", 0));
        return queryWrapper;

    }
}