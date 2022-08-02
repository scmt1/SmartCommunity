package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.TZhsqEventListMapper;
import me.zhengjie.entity.TZhsqEventList;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqEventListService;
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
@AllArgsConstructor
public class TZhsqEventListServiceImpl extends ServiceImpl<TZhsqEventListMapper, TZhsqEventList> implements ITZhsqEventListService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final TZhsqEventListMapper tZhsqEventListMapper;

	public Result<Object> getTZhsqEventListById(String id){
		TZhsqEventList tZhsqEventList = tZhsqEventListMapper.selectById(id);
		if(tZhsqEventList!=null){
			return  ResultUtil.data(tZhsqEventList);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTZhsqEventListListByPage(TZhsqEventList  tZhsqEventList, SearchVo searchVo, PageVo pageVo){
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
		Page<TZhsqEventList> pageData = new Page<>(page, limit);
		QueryWrapper<TZhsqEventList> queryWrapper = new QueryWrapper<>();
		if (tZhsqEventList !=null) {
			queryWrapper = LikeAllFeild(tZhsqEventList,searchVo);
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
		IPage<TZhsqEventList> result = tZhsqEventListMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TZhsqEventList tZhsqEventList, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TZhsqEventList> queryWrapper = new QueryWrapper<>();
		if (tZhsqEventList !=null) {
			queryWrapper = LikeAllFeild(tZhsqEventList,null);
		}
		List<TZhsqEventList> list = tZhsqEventListMapper.selectList(queryWrapper);
		for (TZhsqEventList re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("事件分类", re.getEventClassification());
			map.put("事件名称", re.getEventName());
			map.put("所属社区", re.getCommunity());
			map.put("所属网格", re.getGrid());
			map.put("发起人", re.getSponsor());
			map.put("事件开始时间", re.getEventStartingTime());
			map.put("事件结束时间", re.getEventEndTime());
			map.put("执行人", re.getExecutor());
			map.put("事件状态 0处理中 1已完成", re.getEventStatus());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	@Override
	public Result<Object> queryAllTZhsqGridMemberListByAnyWayWhere(TZhsqEventList tZhsqEventList) {
		QueryWrapper<TZhsqEventList> queryWrapper = new QueryWrapper<>();
		if (tZhsqEventList != null) {
			queryWrapper = LikeAllFeild(tZhsqEventList, null);
		}
		List<TZhsqEventList> list = tZhsqEventListMapper.selectList(queryWrapper);
		return ResultUtil.data(list);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tZhsqEventList 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TZhsqEventList>  LikeAllFeild(TZhsqEventList  tZhsqEventList, SearchVo searchVo) {
		QueryWrapper<TZhsqEventList> queryWrapper = new QueryWrapper<>();
		if(tZhsqEventList.getId() != null){
			queryWrapper.lambda().and(i -> i.eq(TZhsqEventList::getId, tZhsqEventList.getId()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getEventClassification())){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getEventClassification, tZhsqEventList.getEventClassification()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getEventName())){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getEventName, tZhsqEventList.getEventName()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getCommunity())){
			queryWrapper.lambda().and(i -> i.eq(TZhsqEventList::getCommunity, tZhsqEventList.getCommunity()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getGrid())){
			queryWrapper.lambda().and(i -> i.eq(TZhsqEventList::getGrid, tZhsqEventList.getGrid()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getSponsor())){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getSponsor, tZhsqEventList.getSponsor()));
		}
		if(tZhsqEventList.getEventStartingTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getEventStartingTime, tZhsqEventList.getEventStartingTime()));
		}
		if(tZhsqEventList.getEventEndTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getEventEndTime, tZhsqEventList.getEventEndTime()));
		}
		if(StringUtils.isNotBlank(tZhsqEventList.getExecutor())){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getExecutor, tZhsqEventList.getExecutor()));
		}
		if(tZhsqEventList.getEventStatus() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getEventStatus, tZhsqEventList.getEventStatus()));
		}
		if(tZhsqEventList.getPosition() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getPosition, tZhsqEventList.getPosition()));
		}
		if(tZhsqEventList.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getCreateTime, tZhsqEventList.getCreateTime()));
		}
		if(tZhsqEventList.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getCreateId, tZhsqEventList.getCreateId()));
		}
		if(tZhsqEventList.getUpdateTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getUpdateTime, tZhsqEventList.getUpdateTime()));
		}
		if(tZhsqEventList.getUpdateId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getUpdateId, tZhsqEventList.getUpdateId()));
		}
		if(tZhsqEventList.getIsDelete() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqEventList::getIsDelete, tZhsqEventList.getIsDelete()));
		}
		if(searchVo!=null){
			if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
				queryWrapper.lambda().and(i -> i.between(TZhsqEventList::getEventEndTime, searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.lambda().and(i -> i.eq(TZhsqEventList::getIsDelete, 0));
		return queryWrapper;

}
}
