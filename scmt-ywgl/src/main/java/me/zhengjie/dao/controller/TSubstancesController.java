package me.zhengjie.dao.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.dao.entity.TShooting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.TSubstances;
import me.zhengjie.dao.service.ITSubstancesService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author
 **/
@RestController
@Api(tags =" tSubstances数据接口")
@RequestMapping("/api/tSubstances")
public class TSubstancesController{
	@Autowired
	private ITSubstancesService tSubstancesService;
	@Autowired
	private LogService logService;
	@Autowired
	private SecurityUtil securityUtil;
	/**
	* 功能描述：新增tSubstances数据
	* @param tSubstances 实体
	* @return 返回新增结果
	*/
	@Log("新增tSubstances数据")
	@ApiOperation("新增tSubstances数据")
	@PostMapping("addTSubstances")
	public Result<Object> addTSubstances(TSubstances tSubstances){
		Long time = System.currentTimeMillis();
		try {
			tSubstances.setIsdelete(1);
			tSubstances.setCreateId(securityUtil.getCurrUser().getId().toString());
			tSubstances.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tSubstancesService.save(tSubstances);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：根据主键来删除数据
	* @param ids 主键集合
	* @return 返回删除结果
	*/
	@Log("根据主键来删除tSubstances数据")
	@ApiOperation("根据主键来删除tSubstances数据")
	@PostMapping("deleteTSubstances")
	public Result<Object> deleteTSubstances(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tSubstancesService.removeByIds(Arrays.asList(ids));
			if (res) {
				return ResultUtil.data(res, "删除成功");
			} else {
				return ResultUtil.error( "删除失败");
			}
		} catch (Exception e) {
			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("删除异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	@Log("根据主键来获取tSubstances数据")
	@ApiOperation("根据主键来获取tSubstances数据")
	@GetMapping("getTSubstances")
	public Result<Object> getTSubstances(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TSubstances res = tSubstancesService.getById(id);
			if (res != null) {
				return ResultUtil.data(res, "查询成功");
			} else {
				return ResultUtil.error("查询失败");
			}
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：实现分页查询
	* @param tSubstances 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询tSubstances数据")
	@ApiOperation("分页查询tSubstances数据")
	@GetMapping("queryTSubstancesList")
	public Result<Object> queryTSubstancesList(TSubstances tSubstances, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tSubstancesService.queryTSubstancesListByPage(tSubstances, searchVo, pageVo);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tSubstances 实体
	* @return 返回更新结果
	*/
	@Log("更新tSubstances数据")
	@ApiOperation("更新tSubstances数据")
	@PostMapping("updateTSubstances")
	public Result<Object> updateTSubstances(TSubstances tSubstances){
		if (StringUtils.isBlank(tSubstances.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tSubstances.setUpdateId(securityUtil.getCurrUser().getId().toString());
			tSubstances.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tSubstancesService.updateById(tSubstances);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：导出数据
	* @param response 请求参数
	* @param tSubstances 查询参数
	* @return
	*/
	@Log("导出tSubstances数据")
	@ApiOperation("导出tSubstances数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response, TSubstances tSubstances){
		Long time = System.currentTimeMillis();
		try {
			tSubstancesService.download(tSubstances,response);
		} catch (Exception e) {
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}

	/**
	 * 功能描述：查询全部数据
	 * @param name 需要模糊查询的信息
	 * @return 返回获取结果
	 */
	@Log("查询全部tSubstances数据")
	@ApiOperation("查询全部tSubstances数据")
	@GetMapping("queryAllTSubstancesList")
	@AnonymousAccess
	public Result<Object> queryAllTSubstancesList(@RequestParam(name = "name", required = false)String name){
		Long time = System.currentTimeMillis();
		try {
			QueryWrapper<TSubstances> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().or(i -> i.like(TSubstances::getName, name));
			queryWrapper.lambda().and(i -> i.eq(TSubstances::getIsdelete, 1));

			List<TSubstances> list = tSubstancesService.list(queryWrapper);
			return new ResultUtil<>().setData(list);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
}
