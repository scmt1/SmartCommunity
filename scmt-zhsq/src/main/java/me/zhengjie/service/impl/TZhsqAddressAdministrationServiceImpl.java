package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqAddressAdministration;
import me.zhengjie.mapper.TZhsqAddressAdministrationMapper;
import me.zhengjie.service.ITZhsqAddressAdministrationService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *@author 
 **/
@Service
public class TZhsqAddressAdministrationServiceImpl extends ServiceImpl<TZhsqAddressAdministrationMapper, TZhsqAddressAdministration> implements ITZhsqAddressAdministrationService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TZhsqAddressAdministrationMapper tZhsqAddressAdministrationMapper;

	@Override
	public TZhsqAddressAdministration getTZhsqAddressAdministrationById(String id){
		TZhsqAddressAdministration tZhsqAddressAdministration = tZhsqAddressAdministrationMapper.selectById(id);
		 return tZhsqAddressAdministration ;
	}

	@Override
	public Result<Object> queryTZhsqAddressAdministrationListByPage(TZhsqAddressAdministration  tZhsqAddressAdministration, SearchVo searchVo, PageVo pageVo){
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
		Page<TZhsqAddressAdministration> pageData = new Page<>(page, limit);
		QueryWrapper<TZhsqAddressAdministration> queryWrapper = new QueryWrapper<>();
		if (tZhsqAddressAdministration !=null) {
			queryWrapper = LikeAllFeild(tZhsqAddressAdministration,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_zhsq_address_administration."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_zhsq_address_administration."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_zhsq_address_administration.create_time");
		}
		IPage<TZhsqAddressAdministration> result = tZhsqAddressAdministrationMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TZhsqAddressAdministration tZhsqAddressAdministration, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TZhsqAddressAdministration> queryWrapper = new QueryWrapper<>();
		if (tZhsqAddressAdministration !=null) {
			queryWrapper = LikeAllFeild(tZhsqAddressAdministration,null);
		}
		List<TZhsqAddressAdministration> list = tZhsqAddressAdministrationMapper.selectList(queryWrapper);
		for (TZhsqAddressAdministration re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("??????", re.getId());
			map.put("????????????", re.getSiteLevel());
			map.put("??????", re.getSiteName());
			map.put("?????????id", re.getPid());
			map.put("??????", re.getOrderNumber());
			map.put("???????????? 1??? 0 ???", re.getIsDelete());
			map.put("?????????", re.getCreateId());
			map.put("????????????", re.getCreateTime());
			map.put("?????????", re.getUpdateId());
			map.put("????????????", re.getUpdateTime());
			map.put("?????????", re.getDeleteId());
			map.put("????????????", re.getDeleteTime());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}
	/**
	 * ??????????????????????????????
	 * @return ????????????
	 */
	public List<TZhsqAddressAdministration> listWithTree() {
		QueryWrapper<TZhsqAddressAdministration> queryWrapper = new QueryWrapper<>();
		// 1. ???????????????????????????
		List<TZhsqAddressAdministration> categories = tZhsqAddressAdministrationMapper.selectList(queryWrapper);
		// 2. ????????????????????????
		//    ?????????????????????????????? ????????????????????????
		return categories.stream()
				.filter(o -> o.getPid().equals("0"))
				// ?????????????????????????????????
				.peek(o -> o.setChildrens(getChildCategoryList(o, categories)))
				// ??????
				.sorted(Comparator.comparingInt(TZhsqAddressAdministration::getSort))
				// ??????
				.collect(Collectors.toList());
	}

	// ?????????????????? ??????????????? ????????????????????????????????????
	private List<TZhsqAddressAdministration> getChildCategoryList(TZhsqAddressAdministration currMenu, List<TZhsqAddressAdministration> categories) {
		return categories.stream().filter(o -> o.getPid().equals(currMenu.getId()))
				.peek(o -> o.setChildrens(getChildCategoryList(o, categories)))
				.sorted(Comparator.comparingInt(TZhsqAddressAdministration::getSort))
				.collect(Collectors.toList());
	}

	/**
	* ?????????????????????????????????
	* @param tZhsqAddressAdministration ???????????????????????????
	* @return ????????????
	*/
	public QueryWrapper<TZhsqAddressAdministration>  LikeAllFeild(TZhsqAddressAdministration  tZhsqAddressAdministration, SearchVo searchVo) {
		QueryWrapper<TZhsqAddressAdministration> queryWrapper = new QueryWrapper<>();
		if(tZhsqAddressAdministration.getId() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.id", tZhsqAddressAdministration.getId()));
		}
		if(tZhsqAddressAdministration.getSiteLevel() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.site_level", tZhsqAddressAdministration.getSiteLevel()));
		}
		if(tZhsqAddressAdministration.getSiteName() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.site_name", tZhsqAddressAdministration.getSiteName()));
		}
		if(tZhsqAddressAdministration.getPid() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.pid", tZhsqAddressAdministration.getPid()));
		}
		if(tZhsqAddressAdministration.getOrderNumber() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.order_number", tZhsqAddressAdministration.getOrderNumber()));
		}
		if(tZhsqAddressAdministration.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.is_delete", tZhsqAddressAdministration.getIsDelete()));
		}
		if(tZhsqAddressAdministration.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.create_id", tZhsqAddressAdministration.getCreateId()));
		}
		if(tZhsqAddressAdministration.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.create_time", tZhsqAddressAdministration.getCreateTime()));
		}
		if(tZhsqAddressAdministration.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.update_id", tZhsqAddressAdministration.getUpdateId()));
		}
		if(tZhsqAddressAdministration.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.update_time", tZhsqAddressAdministration.getUpdateTime()));
		}
		if(tZhsqAddressAdministration.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.delete_id", tZhsqAddressAdministration.getDeleteId()));
		}
		if(tZhsqAddressAdministration.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_zhsq_address_administration.delete_time", tZhsqAddressAdministration.getDeleteTime()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_zhsq_address_administration.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_zhsq_address_administration.is_delete", 0));
		return queryWrapper;
	
}
}