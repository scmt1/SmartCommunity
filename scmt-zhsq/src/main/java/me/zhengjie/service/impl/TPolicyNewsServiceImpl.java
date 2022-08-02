package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TEarlywarning;
import me.zhengjie.entity.TPolicyNews;
import me.zhengjie.mapper.TPolicyNewsMapper;
import me.zhengjie.service.ITPolicyNewsService;
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
public class TPolicyNewsServiceImpl extends ServiceImpl<TPolicyNewsMapper, TPolicyNews> implements ITPolicyNewsService {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private TPolicyNewsMapper tPolicyNewsMapper;

	@Override
	public Result<Object> getTPolicyNewsById(String id){
		TPolicyNews TPolicyNews = tPolicyNewsMapper.selectById(id);
		if(TPolicyNews!=null){
			return  ResultUtil.data(TPolicyNews);
		}
		return  ResultUtil.error("获取据败，失败原因：查无此数据！");
	}

	@Override
	public Result<Object> queryTPolicyNewsListByPage(TPolicyNews  tPolicyNews, SearchVo searchVo, PageVo pageVo){
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
		Page<TPolicyNews> pageData = new Page<>(page, limit);
		QueryWrapper<TPolicyNews> queryWrapper = new QueryWrapper<>();
		if (tPolicyNews !=null) {
			queryWrapper = LikeAllFeild(tPolicyNews,searchVo);
		}
		if (pageVo.getSort() != null) {
		    if (pageVo.getSort().equals("asc")) {
		        queryWrapper.orderByAsc("t_policy_news."+pageVo.getSort());
		    } else {
		        queryWrapper.orderByDesc("t_policy_news."+pageVo.getSort());
		    }
		} else {
		    queryWrapper.orderByDesc("t_policy_news.create_time");
		}
		IPage<TPolicyNews> result = tPolicyNewsMapper.selectTPolicyNewsPageList(queryWrapper, pageData);
		return  ResultUtil.data(result);
	}
	@Override
	public void download(TPolicyNews tPolicyNews, HttpServletResponse response) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QueryWrapper<TPolicyNews> queryWrapper = new QueryWrapper<>();
		if (tPolicyNews !=null) {
			queryWrapper = LikeAllFeild(tPolicyNews,null);
		}
		List<TPolicyNews> list = tPolicyNewsMapper.selectList(queryWrapper);
		for (TPolicyNews re : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("新闻名称", re.getName());
			map.put("内容描述", re.getContent());
			map.put("类型", re.getType());
			map.put("发布单位", re.getIssuedBy());
			map.put("图标地址", re.getImgUrl());
			map.put("创建时间", re.getCreateTime());
			map.put("是否显示 1是 0 否", re.getIsShow());
			mapList.add(map);
		}
		FileUtil.createExcel(mapList, "exel.xlsx", response);
	}

	/**
	* 功能描述：构建模糊查询
	* @param tPolicyNews 需要模糊查询的信息
	* @return 返回查询
	*/
	public QueryWrapper<TPolicyNews>  LikeAllFeild(TPolicyNews  tPolicyNews, SearchVo searchVo) {
		QueryWrapper<TPolicyNews> queryWrapper = new QueryWrapper<>();
		if(tPolicyNews.getId() != null){
			queryWrapper.and(i -> i.like("t_policy_news.id", tPolicyNews.getId()));
		}
		if(tPolicyNews.getName() != null){
			queryWrapper.and(i -> i.like("t_policy_news.name", tPolicyNews.getName()));
		}
		if(tPolicyNews.getContent() != null){
			queryWrapper.and(i -> i.like("t_policy_news.content", tPolicyNews.getContent()));
		}
		if(tPolicyNews.getType() != null){
			queryWrapper.and(i -> i.like("t_policy_news.type", tPolicyNews.getType()));
		}
		if(tPolicyNews.getIssuedBy() != null){
			queryWrapper.and(i -> i.like("t_policy_news.issued_by", tPolicyNews.getIssuedBy()));
		}
		if(tPolicyNews.getImgUrl() != null){
			queryWrapper.and(i -> i.like("t_policy_news.img_url", tPolicyNews.getImgUrl()));
		}
		if(tPolicyNews.getIsDelete() != null){
			queryWrapper.and(i -> i.like("t_policy_news.is_delete", tPolicyNews.getIsDelete()));
		}
		if(tPolicyNews.getCreateId() != null){
			queryWrapper.and(i -> i.like("t_policy_news.create_id", tPolicyNews.getCreateId()));
		}
		if(tPolicyNews.getCreateTime() != null){
			queryWrapper.and(i -> i.like("t_policy_news.create_time", tPolicyNews.getCreateTime()));
		}
		if(tPolicyNews.getUpdateId() != null){
			queryWrapper.and(i -> i.like("t_policy_news.update_id", tPolicyNews.getUpdateId()));
		}
		if(tPolicyNews.getUpdateTime() != null){
			queryWrapper.and(i -> i.like("t_policy_news.update_time", tPolicyNews.getUpdateTime()));
		}
		if(tPolicyNews.getDeleteId() != null){
			queryWrapper.and(i -> i.like("t_policy_news.delete_id", tPolicyNews.getDeleteId()));
		}
		if(tPolicyNews.getDeleteTime() != null){
			queryWrapper.and(i -> i.like("t_policy_news.delete_time", tPolicyNews.getDeleteTime()));
		}
		if(tPolicyNews.getIsShow() != null){
			queryWrapper.and(i -> i.like("t_policy_news.is_show", tPolicyNews.getIsShow()));
		}
		if(searchVo!=null){
			if(StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())){
				queryWrapper.and(i -> i.between("t_policy_news.create_time", searchVo.getStartDate(),searchVo.getEndDate()));
			}
		}
		queryWrapper.and(i -> i.eq("t_policy_news.is_delete", 0));
		return queryWrapper;
	
}
}