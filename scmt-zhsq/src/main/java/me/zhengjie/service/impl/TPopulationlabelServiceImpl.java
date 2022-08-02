package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.entity.TPopulationlabel;
import me.zhengjie.mapper.TPopulationlabelMapper;
import me.zhengjie.service.ITPopulationlabelService;
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
public class TPopulationlabelServiceImpl extends ServiceImpl<TPopulationlabelMapper, TPopulationlabel> implements ITPopulationlabelService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TPopulationlabelMapper tPopulationlabelMapper;

	@Override
	public Result<Object> getTPopulationlabelById(String id){
		TPopulationlabel tPopulationlabel = tPopulationlabelMapper.selectById(id);
		if(tPopulationlabel!=null){
			return  ResultUtil.data(tPopulationlabel);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public List<TPopulationlabel> queryTPopulationlabelTreeByPage(JSONObject query) {
		List<TPopulationlabel> tPopulationlabelNotPage = tPopulationlabelMapper.loadNotPage(query);
		if(tPopulationlabelNotPage!=null){
			tPopulationlabelNotPage.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TPopulationlabel> TPopulationlabels = tPopulationlabelMapper.selectDeptTreeByParentId(dept.getId());
					dept.setChildren(TPopulationlabels);
					getChildren(TPopulationlabels);
				}
			});
		}
		return tPopulationlabelNotPage;
	}

	public void getChildren( List<TPopulationlabel> tPopulationlabel){
		if(tPopulationlabel!=null && tPopulationlabel.size()>0){
			tPopulationlabel.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TPopulationlabel> tPopulationlabelList = tPopulationlabelMapper.selectDeptTreeByParentId(dept.getId());
					if(tPopulationlabelList!=null && tPopulationlabelList.size()>0){
						dept.setChildren(tPopulationlabelList);
						getChildren(tPopulationlabelList);
					}
				}
			});
		}

	}

	@Override
	public Result<Object> queryTPopulationlabelListByPage(TPopulationlabel  tPopulationlabel, SearchVo searchVo, PageVo pageVo){
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
		Page<TPopulationlabel> pageData = new Page<>(page, limit);
		QueryWrapper<TPopulationlabel> queryWrapper = new QueryWrapper<>();
		if (tPopulationlabel !=null) {
			queryWrapper = LikeAllFeild(tPopulationlabel,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_populationlabel."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_populationlabel."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_populationlabel.create_time");
		}
		IPage<TPopulationlabel> result = tPopulationlabelMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TPopulationlabel tPopulationlabel, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TPopulationlabel> queryWrapper = new QueryWrapper<>();
		if (tPopulationlabel !=null) {
			queryWrapper = LikeAllFeild(tPopulationlabel,null);
		}
		List<TPopulationlabel> list = tPopulationlabelMapper.selectList(queryWrapper);
		for (TPopulationlabel re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("名称", re.getName());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tPopulationlabel 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TPopulationlabel>  LikeAllFeild(TPopulationlabel  tPopulationlabel, SearchVo searchVo) {
		QueryWrapper<TPopulationlabel> queryWrapper = new QueryWrapper<>();
		if(tPopulationlabel.getId() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.id", tPopulationlabel.getId()));
		}
		if(tPopulationlabel.getName() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.name", tPopulationlabel.getName()));
		}
		if(tPopulationlabel.getPid() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.pid", tPopulationlabel.getPid()));
		}
		if(tPopulationlabel.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.is_delete", tPopulationlabel.getIsDelete()));
		}
		if(tPopulationlabel.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.create_id", tPopulationlabel.getCreateId()));
		}
		if(tPopulationlabel.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.create_time", tPopulationlabel.getCreateTime()));
		}
		if(tPopulationlabel.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.update_id", tPopulationlabel.getUpdateId()));
		}
		if(tPopulationlabel.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.update_time", tPopulationlabel.getUpdateTime()));
		}
		if(tPopulationlabel.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.delete_id", tPopulationlabel.getDeleteId()));
		}
		if(tPopulationlabel.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.delete_time", tPopulationlabel.getDeleteTime()));
		}
		if(tPopulationlabel.getIsLabel() != null){
			queryWrapper.and(i -> i.like("t_populationlabel.is_label", tPopulationlabel.getIsLabel()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_populationlabel.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_populationlabel.is_delete", 0));
		return queryWrapper;
	
}
}