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

import me.zhengjie.mapper.RelaPersonRelativesMapper;
import me.zhengjie.entity.RelaPersonRelatives;
import me.zhengjie.service.IRelaPersonRelativesService;
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
 *@author
 **/
@Service
@AllArgsConstructor
public class RelaPersonRelativesServiceImpl extends ServiceImpl<RelaPersonRelativesMapper, RelaPersonRelatives> implements IRelaPersonRelativesService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final RelaPersonRelativesMapper relaPersonRelativesMapper;

	public Result<Object> getRelaPersonRelativesById(String id){
		RelaPersonRelatives relaPersonRelatives = relaPersonRelativesMapper.selectById(id);
		if(relaPersonRelatives!=null){
			return  ResultUtil.data(relaPersonRelatives);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryRelaPersonRelativesListByPage(RelaPersonRelatives  relaPersonRelatives, SearchVo searchVo, PageVo pageVo){
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
		Page<RelaPersonRelatives> pageData = new Page<>(page, limit);
		QueryWrapper<RelaPersonRelatives> queryWrapper = new QueryWrapper<>();
		if (relaPersonRelatives !=null) {
			queryWrapper = LikeAllFeild(relaPersonRelatives,searchVo);
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
		IPage<RelaPersonRelatives> result = relaPersonRelativesMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(RelaPersonRelatives relaPersonRelatives, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<RelaPersonRelatives> queryWrapper = new QueryWrapper<>();
		if (relaPersonRelatives !=null) {
			queryWrapper = LikeAllFeild(relaPersonRelatives,null);
		}
		List<RelaPersonRelatives> list = relaPersonRelativesMapper.selectList(queryWrapper);
		for (RelaPersonRelatives re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param relaPersonRelatives 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<RelaPersonRelatives>  LikeAllFeild(RelaPersonRelatives  relaPersonRelatives, SearchVo searchVo) {
		QueryWrapper<RelaPersonRelatives> queryWrapper = new QueryWrapper<>();
		if(relaPersonRelatives.getId() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getId, relaPersonRelatives.getId()));
		}
		if(relaPersonRelatives.getPersonId() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getPersonId, relaPersonRelatives.getPersonId()));
		}
		if(relaPersonRelatives.getName() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getName, relaPersonRelatives.getName()));
		}
		if(relaPersonRelatives.getPhone() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getPhone, relaPersonRelatives.getPhone()));
		}
		if(relaPersonRelatives.getRelationship() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getRelationship, relaPersonRelatives.getRelationship()));
		}
		if(relaPersonRelatives.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getCreateId, relaPersonRelatives.getCreateId()));
		}
		if(relaPersonRelatives.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(RelaPersonRelatives::getCreateTime, relaPersonRelatives.getCreateTime()));
		}
		return queryWrapper;

}
}
