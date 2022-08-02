package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.RelaVolunteerActivityMapper;
import me.zhengjie.entity.RelaVolunteerActivity;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.IRelaVolunteerActivityService;
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
public class RelaVolunteerActivityServiceImpl extends ServiceImpl<RelaVolunteerActivityMapper, RelaVolunteerActivity> implements IRelaVolunteerActivityService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final RelaVolunteerActivityMapper relaVolunteerActivityMapper;

    public Result<Object> getRelaVolunteerActivityById(String id) {
        RelaVolunteerActivity relaVolunteerActivity = relaVolunteerActivityMapper.selectById(id);
        if (relaVolunteerActivity != null) {
            return ResultUtil.data(relaVolunteerActivity);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryRelaVolunteerActivityListByPage(RelaVolunteerActivity relaVolunteerActivity, SearchVo searchVo, PageVo pageVo) {
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
        Page<RelaVolunteerActivity> pageData = new Page<>(page, limit);
        QueryWrapper<RelaVolunteerActivity> queryWrapper = new QueryWrapper<>();

        IPage<RelaVolunteerActivity> result = relaVolunteerActivityMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(RelaVolunteerActivity relaVolunteerActivity, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<RelaVolunteerActivity> queryWrapper = new QueryWrapper<>();

        List<RelaVolunteerActivity> list = relaVolunteerActivityMapper.selectList(queryWrapper);
        for (RelaVolunteerActivity re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

}
