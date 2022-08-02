package me.zhengjie.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TPopulationlabel;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 *@author 
 **/
public interface ITPopulationlabelService extends IService<TPopulationlabel> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getTPopulationlabelById(String id);

	/**
	 * 分页查询树级数据
	 * @return
	 */
	List<TPopulationlabel> queryTPopulationlabelTreeByPage(JSONObject query);

	/**
	* 功能描述：实现分页查询
	* @param tPopulationlabel 需要模糊查询的信息
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryTPopulationlabelListByPage(TPopulationlabel tPopulationlabel, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param tPopulationlabel 查询参数
	* @param response response参数
	*/
	public void download(TPopulationlabel tPopulationlabel, HttpServletResponse response) ;
}
