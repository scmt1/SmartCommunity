package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TRiskSources;
import me.zhengjie.dao.mapper.TRiskSourcesMapper;
import me.zhengjie.dao.service.ITRiskSourcesService;
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
public class TRiskSourcesServiceImpl extends ServiceImpl<TRiskSourcesMapper, TRiskSources> implements ITRiskSourcesService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TRiskSourcesMapper tRiskSourcesMapper;

	@Override
	public Result<Object> getTRiskSourcesById(String id){
		TRiskSources tRiskSources = tRiskSourcesMapper.selectById(id);
		if(tRiskSources!=null){
			return  ResultUtil.data(tRiskSources);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTRiskSourcesListByPage(String search, SearchVo searchVo, PageVo pageVo){
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
		Page<TRiskSources> pageData = new Page<>(page, limit);
		QueryWrapper<TRiskSources> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(search)) {
			queryWrapper = LikeAllFeild(search);
		}
		IPage<TRiskSources> result = tRiskSourcesMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(String likeValue, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TRiskSources> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(likeValue)) {
			queryWrapper = LikeAllFeild(likeValue);
		}
		List<TRiskSources> list = tRiskSourcesMapper.selectList(queryWrapper);
		for (TRiskSources re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("风险类型 对应字典表主键", re.getType());
			map.put("风险名称", re.getName());
			map.put("风险图片", re.getImgPath());
			map.put("风险描述", re.getDescription());
			map.put("所处地点", re.getAddress());
			map.put("风险等级", re.getRiskLevel());
			map.put("可能导致事故", re.getMayCause());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param likeValue 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TRiskSources>  LikeAllFeild(String likeValue) {
		QueryWrapper<TRiskSources> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getType, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getName, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getIsDelete, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getImgPath, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getDescription, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getAddress, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getRiskLevel, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getMayCause, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getBak1, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getBak2, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getBak3, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getBak4, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getBak5, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getRemark, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getCreateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getCreateId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getUpdateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getUpdateId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getDeleteId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TRiskSources::getDeleteTime, likeValue));
		queryWrapper.lambda().and(i -> i.eq(TRiskSources::getIsDelete, 0));
		return queryWrapper;

}
}
