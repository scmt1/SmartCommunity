package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TEmergencyhedging;
import me.zhengjie.mapper.TEmergencyhedgingMapper;
import me.zhengjie.service.ITEmergencyhedgingService;
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
 *@author 
 **/
@Service
public class TEmergencyhedgingServiceImpl extends ServiceImpl<TEmergencyhedgingMapper, TEmergencyhedging> implements ITEmergencyhedgingService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TEmergencyhedgingMapper tEmergencyhedgingMapper;

	@Override
	public Result<Object> getTEmergencyhedgingById(String id){
		TEmergencyhedging tEmergencyhedging = tEmergencyhedgingMapper.selectById(id);
		if(tEmergencyhedging!=null){
			return  ResultUtil.data(tEmergencyhedging);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTEmergencyhedgingListByPage(TEmergencyhedging  tEmergencyhedging, SearchVo searchVo, PageVo pageVo){
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
		Page<TEmergencyhedging> pageData = new Page<>(page, limit);
		QueryWrapper<TEmergencyhedging> queryWrapper = new QueryWrapper<>();
		if (tEmergencyhedging !=null) {
			queryWrapper = LikeAllFeild(tEmergencyhedging,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_emergencyhedging."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_emergencyhedging."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_emergencyhedging.create_time");
		}
		IPage<TEmergencyhedging> result = tEmergencyhedgingMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TEmergencyhedging tEmergencyhedging, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TEmergencyhedging> queryWrapper = new QueryWrapper<>();
		if (tEmergencyhedging !=null) {
			queryWrapper = LikeAllFeild(tEmergencyhedging,null);
		}
		List<TEmergencyhedging> list = tEmergencyhedgingMapper.selectList(queryWrapper);
		for (TEmergencyhedging re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("名称", re.getName());
			map.put("位置", re.getPosition());
			map.put("地名", re.getPlaceName());
			map.put("创建时间", re.getCreateTime());
			map.put("是否显示标签 0隐藏 1显示", re.getIsLabel());
			map.put("面积", re.getMeasureArea());
			map.put("负责人", re.getResponsiblePerson());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tEmergencyhedging 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TEmergencyhedging>  LikeAllFeild(TEmergencyhedging  tEmergencyhedging, SearchVo searchVo) {
		QueryWrapper<TEmergencyhedging> queryWrapper = new QueryWrapper<>();
		if(tEmergencyhedging.getId() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.id", tEmergencyhedging.getId()));
		}
		if(tEmergencyhedging.getName() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.name", tEmergencyhedging.getName()));
		}
		if(tEmergencyhedging.getPosition() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.position", tEmergencyhedging.getPosition()));
		}
		if(tEmergencyhedging.getPlaceName() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.place_name", tEmergencyhedging.getPlaceName()));
		}
		if(tEmergencyhedging.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.is_delete", tEmergencyhedging.getIsDelete()));
		}
		if(tEmergencyhedging.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.create_id", tEmergencyhedging.getCreateId()));
		}
		if(tEmergencyhedging.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.create_time", tEmergencyhedging.getCreateTime()));
		}
		if(tEmergencyhedging.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.update_id", tEmergencyhedging.getUpdateId()));
		}
		if(tEmergencyhedging.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.update_time", tEmergencyhedging.getUpdateTime()));
		}
		if(tEmergencyhedging.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.delete_id", tEmergencyhedging.getDeleteId()));
		}
		if(tEmergencyhedging.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.delete_time", tEmergencyhedging.getDeleteTime()));
		}
		if(tEmergencyhedging.getIsLabel() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.is_label", tEmergencyhedging.getIsLabel()));
		}
		if(tEmergencyhedging.getMeasureArea() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.measure_area", tEmergencyhedging.getMeasureArea()));
		}
		if(tEmergencyhedging.getResponsiblePerson() != null){
			queryWrapper.and(i -> i.like("t_emergencyhedging.responsible_person", tEmergencyhedging.getResponsiblePerson()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_emergencyhedging.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_emergencyhedging.is_delete", 0));
		return queryWrapper;
	
}
}