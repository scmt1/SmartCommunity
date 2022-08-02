package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.RelaRoleService;
import me.zhengjie.entity.TZhsqBottomTool;
import me.zhengjie.mapper.TZhsqBottomToolMapper;
import me.zhengjie.service.ITZhsqBottomToolService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dengjie
 * @since 2021-08-24
 */
@Service
@AllArgsConstructor
public class TZhsqBottomToolServiceImpl extends ServiceImpl<TZhsqBottomToolMapper, TZhsqBottomTool> implements ITZhsqBottomToolService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqBottomToolMapper tZhsqBottomToolMapper;

    public TZhsqBottomTool getTZhsqBottomToolById(String id){
        TZhsqBottomTool tZhsqBottomTool = tZhsqBottomToolMapper.selectById(id);
        if(tZhsqBottomTool!=null){
            return  tZhsqBottomTool;
        }
        return  null;
    }

    @Override
    public Result<Object> queryTZhsqBottomToolByPage(TZhsqBottomTool tZhsqBottomTool , SearchVo searchVo, PageVo pageVo){
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
        Page<TZhsqBottomTool> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqBottomTool> queryWrapper = new QueryWrapper<>();
        if (tZhsqBottomTool !=null) {
            queryWrapper = LikeAllFeild(tZhsqBottomTool,searchVo);
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
        IPage<TZhsqBottomTool> result = tZhsqBottomToolMapper.selectPage(pageData, queryWrapper);
        return  ResultUtil.data(result);
    }

    @Override
    public Map<String,Object> getTreeData() {
        QueryWrapper<TZhsqBottomTool> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getIsDelete,0));
        List<TZhsqBottomTool> tDataServices = tZhsqBottomToolMapper.selectList(queryWrapper);
        return buildTree(tDataServices,null);
    }

    public Map<String,Object> buildTree(List<TZhsqBottomTool> tDataServices, String roleId) {
        List<RelaRoleService> relaRoleServices = null;
        Set<TZhsqBottomTool> trees = new LinkedHashSet<>();
        Set<TZhsqBottomTool> tDataServiceSet= new LinkedHashSet<>();
        for (TZhsqBottomTool tDataService : tDataServices){
            if ("0".equals(tDataService.getPid())){
                if (roleId!=null){
                    relaRoleServices.forEach(item -> {
                        if (item.getServiceId().equals(tDataService.getId())){
                            tDataService.setPick(true);
                        }
                    });
                }
                trees.add(tDataService);
            }
            for (TZhsqBottomTool dataService : tDataServices){
                if (dataService.getPid().equals(tDataService.getId())){
//                    isChild = true;
                    if (tDataService.getChildren() == null){
                        tDataService.setChildren(new ArrayList<>());
                    }
                    if (roleId!=null){
                        relaRoleServices.forEach(item -> {
                            if (item.getServiceId().equals(dataService.getId())){
                                dataService.setPick(true);
                            }
                        });
                    }
                    tDataService.getChildren().add(dataService);
                }
            }
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = tDataServiceSet;
        }
        Integer total = tDataServices.size();
        Map<String,Object> map = new HashMap<>(2);
        map.put("total",total);
        map.put("records",CollectionUtils.isEmpty(trees)? tDataServices :trees);
        return map;
    }
    @Override
    public void download(TZhsqBottomTool tZhsqBottomTool, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqBottomTool> queryWrapper = new QueryWrapper<>();
        if (tZhsqBottomTool !=null) {
            queryWrapper = LikeAllFeild(tZhsqBottomTool,null);
        }
        List<TZhsqBottomTool> list = tZhsqBottomToolMapper.selectList(queryWrapper);
        for (TZhsqBottomTool re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", re.getName());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 模糊查询所有信息
     *
     * @param tZhsqBottomTool
     * @return
     */
    @Override
    public Result<Object> queryAllList(TZhsqBottomTool tZhsqBottomTool) {
        QueryWrapper<TZhsqBottomTool> queryWrapper = new QueryWrapper<>();
        if (tZhsqBottomTool != null) {
            queryWrapper = LikeAllFeild(tZhsqBottomTool, new SearchVo());
        }
        List<TZhsqBottomTool> selectList = tZhsqBottomToolMapper.selectList(queryWrapper);
        return ResultUtil.data(selectList);
    }

    /**
     * 功能描述：构建模糊查询
     * @param tZhsqBottomTool 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqBottomTool>  LikeAllFeild(TZhsqBottomTool tZhsqBottomTool, SearchVo searchVo) {
        QueryWrapper<TZhsqBottomTool> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(tZhsqBottomTool.getId())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getId, tZhsqBottomTool.getId()));
        }
        if(tZhsqBottomTool.getCreateId() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getCreateId, tZhsqBottomTool.getCreateId()));
        }
        if(tZhsqBottomTool.getCreateTime() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getCreateTime, tZhsqBottomTool.getCreateTime()));
        }
        if(tZhsqBottomTool.getGriddingId() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getGriddingId, tZhsqBottomTool.getGriddingId()));
        }
        if(StringUtils.isNotBlank(tZhsqBottomTool.getGriddingName())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getGriddingName, tZhsqBottomTool.getGriddingName()));
        }
        if(tZhsqBottomTool.getIcon() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getIcon, tZhsqBottomTool.getIcon()));
        }
        if(tZhsqBottomTool.getLevel() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getLevel, tZhsqBottomTool.getLevel()));
        }
        if(StringUtils.isNotBlank(tZhsqBottomTool.getName())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getName, tZhsqBottomTool.getName()));
        }
        if(StringUtils.isNotBlank(tZhsqBottomTool.getPid())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getPid, tZhsqBottomTool.getPid()));
        }
        if(StringUtils.isNotBlank(tZhsqBottomTool.getSourceId())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getSourceId, tZhsqBottomTool.getSourceId()));
        }
        if(StringUtils.isNotBlank(tZhsqBottomTool.getSourceName())){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getSourceName, tZhsqBottomTool.getSourceName()));
        }
        if(tZhsqBottomTool.getTypeId() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getTypeId, tZhsqBottomTool.getTypeId()));
        }
        if(tZhsqBottomTool.getTypeName() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getTypeName, tZhsqBottomTool.getTypeName()));
        }
        if(tZhsqBottomTool.getUpdateId() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getUpdateId, tZhsqBottomTool.getUpdateId()));
        }
        if(tZhsqBottomTool.getUpdateTime() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getUpdateTime, tZhsqBottomTool.getUpdateTime()));
        }
        if(tZhsqBottomTool.getRemark() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getRemark, tZhsqBottomTool.getRemark()));
        }
        if(tZhsqBottomTool.getParameter() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getParameter, tZhsqBottomTool.getParameter()));
        }
        if(tZhsqBottomTool.getUrl() != null){
            queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getUrl, tZhsqBottomTool.getUrl()));
        }
        if(searchVo!=null){
            if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                queryWrapper.lambda().and(i -> i.between(TZhsqBottomTool::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqBottomTool::getIsDelete, 0));
        return queryWrapper;

    }
}
