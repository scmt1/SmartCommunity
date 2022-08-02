package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicSocialOrganization;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface IBasicSocialOrganizationService extends IService<BasicSocialOrganization> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getBasicSocialOrganizationById(String id);

	/**
	* 功能描述：实现分页查询
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryBasicSocialOrganizationListByPage(BasicSocialOrganization basicSocialOrganization, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param basicSocialOrganization 查询参数
	* @param response response参数
	*/
	public void download(BasicSocialOrganization basicSocialOrganization, HttpServletResponse response) ;

	/**
	 * 社会组织按社区统计
	 * @return
	 */
	List<Map<String, Object>> getCommunityCountData(BasicSocialOrganization basicSocialOrganization);

	/**
	 * 社会组织按网格统计
	 * @return
	 */
	List<Map<String, Object>> getGridsCountData(BasicSocialOrganization basicSocialOrganization);

	/**
	 * 功能描述：实现分页查询
	 *  用于一张图显示
	 * @param key    查询参数
	 * @param pageVo 分页参数
	 * @return 返回获取结果
	 */
    Result<Object> queryBasicSocialOrganizationListByAnyWayWhere(BasicSocialOrganization basicSocialOrganization, String key, PageVo pageVo);
}
