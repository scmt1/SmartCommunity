package me.zhengjie.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TShooting;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 *@author
 **/
public interface ITShootingService extends IService<TShooting> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getTShootingById(String id);

	/**
	* 功能描述：实现分页查询
	* @param tShooting 需要模糊查询的信息
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryTShootingListByPage(TShooting tShooting, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param tShooting 查询参数
	* @param response response参数
	*/
	public void download(TShooting tShooting, HttpServletResponse response);
}