package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqEventListService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqEventList;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * TZhsqEventList数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqEventList")
public class TZhsqEventListController{

	private final ITZhsqEventListService tZhsqEventListService;


	/**
	 * 新增TZhsqEventList数据
	 * @param tZhsqEventList
	 * @return
	 */
	@PostMapping("addTZhsqEventList")
	public Result<Object> addTZhsqEventList(TZhsqEventList tZhsqEventList){
		Long time = System.currentTimeMillis();
		try {
			tZhsqEventList.setIsDelete(0);
			tZhsqEventList.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tZhsqEventListService.save(tZhsqEventList);
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
	 * 根据主键来删除TZhsqEventList数据
	 * @param map
	 * @return
	 */
	@PostMapping("deleteTZhsqEventList")
	public Result<Object> deleteTZhsqEventList(@RequestBody Map<String ,Object> map) {
		if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			List<String> ids = (List<String>) map.get("ids");
			if (ids == null || ids.size() == 0 ) {
				return ResultUtil.error("参数为空，请联系管理员！！");
			}
			boolean res = tZhsqEventListService.removeByIds(ids);
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
	 * 根据主键来获取TZhsqEventList数据
	 * @param id
	 * @return
	 */
	@GetMapping("getTZhsqEventList")
	public Result<Object> getTZhsqEventList(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			TZhsqEventList res = tZhsqEventListService.getById(id);
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
	 * 分页查询TZhsqEventList数据
	 * @param tZhsqEventList
	 * @param searchVo
	 * @param pageVo
	 * @return
	 */
	@GetMapping("queryTZhsqEventListList")
	public Result<Object> queryTZhsqEventListList(TZhsqEventList  tZhsqEventList, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return tZhsqEventListService.queryTZhsqEventListListByPage(tZhsqEventList, searchVo, pageVo);
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 更新TZhsqEventList数据
	 * @param tZhsqEventList
	 * @return
	 */
	@PostMapping("updateTZhsqEventList")
	public Result<Object> updateTZhsqEventList(TZhsqEventList tZhsqEventList){
		if (StringUtils.isBlank(tZhsqEventList.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			tZhsqEventList.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = tZhsqEventListService.updateById(tZhsqEventList);
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
	 * 导出TZhsqEventList数据
	 * @param response
	 * @param tZhsqEventList
	 */
	@PostMapping("/download")
	public void download(HttpServletResponse response,TZhsqEventList  tZhsqEventList){
		Long time = System.currentTimeMillis();
		try {
			tZhsqEventListService.download( tZhsqEventList,response);
		} catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}

	/**
	 * 根据条件查询所有事件，用于一张图显示
	 * @param tZhsqEventList
	 * @param key
	 * @return
	 */
	@GetMapping("/queryAllTZhsqEventListByAnyWayWhere")
	public Result<Object> queryAllTZhsqEventListByAnyWayWhere(TZhsqEventList tZhsqEventList, String key) {
		Long time = System.currentTimeMillis();
		try {
			Result<Object> objectResult = tZhsqEventListService.queryAllTZhsqGridMemberListByAnyWayWhere(tZhsqEventList);
			return objectResult;
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
}
