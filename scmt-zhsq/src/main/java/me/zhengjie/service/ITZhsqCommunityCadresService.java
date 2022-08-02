package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqCommunityCadres;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *@author
 **/
public interface ITZhsqCommunityCadresService extends IService<TZhsqCommunityCadres> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getTZhsqCommunityCadresById(String id);

	/**
	* 功能描述：实现分页查询
	* @param tZhsqCommunityCadres 需要模糊查询的信息
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryTZhsqCommunityCadresListByPage(TZhsqCommunityCadres tZhsqCommunityCadres, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param tZhsqCommunityCadres 查询参数
	* @param response response参数
	*/
	public void download(TZhsqCommunityCadres tZhsqCommunityCadres, HttpServletResponse response) ;

	/**
	 * 模糊查询所有
	 * @param tZhsqCommunityCadres
	 * @return
	 */
    Result<Object> queryCommunityCadresList(TZhsqCommunityCadres tZhsqCommunityCadres);

    int getCountByDept(Integer deptId);

	/**
	 * 功能描述：Excel导入信息数据
	 *
	 * @return 返回获取结果
	 */
	Result<Object> importExcel(MultipartFile file) throws Exception;
}
