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
import me.zhengjie.dao.entity.EpidemicReport;
import me.zhengjie.dao.mapper.EpidemicReportMapper;
import me.zhengjie.dao.service.IEpidemicReportService;
import me.zhengjie.utils.FileUtil;
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
public class EpidemicReportServiceImpl extends ServiceImpl<EpidemicReportMapper, EpidemicReport> implements IEpidemicReportService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private EpidemicReportMapper epidemicReportMapper;

	@Override
	public Result<Object> getEpidemicReportById(String id){
		EpidemicReport epidemicReport = epidemicReportMapper.selectById(id);
		if(epidemicReport!=null){
			return  ResultUtil.data(epidemicReport);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryEpidemicReportListByPage(EpidemicReport  epidemicReport, SearchVo searchVo, PageVo pageVo){
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
		Page<EpidemicReport> pageData = new Page<>(page, limit);
		QueryWrapper<EpidemicReport> queryWrapper = new QueryWrapper<>();
		if (epidemicReport !=null) {
			queryWrapper = LikeAllFeild(epidemicReport,searchVo);
		}
		IPage<EpidemicReport> result = epidemicReportMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(EpidemicReport epidemicReport, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<EpidemicReport> queryWrapper = new QueryWrapper<>();
		if (epidemicReport !=null) {
			queryWrapper = LikeAllFeild(epidemicReport,null);
		}
		List<EpidemicReport> list = epidemicReportMapper.selectList(queryWrapper);
		for (EpidemicReport re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("姓名",re.getPersonName());
			map.put("身份证",re.getPersonIdCard());
			map.put("电话号码",re.getPersonMobile());
			map.put("来源地",re.getPersonOrigin());
			map.put("来源地省",re.getOriginProvince());
			map.put("来源地市",re.getOriginCity());
			map.put("来源地区",re.getOriginArea());
			map.put("来源地详细地址",re.getOriginAddress());
			map.put("到泸街道",re.getToStree());
			map.put("到泸社区",re.getToCommunity());
			map.put("到泸详细地址",re.getToAddress());
			map.put("到泸日期",re.getArriveDate());
			map.put("交通方式",re.getTransportation());
			map.put("健康码颜色",re.getHealthCodeColor());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param epidemicReport 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<EpidemicReport>  LikeAllFeild(EpidemicReport  epidemicReport, SearchVo searchVo) {
		QueryWrapper<EpidemicReport> queryWrapper = new QueryWrapper<>();
		if(epidemicReport.getId() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getId, epidemicReport.getId()));
		}
		if(epidemicReport.getPersonName() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonName, epidemicReport.getPersonName()));
		}
		if(epidemicReport.getPersonIdCard() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonIdCard, epidemicReport.getPersonIdCard()));
		}
		if(epidemicReport.getPersonMobile() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonMobile, epidemicReport.getPersonMobile()));
		}
		if(epidemicReport.getPersonIdentity() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonIdentity, epidemicReport.getPersonIdentity()));
		}
		if(epidemicReport.getPersonIndustry() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonIndustry, epidemicReport.getPersonIndustry()));
		}
		if(epidemicReport.getSpareMobile() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getSpareMobile, epidemicReport.getSpareMobile()));
		}
		if(epidemicReport.getPersonOrigin() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPersonOrigin, epidemicReport.getPersonOrigin()));
		}
		if(epidemicReport.getOriginProvince() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getOriginProvince, epidemicReport.getOriginProvince()));
		}
		if(epidemicReport.getOriginCity() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getOriginCity, epidemicReport.getOriginCity()));
		}
		if(epidemicReport.getOriginArea() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getOriginArea, epidemicReport.getOriginArea()));
		}
		if(epidemicReport.getOriginAddress() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getOriginAddress, epidemicReport.getOriginAddress()));
		}
		if(epidemicReport.getToAddress() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getToAddress, epidemicReport.getToAddress()));
		}
		if(epidemicReport.getToStree() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getToStree, epidemicReport.getToStree()));
		}
		if(epidemicReport.getToCommunity() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getToCommunity, epidemicReport.getToCommunity()));
		}
		if(epidemicReport.getContactPersonName() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getContactPersonName, epidemicReport.getContactPersonName()));
		}
		if(epidemicReport.getContactPersonMobile() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getContactPersonMobile, epidemicReport.getContactPersonMobile()));
		}
		if(epidemicReport.getLeaveDate() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getLeaveDate, epidemicReport.getLeaveDate()));
		}
		if(epidemicReport.getArriveDate() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getArriveDate, epidemicReport.getArriveDate()));
		}
		if(epidemicReport.getTransportation() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getTransportation, epidemicReport.getTransportation()));
		}
		if(epidemicReport.getHealthCodeColor() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getHealthCodeColor, epidemicReport.getHealthCodeColor()));
		}
		if(epidemicReport.getPromiseDate() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getPromiseDate, epidemicReport.getPromiseDate()));
		}
		if(epidemicReport.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(EpidemicReport::getCreateTime, epidemicReport.getCreateTime()));
		}
		if(searchVo!=null){
			if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
				queryWrapper.lambda().and(i -> i.between(EpidemicReport::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		return queryWrapper;

}
}
