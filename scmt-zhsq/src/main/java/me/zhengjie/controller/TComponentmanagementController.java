package me.zhengjie.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.entity.GridDept;
import me.zhengjie.system.service.dto.DeptDto;
import me.zhengjie.system.service.dto.DeptQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.service.ITComponentmanagementService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author 
 **/
@RestController
@Api(tags =" tComponentmanagement数据接口")
@RequestMapping("/api/tComponentmanagement")
public class TComponentmanagementController{
	@Autowired
	private ITComponentmanagementService tComponentmanagementService;
	@Autowired
	private LogService logService;

	/**
	 * 分页查询部件（树级）
	 * @return
	 */
	@Log("分页查询部件")
	@ApiOperation("分页查询部件")
	@PostMapping("/queryTComponentmanagementTreeByPage")
	public Result<Object> queryTComponentmanagementTreeByPage(@RequestBody JSONObject params){
		IPage<TComponentmanagement> tComponentmanagementIPage = tComponentmanagementService.queryTComponentmanagementTreeByPage(params);
		return ResultUtil.data(tComponentmanagementIPage);
	}
	/**
	 * 查询部件 不分页（树级）
	 * @return
	 */
	@Log("不分页查询部件")
	@ApiOperation("不分页查询部件")
	@PostMapping("/queryTComponentmanagementTreeNotPage")
	public Result<Object> queryTComponentmanagementTreeNotPage(@RequestBody JSONObject params){
		List<TComponentmanagement> tComponentmanagementIPage = tComponentmanagementService.loadComponentmanagementTreeNotPage(params);
		return ResultUtil.data(tComponentmanagementIPage);
	}

	/**
	* 功能描述：新增tComponentmanagement数据
	* @param tComponentmanagement 实体
	* @return 返回新增结果
	*/
	@Log("新增tComponentmanagement数据")
	@ApiOperation("新增tComponentmanagement数据")
	@PostMapping("addTComponentmanagement")
	public Result<Object> addTComponentmanagement(@RequestBody TComponentmanagement tComponentmanagement){
		Long time = System.currentTimeMillis();
		try {
			tComponentmanagement.setIsDelete(0);
			tComponentmanagement.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tComponentmanagementService.save(tComponentmanagement);
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
	@Log("根据主键来删除tComponentmanagement数据")
	@ApiOperation("根据主键来删除tComponentmanagement数据")
	@PostMapping("deleteTComponentmanagement")
	public Result<Object> deleteTComponentmanagement(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tComponentmanagementService.removeByIds(Arrays.asList(ids));
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
	@Log("根据主键来获取tComponentmanagement数据")
	@ApiOperation("根据主键来获取tComponentmanagement数据")
	@GetMapping("getTComponentmanagement")
	public Result<Object> getTComponentmanagement(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TComponentmanagement res = tComponentmanagementService.getById(id);
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
	* @param tComponentmanagement 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询tComponentmanagement数据")
	@ApiOperation("分页查询tComponentmanagement数据")
	@GetMapping("queryTComponentmanagementList")
	public Result<Object> queryTComponentmanagementList(TComponentmanagement  tComponentmanagement, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tComponentmanagementService.queryTComponentmanagementListByPage(tComponentmanagement, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tComponentmanagement 实体
	* @return 返回更新结果
	*/
	@Log("更新tComponentmanagement数据")
	@ApiOperation("更新tComponentmanagement数据")
	@PostMapping("updateTComponentmanagement")
	public Result<Object> updateTComponentmanagement(@RequestBody TComponentmanagement tComponentmanagement){
		if (StringUtils.isBlank(tComponentmanagement.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tComponentmanagement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tComponentmanagementService.updateById(tComponentmanagement);
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
	* @param tComponentmanagement 查询参数
	* @return 
	*/
	@Log("导出tComponentmanagement数据")
	@ApiOperation("导出tComponentmanagement数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TComponentmanagement  tComponentmanagement){
		Long time = System.currentTimeMillis();
		try {
			tComponentmanagementService.download( tComponentmanagement,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}