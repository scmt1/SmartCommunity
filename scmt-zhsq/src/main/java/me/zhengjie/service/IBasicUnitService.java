package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicUnit;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface IBasicUnitService extends IService<BasicUnit> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getBasicUnitById(String id);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryBasicUnitListByPage(BasicUnit basicUnit, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param basicUnit 查询参数
	* @param response response参数
	*/
	public void download(BasicUnit basicUnit, HttpServletResponse response) ;

	/**
	 * 查询当前建筑设施下的单元
	 * @param archiveId
	 * @return
	 */
    List<BasicUnit> queryBasicUnitListByArchiveId(String archiveId);

	/**
	 * 功能描述：查出最大单元数
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getMaxUnit(String id);

	/**
	 * 功能描述：查出楼层数、每层户数
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getFloorAndDoor( String id, Integer name);
}
