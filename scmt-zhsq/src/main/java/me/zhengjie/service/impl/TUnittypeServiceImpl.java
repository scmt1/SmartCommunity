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
import me.zhengjie.entity.TUnittype;
import me.zhengjie.mapper.TUnittypeMapper;
import me.zhengjie.service.ITUnittypeService;
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
public class TUnittypeServiceImpl extends ServiceImpl<TUnittypeMapper, TUnittype> implements ITUnittypeService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TUnittypeMapper tUnittypeMapper;

	@Override
	public Result<Object> getTUnittypeById(String id){
		TUnittype tUnittype = tUnittypeMapper.selectById(id);
		if(tUnittype!=null){
			return  ResultUtil.data(tUnittype);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public List<TUnittype> queryTUnittypeTreeByPage(JSONObject query) {
		List<TUnittype> tUnittypeNotPage = tUnittypeMapper.loadNotPage(query);
		if(tUnittypeNotPage!=null){
			tUnittypeNotPage.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TUnittype> TUnittypes = tUnittypeMapper.selectDeptTreeByParentId(dept.getId());
					dept.setChildren(TUnittypes);
					getChildren(TUnittypes);
				}
			});
		}
		return tUnittypeNotPage;
	}

	public void getChildren( List<TUnittype> tUnittype){
		if(tUnittype!=null && tUnittype.size()>0){
			tUnittype.forEach(dept -> {
				if ( dept.getId()!="0"){
					List<TUnittype> tUnittypeList = tUnittypeMapper.selectDeptTreeByParentId(dept.getId());
					if(tUnittypeList!=null && tUnittypeList.size()>0){
						dept.setChildren(tUnittypeList);
						getChildren(tUnittypeList);
					}
				}
			});
		}

	}

	@Override
	public Result<Object> queryTUnittypeListByPage(TUnittype  tUnittype, SearchVo searchVo, PageVo pageVo){
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
		Page<TUnittype> pageData = new Page<>(page, limit);
		QueryWrapper<TUnittype> queryWrapper = new QueryWrapper<>();
		if (tUnittype !=null) {
			queryWrapper = LikeAllFeild(tUnittype,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_unittype."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_unittype."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_unittype.create_time");
		}
		IPage<TUnittype> result = tUnittypeMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TUnittype tUnittype, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TUnittype> queryWrapper = new QueryWrapper<>();
		if (tUnittype !=null) {
			queryWrapper = LikeAllFeild(tUnittype,null);
		}
		List<TUnittype> list = tUnittypeMapper.selectList(queryWrapper);
		for (TUnittype re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("名称", re.getName());
			map.put("级别 1类别-0分类", re.getLevel());
			map.put("代码", re.getCode());
			map.put("排序码", re.getSortCode());
			map.put("父id", re.getPid());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tUnittype 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TUnittype>  LikeAllFeild(TUnittype  tUnittype, SearchVo searchVo) {
		QueryWrapper<TUnittype> queryWrapper = new QueryWrapper<>();
		if(tUnittype.getId() != null){
			queryWrapper.and(i -> i.like("t_unittype.id", tUnittype.getId()));
		}
		if(tUnittype.getName() != null){
			queryWrapper.and(i -> i.like("t_unittype.name", tUnittype.getName()));
		}
		if(tUnittype.getLevel() != null){
			queryWrapper.and(i -> i.like("t_unittype.level", tUnittype.getLevel()));
		}
		if(tUnittype.getCode() != null){
			queryWrapper.and(i -> i.like("t_unittype.code", tUnittype.getCode()));
		}
		if(tUnittype.getSortCode() != null){
			queryWrapper.and(i -> i.like("t_unittype.sort_code", tUnittype.getSortCode()));
		}
		if(tUnittype.getPid() != null){
			queryWrapper.and(i -> i.like("t_unittype.pid", tUnittype.getPid()));
		}
		if(tUnittype.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_unittype.is_delete", tUnittype.getIsDelete()));
		}
		if(tUnittype.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_unittype.create_id", tUnittype.getCreateId()));
		}
		if(tUnittype.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_unittype.create_time", tUnittype.getCreateTime()));
		}
		if(tUnittype.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_unittype.update_id", tUnittype.getUpdateId()));
		}
		if(tUnittype.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_unittype.update_time", tUnittype.getUpdateTime()));
		}
		if(tUnittype.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_unittype.delete_id", tUnittype.getDeleteId()));
		}
		if(tUnittype.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_unittype.delete_time", tUnittype.getDeleteTime()));
		}
		if(tUnittype.getIsLabel() != null){
			queryWrapper.and(i -> i.like("t_unittype.is_label", tUnittype.getIsLabel()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_unittype.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_unittype.is_delete", 0));
		return queryWrapper;
	
}
}