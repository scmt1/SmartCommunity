package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TUrbancomponents;
import me.zhengjie.mapper.TUrbancomponentsMapper;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITUrbancomponentsService;
import me.zhengjie.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
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
public class TUrbancomponentsServiceImpl extends ServiceImpl<TUrbancomponentsMapper, TUrbancomponents> implements ITUrbancomponentsService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TUrbancomponentsMapper tUrbancomponentsMapper;

	@Override
	public Result<Object> getTUrbancomponentsById(String id){
		TUrbancomponents tUrbancomponents = tUrbancomponentsMapper.selectById(id);
		if(tUrbancomponents!=null){
			return  ResultUtil.data(tUrbancomponents);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTUrbancomponentsListByPage(TUrbancomponents  tUrbancomponents, SearchVo searchVo, PageVo pageVo){
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
		Page<TUrbancomponents> pageData = new Page<>(page, limit);
		QueryWrapper<TUrbancomponents> queryWrapper = new QueryWrapper<>();
		if (tUrbancomponents !=null) {
			queryWrapper = LikeAllFeild(tUrbancomponents,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_urbancomponents."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_urbancomponents."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_urbancomponents.create_time");
		}
		IPage<TUrbancomponents> result = tUrbancomponentsMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TUrbancomponents tUrbancomponents, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TUrbancomponents> queryWrapper = new QueryWrapper<>();
		if (tUrbancomponents !=null) {
			queryWrapper = LikeAllFeild(tUrbancomponents,null);
		}
		List<TUrbancomponents> list = tUrbancomponentsMapper.selectList(queryWrapper);
		for (TUrbancomponents re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("部件类型", re.getComponentType());
			map.put("部门名称", re.getDepartmentName());
			map.put("下级部门", re.getLowerDepartment());
			map.put("部件状态 1完好 0损坏", re.getDepartmentStatu());
			map.put("所属辖区", re.getJurisdiction());
			map.put("坐标位置", re.getPosition());
			map.put("创建时间", re.getCreateTime());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tUrbancomponents 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TUrbancomponents>  LikeAllFeild(TUrbancomponents  tUrbancomponents, SearchVo searchVo) {
		QueryWrapper<TUrbancomponents> queryWrapper = new QueryWrapper<>();
		if(tUrbancomponents.getId() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.id", tUrbancomponents.getId()));
		}
		if(tUrbancomponents.getComponentType() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.component_type", tUrbancomponents.getComponentType()));
		}
		if(tUrbancomponents.getDepartmentName() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.department_name", tUrbancomponents.getDepartmentName()));
		}
		if(tUrbancomponents.getLowerDepartment() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.lower_department", tUrbancomponents.getLowerDepartment()));
		}
		if(tUrbancomponents.getDepartmentStatu() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.department_statu", tUrbancomponents.getDepartmentStatu()));
		}
		if(tUrbancomponents.getJurisdiction() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.jurisdiction", tUrbancomponents.getJurisdiction()));
		}
		if(tUrbancomponents.getPosition() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.position", tUrbancomponents.getPosition()));
		}
		if(tUrbancomponents.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.is_delete", tUrbancomponents.getIsDelete()));
		}
		if(tUrbancomponents.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.create_id", tUrbancomponents.getCreateId()));
		}
		if(tUrbancomponents.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.create_time", tUrbancomponents.getCreateTime()));
		}
		if(tUrbancomponents.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.update_id", tUrbancomponents.getUpdateId()));
		}
		if(tUrbancomponents.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.update_time", tUrbancomponents.getUpdateTime()));
		}
		if(tUrbancomponents.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.delete_id", tUrbancomponents.getDeleteId()));
		}
		if(tUrbancomponents.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_urbancomponents.delete_time", tUrbancomponents.getDeleteTime()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_urbancomponents.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_urbancomponents.is_delete", 0));
		return queryWrapper;
	
}
}