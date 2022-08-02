package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.domain.Person;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGridPersonPoint;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.BasicSocialOrganization;

import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.service.IBasicGridPersonPointService;
import me.zhengjie.service.IBasicGridsService;
import me.zhengjie.service.IBasicSocialOrganizationService;
import lombok.AllArgsConstructor;
import me.zhengjie.service.ITZhsqGridMemberService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 社会组织数据接口
 *@author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicSocialOrganization")
public class BasicSocialOrganizationController{
	
	private final IBasicSocialOrganizationService basicSocialOrganizationService;
	
	private final IBasicGridsService basicGridsService;
	
	private final ITZhsqGridMemberService itZhsqGridMemberService;

	private final SecurityUtil securityUtil;

	/**
	 * 新增社会组织数据
	 * @param basicSocialOrganization
	 * @return
	 */
	@PostMapping("addBasicSocialOrganization")
	public Result<Object> addBasicSocialOrganization(@RequestBody BasicSocialOrganization basicSocialOrganization){
		Long time = System.currentTimeMillis();
		try {
			basicSocialOrganization.setIsDelete(0);
//			basicSocialOrganization.setCreateId(securityUtil.getCurrUser().getId().toString());
//			if(StringUtils.isNotBlank(basicSocialOrganization.getHeadPortrait())){
//				//base64 转文件
//				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicSocialOrganization.getHeadPortrait());
//				//文件存储在nginx代理路径下
//				String fileName = UploadFile.uploadFile(imgFile);
//				basicSocialOrganization.setHeadPortrait(fileName);
//			}
			basicSocialOrganization.setCreateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = basicSocialOrganizationService.save(basicSocialOrganization);
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
	 * 根据主键来删除社会组织数据
	 * @param map
	 * @return
	 */
	@PostMapping("deleteBasicSocialOrganization")
	public Result<Object> deleteBasicSocialOrganization(@RequestBody Map<String ,Object> map) {
		if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			List<String> ids = (List<String>) map.get("ids");
			if (ids == null || ids.size() == 0 ) {
				return ResultUtil.error("参数为空，请联系管理员！！");
			}
			boolean res = basicSocialOrganizationService.removeByIds(ids);
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
	 * 根据主键来获取社会组织数据
	 * @param id
	 * @return
	 */
	@GetMapping("getBasicSocialOrganization")
	public Result<Object> getBasicSocialOrganization(@RequestParam(name = "id")String id){
		if (StringUtils.isBlank(id)) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
			BasicSocialOrganization res = basicSocialOrganizationService.getById(id);
			BasicGrids byId = basicGridsService.getById(res.getGridsId());
			if (res != null) {
				if (byId != null) {
					res.setGridsName(byId.getName());
				}
				if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(res.getPolicePrincipal())){
					TZhsqGridMember Person = itZhsqGridMemberService.getById(res.getPolicePrincipal());
					if(Person!=null){
						res.setPolicePrincipalName(Person.getName());
					}
				}

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
	 * 分页查询社会组织数据
	 * @param basicSocialOrganization
	 * @param searchVo
	 * @param pageVo
	 * @return
	 */
	@GetMapping("queryBasicSocialOrganizationList")
	public Result<Object> queryBasicSocialOrganizationList(BasicSocialOrganization basicSocialOrganization, SearchVo searchVo, PageVo pageVo){
		Long time = System.currentTimeMillis();
		try {
			//用户数据权限
			UserDto user = securityUtil.getCurrUser();
			if (user.getPower() != null){
				if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
					String deptId = user.getPower().getDeptId();
					Integer attribute = user.getPower().getAttribute();
					if (attribute == 1){// 街道
						basicSocialOrganization.setStreetId(deptId);
					}else if (attribute == 2) {// 社区
						basicSocialOrganization.setCommunityId(deptId);
					}else if (attribute == 3) {// 网格
						basicSocialOrganization.setGridsId(deptId);
					}
				}
			}
			return basicSocialOrganizationService.queryBasicSocialOrganizationListByPage(basicSocialOrganization, searchVo, pageVo);
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}

	/**
	 * 更新社会组织数据
	 * @param basicSocialOrganization
	 * @return
	 */
	@PostMapping("updateBasicSocialOrganization")
	public Result<Object> updateBasicSocialOrganization(@RequestBody BasicSocialOrganization basicSocialOrganization){
		if (StringUtils.isBlank(basicSocialOrganization.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		Long time = System.currentTimeMillis();
		try {
//			if(StringUtils.isNotBlank(basicSocialOrganization.getHeadPortrait()) && basicSocialOrganization.getImageIsUpdate()){
//				//base64 转文件
//				MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicSocialOrganization.getHeadPortrait());
//				//文件存储在nginx代理路径下
//				String fileName = UploadFile.uploadFile(imgFile);
//				basicSocialOrganization.setHeadPortrait(fileName);
//			}
			basicSocialOrganization.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			boolean res = basicSocialOrganizationService.updateById(basicSocialOrganization);
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
	 * 导出社会组织数据
	 * @param response
	 * @param basicSocialOrganization
	 */
	@PostMapping("/download")
	public void download(HttpServletResponse response,BasicSocialOrganization  basicSocialOrganization){
		Long time = System.currentTimeMillis();
		try {
			basicSocialOrganizationService.download( basicSocialOrganization,response);
		} catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
		}
	}


	/**
	 * 统计社区按类型
	 * @return
	 */
	@GetMapping("/getCommunityCountData")
	public Result<Object> getCommunityCountData() {
		//用户数据权限
		UserDto user = securityUtil.getCurrUser();
		BasicSocialOrganization basicSocialOrganization = null;
		if (user.getPower() != null){
			if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
				basicSocialOrganization = new BasicSocialOrganization();
				String deptId = user.getPower().getDeptId();
				Integer attribute = user.getPower().getAttribute();
				if (attribute == 1){//街道
					basicSocialOrganization.setStreetId(deptId);
				}else if (attribute == 2){//社区
					basicSocialOrganization.setCommunityId(deptId);
				}else if (attribute == 3){//网格
					basicSocialOrganization.setGridsId(deptId);
				}
			}
		}
		return new ResultUtil<>().setData(basicSocialOrganizationService.getCommunityCountData(basicSocialOrganization));
	}

	/**
	 * 统计网格按类型
	 * @return
	 */
	@GetMapping("/getGridsCountData")
	public Result<Object> getGridsCountData() {
		//用户数据权限
		UserDto user = securityUtil.getCurrUser();
		BasicSocialOrganization basicSocialOrganization = null;
		if (user.getPower() != null){
			if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
				basicSocialOrganization = new BasicSocialOrganization();
				String deptId = user.getPower().getDeptId();
				Integer attribute = user.getPower().getAttribute();
				if (attribute == 1){//街道
					basicSocialOrganization.setStreetId(deptId);
				}else if (attribute == 2){//社区
					basicSocialOrganization.setCommunityId(deptId);
				}else if (attribute == 3){//网格
					basicSocialOrganization.setGridsId(deptId);
				}
			}
		}
		return new ResultUtil<>().setData(basicSocialOrganizationService.getGridsCountData(basicSocialOrganization));
	}

	/**
	 * 分页查询社会组织数据
	 * @param basicSocialOrganization
	 * @param key
	 * @param pageVo
	 * @return
	 */
	@GetMapping("queryBasicSocialOrganizationListByAnyWayWhere")
	public Result<Object> queryBasicSocialOrganizationListByAnyWayWhere(BasicSocialOrganization basicSocialOrganization, String key, PageVo pageVo) {
		Long time = System.currentTimeMillis();
		try {
			//用户数据权限
			UserDto user = securityUtil.getCurrUser();
			if (user.getPower() != null){
				if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
					String deptId = user.getPower().getDeptId();
					Integer attribute = user.getPower().getAttribute();
					if (attribute == 1){// 街道
						basicSocialOrganization.setStreetId(deptId);
					}else if (attribute == 2) {// 社区
						basicSocialOrganization.setCommunityId(deptId);
					}else if (attribute == 3) {// 网格
						basicSocialOrganization.setGridsId(deptId);
					}
				}
			}
			return basicSocialOrganizationService.queryBasicSocialOrganizationListByAnyWayWhere(basicSocialOrganization, key, pageVo);
		} catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
			return ResultUtil.error("查询异常:" + e.getMessage());
		}
	}
}
