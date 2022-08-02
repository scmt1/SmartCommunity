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
import me.zhengjie.entity.TPopulationlabel;
import me.zhengjie.service.ITPopulationlabelService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *@author 
 **/
@RestController
@Api(tags =" tPopulationLabel数据接口")
@RequestMapping("/api/tPopulationlabel")
public class TPopulationlabelController{
	@Autowired
	private ITPopulationlabelService tPopulationlabelService;
	@Autowired
	private LogService logService;

	/**
	 * 查询标签（树级）
	 * @return
	 */
	@Log("查询标签")
	@ApiOperation("查询标签")
	@PostMapping("/queryTPopulationlabelTree")
	public Result<Object> queryTPopulationlabelTree(@RequestBody JSONObject params){
		List<TPopulationlabel> tPopulationlabelIPage = tPopulationlabelService.queryTPopulationlabelTreeByPage(params);
		return ResultUtil.data(tPopulationlabelIPage);
	}

	/**
	* 功能描述：新增tPopulationLabel数据
	* @param tPopulationlabel 实体
	* @return 返回新增结果
	*/
	@Log("新增tPopulationLabel数据")
	@ApiOperation("新增tPopulationLabel数据")
	@PostMapping("addTPopulationlabel")
	public Result<Object> addTPopulationlabel(@RequestBody TPopulationlabel tPopulationlabel){
		Long time = System.currentTimeMillis();
		try {
			tPopulationlabel.setIsDelete(0);
			tPopulationlabel.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tPopulationlabelService.save(tPopulationlabel);
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
	@Log("根据主键来删除tPopulationLabel数据")
	@ApiOperation("根据主键来删除tPopulationLabel数据")
	@PostMapping("deleteTPopulationlabel")
	public Result<Object> deleteTPopulationlabel(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tPopulationlabelService.removeByIds(Arrays.asList(ids));
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
	@Log("根据主键来获取tPopulationLabel数据")
	@ApiOperation("根据主键来获取tPopulationLabel数据")
	@GetMapping("getTPopulationlabel")
	public Result<Object> getTPopulationlabel(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TPopulationlabel res = tPopulationlabelService.getById(id);
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
	* @param tPopulationlabel 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询tPopulationLabel数据")
	@ApiOperation("分页查询tPopulationLabel数据")
	@GetMapping("queryTPopulationlabelList")
	public Result<Object> queryTPopulationlabelList(TPopulationlabel  tPopulationlabel, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tPopulationlabelService.queryTPopulationlabelListByPage(tPopulationlabel, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tPopulationlabel 实体
	* @return 返回更新结果
	*/
	@Log("更新tPopulationLabel数据")
	@ApiOperation("更新tPopulationLabel数据")
	@PostMapping("updateTPopulationlabel")
	public Result<Object> updateTPopulationlabel(@RequestBody TPopulationlabel tPopulationlabel){
		if (StringUtils.isBlank(tPopulationlabel.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tPopulationlabel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tPopulationlabelService.updateById(tPopulationlabel);
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
	* @param tPopulationlabel 查询参数
	* @return 
	*/
	@Log("导出tPopulationLabel数据")
	@ApiOperation("导出tPopulationLabel数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TPopulationlabel  tPopulationlabel){
		Long time = System.currentTimeMillis();
		try {
			tPopulationlabelService.download( tPopulationlabel,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}