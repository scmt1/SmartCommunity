package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.mapper.RelaVolunteerActivityMapper;
import me.zhengjie.mapper.TZhsqVolunteerActivityMapper;
import me.zhengjie.mapper.TZhsqVolunteerMapper;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.RelaVolunteerActivity;
import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.entity.TZhsqVolunteerActivity;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqVolunteerActivityService;
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
public class TZhsqVolunteerActivityServiceImpl extends ServiceImpl<TZhsqVolunteerActivityMapper, TZhsqVolunteerActivity> implements ITZhsqVolunteerActivityService {
    
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqVolunteerActivityMapper tZhsqVolunteerActivityMapper;

    private final RelaVolunteerActivityMapper relaVolunteerActivityMapper;

    private final TZhsqVolunteerMapper tZhsqVolunteerMapper;

    @Override
    public Result<Object> getTZhsqVolunteerActivityById(String id) {
        TZhsqVolunteerActivity tZhsqVolunteerActivity = tZhsqVolunteerActivityMapper.selectById(id);
        if (tZhsqVolunteerActivity != null) {
            return ResultUtil.data(tZhsqVolunteerActivity);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTZhsqVolunteerActivityListByPage(TZhsqVolunteerActivity tZhsqVolunteerActivity, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqVolunteerActivity> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqVolunteerActivity> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteerActivity != null) {
            queryWrapper = LikeAllFeild(tZhsqVolunteerActivity, searchVo);
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
        IPage<TZhsqVolunteerActivity> result = tZhsqVolunteerActivityMapper.selectPage(pageData, queryWrapper);
        QueryWrapper<RelaVolunteerActivity> objectQueryWrapper = new QueryWrapper<>();

        for (int i = 0; i < result.getRecords().size(); i++) {
            TZhsqVolunteerActivity tZhsqVolunteerActivity1 = result.getRecords().get(i);
            objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().and(x -> x.eq(RelaVolunteerActivity::getActivityId, tZhsqVolunteerActivity1.getId()));
            List<RelaVolunteerActivity> relaVolunteerActivities = relaVolunteerActivityMapper.selectList(objectQueryWrapper);

			ArrayList<String> strings = new ArrayList<>();
			for (int j = 0; j < relaVolunteerActivities.size(); j++) {
				strings.add(relaVolunteerActivities.get(j).getVolunteerId());
            }

			if(strings.size() > 0){
				QueryWrapper<TZhsqVolunteer> wrapper = new QueryWrapper<>();
				wrapper.lambda().and(x -> x.in(TZhsqVolunteer::getId, strings.toArray()));
				List<TZhsqVolunteer> tZhsqVolunteers = tZhsqVolunteerMapper.selectList(wrapper);
				String name = "";
				for (int j = 0; j < tZhsqVolunteers.size(); j++) {
					name += "," + tZhsqVolunteers.get(j).getName();
				}
				if (!"".equals(name)) {
					name = name.substring(1);
				}
				tZhsqVolunteerActivity1.setParticipantName(name);
			}
        }
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqVolunteerActivity tZhsqVolunteerActivity, SearchVo searchVo, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqVolunteerActivity> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteerActivity != null) {
            queryWrapper = LikeAllFeild(tZhsqVolunteerActivity, searchVo);
        }
        List<TZhsqVolunteerActivity> list = tZhsqVolunteerActivityMapper.selectList(queryWrapper);
        for (TZhsqVolunteerActivity re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("活动名称", re.getName());
            map.put("活动地址", re.getActivitiyAddress());
            map.put("活动开始时间", re.getStartDate1());
            map.put("活动结束时间", re.getEndDate1());
            map.put("组织者", re.getOrganizer());
            map.put("参与志愿者", re.getParticipant());
            map.put("活动简介", re.getActivityProfile());
            map.put("地图标注", re.getPosition());
            map.put("排序", re.getOrderNumber());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 新增志愿者活动数据
     * @param tZhsqVolunteerActivity
     * @return
     */
    @Override
    public int insertData(TZhsqVolunteerActivity tZhsqVolunteerActivity) {
        return tZhsqVolunteerActivityMapper.insert(tZhsqVolunteerActivity);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqVolunteerActivity 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqVolunteerActivity> LikeAllFeild(TZhsqVolunteerActivity
                                                                     tZhsqVolunteerActivity, SearchVo searchVo) {
        QueryWrapper<TZhsqVolunteerActivity> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteerActivity.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getId, tZhsqVolunteerActivity.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getName, tZhsqVolunteerActivity.getName()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getActivitiyAddress())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getActivitiyAddress, tZhsqVolunteerActivity.getActivitiyAddress()));
        }
        if (searchVo!=null && StringUtils.isNotBlank(searchVo.getStartDate())) {
            queryWrapper.lambda().and(i -> i.ge(TZhsqVolunteerActivity::getStartDate1, searchVo.getStartDate()));
        }
        if (searchVo!=null && StringUtils.isNotBlank(searchVo.getEndDate())) {
            queryWrapper.lambda().and(i -> i.le(TZhsqVolunteerActivity::getEndDate1, searchVo.getEndDate()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getOrganizer())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getOrganizer, tZhsqVolunteerActivity.getOrganizer()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getParticipant())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getParticipant, tZhsqVolunteerActivity.getParticipant()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getActivityProfile())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getActivityProfile, tZhsqVolunteerActivity.getActivityProfile()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getPosition())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getPosition, tZhsqVolunteerActivity.getPosition()));
        }

        if (tZhsqVolunteerActivity.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getIsDelete, tZhsqVolunteerActivity.getIsDelete()));
        }
        if (tZhsqVolunteerActivity.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getCreateId, tZhsqVolunteerActivity.getCreateId()));
        }
        if (tZhsqVolunteerActivity.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getCreateTime, tZhsqVolunteerActivity.getCreateTime()));
        }
        if (tZhsqVolunteerActivity.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getUpdateId, tZhsqVolunteerActivity.getUpdateId()));
        }
        if (tZhsqVolunteerActivity.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getUpdateTime, tZhsqVolunteerActivity.getUpdateTime()));
        }
        if (tZhsqVolunteerActivity.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getDeleteId, tZhsqVolunteerActivity.getDeleteId()));
        }
        if (tZhsqVolunteerActivity.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteerActivity::getDeleteTime, tZhsqVolunteerActivity.getDeleteTime()));
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteerActivity::getIsDelete, 0));
        return queryWrapper;

    }
}
