package me.zhengjie.controller.rela;

import java.util.Arrays;

//import  me.zhengjie.aop.log.Log;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.entity.RelaVolunteerActivity;
import me.zhengjie.service.IRelaVolunteerActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
/**
 * rela数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/relaVolunteerActivity")
public class RelaVolunteerActivityController{

	private final IRelaVolunteerActivityService relaVolunteerActivityService;

	/**
	 * 新增rela数据
	 * @param relaVolunteerActivity
	 * @return
	 */
	@PostMapping("addRelaVolunteerActivity")
	public Result<Object> addRelaVolunteerActivity(RelaVolunteerActivity relaVolunteerActivity){
		Long time = System.currentTimeMillis();
		try {
			boolean res = relaVolunteerActivityService.save(relaVolunteerActivity);
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
	 * 根据主键来删除rela数据
	 * @param ids
	 * @return
	 */
	@PostMapping("deleteRelaVolunteerActivity")
	public Result<Object> deleteRelaVolunteerActivity(@RequestParam(name = "ids[]")String[] ids){
		if (ids == null || ids.length == 0) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = relaVolunteerActivityService.removeByIds(Arrays.asList(ids));
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
	 * 根据主键来获取rela数据
	 * @param id
	 * @return
	 */
	@GetMapping("getRelaVolunteerActivity")
	public Result<Object> getRelaVolunteerActivity(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			RelaVolunteerActivity res = relaVolunteerActivityService.getById(id);
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
	 * 分页查询rela数据
	 * @param relaVolunteerActivity
	 * @param searchVo
	 * @param pageVo
	 * @return
	 */
	@GetMapping("queryRelaVolunteerActivityList")
	public Result<Object> queryRelaVolunteerActivityList(RelaVolunteerActivity  relaVolunteerActivity, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			return relaVolunteerActivityService.queryRelaVolunteerActivityListByPage(relaVolunteerActivity, searchVo, pageVo);
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 更新rela数据
	 * @param relaVolunteerActivity
	 * @return
	 */
	@PostMapping("updateRelaVolunteerActivity")
	public Result<Object> updateRelaVolunteerActivity(RelaVolunteerActivity relaVolunteerActivity){
		if (StringUtils.isBlank(relaVolunteerActivity.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			boolean res = relaVolunteerActivityService.updateById(relaVolunteerActivity);
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
}
