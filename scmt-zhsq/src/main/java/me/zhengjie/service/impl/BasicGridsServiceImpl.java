package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.BasicSocialOrganization;
import me.zhengjie.entity.GridDept;
import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.service.IBasicGridsService;
import me.zhengjie.util.BeanUtil;
import me.zhengjie.util.DictionaryUtils;
import me.zhengjie.util.ImportExeclUtil;
import me.zhengjie.util.StreetUtil;
import me.zhengjie.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.tree.VoidDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @author
 **/
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "BasicGrids")
public class BasicGridsServiceImpl extends ServiceImpl<BasicGridsMapper, BasicGrids> implements IBasicGridsService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicGridsMapper basicGridsMapper;

    private final GridDeptMapper deptMapper;


    @Override
    public Result<Object> getBasicGridsById(String id) {
        BasicGrids basicGrids = basicGridsMapper.selectById(id);
        if (basicGrids != null) {
            return ResultUtil.data(basicGrids);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryBasicGridsListByPage(BasicGrids basicGrids, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicGrids> pageData = new Page<>(page, limit);
        QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
        if (basicGrids != null) {
            queryWrapper = LikeAllFeild(basicGrids, searchVo);
        }
        if(pageVo !=null && pageVo.getSort()!=null){
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
        queryWrapper.orderByAsc("create_time");
        IPage<BasicGrids> result = basicGridsMapper.selectPage(pageData,queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicGrids basicGrids, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
        if (basicGrids != null) {
            queryWrapper = LikeAllFeild(basicGrids, null);
        }
        List<BasicGrids> list = basicGridsMapper.selectList(queryWrapper);
        for (BasicGrids re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("网格名称", re.getName());
            map.put("所属街道", re.getStreetName());
            map.put("所属社区", re.getCommunityName());
            map.put("网格长", re.getGridPersonName());
            map.put("党组织", re.getOrganization());
//            map.put("地图标注", re.getPosition());
//            map.put("排序", re.getOrderNumber());
            map.put("备注", re.getRemark());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param basicGrids 需要模糊查询的信息
     * @return 返回查询
     */

    public QueryWrapper<BasicGrids> LikeAllFeild(BasicGrids basicGrids, SearchVo searchVo) {
        QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(i -> i.eq(BasicGrids::getIsDelete, 0));

        if (basicGrids.getId() != null && StringUtils.isNotBlank(basicGrids.getId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getId, basicGrids.getId()));
        }
        if (StringUtils.isNotBlank(basicGrids.getName())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getName, basicGrids.getName()));
        }
        if (StringUtils.isNotBlank(basicGrids.getStreetId())) {
            String[] streetIds = basicGrids.getStreetId().split(",");
            queryWrapper.lambda().and(i -> i.in(BasicGrids::getStreetId, streetIds));
           // queryWrapper.lambda().and(i -> i.eq(BasicGrids::getStreetId, basicGrids.getStreetId()));
        }
        if (StringUtils.isNotBlank(basicGrids.getStreetName())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getStreetName, basicGrids.getStreetName()));
        }

        if (StringUtils.isNotBlank(basicGrids.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getCommunityId, basicGrids.getCommunityId()));
        }
        if (StringUtils.isNotBlank(basicGrids.getCommunityName())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getCommunityName, basicGrids.getCommunityName()));
        }

        if (StringUtils.isNotBlank(basicGrids.getGridPersonId())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getGridPersonId, basicGrids.getGridPersonId()));
        }
        if (StringUtils.isNotBlank(basicGrids.getGridPersonName())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getGridPersonName, basicGrids.getGridPersonName()));
        }
        if (StringUtils.isNotBlank(basicGrids.getOrganization())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getOrganization, basicGrids.getOrganization()));
        }
        if (StringUtils.isNotBlank(basicGrids.getPosition())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getPosition, basicGrids.getPosition()));
        }
        if (null != basicGrids.getOrderNumber()) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getOrderNumber, basicGrids.getOrderNumber()));
        }
        if (StringUtils.isNotBlank(basicGrids.getRemark())) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getRemark, basicGrids.getRemark()));
        }
        if (basicGrids.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getIsDelete, basicGrids.getIsDelete()));
        }
        if (basicGrids.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getCreateId, basicGrids.getCreateId()));
        }
        if (basicGrids.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getCreateTime, basicGrids.getCreateTime()));
        }
        if (basicGrids.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getUpdateId, basicGrids.getUpdateId()));
        }
        if (basicGrids.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getUpdateTime, basicGrids.getUpdateTime()));
        }
        if (basicGrids.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getDeleteId, basicGrids.getDeleteId()));
        }
        if (basicGrids.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicGrids::getDeleteTime, basicGrids.getDeleteTime()));
        }

        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                //queryWrapper.lambda().and(i -> i.between(BasicGrids::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
                queryWrapper.lambda().and(i -> i.ge(BasicGrids::getCreateTime, searchVo.getStartDate() + " 00:00:00"));
                queryWrapper.lambda().and(i -> i.le(BasicGrids::getCreateTime, searchVo.getEndDate() + " 23:59:59"));
            }
            //搜索条件
            if (!StringUtils.isEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(BasicGrids::getName, searchVo.getSearchInfo())
                        .or().like(BasicGrids::getGridPersonName, searchVo.getSearchInfo())
                );
            }
        }
        return queryWrapper;

    }

    @Override
    @Cacheable
    public Result<Object> queryAllList(BasicGrids basicGrids) {
        QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
        if (basicGrids != null) {
            queryWrapper = LikeAllFeild(basicGrids, null);
        }
        queryWrapper.orderByAsc("create_time");
        List<BasicGrids> result = basicGridsMapper.selectList(queryWrapper);
        return ResultUtil.data(result);
    }


    /***
     * 根据人员Id查询所管理的网格
     */
    @Override
    @Cacheable
    public List<BasicGrids> queryMyManagedGridsList(String personId) {
        List<BasicGrids> result = basicGridsMapper.queryMyManagedGridsList(personId);
        return result;
    }

    /**
     * 根据网格Id查询该网格下个网格员、建筑、房屋等。。
     * @param gridsId
     * @return
     */
    @Override
    @Cacheable
    public List<Map<String, Object>> queryGridsOwnInformation(String gridsId) {
        List<Map<String, Object>> result = basicGridsMapper.queryGridsOwnInformation(gridsId);
        return result;
    }

    /**
     * 查询该网格下有多少个网格员
     * @param gridsId
     * @return
     */
    @Override
    @Cacheable
    public List<Map<String, Object>> queryGridmanList(String gridsId) {
        return basicGridsMapper.queryGridmanList(gridsId);
    }

    @Override
    public List<Map<String, Object>> queryAllGridsTree() {
        return basicGridsMapper.queryAllGridsTree();
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //读取一个对象的信息
        BasicGrids basicPerson = new BasicGrids();
        List<BasicGrids> readData =ImportExeclUtil.readDateListT(wb, basicPerson, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData =  matchingData(readData,result);
            if(isMatchingData)
            {
                return ResultUtil.data(result);
            }
            for(BasicGrids basicGrids:readData){
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                basicGrids.setIsDelete(0);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = basicGridsMapper.insert(basicGrids);
                if(insert!=1){
                    throw new BusinessException(ResultCode.FAILURE);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILURE);
        }

        return ResultUtil.data(result);
    }

    /**
     * 匹配数据
     */
    public Boolean  matchingData(List<BasicGrids> readData,List<Map<String, Object>>  result ){
        if(result==null){
            result = new ArrayList<>();
        }
        Boolean isMatchingData = false;
        StreetUtil streetUtil = new StreetUtil(deptMapper);
        streetUtil.Init();
        if(readData!=null && readData.size()>0){
            for (int i=0;i<readData.size();i++ ) {
                BasicGrids basicGrids = readData.get(i);
                if(basicGrids==null|| BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if(basicGrids!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("number",i+1);
                    map.put("success","成功");

                    String msg="";
                    //网格名称
                    if (StringUtils.isNotBlank(basicGrids.getName())) {
                        QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().and(item ->item.eq(BasicGrids::getIsDelete, 0));
                        queryWrapper.lambda().and(item ->item.eq(BasicGrids::getName, basicGrids.getName()));
                        queryWrapper.lambda().and(item ->item.eq(BasicGrids::getCommunityName, basicGrids.getCommunityName()));
                        queryWrapper.lambda().and(item ->item.eq(BasicGrids::getStreetName, basicGrids.getStreetName()));
                        Integer integer = basicGridsMapper.selectCount(queryWrapper);
                        //数据库重复
                        if(integer>0){
                            msg+="网格名称重复\n";
                        }
                        //map 重复
                        else{
                           for(int j=i-1;j>=0;j--){
                               BasicGrids basicGrids1 = readData.get(j);
                               if(basicGrids1!=null && StringUtils.isNotBlank(basicGrids.getName()) && StringUtils.isNotBlank(basicGrids.getCommunityName()) && StringUtils.isNotBlank(basicGrids.getStreetName())){
                                   if(basicGrids.getName().equals(basicGrids1.getName())&&basicGrids.getCommunityName().equals(basicGrids1.getCommunityName())&&basicGrids.getStreetName().equals(basicGrids1.getStreetName()) ){
                                       msg+="与第"+(j+1)+"条数据小区名称重复\n";
                                       break;
                                   }
                               }
                           }
                        }

                    }
                    else{
                        msg+="网格名称为空\n";
                    }
                    //所属街道
                    if (StringUtils.isNotBlank(basicGrids.getStreetName())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreetName(), streetUtil.streetDepts);
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setStreetId(s);
                        }
                        else{
                            msg+="所属街道未匹配上，请检查所属街道是否入库\n";
                        }
                    }
                    else{
                        msg+="所属街道为空\n";
                    }
                    //所属社区
                    if (StringUtils.isNotBlank(basicGrids.getCommunityName())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreetName(),basicGrids.getCommunityName(), streetUtil.gridDepts);
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setCommunityId(s);
                        }
                        else{
                            msg+="所属社区未匹配上，请检查所属社区是否入库\n";
                        }
                    }
                    else{
                        msg+="所属社区为空\n";
                    }
                    if(msg.length()>0){
                        isMatchingData = true;
                        map.put("success","失败");
                    }
                    map.put("msg",msg);
                    result.add(map);
                }
            }
        }

        return isMatchingData;
    }

}
