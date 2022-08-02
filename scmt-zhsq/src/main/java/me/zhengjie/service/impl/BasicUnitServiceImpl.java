package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.BasicUnitMapper;
import me.zhengjie.entity.BasicUnit;
import me.zhengjie.service.IBasicUnitService;
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
public class BasicUnitServiceImpl extends ServiceImpl<BasicUnitMapper, BasicUnit> implements IBasicUnitService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final BasicUnitMapper basicUnitMapper;

	public Result<Object> getBasicUnitById(String id){
		BasicUnit basicUnit = basicUnitMapper.selectById(id);
		if(basicUnit!=null){
			return  ResultUtil.data(basicUnit);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryBasicUnitListByPage(BasicUnit  basicUnit, SearchVo searchVo, PageVo pageVo){
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
		Page<BasicUnit> pageData = new Page<>(page, limit);
		QueryWrapper<BasicUnit> queryWrapper = new QueryWrapper<>();
		if (basicUnit !=null) {
			queryWrapper = LikeAllFeild(basicUnit,searchVo);
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
		IPage<BasicUnit> result = basicUnitMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(BasicUnit basicUnit, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<BasicUnit> queryWrapper = new QueryWrapper<>();
		if (basicUnit !=null) {
			queryWrapper = LikeAllFeild(basicUnit,null);
		}
		List<BasicUnit> list = basicUnitMapper.selectList(queryWrapper);
		for (BasicUnit re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	 * 查询当前建筑设施下的单元
	 * @param archiveId
	 * @return
	 */
	@Override
	public List<BasicUnit> queryBasicUnitListByArchiveId(String archiveId) {
		return basicUnitMapper.queryBasicUnitListByArchiveId(archiveId);
	}

	/**
	 * 查询最大的单元数
	 * @param id
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getMaxUnit(String id) {
		return basicUnitMapper.getMaxUnit(id);
	}

	/**
	 * 查询单元中楼层数和层户数
	 * @param id
	 * @param name
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getFloorAndDoor(String id,Integer name) {
		return basicUnitMapper.getFloorAndDoor(id,name);
	}

	/**
	* 功能描述：构建模糊查询
	* @param basicUnit 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<BasicUnit>  LikeAllFeild(BasicUnit  basicUnit, SearchVo searchVo) {
		QueryWrapper<BasicUnit> queryWrapper = new QueryWrapper<>();
		if(basicUnit.getId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getId, basicUnit.getId()));
		}
		if(basicUnit.getName() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getName, basicUnit.getName()));
		}
		if(basicUnit.getBuildArchiveId() != null){
			queryWrapper.lambda().and(i -> i.eq(BasicUnit::getBuildArchiveId, basicUnit.getBuildArchiveId()));
		}
		if(basicUnit.getFloorNumber() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getFloorNumber, basicUnit.getFloorNumber()));
		}
		if(basicUnit.getHouseholdsNumber() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getHouseholdsNumber, basicUnit.getHouseholdsNumber()));
		}
		if(basicUnit.getRemark() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getRemark, basicUnit.getRemark()));
		}
		if(basicUnit.getIsDelete() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getIsDelete, basicUnit.getIsDelete()));
		}
		if(basicUnit.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getCreateId, basicUnit.getCreateId()));
		}
		if(basicUnit.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getCreateTime, basicUnit.getCreateTime()));
		}
		if(basicUnit.getUpdateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getUpdateId, basicUnit.getUpdateId()));
		}
		if(basicUnit.getUpdateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getUpdateTime, basicUnit.getUpdateTime()));
		}
		if(basicUnit.getDeleteId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getDeleteId, basicUnit.getDeleteId()));
		}
		if(basicUnit.getDeleteTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicUnit::getDeleteTime, basicUnit.getDeleteTime()));
		}
		queryWrapper.lambda().and(i -> i.eq(BasicUnit::getIsDelete, 0));
		return queryWrapper;

}
}
