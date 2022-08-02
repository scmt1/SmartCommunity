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
import me.zhengjie.dao.mapper.ActivityMapper;
import me.zhengjie.dao.mapper.TFormGeneratorMapper;
import me.zhengjie.dao.service.ActivityService;
import me.zhengjie.dao.service.ITFormGeneratorService;
import me.zhengjie.domain.ColumnInfo;
import me.zhengjie.service.GeneratorService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *@author 
 **/
@Service
public class ActivityServiceImpl extends ServiceImpl<TFormGeneratorMapper, TFormGenerator> implements ActivityService {
	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private GeneratorService generatorService;

	@Override
	public boolean AddActivity(Map<String, Object> map) {
		if(map==null||!map.containsKey("tableName") ||!map.containsKey("model")){
			return false;
		}
		//获取表名
		String tableName = map.get("tableName").toString();
		if(StringUtils.isBlank(tableName)){
			return false;
		}
		//获取要插入的数据
		LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) map.get("model");
		if(linkedHashMap==null){
			return false;
		}
		//筛选真实的之字段
		List<ColumnInfo> columns = generatorService.getColumns(tableName);//通过表名获取字段
		List<String> columnNameList=columns.stream().map(ColumnInfo::getColumnName).collect(Collectors.toList());
		Map<String, Object> model = new HashMap<>();
		for (String key : linkedHashMap.keySet()) {
			if(columnNameList.contains(key)){
				model.put(key,linkedHashMap.get(key));
			}

		}
		if(map.size()<0){
			return false;
		}
		int i = activityMapper.AddActivity(model, tableName);
		if(i>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean UpdateActivity(Map<String, Object> map) {
		if(map==null||!map.containsKey("tableName") ||!map.containsKey("model")){
			return false;
		}
		//获取表名
		String tableName = map.get("tableName").toString();
		if(StringUtils.isBlank(tableName)){
			return false;
		}
		//获取要插入的数据
		LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) map.get("model");
		if(linkedHashMap==null || !linkedHashMap.containsKey("id")){
			return false;
		}
		//筛选真实的之字段
		List<ColumnInfo> columns = generatorService.getColumns(tableName);//通过表名获取字段
		List<String> columnNameList=columns.stream().map(ColumnInfo::getColumnName).collect(Collectors.toList());
		Map<String, Object> model = new HashMap<>();
		for (String key : linkedHashMap.keySet()) {
			if(columnNameList.contains(key) && linkedHashMap.get(key)!=null && StringUtils.isNotBlank(linkedHashMap.get(key).toString())){
				model.put(key,linkedHashMap.get(key));
			}
		}
		if(model.size()<0){
			return false;
		}
		int i = activityMapper.UpdateActivity(model, tableName);
		if(i>0){
			return true;
		}
		return false;
	}

	@Override
	public Map<String ,Object> getActivityById(String id, String tableName) {

		if(StringUtils.isBlank(tableName)){
			return null;
		}
		//通过表名获取字段
		List<ColumnInfo> columns = generatorService.getColumns(tableName);

		return activityMapper.getActivityById(id,tableName,columns);
	}
}