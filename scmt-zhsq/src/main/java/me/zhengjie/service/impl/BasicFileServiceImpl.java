package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.mapper.BasicFileMapper;
import me.zhengjie.service.IBasicFileService;
import me.zhengjie.entity.BasicFile;

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
public class BasicFileServiceImpl extends ServiceImpl<BasicFileMapper, BasicFile> implements IBasicFileService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicFileMapper basicFileMapper;

    @Override
    public Result<Object> getBasicFileById(String id) {
        BasicFile basicFile = basicFileMapper.selectById(id);
        if (basicFile != null) {
            return ResultUtil.data(basicFile);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryBasicFileListByPage(BasicFile basicFile, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicFile> pageData = new Page<>(page, limit);
        QueryWrapper<BasicFile> queryWrapper = new QueryWrapper<>();
        if (basicFile != null) {
            queryWrapper = LikeAllFeild(basicFile, searchVo);
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc(pageVo.getSort());
            } else {
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        IPage<BasicFile> result = basicFileMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicFile basicFile, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicFile> queryWrapper = new QueryWrapper<>();
        if (basicFile != null) {
            queryWrapper = LikeAllFeild(basicFile, null);
        }
        List<BasicFile> list = basicFileMapper.selectList(queryWrapper);
        for (BasicFile re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名称", re.getFileName());
            map.put("文件路径", re.getFilePath());
            map.put("文件类型", re.getFileType());
            map.put("文件大小", re.getFileSize());
            map.put("备注", re.getRemark());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param basicFile 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<BasicFile> LikeAllFeild(BasicFile basicFile, SearchVo searchVo) {
        QueryWrapper<BasicFile> queryWrapper = new QueryWrapper<>();
        if (basicFile.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getId, basicFile.getId()));
        }
        if (basicFile.getType() != null) {
            queryWrapper.lambda().and(i -> i.eq(BasicFile::getType, basicFile.getType()));
        }
        if (StringUtils.isNotBlank(basicFile.getFileName())) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getFileName, basicFile.getFileName()));
        }
        if (StringUtils.isNotBlank(basicFile.getFilePath())) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getFilePath, basicFile.getFilePath()));
        }
        if (StringUtils.isNotBlank(basicFile.getFileType())) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getFileType, basicFile.getFileType()));
        }
        if (null != basicFile.getFileSize()) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getFileSize, basicFile.getFileSize()));
        }
        if (StringUtils.isNotBlank(basicFile.getRemark())) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getRemark, basicFile.getRemark()));
        }
        if (basicFile.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getIsDelete, basicFile.getIsDelete()));
        }
        if (basicFile.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getCreateId, basicFile.getCreateId()));
        }
        if (basicFile.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getCreateTime, basicFile.getCreateTime()));
        }
        if (basicFile.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getUpdateId, basicFile.getUpdateId()));
        }
        if (basicFile.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getUpdateTime, basicFile.getUpdateTime()));
        }
        if (basicFile.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getDeleteId, basicFile.getDeleteId()));
        }
        if (basicFile.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicFile::getDeleteTime, basicFile.getDeleteTime()));
        }
        queryWrapper.orderByDesc("create_time");
        //queryWrapper.lambda().and(i -> i.eq(BasicFile::getIsDelete, 0));
        return queryWrapper;

    }
}
