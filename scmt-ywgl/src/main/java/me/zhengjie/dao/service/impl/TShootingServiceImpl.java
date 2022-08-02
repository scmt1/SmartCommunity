package me.zhengjie.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.TShooting;
import me.zhengjie.dao.mapper.TShootingMapper;
import me.zhengjie.dao.service.ITShootingService;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.FileUtil;
import javax.servlet.http.HttpServletResponse;
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
public class TShootingServiceImpl extends ServiceImpl<TShootingMapper, TShooting> implements ITShootingService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TShootingMapper tShootingMapper;

	public Result<Object> getTShootingById(String id){
		TShooting tShooting = tShootingMapper.selectById(id);
		if(tShooting!=null){
			return  ResultUtil.data(tShooting);
		}
		 return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTShootingListByPage(TShooting tShooting, SearchVo searchVo, PageVo pageVo){
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
		Page<TShooting> pageData = new Page<>(page, limit);
		QueryWrapper<TShooting> queryWrapper = new QueryWrapper<>();

		if(StringUtils.isNotEmpty(tShooting.getName())){
            queryWrapper.lambda().and(i -> i.like(TShooting::getName, tShooting.getName().trim()));
        }

		if(tShooting.getState() != null){
            queryWrapper.lambda().and(i -> i.eq(TShooting::getState, tShooting.getState()));
        }

		IPage<TShooting> result = tShootingMapper.selectPage(pageData, queryWrapper);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TShooting tShooting, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TShooting> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(tShooting.getName())){
            queryWrapper.lambda().and(i -> i.like(TShooting::getName, tShooting.getName().trim()));
        }

        if(tShooting.getState() != null){
            queryWrapper.lambda().and(i -> i.eq(TShooting::getState, tShooting.getState()));
        }


		List<TShooting> list = tShootingMapper.selectList(queryWrapper);
		for (TShooting re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("隐患等级", re.getLevel());
			map.put("隐患类型", re.getType());
			map.put("隐患名称", re.getName());
			map.put("隐患坐标点", re.getPosition());
			map.put("隐患描述", re.getDescription());
			map.put("检查地点", re.getAddress());
			map.put("违规部门", re.getDepId());
			map.put("风险等级", re.getRiskLevel());
			map.put("可能导致事故", re.getMayCause());
			map.put("备注", re.getRemark());
			map.put("隐患图片", re.getImgPath());
			map.put("隐患视频", re.getVideoPath());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "隐患排查.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param likeValue 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TShooting>  LikeAllFeild(String likeValue) {
		QueryWrapper<TShooting> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().or(i -> i.like(TShooting::getId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getLevel, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getType, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getName, likeValue));
		//queryWrapper.lambda().or(i -> i.like(TShooting::getState, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getPosition, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getDescription, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getAddress, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getDepId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getDepName, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getRiskLevel, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getMayCause, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getRemark, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getCreateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getCreateId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getUpdateTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getUpdateId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getDeleteId, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getDeleteTime, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getImgPath, likeValue));
		queryWrapper.lambda().or(i -> i.like(TShooting::getVideoPath, likeValue));
		queryWrapper.lambda().and(i -> i.eq(TShooting::getState, 1));
		return queryWrapper;

}
}
