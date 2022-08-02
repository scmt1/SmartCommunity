package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.BasicResumeMapper;
import me.zhengjie.entity.BasicResume;

import me.zhengjie.service.IBasicResumeService;
import me.zhengjie.utils.FileUtil;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class BasicResumeServiceImpl extends ServiceImpl<BasicResumeMapper, BasicResume> implements IBasicResumeService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final BasicResumeMapper basicResumeMapper;

	public Result<Object> getBasicResumeById(String id){
		BasicResume basicResume = basicResumeMapper.selectById(id);
		if(basicResume!=null){
			return  ResultUtil.data(basicResume);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryBasicResumeListByPage(BasicResume  basicResume, SearchVo searchVo, PageVo pageVo){
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
		Page<BasicResume> pageData = new Page<>(page, limit);
		QueryWrapper<BasicResume> queryWrapper = new QueryWrapper<>();
		if (basicResume !=null) {
			queryWrapper = LikeAllFeild(basicResume,searchVo);
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
		IPage<BasicResume> result = basicResumeMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(BasicResume basicResume, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<BasicResume> queryWrapper = new QueryWrapper<>();
		if (basicResume !=null) {
			queryWrapper = LikeAllFeild(basicResume,null);
		}
		List<BasicResume> list = basicResumeMapper.selectList(queryWrapper);
		for (BasicResume re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	 * 查询当前社区干部最后一个履历
	 * @param id
	 * @return
	 */
	@Override
	public BasicResume queryLastOneData(String id) {
		return basicResumeMapper.queryLastOneData(id);
	}

	/**
	 * 根据社区干部Id,查询履历
	 * @param personId
	 * @return
	 */
	@Override
	public List<BasicResume> queryBasicResumeListByPersonId(String personId) {
		return basicResumeMapper.queryBasicResumeListByPersonId(personId);
	}

	/**
	* 功能描述：构建模糊查询
	* @param basicResume 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<BasicResume>  LikeAllFeild(BasicResume  basicResume, SearchVo searchVo) {
		QueryWrapper<BasicResume> queryWrapper = new QueryWrapper<>();
		if(basicResume.getId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getId, basicResume.getId()));
		}
		if(basicResume.getPersonId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getPersonId, basicResume.getPersonId()));
		}
		if(basicResume.getPersonName() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getPersonName, basicResume.getPersonName()));
		}
		if(basicResume.getStartTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getStartTime, basicResume.getStartTime()));
		}
		if(basicResume.getEndTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getEndTime, basicResume.getEndTime()));
		}
		if(basicResume.getPost() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getPost, basicResume.getPost()));
		}
		if(basicResume.getDepartment() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getDepartment, basicResume.getDepartment()));
		}
		if(basicResume.getIsDelete() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getIsDelete, basicResume.getIsDelete()));
		}
		if(basicResume.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getCreateId, basicResume.getCreateId()));
		}
		if(basicResume.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getCreateTime, basicResume.getCreateTime()));
		}
		if(basicResume.getUpdateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getUpdateId, basicResume.getUpdateId()));
		}
		if(basicResume.getUpdateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getUpdateTime, basicResume.getUpdateTime()));
		}
		if(basicResume.getDeleteId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getDeleteId, basicResume.getDeleteId()));
		}
		if(basicResume.getDeleteTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicResume::getDeleteTime, basicResume.getDeleteTime()));
		}
		queryWrapper.lambda().and(i -> i.eq(BasicResume::getIsDelete, 0));
		return queryWrapper;

}
}
