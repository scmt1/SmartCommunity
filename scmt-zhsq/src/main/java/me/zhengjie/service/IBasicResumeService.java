package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicResume;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 *@author
 **/
public interface IBasicResumeService extends IService<BasicResume> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getBasicResumeById(String id);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryBasicResumeListByPage(BasicResume basicResume, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param basicResume 查询参数
	* @param response response参数
	*/
	public void download(BasicResume basicResume, HttpServletResponse response) ;

	/**
	 * 查询当前社区干部最后一个履历
	 * @param id
	 * @return
	 */
    BasicResume queryLastOneData(String id);

	/**
	 * 根据社区干部Id,查询履历
	 * @param personId
	 * @return
	 */
	List<BasicResume> queryBasicResumeListByPersonId(String personId);
}
