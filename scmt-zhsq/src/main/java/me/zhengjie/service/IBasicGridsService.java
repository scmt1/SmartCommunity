package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGrids;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface IBasicGridsService extends IService<BasicGrids> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getBasicGridsById(String id);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryBasicGridsListByPage(BasicGrids basicGrids, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param basicGrids 查询参数
	* @param response response参数
	*/
	public void download(BasicGrids basicGrids, HttpServletResponse response) ;

	/**
	 * 功能描述：模糊查询所有信息
	 * @return 返回获取结果
	 */
	public Result<Object> queryAllList(BasicGrids basicGrids);


	/***
	 * 根据人员Id查询所管理的网格
	 */
	List<BasicGrids> queryMyManagedGridsList(String personId);

	/**
	 * 根据网格Id查询该网格下个网格员、建筑、房屋等。。
	 * @param gridsId
	 * @return
	 */
	List<Map<String, Object>> queryGridsOwnInformation(String gridsId);

	/**
	 * 查询该网格下有多少个网格员
	 * @param gridsId
	 * @return
	 */
	List<Map<String, Object>> queryGridmanList(String gridsId);

	/***
	 * 查询网格区域划分树
	 */
	List<Map<String, Object>> queryAllGridsTree();

	/**
	 * 功能描述：Excel导入信息数据
	 *
	 * @return 返回获取结果
	 */
	Result<Object> importExcel(MultipartFile file) throws Exception;


}
