package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TArmedequipment;
import me.zhengjie.mapper.TArmedequipmentMapper;
import me.zhengjie.service.ITArmedequipmentService;
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
public class TArmedequipmentServiceImpl extends ServiceImpl<TArmedequipmentMapper, TArmedequipment> implements ITArmedequipmentService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TArmedequipmentMapper tArmedequipmentMapper;

	@Override
	public Result<Object> getTArmedequipmentById(String id){
		TArmedequipment tArmedequipment = tArmedequipmentMapper.selectById(id);
		if(tArmedequipment!=null){
			return  ResultUtil.data(tArmedequipment);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTArmedequipmentListByPage(TArmedequipment  tArmedequipment, SearchVo searchVo, PageVo pageVo){
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
		Page<TArmedequipment> pageData = new Page<>(page, limit);
		QueryWrapper<TArmedequipment> queryWrapper = new QueryWrapper<>();
		if (tArmedequipment !=null) {
			queryWrapper = LikeAllFeild(tArmedequipment,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_armedequipment."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_armedequipment."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_armedequipment.create_time");
		}
//		IPage<TArmedequipment> result = tArmedequipmentMapper.selectPage(pageData, queryWrapper);
		IPage<TArmedequipment> result = tArmedequipmentMapper.selectTArmedequipmentPageList(queryWrapper, pageData);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TArmedequipment tArmedequipment, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TArmedequipment> queryWrapper = new QueryWrapper<>();
		if (tArmedequipment !=null) {
			queryWrapper = LikeAllFeild(tArmedequipment,null);
		}
		List<TArmedequipment> list = tArmedequipmentMapper.selectList(queryWrapper);
		for (TArmedequipment re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("设备名称", re.getEquipmentName());
			map.put("设备类型", re.getEquipmentType());
			map.put("设备数量", re.getEquipmentNumber());
			map.put("位置", re.getPosition());
			map.put("地名", re.getPlaceName());
			map.put("创建时间", re.getCreateTime());
			map.put("是否显示标签 0隐藏 1显示", re.getIsLabel());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tArmedequipment 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TArmedequipment>  LikeAllFeild(TArmedequipment  tArmedequipment, SearchVo searchVo) {
		QueryWrapper<TArmedequipment> queryWrapper = new QueryWrapper<>();
		if(tArmedequipment.getId() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.id", tArmedequipment.getId()));
		}
		if(StringUtils.isNotBlank(tArmedequipment.getEquipmentName())){
			queryWrapper.and(i -> i.like("t_armedequipment.equipment_name", tArmedequipment.getEquipmentName()));
		}
		if(StringUtils.isNotBlank(tArmedequipment.getEquipmentType())){
			queryWrapper.and(i -> i.like("t_armedequipment.equipment_type", tArmedequipment.getEquipmentType()));
		}
		if(tArmedequipment.getEquipmentNumber() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.equipment_number", tArmedequipment.getEquipmentNumber()));
		}
		if(StringUtils.isNotBlank(tArmedequipment.getPosition())){
			queryWrapper.and(i -> i.like("t_armedequipment.position", tArmedequipment.getPosition()));
		}
		if(StringUtils.isNotBlank(tArmedequipment.getPlaceName())){
			queryWrapper.and(i -> i.like("t_armedequipment.place_name", tArmedequipment.getPlaceName()));
		}
		if(tArmedequipment.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.is_delete", tArmedequipment.getIsDelete()));
		}
		if(tArmedequipment.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.create_id", tArmedequipment.getCreateId()));
		}
		if(tArmedequipment.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.create_time", tArmedequipment.getCreateTime()));
		}
		if(tArmedequipment.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.update_id", tArmedequipment.getUpdateId()));
		}
		if(tArmedequipment.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.update_time", tArmedequipment.getUpdateTime()));
		}
		if(tArmedequipment.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.delete_id", tArmedequipment.getDeleteId()));
		}
		if(tArmedequipment.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.delete_time", tArmedequipment.getDeleteTime()));
		}
		if(tArmedequipment.getIsLabel() != null){
			queryWrapper.and(i -> i.like("t_armedequipment.is_label", tArmedequipment.getIsLabel()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_armedequipment.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_armedequipment.is_delete", 0));
		return queryWrapper;
	
}
}