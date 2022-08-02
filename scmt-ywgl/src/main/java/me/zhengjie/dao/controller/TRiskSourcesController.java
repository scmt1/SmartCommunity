package me.zhengjie.dao.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.dao.entity.TShooting;
import me.zhengjie.utils.BASE64DecodedMultipartFile;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.TRiskSources;
import me.zhengjie.dao.service.ITRiskSourcesService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 *@author
 **/
@RestController
@Api(tags =" tRiskSources数据接口")
@RequestMapping("/api/tRiskSources")
public class TRiskSourcesController{
	@Autowired
	private ITRiskSourcesService tRiskSourcesService;
	@Autowired
	private LogService logService;

	/**
	* 功能描述：新增tRiskSources数据
	* @param tRiskSources 实体
	* @return 返回新增结果
	*/
	@Log("新增tRiskSources数据")
	@ApiOperation("新增tRiskSources数据")
	@PostMapping("addTRiskSources")
	public Result<Object> addTRiskSources(@RequestBody TRiskSources tRiskSources){
		Long time = System.currentTimeMillis();
		try {
			tRiskSources.setCreateTime(new Timestamp(System.currentTimeMillis()));
			tRiskSources.setIsDelete(1);
			if(StringUtils.isNotBlank(tRiskSources.getImgPath())){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tRiskSources.getImgPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tRiskSources.setImgPath(fileName);
			}
			boolean res = tRiskSourcesService.save(tRiskSources);
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
	@Log("根据主键来删除tRiskSources数据")
	@ApiOperation("根据主键来删除tRiskSources数据")
	@PostMapping("deleteTRiskSources")
	public Result<Object> deleteTRiskSources(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tRiskSourcesService.removeByIds(Arrays.asList(ids));
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
	@Log("根据主键来获取tRiskSources数据")
	@ApiOperation("根据主键来获取tRiskSources数据")
	@GetMapping("getTRiskSources")
	public Result<Object> getTRiskSources(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TRiskSources res = tRiskSourcesService.getById(id);
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
	* @param search 需要模糊查询的信息
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询tRiskSources数据")
	@ApiOperation("分页查询tRiskSources数据")
	@GetMapping("queryTRiskSourcesList")
	public Result<Object> queryTRiskSourcesList(@RequestParam(name = "search", required = false)String search, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tRiskSourcesService.queryTRiskSourcesListByPage(search, searchVo, pageVo);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 功能描述：实现查询所有
	 * @return 返回获取结果
	 */
	@Log("分页查询tRiskSources数据")
	@ApiOperation("分页查询tRiskSources数据")
	@GetMapping("queryAlltRiskSourcesList")
	@AnonymousAccess
	public Result<Object> queryAlltRiskSourcesList(@RequestParam(name = "name", required = false)String name){

		Long time = System.currentTimeMillis();
		try {
			QueryWrapper<TRiskSources> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().or(i -> i.like(TRiskSources::getName, name));
			queryWrapper.lambda().and(i -> i.eq(TRiskSources::getIsDelete, 1));
			List<TRiskSources> list = tRiskSourcesService.list(queryWrapper);
			return new ResultUtil<>().setData(list);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：更新数据
	* @param tRiskSources 实体
	* @return 返回更新结果
	*/
	@Log("更新tRiskSources数据")
	@ApiOperation("更新tRiskSources数据")
	@PostMapping("updateTRiskSources")
	public Result<Object> updateTRiskSources(@RequestBody TRiskSources tRiskSources){
		if (StringUtils.isBlank(tRiskSources.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tRiskSources.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			if(StringUtils.isNotBlank(tRiskSources.getImgPath()) && tRiskSources.getImageIsUpdate()){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tRiskSources.getImgPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tRiskSources.setImgPath(fileName);
			}
			boolean res = tRiskSourcesService.updateById(tRiskSources);
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
	* @param likeValue 查询参数
	* @return
	*/
	@Log("导出tRiskSources数据")
	@ApiOperation("导出tRiskSources数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response, String likeValue){
		Long time = System.currentTimeMillis();
		try {
			tRiskSourcesService.download(likeValue,response);
		} catch (Exception e) {
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}
