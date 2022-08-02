package me.zhengjie.controller;

import java.util.Arrays;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.entity.TEmergencyhedging;
import me.zhengjie.service.ITEmergencyhedgingService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author 
 **/
@RestController
@Api(tags =" me.zhengjie数据接口")
@RequestMapping("/api/tEmergencyhedging")
public class TEmergencyhedgingController{
	@Autowired
	private ITEmergencyhedgingService tEmergencyhedgingService;
	@Autowired
	private LogService logService;

	/**
	* 功能描述：新增me.zhengjie数据
	* @param tEmergencyhedging 实体
	* @return 返回新增结果
	*/
	@Log("新增me.zhengjie数据")
	@ApiOperation("新增me.zhengjie数据")
	@PostMapping("addTEmergencyhedging")
	public Result<Object> addTEmergencyhedging(@RequestBody TEmergencyhedging tEmergencyhedging){
		Long time = System.currentTimeMillis();
		try {
			tEmergencyhedging.setIsDelete(0);
			tEmergencyhedging.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tEmergencyhedgingService.save(tEmergencyhedging);
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
	@PostMapping("deleteTEmergencyhedging")
	public Result<Object> deleteTEmergencyhedging(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tEmergencyhedgingService.removeByIds(Arrays.asList(ids));
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
	@GetMapping("getTEmergencyhedging")
	public Result<Object> getTEmergencyhedging(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TEmergencyhedging res = tEmergencyhedgingService.getById(id);
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
	* @param tEmergencyhedging 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询me.zhengjie数据")
	@ApiOperation("分页查询me.zhengjie数据")
	@GetMapping("queryTEmergencyhedgingList")
	public Result<Object> queryTEmergencyhedgingList(TEmergencyhedging  tEmergencyhedging, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tEmergencyhedgingService.queryTEmergencyhedgingListByPage(tEmergencyhedging, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tEmergencyhedging 实体
	* @return 返回更新结果
	*/
	@Log("更新me.zhengjie数据")
	@ApiOperation("更新me.zhengjie数据")
	@PostMapping("updateTEmergencyhedging")
	public Result<Object> updateTEmergencyhedging(@RequestBody TEmergencyhedging tEmergencyhedging){
		if (StringUtils.isBlank(tEmergencyhedging.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tEmergencyhedging.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tEmergencyhedgingService.updateById(tEmergencyhedging);
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
	* @param tEmergencyhedging 查询参数
	* @return 
	*/
	@Log("导出me.zhengjie数据")
	@ApiOperation("导出me.zhengjie数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TEmergencyhedging  tEmergencyhedging){
		Long time = System.currentTimeMillis();
		try {
			tEmergencyhedgingService.download( tEmergencyhedging,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}