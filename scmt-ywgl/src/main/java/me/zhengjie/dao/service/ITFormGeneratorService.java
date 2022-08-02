package me.zhengjie.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TFormGenerator;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author 
 **/
public interface ITFormGeneratorService extends IService<TFormGenerator> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getTFormGeneratorById(String id);

	/**
	 * 功能描述：根据主键来获取数据
	 * @param id 主键
	 * @return 返回获取结果
	 */
	public TFormGenerator getOneTFormGenerator(TFormGenerator tFormGenerator);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryTFormGeneratorListByPage(TFormGenerator tFormGenerator, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param tFormGenerator 查询参数
	* @param response response参数
	*/
	public void download(TFormGenerator tFormGenerator, HttpServletResponse response) ;

	/**
	 * 功能描述：实现动态查询
	 * @param searchVo 排序参数
	 * @return 返回获取结果
	 */
	public List<TFormGenerator> queryTFormGeneratorList(TFormGenerator tFormGenerator, SearchVo searchVo);
}
