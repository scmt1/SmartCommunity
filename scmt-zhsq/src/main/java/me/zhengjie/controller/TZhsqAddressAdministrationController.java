package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAddressAdministration;
import me.zhengjie.service.ITZhsqAddressAdministrationService;
import me.zhengjie.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 *@author 
 **/
@RestController
@Api(tags =" me.zhengjie数据接口")
@RequestMapping("/api/tZhsqAddressAdministration")
public class TZhsqAddressAdministrationController{
	@Autowired
	private ITZhsqAddressAdministrationService tZhsqAddressAdministrationService;
	@Autowired
	private LogService logService;

	/**
	* 功能描述：新增me.zhengjie数据
	* @param tZhsqAddressAdministration 实体
	* @return 返回新增结果
	*/
	@Log("新增me.zhengjie数据")
	@ApiOperation("新增me.zhengjie数据")
	@PostMapping("addTZhsqAddressAdministration")
	public Result<Object> addTZhsqAddressAdministration(@RequestBody TZhsqAddressAdministration tZhsqAddressAdministration){
		Long time = System.currentTimeMillis();
		try {
			tZhsqAddressAdministration.setIsDelete(0);
			tZhsqAddressAdministration.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tZhsqAddressAdministrationService.save(tZhsqAddressAdministration);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：根据主键来删除数据
	* @param ids 主键集合
	* @return 返回删除结果
	*/
	@Log("根据主键来删除me.zhengjie数据")
	@ApiOperation("根据主键来删除me.zhengjie数据")
	@PostMapping("deleteTZhsqAddressAdministration")
	public Result<Object> deleteTZhsqAddressAdministration(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tZhsqAddressAdministrationService.removeByIds(Arrays.asList(ids));
			if (res) {
				return ResultUtil.data(res, "删除成功");
			} else {
				return ResultUtil.error( "删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("删除异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	@Log("根据主键来获取me.zhengjie数据")
	@ApiOperation("根据主键来获取me.zhengjie数据")
	@GetMapping("getTZhsqAddressAdministration")
	public Result<Object> getTZhsqAddressAdministration(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TZhsqAddressAdministration res = tZhsqAddressAdministrationService.getById(id);
			if (res != null) {
				return ResultUtil.data(res, "查询成功");
			} else {
				return ResultUtil.error("查询失败:暂无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：实现分页查询
	* @param tZhsqAddressAdministration 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询me.zhengjie数据")
	@ApiOperation("分页查询me.zhengjie数据")
	@GetMapping("queryTZhsqAddressAdministrationList")
	public Result<Object> queryTZhsqAddressAdministrationList(TZhsqAddressAdministration  tZhsqAddressAdministration, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tZhsqAddressAdministrationService.queryTZhsqAddressAdministrationListByPage(tZhsqAddressAdministration, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tZhsqAddressAdministration 实体
	* @return 返回更新结果
	*/
	@Log("更新me.zhengjie数据")
	@ApiOperation("更新me.zhengjie数据")
	@PostMapping("updateTZhsqAddressAdministration")
	public Result<Object> updateTZhsqAddressAdministration(@RequestBody TZhsqAddressAdministration tZhsqAddressAdministration){
		if (StringUtils.isBlank(tZhsqAddressAdministration.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tZhsqAddressAdministration.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tZhsqAddressAdministrationService.updateById(tZhsqAddressAdministration);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：导出数据
	* @param response 请求参数
	* @param tZhsqAddressAdministration 查询参数
	* @return 
	*/
	@Log("导出me.zhengjie数据")
	@ApiOperation("导出me.zhengjie数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TZhsqAddressAdministration  tZhsqAddressAdministration){
		Long time = System.currentTimeMillis();
		try {
			tZhsqAddressAdministrationService.download( tZhsqAddressAdministration,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}

	/**
	 * 功能描述：实现树查询
	 * @return 返回获取结果
	 */
	@Log("分页查询me.zhengjie数据")
	@ApiOperation("分页查询me.zhengjie数据")
	@GetMapping("listWithTree")
	public Result<Object> listWithTree(){
		Long time = System.currentTimeMillis();
		try {
			List<TZhsqAddressAdministration> res = tZhsqAddressAdministrationService.listWithTree();
			return ResultUtil.data(res, "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

}