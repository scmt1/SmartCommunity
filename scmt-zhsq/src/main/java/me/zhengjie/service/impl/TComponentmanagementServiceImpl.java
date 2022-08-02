package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.GridDept;
import me.zhengjie.mapper.TComponentmanagementMapper;
import me.zhengjie.entity.TComponentmanagement;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITComponentmanagementService;
import me.zhengjie.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 *@author 
 **/
@Service
public class TComponentmanagementServiceImpl extends ServiceImpl<TComponentmanagementMapper, TComponentmanagement> implements ITComponentmanagementService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TComponentmanagementMapper tComponentmanagementMapper;

	@Override
	public Result<Object> getTComponentmanagementById(String id){
		TComponentmanagement tComponentmanagement = tComponentmanagementMapper.selectById(id);
		if(tComponentmanagement!=null){
			return  ResultUtil.data(tComponentmanagement);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public IPage<TComponentmanagement> queryTComponentmanagementTreeByPage(JSONObject query) {
		Page<TComponentmanagement> page = new Page<>(query.getLongValue("pageNum"),query.getLongValue("pageSize"));

		Page<TComponentmanagement> tComponentmanagementPage = tComponentmanagementMapper.loadByPage(page,  query);
		if(tComponentmanagementPage!=null && tComponentmanagementPage.getRecords()!=null && tComponentmanagementPage.getRecords().size()>0)
			tComponentmanagementPage.getRecords().forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TComponentmanagement> tComponentmanagement = tComponentmanagementMapper.selectDeptTreeByParentId(dept.getId());
					dept.setChildren(tComponentmanagement);
					getChildren(tComponentmanagement);
				}
			});
		return tComponentmanagementPage;
	}

	@Override
	public List<TComponentmanagement> loadComponentmanagementTreeNotPage(JSONObject query) {
		List<TComponentmanagement> ComponentmanagementNotPage = tComponentmanagementMapper.loadNotPage(query);
		if(ComponentmanagementNotPage!=null){
			ComponentmanagementNotPage.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TComponentmanagement> Componentmanagements = tComponentmanagementMapper.selectDeptTreeByParentId(dept.getId());
					dept.setChildren(Componentmanagements);
					getChildren(Componentmanagements);
				}
			});
		}
		return ComponentmanagementNotPage;
	}

	public void getChildren( List<TComponentmanagement> tComponentmanagement){
		if(tComponentmanagement!=null && tComponentmanagement.size()>0){
			tComponentmanagement.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TComponentmanagement> tComponentmanagementList = tComponentmanagementMapper.selectDeptTreeByParentId(dept.getId());
					if(tComponentmanagementList!=null && tComponentmanagementList.size()>0){
						dept.setChildren(tComponentmanagementList);
						getChildren(tComponentmanagementList);
					}
				}
			});
		}

	}

	@Override
	public Result<Object> queryTComponentmanagementListByPage(TComponentmanagement  tComponentmanagement, SearchVo searchVo, PageVo pageVo){
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
		Page<TComponentmanagement> pageData = new Page<>(page, limit);
		QueryWrapper<TComponentmanagement> queryWrapper = new QueryWrapper<>();
		if (tComponentmanagement !=null) {
			queryWrapper = LikeAllFeild(tComponentmanagement,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_componentmanagement."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_componentmanagement."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_componentmanagement.create_time");
		}
		IPage<TComponentmanagement> result = tComponentmanagementMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TComponentmanagement tComponentmanagement, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TComponentmanagement> queryWrapper = new QueryWrapper<>();
		if (tComponentmanagement !=null) {
			queryWrapper = LikeAllFeild(tComponentmanagement,null);
		}
		List<TComponentmanagement> list = tComponentmanagementMapper.selectList(queryWrapper);
		for (TComponentmanagement re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("类型名称", re.getName());
			map.put("排序码", re.getSortCode());
			map.put("显示状态", re.getIsLabel());
			map.put("图标地址", re.getImgUrl());
			map.put("创建时间", re.getCreateTime());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tComponentmanagement 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TComponentmanagement>  LikeAllFeild(TComponentmanagement  tComponentmanagement, SearchVo searchVo) {
		QueryWrapper<TComponentmanagement> queryWrapper = new QueryWrapper<>();
		if(tComponentmanagement.getId() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.id", tComponentmanagement.getId()));
		}
		if(tComponentmanagement.getName() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.name", tComponentmanagement.getName()));
		}
		if(tComponentmanagement.getIsLabel() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.is_label", tComponentmanagement.getIsLabel()));
		}
		if(tComponentmanagement.getSortCode() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.sort_code", tComponentmanagement.getSortCode()));
		}
		if(StringUtils.isNotBlank(tComponentmanagement.getImgUrl())){
			queryWrapper.and(i -> i.like("t_componentmanagement.img_url", tComponentmanagement.getImgUrl()));
		}
		if(tComponentmanagement.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.is_delete", tComponentmanagement.getIsDelete()));
		}
		if(tComponentmanagement.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.create_id", tComponentmanagement.getCreateId()));
		}
		if(tComponentmanagement.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.create_time", tComponentmanagement.getCreateTime()));
		}
		if(tComponentmanagement.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.update_id", tComponentmanagement.getUpdateId()));
		}
		if(tComponentmanagement.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.update_time", tComponentmanagement.getUpdateTime()));
		}
		if(tComponentmanagement.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.delete_id", tComponentmanagement.getDeleteId()));
		}
		if(tComponentmanagement.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.delete_time", tComponentmanagement.getDeleteTime()));
		}
		if(tComponentmanagement.getPid() != null){
			queryWrapper.and(i -> i.like("t_componentmanagement.pid", tComponentmanagement.getPid()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_componentmanagement.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_componentmanagement.is_delete", 0));
		return queryWrapper;
	
}
}