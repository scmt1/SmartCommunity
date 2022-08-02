package me.zhengjie.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TFormGenerator;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author 
 **/
public interface ActivityService extends IService<TFormGenerator> {

	/**
	 * 根据数据字段新增流程表单数据
	 * @param map 数据字段
	 * @return
	 */
	public boolean AddActivity(Map<String,Object> map);

	/**
	 * 根据数据字段编辑流程表单数据
	 * @param map 数据字段
	 * @return
	 */
	public boolean UpdateActivity(Map<String,Object> map);

	/**
	 * 根据id查询流程表单数据
	 * @param id id
	 * @param tableName 表名
	 * @return
	 */
	public Map<String ,Object> getActivityById(String id,String tableName);
}
