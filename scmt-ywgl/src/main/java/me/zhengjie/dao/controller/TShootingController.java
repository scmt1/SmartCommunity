package me.zhengjie.dao.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.utils.BASE64DecodedMultipartFile;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.TShooting;
import me.zhengjie.dao.service.ITShootingService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 *@author
 **/
@RestController
@Api(tags =" TShooting数据接口")
@RequestMapping("/api/tShooting")
public class TShootingController{
	@Autowired
	private ITShootingService tShootingService;
	@Autowired
	private LogService logService;

	@Autowired
	private SecurityUtil securityUtil;

	/**
	* 功能描述：新增TShooting数据
	* @param tShooting 实体
	* @return 返回新增结果
	*/
	@Log("新增TShooting数据")
	@ApiOperation("新增TShooting数据")
	@PostMapping("addTShooting")
	public Result<Object> addTShooting(@RequestBody TShooting tShooting){
		Long time = System.currentTimeMillis();
		try {
			tShooting.setState(1);
			if(StringUtils.isNotBlank(tShooting.getImgPath())){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tShooting.getImgPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tShooting.setImgPath(fileName);
			}

			if(StringUtils.isNotBlank(tShooting.getVideoPath())){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tShooting.getVideoPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tShooting.setVideoPath(fileName);
			}

			tShooting.setCreateId(securityUtil.getCurrUser().getId().toString());
			tShooting.setCreateTime(new Timestamp(System.currentTimeMillis()));

			boolean res = tShootingService.save(tShooting);
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
	@Log("根据主键来删除TShooting数据")
	@ApiOperation("根据主键来删除TShooting数据")
	@PostMapping("deleteTShooting")
	public Result<Object> deleteTShooting(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tShootingService.removeByIds(Arrays.asList(ids));
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
	@Log("根据主键来获取TShooting数据")
	@ApiOperation("根据主键来获取TShooting数据")
	@GetMapping("getTShooting")
	public Result<Object> getTShooting(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TShooting res = tShootingService.getById(id);
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
	 * 功能描述：查询全部数据
	 * @param name 需要模糊查询的信息
	 * @return 返回获取结果
	 */
	@Log("查询全部TShooting数据")
	@ApiOperation("查询全部TShooting数据")
	@GetMapping("queryAllTShootingList")
	@AnonymousAccess
	public Result<Object> queryAllTShootingList(@RequestParam(name = "name", required = false)String name){
		Long time = System.currentTimeMillis();
		try {
			QueryWrapper<TShooting> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().or(i -> i.like(TShooting::getName, name));
			queryWrapper.lambda().and(i -> i.eq(TShooting::getState, 1));

			List<TShooting> list = tShootingService.list(queryWrapper);
			return new ResultUtil<>().setData(list);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}


	/**
	* 功能描述：实现分页查询
	* @param tShooting 需要查询的信息
	* @return 返回获取结果
	*/
	@Log("分页查询TShooting数据")
	@ApiOperation("分页查询TShooting数据")
	@GetMapping("queryTShootingList")
	public Result<Object> queryTShootingList(TShooting tShooting, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tShootingService.queryTShootingListByPage(tShooting, searchVo, pageVo);
		} catch (Exception e) {
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tShooting 实体
	* @return 返回更新结果
	*/
	@Log("更新TShooting数据")
	@ApiOperation("更新TShooting数据")
	@PostMapping("updateTShooting")
	public Result<Object> updateTShooting(@RequestBody TShooting tShooting){
		if (StringUtils.isBlank(tShooting.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tShooting.setUpdateId(securityUtil.getCurrUser().getId().toString());
			tShooting.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			if(StringUtils.isNotBlank(tShooting.getImgPath()) && tShooting.getImageIsUpdate()){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tShooting.getImgPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tShooting.setImgPath(fileName);
			}

			if(StringUtils.isNotBlank(tShooting.getVideoPath()) && tShooting.getVideoIsUpdate()){
				//base64 转文件
				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tShooting.getVideoPath());
				//文件存储在nginx代理路径下
				String fileName = UploadFile.uploadFile(imgFile);
				tShooting.setVideoPath(fileName);
			}

			boolean res = tShootingService.updateById(tShooting);
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
	* @return
	*/
	@Log("导出TShooting数据")
	@ApiOperation("导出TShooting数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response, TShooting tShooting){
		Long time = System.currentTimeMillis();
		try {
			tShootingService.download(tShooting,response);
		} catch (Exception e) {
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}
