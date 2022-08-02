package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TEarlywarning;
import me.zhengjie.mapper.TEarlywarningMapper;
import me.zhengjie.service.ITEarlywarningService;
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
public class TEarlywarningServiceImpl extends ServiceImpl<TEarlywarningMapper, TEarlywarning> implements ITEarlywarningService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TEarlywarningMapper tEarlywarningMapper;

	@Override
	public Result<Object> getTEarlywarningById(String id){
		TEarlywarning tEarlywarning = tEarlywarningMapper.selectById(id);
		if(tEarlywarning!=null){
			return  ResultUtil.data(tEarlywarning);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTEarlywarningListByPage(TEarlywarning  tEarlywarning, SearchVo searchVo, PageVo pageVo){
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
		Page<TEarlywarning> pageData = new Page<>(page, limit);
		QueryWrapper<TEarlywarning> queryWrapper = new QueryWrapper<>();
		if (tEarlywarning !=null) {
			queryWrapper = LikeAllFeild(tEarlywarning,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_earlywarning."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_earlywarning."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_earlywarning.create_time");
		}
//		IPage<TEarlywarning> result = tEarlywarningMapper.selectPage(pageData, queryWrapper);
		IPage<TEarlywarning> result = tEarlywarningMapper.selectTEarlywarningPageList(queryWrapper, pageData);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TEarlywarning tEarlywarning, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TEarlywarning> queryWrapper = new QueryWrapper<>();
		if (tEarlywarning !=null) {
			queryWrapper = LikeAllFeild(tEarlywarning,null);
		}
		List<TEarlywarning> list = tEarlywarningMapper.selectList(queryWrapper);
		for (TEarlywarning re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("预警内容", re.getEarlywarningContent());
			map.put("所属社区", re.getCommunity());
			map.put("预警来源", re.getEarlywarningSource());
			map.put("处理状态", re.getProcessingStatus());
			map.put("创建时间", re.getCreateTime());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tEarlywarning 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TEarlywarning>  LikeAllFeild(TEarlywarning  tEarlywarning, SearchVo searchVo) {
		QueryWrapper<TEarlywarning> queryWrapper = new QueryWrapper<>();
		if(tEarlywarning.getId() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.id", tEarlywarning.getId()));
		}
		if(tEarlywarning.getEarlywarningContent() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.earlywarning_content", tEarlywarning.getEarlywarningContent()));
		}
		if(tEarlywarning.getEarlywarningType() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.earlywarning_type", tEarlywarning.getEarlywarningType()));
		}
		if(tEarlywarning.getKeypersonnelType() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.keypersonnel_type", tEarlywarning.getKeypersonnelType()));
		}
		if(tEarlywarning.getKeypersonnelName() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.keypersonnel_name", tEarlywarning.getKeypersonnelName()));
		}
		if(tEarlywarning.getCommunity() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.community", tEarlywarning.getCommunity()));
		}
		if(tEarlywarning.getEarlywarningSource() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.earlywarning_source", tEarlywarning.getEarlywarningSource()));
		}
		if(tEarlywarning.getProcessingStatus() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.processing_status", tEarlywarning.getProcessingStatus()));
		}
		if(tEarlywarning.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.is_delete", tEarlywarning.getIsDelete()));
		}
		if(tEarlywarning.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.create_id", tEarlywarning.getCreateId()));
		}
		if(tEarlywarning.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.create_time", tEarlywarning.getCreateTime()));
		}
		if(tEarlywarning.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.update_id", tEarlywarning.getUpdateId()));
		}
		if(tEarlywarning.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.update_time", tEarlywarning.getUpdateTime()));
		}
		if(tEarlywarning.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.delete_id", tEarlywarning.getDeleteId()));
		}
		if(tEarlywarning.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.delete_time", tEarlywarning.getDeleteTime()));
		}
		if(tEarlywarning.getIsShow() != null){
			queryWrapper.and(i -> i.like("t_earlywarning.is_show", tEarlywarning.getIsShow()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_earlywarning.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_earlywarning.is_delete", 0));
		return queryWrapper;
	
}
}