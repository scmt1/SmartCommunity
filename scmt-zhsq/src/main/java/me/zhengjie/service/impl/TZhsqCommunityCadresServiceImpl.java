package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.mapper.TZhsqCommunityCadresMapper;
import me.zhengjie.entity.TZhsqCommunityCadres;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqCommunityCadresService;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 *@author
 **/
@Service
@AllArgsConstructor
public class TZhsqCommunityCadresServiceImpl extends ServiceImpl<TZhsqCommunityCadresMapper, TZhsqCommunityCadres> implements ITZhsqCommunityCadresService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final TZhsqCommunityCadresMapper tZhsqCommunityCadresMapper;

	private final GridDeptMapper deptMapper;


	@Override
	public Result<Object> getTZhsqCommunityCadresById(String id){
		TZhsqCommunityCadres tZhsqCommunityCadres = tZhsqCommunityCadresMapper.selectById(id);
		if(tZhsqCommunityCadres!=null){
			return  ResultUtil.data(tZhsqCommunityCadres);
		}
		 return  ResultUtil.error("????????????????????????????????????????????????");
	}

	@Override
	public Result<Object> queryTZhsqCommunityCadresListByPage(TZhsqCommunityCadres  tZhsqCommunityCadres, SearchVo searchVo, PageVo pageVo){
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
		Page<TZhsqCommunityCadres> pageData = new Page<>(page, limit);
		QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper<>();
		if (tZhsqCommunityCadres !=null) {
			queryWrapper = LikeAllFeild(tZhsqCommunityCadres,searchVo);
		}
		if(pageVo.getSort()!=null){
			if(pageVo.getSort().equals("asc")){
				queryWrapper.orderByAsc(pageVo.getSort());
			}
			else{
				queryWrapper.orderByDesc(pageVo.getSort());
			}
		}
		else{
			queryWrapper.orderByDesc("create_time");
		}
//		IPage<TZhsqCommunityCadres> result = tZhsqCommunityCadresMapper.selectPage(pageData, queryWrapper);
		IPage<TZhsqCommunityCadres> result = tZhsqCommunityCadresMapper.selectByMyWrapper(queryWrapper,pageData );

		return  ResultUtil.data(result);
	}

	@Override
	public void download(TZhsqCommunityCadres tZhsqCommunityCadres, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper<>();
		if (tZhsqCommunityCadres !=null) {
			queryWrapper = LikeAllFeild(tZhsqCommunityCadres,null);
		}
		List<TZhsqCommunityCadres> list = tZhsqCommunityCadresMapper.selectByMyWrapper(queryWrapper);

		for (TZhsqCommunityCadres re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("??????", re.getName());
			map.put("??????", re.getSex());
			map.put("??????", re.getNation());
			map.put("????????????", re.getType());
			map.put("????????????", re.getIdCard());
			map.put("?????????", re.getPhone());
			map.put("????????????", re.getFixedTelephone());
			map.put("????????????", re.getPersonalProfile());

			map.put("????????????", re.getStreetName());
			map.put("????????????", re.getCommunityName());
			map.put("????????????", re.getDepartment());
			map.put("????????????", re.getPost());
			map.put("??????", re.getPostLevel());
			map.put("????????????", re.getResponsibilities());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}


	@Override
	public Result<Object> queryCommunityCadresList(TZhsqCommunityCadres tZhsqCommunityCadres){
		QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper<>();
		if (tZhsqCommunityCadres !=null) {
			queryWrapper = LikeAllFeild(tZhsqCommunityCadres,new SearchVo());
		}
		List<TZhsqCommunityCadres> selectList = tZhsqCommunityCadresMapper.selectList(queryWrapper);
		return ResultUtil.data(selectList);
	}

	@Override
	public int getCountByDept(Integer deptId) {
		return tZhsqCommunityCadresMapper.selectCount(new QueryWrapper<TZhsqCommunityCadres>().lambda().eq(TZhsqCommunityCadres::getDepartment,deptId));
	}

	;
	/**
	* ?????????????????????????????????
	* @param tZhsqCommunityCadres ???????????????????????????
	* @return ????????????
	*/
	public QueryWrapper<TZhsqCommunityCadres>  LikeAllFeild(TZhsqCommunityCadres  tZhsqCommunityCadres, SearchVo searchVo) {
		QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper<>();
		if(tZhsqCommunityCadres.getId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getId, tZhsqCommunityCadres.getId()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getName())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getName, tZhsqCommunityCadres.getName()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getType())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getType, tZhsqCommunityCadres.getType()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getSex())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getSex, tZhsqCommunityCadres.getSex()));
		}
		if(tZhsqCommunityCadres.getBirthday() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getBirthday, tZhsqCommunityCadres.getBirthday()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getNation())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getNation, tZhsqCommunityCadres.getNation()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getIdCard())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getIdCard, tZhsqCommunityCadres.getIdCard()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getPhone())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getPhone, tZhsqCommunityCadres.getPhone()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getFixedTelephone())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getFixedTelephone, tZhsqCommunityCadres.getFixedTelephone()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getPersonalProfile())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getPersonalProfile, tZhsqCommunityCadres.getPersonalProfile()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getHeadPortrait())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getHeadPortrait, tZhsqCommunityCadres.getHeadPortrait()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getStreetId())){
			queryWrapper.lambda().and(i -> i.eq(TZhsqCommunityCadres::getStreetId, tZhsqCommunityCadres.getStreetId()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getStreetName())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getStreetName, tZhsqCommunityCadres.getStreetName()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getCommunityId())){
			queryWrapper.lambda().and(i -> i.eq(TZhsqCommunityCadres::getCommunityId, tZhsqCommunityCadres.getCommunityId()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getCommunityName())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getCommunityName, tZhsqCommunityCadres.getCommunityName()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getDepartment())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getDepartment, tZhsqCommunityCadres.getDepartment()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getPost())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getPost, tZhsqCommunityCadres.getPost()));
		}
		if(tZhsqCommunityCadres.getPostLevel() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getPostLevel, tZhsqCommunityCadres.getPostLevel()));
		}
		if(StringUtils.isNotBlank(tZhsqCommunityCadres.getResponsibilities())){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getResponsibilities, tZhsqCommunityCadres.getResponsibilities()));
		}
		if(tZhsqCommunityCadres.getPosition() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getPosition, tZhsqCommunityCadres.getPosition()));
		}
		if(tZhsqCommunityCadres.getOrderNumber() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getOrderNumber, tZhsqCommunityCadres.getOrderNumber()));
		}
		if(tZhsqCommunityCadres.getIsDelete() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getIsDelete, tZhsqCommunityCadres.getIsDelete()));
		}
		if(tZhsqCommunityCadres.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getCreateId, tZhsqCommunityCadres.getCreateId()));
		}
		if(tZhsqCommunityCadres.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getCreateTime, tZhsqCommunityCadres.getCreateTime()));
		}
		if(tZhsqCommunityCadres.getUpdateId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getUpdateId, tZhsqCommunityCadres.getUpdateId()));
		}
		if(tZhsqCommunityCadres.getUpdateTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getUpdateTime, tZhsqCommunityCadres.getUpdateTime()));
		}
		if(tZhsqCommunityCadres.getDeleteId() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getDeleteId, tZhsqCommunityCadres.getDeleteId()));
		}
		if(tZhsqCommunityCadres.getDeleteTime() != null){
			queryWrapper.lambda().and(i -> i.like(TZhsqCommunityCadres::getDeleteTime, tZhsqCommunityCadres.getDeleteTime()));
		}
		if(searchVo!=null){
			if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
				queryWrapper.lambda().and(i -> i.between(TZhsqCommunityCadres::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
			}
			//????????????
			if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
				queryWrapper.and(i -> i.like("t_zhsq_community_cadres.Name", searchVo.getSearchInfo())
						.or().like("t_zhsq_community_cadres.phone", searchVo.getSearchInfo())
						.or().like("t_zhsq_community_cadres.id_card", searchVo.getSearchInfo())
				);
			}
		}
		queryWrapper.lambda().and(i -> i.eq(TZhsqCommunityCadres::getIsDelete, 0));
		return queryWrapper;

	}


	@Override
	public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
		File file = FileUtil.toFile(multipartFile);
		InputStream in = new FileInputStream(file);
		Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
		//???????????????????????????
		TZhsqCommunityCadres tZhsqGridMember = new TZhsqCommunityCadres();
		List<TZhsqCommunityCadres> readData =ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			Boolean isMatchingData = false;
			isMatchingData =  matchingData(readData,result);
			if(isMatchingData)
			{
				return ResultUtil.data(result);
			}
			for(TZhsqCommunityCadres basicGrids:readData){
				if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
					continue;
				}
				basicGrids.setIsDelete(0);
				basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
				int insert = tZhsqCommunityCadresMapper.insert(basicGrids);
				if(insert!=1){
					throw new BusinessErrorException("?????????????????????????????????");
				}
			}
		}
		catch (Exception e){
			throw new BusinessException(ResultCode.FAILURE);
		}

		return ResultUtil.data(result);
	}

	/**
	 * ????????????
	 */
	public Boolean  matchingData(List<TZhsqCommunityCadres> readData,List<Map<String, Object>>  result ){
		if(result==null){
			result = new ArrayList<>();
		}
		Boolean isMatchingData = false;
		StreetUtil streetUtil = new StreetUtil(deptMapper);
		streetUtil.Init();

		DictionaryUtils dictionaryUtils = new DictionaryUtils(deptMapper);
		dictionaryUtils.init();

		if(readData!=null && readData.size()>0){
			for (int i=0;i<readData.size();i++ ) {
				TZhsqCommunityCadres basicGrids = readData.get(i);
				if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
					continue;
				}
				if(basicGrids!=null){
					Map<String, Object> map = new HashMap<>();
					map.put("number",i+1);
					map.put("success","??????");

					String msg="";

					//????????????
					if (StringUtils.isNotBlank(basicGrids.getStreetName())) {
						String s = StreetUtil.matchingStreet(basicGrids.getStreetName(), streetUtil.streetDepts);
						if(StringUtils.isNotBlank(s)){
							basicGrids.setStreetId(s);
						}
						else{
							msg+="????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="??????????????????\n";
					}
					//????????????
					if (StringUtils.isNotBlank(basicGrids.getCommunityName())) {
						String s = StreetUtil.matchingCommunity(basicGrids.getStreetName(),basicGrids.getCommunityName(), streetUtil.gridDepts);
						if(StringUtils.isNotBlank(s)){
							basicGrids.setCommunityId(s);
						}
						else{
							msg+="????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="??????????????????\n";
					}
					//????????????
					if (StringUtils.isNotBlank(basicGrids.getDepartment())) {
						String s = StreetUtil.matchingDept(basicGrids.getDepartment(),basicGrids.getCommunityName(), streetUtil.Depts);
						if(StringUtils.isNotBlank(s)){
							basicGrids.setDepartment(s);
						}
						else{
							msg+="????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="??????????????????\n";
					}
					//????????????
					if (StringUtils.isNotBlank(basicGrids.getPost())) {
						String s = StreetUtil.matchingPost(basicGrids.getPost(), streetUtil.gridPost);
						if(StringUtils.isNotBlank(s)){
							basicGrids.setPost(s);
						}
						else{
							msg+="????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="??????????????????\n";
					}

					//??????
					if(StringUtils.isBlank(basicGrids.getName())){
						msg+="????????????\n";
					}
					else{
						if(!DictionaryUtils.isChinese(basicGrids.getName())){
							msg+="????????????????????????????????????????????????????????????\n";
						}
					}
					//??????
					if(StringUtils.isBlank(basicGrids.getSex())){
						msg+="????????????\n";
					}
					//????????????
					if(StringUtils.isNotBlank(basicGrids.getType())){
						String s = dictionaryUtils.politicalFaceType(basicGrids.getType());
						if(StringUtils.isNotBlank(s)){
							basicGrids.setType(s);
						}
						else{
							msg+="??????????????????????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="??????????????????\n";
					}
					//??????
					if(StringUtils.isNotBlank(basicGrids.getNation())){
						String s = dictionaryUtils.nationType(basicGrids.getNation());
						if(StringUtils.isNotBlank(s)){
							basicGrids.setNation(s);
						}
						else{
							msg+="??????????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="????????????\n";
					}
					//??????
					if(StringUtils.isNotBlank(basicGrids.getPostLevel())){
						String s = dictionaryUtils.levelData(basicGrids.getPostLevel());
						if(StringUtils.isNotBlank(s)){
							basicGrids.setPostLevel(s);
						}
						else{
							msg+="??????????????????????????????????????????????????????????????????\n";
						}
					}
					else{
						msg+="????????????\n";
					}

					//?????????
					if(StringUtils.isNotBlank(basicGrids.getPhone())){
						boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPhone());
						if(!chinaPhoneLegal){
							msg+="????????????????????????????????????\n";
						}
						else{
							//???????????????????????????
							QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
							queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getPhone, basicGrids.getPhone()));
							queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
							int res = tZhsqCommunityCadresMapper.selectCount(queryWrapper);
							//???????????????
							if (res >= 1) {
								msg+="???????????????,?????????\n";
							}
							//map ??????
							else{
								for(int j=i-1;j>=0;j--){
									TZhsqCommunityCadres basicGrids1 = readData.get(j);
									if(basicGrids1!=null && basicGrids.getPhone().equals(basicGrids1.getPhone()) ){
										msg+="??????"+(j+1)+"????????????????????????\n";
										break;
									}
								}
							}
						}
					}
					else{
						msg+="???????????????\n";
					}

					//????????????
					if(StringUtils.isNotBlank(basicGrids.getIdCard())){
						String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getIdCard());
						if(StringUtils.isNotBlank(chinaPhoneLegal)&& !chinaPhoneLegal.equals("YES")){
							msg+=chinaPhoneLegal+"????????????\n";
						}
						else{
							//??????id????????????????????????
							QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
							queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIdCard, basicGrids.getIdCard()));
							queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
							int res = tZhsqCommunityCadresMapper.selectCount(queryWrapper);
							//???????????????
							if (res >= 1) {
								msg+="?????????????????????,?????????\n";
							}
							//map ??????
							else{
								for(int j=i-1;j>=0;j--){
									TZhsqCommunityCadres basicGrids1 = readData.get(j);
									if(basicGrids1!=null && basicGrids.getIdCard().equals(basicGrids1.getIdCard()) ){
										msg+="??????"+(j+1)+"???????????????????????????\n";
										break;
									}
								}
							}
						}
					}
					else{
						msg+="??????????????????\n";
					}
					//????????????
					if(StringUtils.isBlank(basicGrids.getPersonalProfile())){
						msg+="??????????????????\n";
					}
					//????????????
					if(StringUtils.isBlank(basicGrids.getFixedTelephone())){
						msg+="??????????????????\n";
					}
					else{
						if(basicGrids.getFixedTelephone().length()>11){
							msg+="???????????????????????????11\n";
						}
					}

					//????????????
					if(StringUtils.isBlank(basicGrids.getResponsibilities())){
						msg+="??????????????????\n";
					}

					if(msg.length()>0){
						isMatchingData = true;
						map.put("success","??????");
					}
					map.put("msg",msg);
					result.add(map);
				}
			}
		}

		return isMatchingData;
	}
}
