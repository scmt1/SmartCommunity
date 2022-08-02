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
import me.zhengjie.entity.TArmedequipment;
import me.zhengjie.service.ITArmedequipmentService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author 
 **/
@RestController
@Api(tags =" me.zhengjie数据接口")
@RequestMapping("/api/tArmedequipment")
public class TArmedequipmentController{
	@Autowired
	private ITArmedequipmentService tArmedequipmentService;
	@Autowired
	private LogService logService;

	/**
	* 功能描述：新增me.zhengjie数据
	* @param tArmedequipment 实体
	* @return 返回新增结果
	*/
	@Log("新增me.zhengjie数据")
	@ApiOperation("新增me.zhengjie数据")
	@PostMapping("addTArmedequipment")
	public Result<Object> addTArmedequipment(@RequestBody TArmedequipment tArmedequipment){
		Long time = System.currentTimeMillis();
		try {
			tArmedequipment.setIsDelete(0);
			tArmedequipment.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tArmedequipmentService.save(tArmedequipment);
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
	@PostMapping("deleteTArmedequipment")
	public Result<Object> deleteTArmedequipment(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tArmedequipmentService.removeByIds(Arrays.asList(ids));
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
	@GetMapping("getTArmedequipment")
	public Result<Object> getTArmedequipment(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TArmedequipment res = tArmedequipmentService.getById(id);
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
	* @param tArmedequipment 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询me.zhengjie数据")
	@ApiOperation("分页查询me.zhengjie数据")
	@GetMapping("queryTArmedequipmentList")
	public Result<Object> queryTArmedequipmentList(TArmedequipment  tArmedequipment, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tArmedequipmentService.queryTArmedequipmentListByPage(tArmedequipment, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tArmedequipment 实体
	* @return 返回更新结果
	*/
	@Log("更新me.zhengjie数据")
	@ApiOperation("更新me.zhengjie数据")
	@PostMapping("updateTArmedequipment")
	public Result<Object> updateTArmedequipment(@RequestBody TArmedequipment tArmedequipment){
		if (StringUtils.isBlank(tArmedequipment.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tArmedequipment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tArmedequipmentService.updateById(tArmedequipment);
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
	* @param tArmedequipment 查询参数
	* @return 
	*/
	@Log("导出me.zhengjie数据")
	@ApiOperation("导出me.zhengjie数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TArmedequipment  tArmedequipment){
		Long time = System.currentTimeMillis();
		try {
			tArmedequipmentService.download( tArmedequipment,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}