package me.zhengjie.dao.controller;

import java.util.*;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.dao.service.ActivityService;
import me.zhengjie.entity.ActBusiness;
import me.zhengjie.service.ActBusinessService;
import me.zhengjie.service.GeneratorService;
import me.zhengjie.utils.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.TFormGenerator;
import me.zhengjie.dao.service.ITFormGeneratorService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *@author
 **/
@RestController
@Api(tags =" 表单管理数据接口")
@RequestMapping("/api/tFormGenerator")
public class TFormGeneratorController{
	@Autowired
	private ITFormGeneratorService tFormGeneratorService;
	@Autowired
	private LogService logService;
	@Autowired
	private SecurityUtil securityUtil;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActBusinessService actBusinessService;



	/**
	 * 功能描述：表单数据提交
	 * @return 是否成功
	 */
	@Log("表单数据提交")
	@ApiOperation("表单数据提交")
	@PostMapping("addFormData")
	public Result<Object> addFormData(@RequestBody Map<String,Object> map){
		Long time = System.currentTimeMillis();
		try {
			//获取要插入的数据
			LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) map.get("model");
			if(linkedHashMap==null){
				return ResultUtil.error("保存异常:参数为空，请联系管理员" );
			}
			long id = IdWorker.getId();
			linkedHashMap.put("id",id);//统一添加id

//			linkedHashMap.put("create_id",securityUtil.getCurrUser().getId().toString());//统一添加创建id
			linkedHashMap.put("create_time",new Date());//统一添加创建时间
			linkedHashMap.put("status",0);//统一添加状态
			//保存实体信息
			boolean res = activityService.AddActivity(map);
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
	* 功能描述：新增表单管理数据
	* @return 返回新增结果
	*/
	@Log("新增表单管理数据")
	@ApiOperation("新增表单管理数据")
	@PostMapping("addTFormGenerator")
	public Result<Object> addTFormGenerator(@RequestBody TFormGenerator tFormGenerator){

		Long time = System.currentTimeMillis();
		String path = "";
		tFormGenerator.setIsDelete(0);
        tFormGenerator.setStatus(1);
		tFormGenerator.setCreateTime(new Timestamp(System.currentTimeMillis()));
		tFormGenerator.setCreateId(securityUtil.getCurrUser().getId().toString());
		try {
			//保存实体信息
			boolean res = tFormGeneratorService.save(tFormGenerator);
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
	@Log("根据主键来删除表单管理数据")
	@ApiOperation("根据主键来删除表单管理数据")
	@PostMapping("deleteTFormGenerator")
	public Result<Object> deleteTFormGenerator(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = tFormGeneratorService.removeByIds(Arrays.asList(ids));
			if (res) {
				return ResultUtil.data(res, "删除成功");
			} else {
				return ResultUtil.data(res, "删除失败");
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
	@Log("根据主键来获取表单管理数据")
	@ApiOperation("根据主键来获取表单管理数据")
	@GetMapping("getTFormGenerator")
	public Result<Object> getTFormGenerator(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			return tFormGeneratorService.getTFormGeneratorById(id);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 功能描述：根据主键来获取数据
	 * @param tFormGenerator
	 * @return 返回获取结果
	 */
	@Log("根据条件来获取表单管理数据")
	@ApiOperation("根据条件来获取表单管理数据")
	@GetMapping("getTFormGeneratorDynamics")
	public Result<Object> getTFormGeneratorDynamics(TFormGenerator tFormGenerator){
		Long time = System.currentTimeMillis();
		try {
			return ResultUtil.data(tFormGeneratorService.queryTFormGeneratorList(tFormGenerator,null))  ;
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	* 功能描述：实现分页查询
	* @param searchVo 查询参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	@Log("分页查询表单管理数据")
	@ApiOperation("分页查询表单管理数据")
	@GetMapping("queryTFormGeneratorList")
	public Result<Object> queryTFormGeneratorList(TFormGenerator  tFormGenerator, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tFormGeneratorService.queryTFormGeneratorListByPage(tFormGenerator, searchVo, pageVo);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
	/**
	* 功能描述：更新数据
	* @param tFormGenerator 实体
	* @return 返回更新结果
	*/
	@Log("更新表单管理数据")
	@ApiOperation("更新表单管理数据")
	@PostMapping("updateTFormGenerator")
	public Result<Object> updateTFormGenerator(@RequestBody TFormGenerator tFormGenerator){
		Long time = System.currentTimeMillis();
		try {
			tFormGenerator.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			//判断流程是否重复绑定
			if(tFormGenerator!=null && StringUtils.isNotBlank(tFormGenerator.getProcDefId())){
				QueryWrapper<TFormGenerator> queryWrapper = new QueryWrapper<>();
				queryWrapper.lambda().and(i -> i.eq(TFormGenerator::getProcDefId, tFormGenerator.getProcDefId()));
                queryWrapper.lambda().and(i -> i.eq(TFormGenerator::getStatus, 1));
				queryWrapper.lambda().and(i -> i.ne(TFormGenerator::getId, tFormGenerator.getId()));

				int count = tFormGeneratorService.count(queryWrapper);
				if(count>0){
					return ResultUtil.error("保存失败:您绑定的流程已被绑定（流程不允许重复绑定）！" );
				}

			}
			//更新
			boolean res = tFormGeneratorService.updateById(tFormGenerator);
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
	* @param tFormGenerator 查询参数
	* @return
	*/
	@Log("导出表单管理数据")
	@ApiOperation("导出表单管理数据")
	@PostMapping("/download")
	public void download(HttpServletResponse response,TFormGenerator  tFormGenerator){
		Long time = System.currentTimeMillis();
		try {
			tFormGeneratorService.download( tFormGenerator,response);
		} catch (Exception e) {
			e.printStackTrace();
			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}

}
