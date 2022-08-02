package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.BasicHealthFileMapper;
import me.zhengjie.entity.BasicHealthFile;
import me.zhengjie.service.IBasicHealthFileService;
import me.zhengjie.utils.FileUtil;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class BasicHealthFileServiceImpl extends ServiceImpl<BasicHealthFileMapper, BasicHealthFile> implements IBasicHealthFileService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicHealthFileMapper basicHealthFileMapper;

    @Override
    public Result<Object> getBasicHealthFileById(String id) {
        BasicHealthFile basicHealthFile = basicHealthFileMapper.selectById(id);
        if (basicHealthFile != null) {
            return ResultUtil.data(basicHealthFile);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryBasicHealthFileListByPage(BasicHealthFile basicHealthFile, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicHealthFile> pageData = new Page<>(page, limit);
        QueryWrapper<BasicHealthFile> queryWrapper = new QueryWrapper<>();
        if (basicHealthFile != null) {
            queryWrapper = LikeAllFeild(basicHealthFile, searchVo);
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
        IPage<BasicHealthFile> result = basicHealthFileMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicHealthFile basicHealthFile, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicHealthFile> queryWrapper = new QueryWrapper<>();
        if (basicHealthFile != null) {
            queryWrapper = LikeAllFeild(basicHealthFile, null);
        }
        List<BasicHealthFile> list = basicHealthFileMapper.selectList(queryWrapper);
        for (BasicHealthFile re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 根据personId查询最新的健康档案
     * @param personId
     * @return
     */
    @Override
    public BasicHealthFile getTopOne(String personId) {
		return basicHealthFileMapper.getTopOne(personId);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param basicHealthFile 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<BasicHealthFile> LikeAllFeild(BasicHealthFile basicHealthFile, SearchVo searchVo) {
        QueryWrapper<BasicHealthFile> queryWrapper = new QueryWrapper<>();
        if (basicHealthFile.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getId, basicHealthFile.getId()));
        }
        if (basicHealthFile.getHeight() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getHeight, basicHealthFile.getHeight()));
        }
        if (basicHealthFile.getBodyWeight() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getBodyWeight, basicHealthFile.getBodyWeight()));
        }
        if (basicHealthFile.getBloodType() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getBloodType, basicHealthFile.getBloodType()));
        }
        if (basicHealthFile.getWaistline() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getWaistline, basicHealthFile.getWaistline()));
        }
        if (basicHealthFile.getVision() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getVision, basicHealthFile.getVision()));
        }
        if (basicHealthFile.getBloodPressure() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getBloodPressure, basicHealthFile.getBloodPressure()));
        }
        if (basicHealthFile.getAllergies() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getAllergies, basicHealthFile.getAllergies()));
        }
        if (basicHealthFile.getRemark() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getRemark, basicHealthFile.getRemark()));
        }
        if (basicHealthFile.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getIsDelete, basicHealthFile.getIsDelete()));
        }
        if (basicHealthFile.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getCreateId, basicHealthFile.getCreateId()));
        }
        if (basicHealthFile.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getCreateTime, basicHealthFile.getCreateTime()));
        }
        if (basicHealthFile.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getUpdateId, basicHealthFile.getUpdateId()));
        }
        if (basicHealthFile.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getUpdateTime, basicHealthFile.getUpdateTime()));
        }
        if (basicHealthFile.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getDeleteId, basicHealthFile.getDeleteId()));
        }
        if (basicHealthFile.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHealthFile::getDeleteTime, basicHealthFile.getDeleteTime()));
        }
        queryWrapper.lambda().and(i -> i.eq(BasicHealthFile::getIsDelete, 0));
        return queryWrapper;

    }
}
