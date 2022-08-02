package me.zhengjie.controller;

import java.util.Arrays;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.entity.TUnittype;
import me.zhengjie.service.ITUnittypeService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author 
 **/
@RestController
@Api(tags =" tUnitType数据接口")
@RequestMapping("/api/tUnittype")
public class TUnittypeController{
	@Autowired
	private ITUnittypeService tUnittypeService;
	@Autowired
	private LogService logService;

	/**
	 * 查询单位（树级）
	 * @return
	 */
	@Log("分页查询单位")
	@ApiOperation("分页查询单位")
	@PostMapping("/queryTUnittypeTree")
	public Result<Object> queryTUnittypeTree(@RequestBody JSONObject params){
		List<TUnittype> tUnittypeIPage = tUnittypeService.queryTUnittypeTreeByPage(params);
		return ResultUtil.data(tUnittypeIPage);
	}

	/**
	* 功能描述：新增tUnitType数据
	* @param tUnittype 实体
	* @return 返回新增结果
	*/
	@Log("新增tUnitType数据")
	@ApiOperation("新增tUnitType数据")
	@PostMapping("addTUnittype")
	public Result<Object> addTUnittype(@RequestBody TUnittype tUnittype){
		Long time = System.currentTimeMillis();
		try {
			tUnittype.setIsDelete(0);
			tUnittype.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tUnittypeService.save(tUnittype);
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
	@Log("根据主键来删除tUnitType数据")
	@ApiOperation("根据主键来删除tUnitType数据")
	@PostMapping("deleteTUnittype")
	public Result<Object> deleteTUnittype(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tUnittypeService.removeByIds(Arrays.asList(ids));
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
	@Log("根据主键来获取tUnitType数据")
	@ApiOperation("根据主键来获取tUnitType数据")
	@GetMapping("getTUnittype")
	public Result<Object> getTUnittype(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TUnittype res = tUnittypeService.getById(id);
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
	* @param tUnittype 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询tUnitType数据")
	@ApiOperation("分页查询tUnitType数据")
	@GetMapping("queryTUnittypeList")
	public Result<Object> queryTUnittypeList(TUnittype  tUnittype, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tUnittypeService.queryTUnittypeListByPage(tUnittype, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tUnittype 实体
	* @return 返回更新结果
	*/
	@Log("更新tUnitType数据")
	@ApiOperation("更新tUnitType数据")
	@PostMapping("updateTUnittype")
	public Result<Object> updateTUnittype(@RequestBody TUnittype tUnittype){
		if (StringUtils.isBlank(tUnittype.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tUnittype.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tUnittypeService.updateById(tUnittype);
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
	* @param tUnittype 查询参数
	* @return 
	*/
	@Log("导出tUnitType数据")
	@ApiOperation("导出tUnitType数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TUnittype  tUnittype){
		Long time = System.currentTimeMillis();
		try {
			tUnittypeService.download( tUnittype,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}