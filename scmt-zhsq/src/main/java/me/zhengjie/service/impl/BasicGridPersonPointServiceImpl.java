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

import me.zhengjie.mapper.BasicGridPersonPointMapper;
import me.zhengjie.entity.BasicGridPersonPoint;
import me.zhengjie.service.IBasicGridPersonPointService;
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
public class BasicGridPersonPointServiceImpl extends ServiceImpl<BasicGridPersonPointMapper, BasicGridPersonPoint> implements IBasicGridPersonPointService {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	private final BasicGridPersonPointMapper basicGridPersonPointMapper;

	@Override
	public Result<Object> getBasicGridPersonPointById(String id){
		BasicGridPersonPoint basicGridPersonPoint = basicGridPersonPointMapper.selectById(id);
		if(basicGridPersonPoint!=null){
			return  ResultUtil.data(basicGridPersonPoint);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryBasicGridPersonPointListByPage(BasicGridPersonPoint  basicGridPersonPoint, SearchVo searchVo, PageVo pageVo){
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
		Page<BasicGridPersonPoint> pageData = new Page<>(page, limit);
		QueryWrapper<BasicGridPersonPoint> queryWrapper = new QueryWrapper<>();
		if (basicGridPersonPoint !=null) {
			queryWrapper = LikeAllFeild(basicGridPersonPoint,searchVo);
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
		IPage<BasicGridPersonPoint> result = basicGridPersonPointMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(BasicGridPersonPoint basicGridPersonPoint, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<BasicGridPersonPoint> queryWrapper = new QueryWrapper<>();
		if (basicGridPersonPoint !=null) {
			queryWrapper = LikeAllFeild(basicGridPersonPoint,null);
		}
		List<BasicGridPersonPoint> list = basicGridPersonPointMapper.selectList(queryWrapper);
		for (BasicGridPersonPoint re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	 * 查询当天网格员坐标数据
	 * @param gridPersonId
	 * @param gridPersonName
	 * @param date
	 * @return
	 */
	@Override
	public List<BasicGridPersonPoint> getToDayGridPersonPoint(String gridPersonId,String gridPersonName, String date) {
		return basicGridPersonPointMapper.getToDayGridPersonPoint(gridPersonId,gridPersonName,date);
	}

	/**
	 * 查询网格人员最新的坐标数据
	 * @param gridPersonId
	 * @return
	 */
    @Override
    public BasicGridPersonPoint getCurrentGridPersonPoint(String gridPersonId) {
        return basicGridPersonPointMapper.getCurrentGridPersonPoint(gridPersonId);
    }

    /**
	* 功能描述：构建模糊查询
	* @param basicGridPersonPoint 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<BasicGridPersonPoint>  LikeAllFeild(BasicGridPersonPoint  basicGridPersonPoint, SearchVo searchVo) {
		QueryWrapper<BasicGridPersonPoint> queryWrapper = new QueryWrapper<>();
		if(basicGridPersonPoint.getId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getId, basicGridPersonPoint.getId()));
		}
		if(basicGridPersonPoint.getGridPersonId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getGridPersonId, basicGridPersonPoint.getGridPersonId()));
		}
		if(basicGridPersonPoint.getGridPersonName() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getGridPersonName, basicGridPersonPoint.getGridPersonName()));
		}
		if(basicGridPersonPoint.getPosition() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getPosition, basicGridPersonPoint.getPosition()));
		}
		if(basicGridPersonPoint.getRemark() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getRemark, basicGridPersonPoint.getRemark()));
		}
		if(basicGridPersonPoint.getIsDelete() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getIsDelete, basicGridPersonPoint.getIsDelete()));
		}
		if(basicGridPersonPoint.getCreateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getCreateId, basicGridPersonPoint.getCreateId()));
		}
		if(basicGridPersonPoint.getCreateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getCreateTime, basicGridPersonPoint.getCreateTime()));
		}
		if(basicGridPersonPoint.getUpdateId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getUpdateId, basicGridPersonPoint.getUpdateId()));
		}
		if(basicGridPersonPoint.getUpdateTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getUpdateTime, basicGridPersonPoint.getUpdateTime()));
		}
		if(basicGridPersonPoint.getDeleteId() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getDeleteId, basicGridPersonPoint.getDeleteId()));
		}
		if(basicGridPersonPoint.getDeleteTime() != null){
			queryWrapper.lambda().and(i -> i.like(BasicGridPersonPoint::getDeleteTime, basicGridPersonPoint.getDeleteTime()));
		}
		queryWrapper.lambda().and(i -> i.eq(BasicGridPersonPoint::getIsDelete, 0));
		return queryWrapper;

}
}
