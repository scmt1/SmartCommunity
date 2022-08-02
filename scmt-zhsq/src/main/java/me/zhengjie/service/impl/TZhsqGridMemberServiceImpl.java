package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGridPersonPoint;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.RelaGridsPersonGrids;
import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.mapper.*;
import me.zhengjie.service.IRelaGridsPersonGridsService;
import me.zhengjie.service.ITZhsqGridMemberService;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 **/
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "TZhsqGridMember")
public class TZhsqGridMemberServiceImpl extends ServiceImpl<TZhsqGridMemberMapper, TZhsqGridMember> implements ITZhsqGridMemberService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqGridMemberMapper tZhsqGridMemberMapper;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final YWTZhsqGridMemberMapper ywtZhsqGridMemberMapper;

    private final RelaGridsPersonGridsMapper relaGridsPersonGridsMapper;

    private final IRelaGridsPersonGridsService relaGridsPersonGridsService;


    private final BasicGridPersonPointServiceImpl basicGridPersonPointService;


    private final BasicGridPersonPointMapper basicGridPersonPointMapper;

    private final GridDeptMapper deptMapper;

    private final BasicGridsMapper basicGridsMapper;

    @Override
    public Result<Object> getTZhsqGridMemberById(String id) {
        TZhsqGridMember tZhsqGridMember = tZhsqGridMemberMapper.selectById(id);
        if (tZhsqGridMember != null) {
            return ResultUtil.data(tZhsqGridMember);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTZhsqGridMemberListByPage(TZhsqGridMember tZhsqGridMember, String gridId, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqGridMember> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper<>();
        if (tZhsqGridMember != null) {
            tZhsqGridMember.setOwnedGrid(gridId);
            queryWrapper = LikeAllFeild(tZhsqGridMember, null, searchVo, null);
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
//        queryWrapper.orderByDesc("create_time");
        IPage<TZhsqGridMember> result = tZhsqGridMemberMapper.selectPage(pageData, queryWrapper);
        result.setRecords(relaGridsPersonGridsService.getGridsPersonGridsData(result.getRecords()));
        return ResultUtil.data(result);
    }

    @Override
    public List<Map<String, Object>> selectGridMemberTree() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList = tZhsqGridMemberMapper.selectGridMemberTree();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map:mapList ){
            if(map != null){
                if(map.containsKey("id") && map.containsKey("pid") && map.containsKey("title") && map.get("title")!=null){

                    String id = map.get("id").toString();
                    String pid = map.get("pid").toString();
                    String title = map.get("title")!=null?map.get("title").toString():"";
                    List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                    for(Map<String, Object> mapChild:mapList ){
                        if(mapChild != null ){
                            if( mapChild.containsKey("pid") && mapChild.get("pid").toString().trim().equals(id)){
                                children.add(mapChild);
                            }
                        }
                    }
                    map.put("children",children);

                    if("0".equals(pid)){
                        resultList.add(map);
                    }
                }
            }
        }
        return resultList;
    }
    @Override
    public void download(TZhsqGridMember tZhsqGridMember, HttpServletResponse response,SearchVo searchVo) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper<>();
        if (tZhsqGridMember != null) {
            tZhsqGridMember.setOwnedGrid(tZhsqGridMember.getGridId());
            queryWrapper = LikeAllFeild(tZhsqGridMember, null,searchVo, null);
        }
        List<TZhsqGridMember> list = tZhsqGridMemberMapper.selectByMyWrapper(queryWrapper);
        for (TZhsqGridMember re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", re.getName());
            map.put("政治面貌", re.getType());
            map.put("性别", re.getSex());
            map.put("民族", re.getNation());
            map.put("身份证号", re.getIdCard());
            map.put("手机号", re.getPhone());
            map.put("个人简介", re.getPersonalProfile());
//            map.put("头像", re.getHeadPortrait());
            map.put("所属街道", re.getStreetName());
            map.put("所属社区", re.getCommunityName());
            map.put("所属岗位", re.getPost());
//            map.put("级别", re.getPostLevel());
            map.put("职责范畴", re.getResponsibilities());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述： 自定义id 单条保存（用于获取ID）
     *
     * @param tZhsqGridMember 保存数据
     * @return 返回是否成功
     */
    @Override
    public boolean insertEntity(TZhsqGridMember tZhsqGridMember) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        tZhsqGridMember.setId(uuid);
        int insert = tZhsqGridMemberMapper.insert(tZhsqGridMember);
        if (insert > 0) {
            if (tZhsqGridMember.getGrid() != null && tZhsqGridMember.getGrid().length > 0) {
                String[] grid = tZhsqGridMember.getGrid();
                for (String item : grid) {
                    RelaGridsPersonGrids relaGridsPersonGrids = new RelaGridsPersonGrids();
                    relaGridsPersonGrids.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    relaGridsPersonGrids.setGridsId(item);
                    relaGridsPersonGrids.setGridsPersonId(uuid);
                    relaGridsPersonGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    relaGridsPersonGridsMapper.insert(relaGridsPersonGrids);
                }

            }

            return true;
        } else {
            return false;
        }

    }

    ;


    @Override
    public boolean updateByEntity(TZhsqGridMember tZhsqGridMember) {
//		tZhsqGridMemberService.updateByEntity(tZhsqGridMember);
        int num = tZhsqGridMemberMapper.updateById(tZhsqGridMember);
        if (num > 0) {
            //删除原来的中间表信息
            QueryWrapper<RelaGridsPersonGrids> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(RelaGridsPersonGrids::getGridsPersonId, tZhsqGridMember.getId()));
            relaGridsPersonGridsMapper.delete(queryWrapper);
            //新增
            if (tZhsqGridMember.getGrid() != null && tZhsqGridMember.getGrid().length > 0) {
                String[] grid = tZhsqGridMember.getGrid();
                for (String item : grid) {
                    RelaGridsPersonGrids relaGridsPersonGrids = new RelaGridsPersonGrids();
                    relaGridsPersonGrids.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    relaGridsPersonGrids.setGridsId(item);
                    relaGridsPersonGrids.setGridsPersonId(tZhsqGridMember.getId());
                    relaGridsPersonGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    relaGridsPersonGridsMapper.insert(relaGridsPersonGrids);
                }
            }
            return true;
        } else {
            return false;

        }
    }


    @Override//TZhsqGridMember gridMember, PageVo pageVo
    public Result<Object> getStatisticsData(TZhsqGridMember gridMember, PageVo pageVo) {
        int page = 1;
        int size = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                size = pageVo.getPageSize();
            }
        }
        Page<TZhsqGridMember> pageEntity = new Page<>(page, size);
        IPage<Map<String, Object>> statisticsData = ywtZhsqGridMemberMapper.getStatisticsData(gridMember, pageEntity);
        return ResultUtil.data(statisticsData);
    }

    /**
     * 网格干部 模糊查询所有
     *
     * @param tZhsqGridMember
     * @return
     */
    @Override
    public Result<Object> queryGridMemberList(TZhsqGridMember tZhsqGridMember,String gridId) {
        QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper<>();
        if (tZhsqGridMember != null) {
            queryWrapper = LikeAllFeild(tZhsqGridMember,gridId, null, null);
        }
        queryWrapper.orderByDesc("create_time");
        List<TZhsqGridMember> tZhsqGridMembers = tZhsqGridMemberMapper.selectList(queryWrapper);
        //查询中间表、关联网格信息
        for (TZhsqGridMember zhsqGridMember : tZhsqGridMembers) {
            List<RelaGridsPersonGrids> dataByPersonId = relaGridsPersonGridsService.getDataByPersonId(zhsqGridMember.getId());
            zhsqGridMember.setGridsPersonGrids(dataByPersonId);
        }
        return ResultUtil.data(tZhsqGridMembers);
    }

    /**
     * 查询当前网格下的网格长
     * @param gridId
     * @return
     */
    @Override
    public Result<Object> queryAllTZhsqGridMemberListByGridId(String gridId) {
        List<TZhsqGridMember> tZhsqGridMembers = tZhsqGridMemberMapper.queryAllTZhsqGridManageListByGridId(gridId);
        return ResultUtil.data(tZhsqGridMembers);
    }

    /**
     * 查询全部网格人口档案
     * @param tZhsqGridMember
     * @param key
     * @return
     */
    @Override
    public Result<Object> queryAllTZhsqGridMemberListByAnyWayWhere(TZhsqGridMember tZhsqGridMember, String key) {
        QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper<>();
        if (tZhsqGridMember != null) {
            String gridId = null;
            if(StringUtils.isNotBlank(tZhsqGridMember.getGridId())){
                gridId = tZhsqGridMember.getGridId();
            }
            queryWrapper = LikeAllFeild(tZhsqGridMember, gridId,null, key);
        }
        queryWrapper.orderByDesc("create_time");
        List<TZhsqGridMember> tZhsqGridMembers = tZhsqGridMemberMapper.selectList(queryWrapper);
        for (TZhsqGridMember record : tZhsqGridMembers) {
            BasicGridPersonPoint currentGridPersonPoint = basicGridPersonPointService.getCurrentGridPersonPoint(record.getId());
            if (currentGridPersonPoint != null) {
                record.setPosition(currentGridPersonPoint.getPosition());
            }
        }
        tZhsqGridMembers= relaGridsPersonGridsService.getGridsPersonGridsData(tZhsqGridMembers);
        return ResultUtil.data(tZhsqGridMembers);
    }

    /**
     * 查询网格人员轨迹
     * @param gridPersonId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<BasicGridPersonPoint> gridPersonTrackQuery(String gridPersonId, String startTime, String endTime) {
        return basicGridPersonPointMapper.gridPersonTrackQuery(gridPersonId, startTime, endTime);
    }

    /**
     * 查询当前网格下的网格员
     * @param gridId
     * @return
     */
    @Override
    public Result<Object> queryAllGridMemberListByGridId(String gridId) {
        List<TZhsqGridMember> tZhsqGridMembers = tZhsqGridMemberMapper.queryAllTZhsqGridMemberListByGridId(gridId);
        return ResultUtil.data(tZhsqGridMembers);
    }

    /**
     * 判断网格是否已有网格长
     * @param s
     * @return
     */
    @Override
    public List<TZhsqGridMember> existsGrid(String s) {
        return tZhsqGridMemberMapper.existsGrid(s);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqGridMember 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqGridMember> LikeAllFeild(TZhsqGridMember tZhsqGridMember, String gridId, SearchVo searchVo, String key) {
        QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper<>();
        if (tZhsqGridMember.getId() != null) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getId, tZhsqGridMember.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getName, tZhsqGridMember.getName()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getType())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getType, tZhsqGridMember.getType()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getSex())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getSex, tZhsqGridMember.getSex()));
        }
        if (tZhsqGridMember.getBirthday() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getBirthday, tZhsqGridMember.getBirthday()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getNation())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getNation, tZhsqGridMember.getNation()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getIdCard())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getIdCard, tZhsqGridMember.getIdCard()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getPhone())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getPhone, tZhsqGridMember.getPhone()));
        }
        if (tZhsqGridMember.getFixedTelephone() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getFixedTelephone, tZhsqGridMember.getFixedTelephone()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getPersonalProfile())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getPersonalProfile, tZhsqGridMember.getPersonalProfile()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getHeadPortrait())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getHeadPortrait, tZhsqGridMember.getHeadPortrait()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getStreetId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getStreetId, tZhsqGridMember.getStreetId()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getStreetName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getStreetName, tZhsqGridMember.getStreetName()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getCommunityId, tZhsqGridMember.getCommunityId()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getCommunityName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getCommunityName, tZhsqGridMember.getCommunityName()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getPost())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getPost, tZhsqGridMember.getPost()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getPostLevel())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getPostLevel, tZhsqGridMember.getPostLevel()));
        }
        if (StringUtils.isNotBlank(tZhsqGridMember.getResponsibilities())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getResponsibilities, tZhsqGridMember.getResponsibilities()));
        }
        if (tZhsqGridMember.getPosition() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getPosition, tZhsqGridMember.getPosition()));
        }
        if (tZhsqGridMember.getOrderNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getOrderNumber, tZhsqGridMember.getOrderNumber()));
        }
        if (tZhsqGridMember.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getIsDelete, tZhsqGridMember.getIsDelete()));
        }
        if (tZhsqGridMember.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getCreateId, tZhsqGridMember.getCreateId()));
        }
        if (tZhsqGridMember.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getCreateTime, tZhsqGridMember.getCreateTime()));
        }
        if (tZhsqGridMember.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getUpdateId, tZhsqGridMember.getUpdateId()));
        }
        if (tZhsqGridMember.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getUpdateTime, tZhsqGridMember.getUpdateTime()));
        }
        if (tZhsqGridMember.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getDeleteId, tZhsqGridMember.getDeleteId()));
        }
        if (tZhsqGridMember.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getDeleteTime, tZhsqGridMember.getDeleteTime()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TZhsqGridMember::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }

            //搜索条件
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(TZhsqGridMember::getName, searchVo.getSearchInfo())
                        .or().like(TZhsqGridMember::getPhone, searchVo.getSearchInfo())
                        .or().like(TZhsqGridMember::getIdCard, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqGridMember::getIsDelete, 0));

        if (StringUtils.isNotEmpty(tZhsqGridMember.getOwnedGrid())) {
            QueryWrapper<RelaGridsPersonGrids> relaGridsPersonGridsQueryWrapper = new QueryWrapper<>();
            relaGridsPersonGridsQueryWrapper.lambda().and(i -> i.eq(RelaGridsPersonGrids::getGridsId, tZhsqGridMember.getOwnedGrid()));
            List<RelaGridsPersonGrids> relaGridsPersonGrids = relaGridsPersonGridsMapper.selectList(relaGridsPersonGridsQueryWrapper);
            ArrayList<String> strings = new ArrayList<>();
            for (RelaGridsPersonGrids relaGridsPersonGrid : relaGridsPersonGrids) {
                strings.add(relaGridsPersonGrid.getGridsPersonId());
            }
            if (strings != null && strings.size() > 0) {
                queryWrapper.and(i -> i.in("id", strings));
            }
            else {
                queryWrapper.and(i -> i.isNull("id"));
            }
        }

        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(wrapper -> wrapper.like("name", key).or().like("id_card", key).or().like("phone", key));
        }

        if(StringUtils.isNotEmpty(gridId)){
            queryWrapper.lambda().and(i -> i.inSql(TZhsqGridMember::getId, "(SELECT grids_person_id FROM rela_grids_person_grids WHERE grids_id = '" + gridId + "')"));
        }
        return queryWrapper;
    }

    @Override
    public int selectByCommunityIdAndGridId(String communityId, String gridId){
        return tZhsqGridMemberMapper.selectByCommunityIdAndGridId(communityId,gridId);
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile,Boolean isGridLength) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //读取一个对象的信息
        TZhsqGridMember tZhsqGridMember = new TZhsqGridMember();
        List<TZhsqGridMember> readData =ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData =  matchingData(readData, isGridLength,result);
            if(isMatchingData)
            {
                return ResultUtil.data(result);
            }
            for(TZhsqGridMember basicGrids:readData){
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                basicGrids.setIsDelete(0);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                Boolean insert = insertEntity(basicGrids);
                if(!insert){
                    throw new BusinessException(ResultCode.FAILURE);
                }
                else {
                    //更新网格中的网格长
                    if (isGridLength) {
                        String[] grid = tZhsqGridMember.getGrid();
                        BasicGrids basicGrid = basicGridsMapper.selectById(basicGrids.getGridId());
                        basicGrid.setId(basicGrids.getGridId());
                        basicGrid.setGridPersonId(basicGrids.getId());
                        basicGrid.setGridPersonName(basicGrids.getName());
                        int i = basicGridsMapper.updateById(basicGrid);
                        if(i!=1){
                            throw new BusinessException(ResultCode.FAILURE);
                        }
                    }
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
    public Boolean  matchingData(List<TZhsqGridMember> readData,Boolean isGridLength,List<Map<String, Object>>  result ){
        if(result==null){
            result = new ArrayList<>();
        }
        Boolean isMatchingData = false;
        StreetUtil streetUtil = new StreetUtil(deptMapper);
        streetUtil.Init();

        DictionaryUtils dictionaryUtils = new DictionaryUtils(deptMapper);
        dictionaryUtils.init();

        GridUtil gridUtil = new GridUtil(basicGridsMapper);
        gridUtil.init();

        if(readData!=null && readData.size()>0){
            for (int i=0;i<readData.size();i++ ) {
                TZhsqGridMember basicGrids = readData.get(i);
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if(basicGrids!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("number",i+1);
                    map.put("success","成功");

                    String msg="";

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
                    //所属网格
                    if (StringUtils.isNotBlank(basicGrids.getGridName())) {
                        String s = GridUtil.matchingGrid(basicGrids.getGridName(),basicGrids.getCommunityName(),basicGrids.getStreetName(), gridUtil.basicGrids);
                        if(StringUtils.isNotBlank(s)){
                            String[] ss = new String[]{s};
                            basicGrids.setGrid(ss);
                            basicGrids.setGridId(s);
                            if(isGridLength){
                                List<TZhsqGridMember> tZhsqGridMembers = existsGrid(s);
                                if(tZhsqGridMembers.size()>0){
                                    msg+="该网格已有网格长，请检查！\n";
                                }
                            }

                        }
                        else{
                            msg+="所属网格未匹配上，请检查所属网格是否入库\n";
                        }
                    }
                    else{
                        msg+="所属网格为空\n";
                    }
                    //姓名
                    if(StringUtils.isBlank(basicGrids.getName())){
                        msg+="姓名为空\n";
                    }
                    else{
                        if(!DictionaryUtils.isChinese(basicGrids.getName())){
                            msg+="姓名格式不正确，不能包含英文字母或者数字\n";
                        }
                    }
                    //性别
                    if(StringUtils.isBlank(basicGrids.getSex())){
                        msg+="性别为空\n";
                    }
                    //政治面貌
                    if(StringUtils.isNotBlank(basicGrids.getType())){
                        String s = dictionaryUtils.politicalFaceType(basicGrids.getType());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setType(s);
                        }
                        else{
                            msg+="政治面貌未匹配上，请检查当前政治面貌（字典）是否入库\n";
                        }
                    }
                    else{
                        msg+="政治面貌为空\n";
                    }
                    //民族
                    if(StringUtils.isNotBlank(basicGrids.getNation())){
                        String s = dictionaryUtils.nationType(basicGrids.getNation());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setNation(s);
                        }
                        else{
                            msg+="民族未匹配上，请检查当前民族（字典）是否入库\n";
                        }
                    }
                    else{
                        msg+="民族为空\n";
                    }

                    //手机号
                    if(StringUtils.isNotBlank(basicGrids.getPhone())){
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPhone());
                        if(!chinaPhoneLegal){
                            msg+="手机号格式不正确，请检查\n";
                        }
                        else{
                            //根据手机号获取信息
                            QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getPhone, basicGrids.getPhone()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                            int res = tZhsqGridMemberMapper.selectCount(queryWrapper);
                            //数据库重复
                            if (res >= 1) {
                                msg+="手机号重复,请检查\n";
                            }
                            //map 重复
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqGridMember basicGrids1 = readData.get(j);
                                    if(basicGrids1!=null && basicGrids.getPhone().equals(basicGrids1.getPhone()) ){
                                        msg+="与第"+(j+1)+"条数据手机号重复\n";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        msg+="手机号为空\n";
                    }

                    //身份证号
                    if(StringUtils.isNotBlank(basicGrids.getIdCard())){
                        String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getIdCard());
                        if(StringUtils.isNotBlank(chinaPhoneLegal)&& !chinaPhoneLegal.equals("YES")){
                            msg+=chinaPhoneLegal+"，请检查\n";
                        }
                        else{
                            //根据id和身份证获取信息
                            QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIdCard, basicGrids.getIdCard()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                            int res = tZhsqGridMemberMapper.selectCount(queryWrapper);
                            //数据库重复
                            if (res >= 1) {
                                msg+="身份证号码重复,请检查\n";
                            }
                            //map 重复
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqGridMember basicGrids1 = readData.get(j);
                                    if(basicGrids1!=null && basicGrids.getIdCard().equals(basicGrids1.getIdCard()) ){
                                        msg+="与第"+(j+1)+"条数据身份证号重复\n";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        msg+="身份证号为空\n";
                    }
                    //个人简介
                    if(StringUtils.isBlank(basicGrids.getPersonalProfile())){
                        msg+="个人简介为空\n";
                    }
                    //所属岗位
                    if(StringUtils.isNotBlank(basicGrids.getPost())){
                        if(!"网格长".equals(basicGrids.getPost())&&!"专职网格员".equals(basicGrids.getPost())&&!"兼职网格员".equals(basicGrids.getPost())){
                            msg+="所属岗位错误，不是网格长、专职网格员、兼职网格员其中一种\n";
                        }
                    }
                    else {
                        msg+="所属岗位为空\n";
                    }
                    //职责范畴
                    if(StringUtils.isBlank(basicGrids.getResponsibilities())){
                        msg+="职责范畴为空\n";
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
