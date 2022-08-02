package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.SysSetting;
import me.zhengjie.dao.mapper.SysSettingMapper;
import me.zhengjie.dao.service.ISysSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dengjie
 * @since 2020-06-10
 */
@Service
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements ISysSettingService {

    @Autowired
    private SysSettingMapper sysSettingMapper;

    public SysSetting getSysSettingById(String id) {
        return sysSettingMapper.selectById(id);
    }

    @Override
    public IPage<SysSetting> querySysSettingListByPage(String search, SearchVo searchVo, PageVo pageVo) {
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
        Page<SysSetting> pageData = new Page<>(page, limit);
        QueryWrapper<SysSetting> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(search)) {
            queryWrapper = LikeAllFeild(search);
        }
        IPage<SysSetting> result = sysSettingMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    @Override
    public void download(String likeValue, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<SysSetting> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(likeValue)) {
            queryWrapper = LikeAllFeild(likeValue);
        }
        List<SysSetting> list = sysSettingMapper.selectList(queryWrapper);
        for (SysSetting re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", re.getName());
            map.put("是否开启 1是0否", re.getValue());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param likeValue 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<SysSetting> LikeAllFeild(String likeValue) {
        QueryWrapper<SysSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().or(i -> i.like(SysSetting::getId, likeValue));
        queryWrapper.lambda().or(i -> i.like(SysSetting::getName, likeValue));
        queryWrapper.lambda().or(i -> i.like(SysSetting::getValue, likeValue));
        queryWrapper.lambda().or(i -> i.like(SysSetting::getCreateId, likeValue));
        queryWrapper.lambda().or(i -> i.like(SysSetting::getCreateTime, likeValue));
//        queryWrapper.lambda().and(i -> i.eq(SysSetting::getIsdelete, 0));
        return queryWrapper;

    }
}
