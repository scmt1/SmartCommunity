package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TFormGenerator;
import me.zhengjie.dao.mapper.TFormGeneratorMapper;
import me.zhengjie.dao.service.ITFormGeneratorService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *@author
 **/
@Service
public class TFormGeneratorServiceImpl extends ServiceImpl<TFormGeneratorMapper, TFormGenerator> implements ITFormGeneratorService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TFormGeneratorMapper tFormGeneratorMapper;


	public Result<Object> getTFormGeneratorById(String id){
		TFormGenerator tFormGenerator = tFormGeneratorMapper.selectById(id);
		if(tFormGenerator!=null){
			return  ResultUtil.data(tFormGenerator);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public TFormGenerator getOneTFormGenerator(TFormGenerator tFormGenerator) {
		QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();
		if (tFormGenerator !=null) {
			queryWrapper = LikeAllFeild(tFormGenerator,null);
		}
		TFormGenerator tFormGenerator1 = tFormGeneratorMapper.selectOne(queryWrapper);
		return  tFormGenerator1;
	}

	@Override
	public Result<Object> queryTFormGeneratorListByPage(TFormGenerator  tFormGenerator, SearchVo searchVo, PageVo pageVo){
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
		Page<TFormGenerator> pageData = new Page<>(page, limit);
		QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();
		if (tFormGenerator !=null) {
			queryWrapper = LikeAllFeild(tFormGenerator,searchVo);
		}
		IPage<TFormGenerator> result = tFormGeneratorMapper.selectListPage(queryWrapper, pageData);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TFormGenerator tFormGenerator, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();
		if (tFormGenerator !=null) {
			queryWrapper = LikeAllFeild(tFormGenerator,null);
		}
		List<TFormGenerator> list = tFormGeneratorMapper.selectList(queryWrapper);
		for (TFormGenerator re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("表单名称", re.getFormName());
			map.put("生成的json字符串", re.getJsonStr());
			map.put("创建时间", re.getCreateTime());
			map.put("是否删除", re.getIsDelete());
			map.put("删除时间", re.getDeleteTime());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	@Override
	public List<TFormGenerator> queryTFormGeneratorList(TFormGenerator tFormGenerator, SearchVo searchVo) {
		QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();
		if (tFormGenerator !=null) {
			queryWrapper = LikeAllFeild(tFormGenerator,null);
		}
		List<TFormGenerator> list = tFormGeneratorMapper.selectList(queryWrapper);
		return list;
	}

	/**
	* 功能描述：构建模糊查询
	* @param tFormGenerator 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TFormGenerator>  LikeAllFeild(TFormGenerator  tFormGenerator, SearchVo searchVo) {
		QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();

		if(StringUtils.isNotBlank(tFormGenerator.getId())){
			queryWrapper.lambda().and(i -> i.eq(TFormGenerator::getId, tFormGenerator.getId()));
		}
		if(StringUtils.isNotBlank(tFormGenerator.getFormName()) ){
			queryWrapper.lambda().and(i -> i.like(TFormGenerator::getFormName, tFormGenerator.getFormName()));
		}
		if(StringUtils.isNotBlank(tFormGenerator.getJsonStr())){
			queryWrapper.lambda().and(i -> i.like(TFormGenerator::getJsonStr, tFormGenerator.getJsonStr()));
		}
		if(StringUtils.isNotBlank(tFormGenerator.getProcDefId())){
			queryWrapper.lambda().and(i -> i.eq(TFormGenerator::getProcDefId, tFormGenerator.getProcDefId()));
		}
		if(StringUtils.isNotBlank(tFormGenerator.getProcDefName())){
			queryWrapper.lambda().and(i -> i.like(TFormGenerator::getProcDefName, tFormGenerator.getProcDefName()));
		}
		if(StringUtils.isNotBlank(tFormGenerator.getRemarke())){
			queryWrapper.lambda().and(i -> i.like(TFormGenerator::getRemarke, tFormGenerator.getRemarke()));
		}
		if(searchVo!=null){
			if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
				queryWrapper.lambda().and(i -> i.between(TFormGenerator::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
			}
			if(StringUtils.isNotBlank(searchVo.getSearchInfo())){
				queryWrapper.lambda().and(i -> i.like(TFormGenerator::getFormName, searchVo.getSearchInfo()));
			}
		}
		queryWrapper.lambda().and(i -> i.eq(TFormGenerator::getIsDelete, 0));

		return queryWrapper;

}
}
