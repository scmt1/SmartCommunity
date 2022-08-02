package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TCompany;
import me.zhengjie.dao.entity.TSubstances;
import me.zhengjie.dao.mapper.TCompanyMapper;
import me.zhengjie.dao.service.ITCompanyService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.LinkedHashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
/**
 *@author
 **/
@Service
public class TCompanyServiceImpl extends ServiceImpl<TCompanyMapper, TCompany> implements ITCompanyService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TCompanyMapper tCompanyMapper;

	public Result<Object> getTCompanyById(String id){
		TCompany tCompany = tCompanyMapper.selectById(id);
		if(tCompany!=null){
			return  ResultUtil.data(tCompany);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTCompanyListByPage(TCompany tCompany, SearchVo searchVo, PageVo pageVo){
		int page = 1;
		int limit = 10;
		if (pageVo != null) {
			if (pageVo.getPageNumber() != 0) {
				page = pageVo.getPageNumber();
			}
			if (pageVo.getPageSize() != 0) {
				limit = pageVo.getPageSize();
			}
		}
		Page<TCompany> pageData = new Page<>(page, limit);
		QueryWrapper<TCompany> queryWrapper = new QueryWrapper<>();
		if (tCompany != null) {
			queryWrapper = LikeAllFeild(tCompany,null);
		}
		IPage<TCompany> result = tCompanyMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}

	@Override
	public void download(TCompany tCompany, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TCompany> queryWrapper = new QueryWrapper<>();
		if (tCompany != null) {
			queryWrapper = LikeAllFeild(tCompany,null);
		}
		List<TCompany> list = tCompanyMapper.selectList(queryWrapper);
		for (TCompany re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("企业名", re.getCompanyName());
			map.put("企业责任人", re.getResponsible());
			map.put("企业xyz地理位置", re.getPositionNum());
			map.put("地理位置", re.getPositionChar());
			map.put("企业的性质", re.getCompanyNature());
			map.put("企业联系电话", re.getCompanyPhone());
			map.put("企业邮箱", re.getCompanyEmail());
			map.put("营业执照", re.getBusinessLicence());
			map.put("组织机构代码", re.getOrgCode());
			map.put("成立日期", re.getFoundDate());
			map.put("注册地址", re.getRegistryAddress());
			map.put("企业隶属关系", re.getEnterpriseRelation());
			map.put("注册登记类型", re.getRegistryType());
			map.put("邮政编码", re.getPostCode());
			map.put("企业规模", re.getEnterpriseScale());
			map.put("四上企业分类", re.getFourEnterprise());
			map.put("上年末从业人数", re.getPeopleEngagedLastyear());
			map.put("上年营业收入(万元)", re.getIncomLastyear());
			map.put("资产总额(万元)", re.getTotalAssets());
			map.put("经营地址", re.getBusinessAddress());
			map.put("行业类别", re.getIndustryType());
			map.put("经营范围", re.getBusinessScope());
			map.put("重点监管级别", re.getKeyRegulatoryLevel());
			map.put("营业状态", re.getBusinessStatus());
			map.put("上级企业名称", re.getEnterpriseRelation());
			map.put("上级企业组织机构代码", re.getUpOrgCode());
			map.put("填报人", re.getReporter());
			map.put("安全监管主管机构", re.getSafetySupervisionUthority());
			map.put("本单位安全生产管理机构", re.getUnitSafetyManage());
			map.put("专职安全管理人数", re.getSafetyPersonNum());
			map.put("安全专业技术人数", re.getSafetyTecNum());
			map.put("单位主要负责人", re.getUnitResponsePerson());
			map.put("主要负责人联系方式", re.getResponsePersonMobile());
			map.put("分管安全负责人", re.getChargeSafetyPerson());
			map.put("分管安全负责人联系方式", re.getSafetyPersonMobile());
			map.put("安全管理机构负责人", re.getSafetyManageResponser());
			map.put("安全管理机构负责人联系方式", re.getManageResponserMobile());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	 * @param searchVo 时间段
	* @return 返回查询
	*/
	public QueryWrapper<TCompany>  LikeAllFeild(TCompany tCompany,SearchVo searchVo) {
		QueryWrapper<TCompany> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(tCompany.getId())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getId, tCompany.getId()));
		}
		if (StringUtils.isNotEmpty(tCompany.getCompanyName())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getCompanyName, tCompany.getCompanyName()));
		}
		if (StringUtils.isNotEmpty(tCompany.getResponsible())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getResponsible, tCompany.getResponsible()));
		}
		if (StringUtils.isNotEmpty(tCompany.getPositionChar())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getPositionChar, tCompany.getPositionChar()));
		}
		if (StringUtils.isNotEmpty(tCompany.getPositionNum())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getPositionNum, tCompany.getPositionNum()));
		}
		if (StringUtils.isNotEmpty(tCompany.getCreateId())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getCreateId, tCompany.getCreateId()));
		}
		if (StringUtils.isNotEmpty(tCompany.getCompanyNature())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getCompanyNature, tCompany.getCompanyNature()));
		}
		/*queryWrapper.lambda().or(i -> i.like(TCompany::getCreateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TCompany::getIsDelete, likeValue));
		queryWrapper.lambda().or(i -> i.like(TCompany::getUpdateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TCompany::getUpdateId, likeValue));*/
		if (StringUtils.isNotEmpty(tCompany.getCompanyPhone())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getCompanyPhone, tCompany.getCompanyPhone()));
		}
		if (StringUtils.isNotEmpty(tCompany.getIndustryType())){
			queryWrapper.lambda().and(i -> i.like(TCompany::getIndustryType, tCompany.getIndustryType()));
		}
		if(searchVo!=null){
			if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
				queryWrapper.lambda().and(i -> i.between(TCompany::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.lambda().and(i -> i.eq(TCompany::getIsDelete, 1));
		return queryWrapper;
	}
}
