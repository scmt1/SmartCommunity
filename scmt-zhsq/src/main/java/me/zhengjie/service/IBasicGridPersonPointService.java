package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGridPersonPoint;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *@author
 **/
public interface IBasicGridPersonPointService extends IService<BasicGridPersonPoint> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getBasicGridPersonPointById(String id);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryBasicGridPersonPointListByPage(BasicGridPersonPoint basicGridPersonPoint, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param basicGridPersonPoint 查询参数
	* @param response response参数
	*/
	public void download(BasicGridPersonPoint basicGridPersonPoint, HttpServletResponse response) ;

	/**
	 * 查询当天网格员坐标数据
	 * @param gridPersonId
	 * @param gridPersonName
	 * @param date
	 * @return
	 */
	List<BasicGridPersonPoint> getToDayGridPersonPoint(String gridPersonId,String gridPersonName, String date);

	/**
	 * 查询网格人员最新的坐标数据
	 * @param gridPersonId
	 * @return
	 */
    BasicGridPersonPoint getCurrentGridPersonPoint(String gridPersonId);
}
