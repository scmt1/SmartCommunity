package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.entity.BasicUnit;
import me.zhengjie.service.IBasicUnitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 单元数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicUnit")
public class BasicUnitController{

	private final IBasicUnitService basicUnitService;


	/**
	 * 新增单元数据
	 * @param basicUnit
	 * @return
	 */
	@PostMapping("addBasicUnit")
	public Result<Object> addBasicUnit(BasicUnit basicUnit){
		Long time = System.currentTimeMillis();
		try {
			basicUnit.setIsDelete(0);
			basicUnit.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = basicUnitService.save(basicUnit);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
//			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	 * 根据主键来删除单元数据
	 * @param map
	 * @return
	 */
	@PostMapping("deleteBasicUnit")
	public Result<Object> deleteBasicUnit(@RequestBody Map<String ,Object> map) {
		if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			List<String> ids = (List<String>) map.get("ids");
			if (ids == null || ids.size() == 0 ) {
				return ResultUtil.error("参数为空，请联系管理员！！");
			}
			boolean res = basicUnitService.removeByIds(ids);
			if (res) {
				return ResultUtil.data(res, "删除成功");
			} else {
				return ResultUtil.error( "删除失败");
			}
		} catch (Exception e) {
//			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("删除异常:" + e.getMessage());
		}
	}

	/**
	 * 根据主键来获取单元数据
	 * @param id
	 * @return
	 */
	@GetMapping("getBasicUnit")
	public Result<Object> getBasicUnit(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			BasicUnit res = basicUnitService.getById(id);
			if (res != null) {
				return ResultUtil.data(res, "查询成功");
			} else {
				return ResultUtil.error("查询失败");
			}
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 分页查询单元数据
	 * @param basicUnit
	 * @param searchVo
	 * @param pageVo
	 * @return
	 */
	@GetMapping("queryBasicUnitList")
	public Result<Object> queryBasicUnitList(BasicUnit  basicUnit, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return basicUnitService.queryBasicUnitListByPage(basicUnit, searchVo, pageVo);
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 更新单元数据
	 * @param basicUnit
	 * @return
	 */
	@PostMapping("updateBasicUnit")
	public Result<Object> updateBasicUnit(BasicUnit basicUnit){
		if (StringUtils.isBlank(basicUnit.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			basicUnit.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = basicUnitService.updateById(basicUnit);
			if (res) {
				return ResultUtil.data(res, "保存成功");
			} else {
				return ResultUtil.error("保存失败");
			}
		} catch (Exception e) {
//			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}

	/**
	 * 导出单元数据
	 * @param response
	 * @param basicUnit
	 */
	@PostMapping("/download")
	public void download(HttpServletResponse response,BasicUnit  basicUnit){
		Long time = System.currentTimeMillis();
		try {
			basicUnitService.download( basicUnit,response);
		} catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}
}
