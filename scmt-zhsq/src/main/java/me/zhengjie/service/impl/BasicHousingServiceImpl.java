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
import me.zhengjie.entity.*;
import me.zhengjie.mapper.*;
import me.zhengjie.service.IBasicHousingService;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class BasicHousingServiceImpl extends ServiceImpl<BasicHousingMapper, BasicHousing> implements IBasicHousingService {

    private final BasicHousingMapper basicHousingMapper;

    private final BasicPersonMapper basicPersonMapper;

    private final RelaPersonHouseMapper relaPersonHouseMapper;

    private final TBuildingArchivesMapper tBuildingArchivesMapper;

    private final GridDeptMapper deptMapper;

    private final BasicGridsMapper basicGridsMapper;

    private final BasicHousingEstateMapper basicHousingEstateMapper;

    private final BasicUnitMapper basicUnitMapper;


    @Override
    public IPage<Map<String, Object>> getAllPersonByRela(BasicPerson basicPerson, PageVo pageVo) {
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
        Page<BasicPerson> pageEntity = new Page<>(page, size);

        return basicHousingMapper.getAllPersonByRela(basicPerson, pageEntity);
    }

    @Override
    public Result<Object> updatePersonAndBasicHouse(BasicHousing basicHousing) {
        Long time = System.currentTimeMillis();
        try {
            if (!StringUtils.isBlank(basicHousing.getId())) {
                //????????????????????????????????????
                List<BasicPerson> personList = basicPersonMapper.getPersonByHouseId(basicHousing.getId());
                //??????????????????
                if (personList.size() > 0) {
                    for (BasicPerson person : personList) {
                        person.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        person.setIsDelete(1);
                        basicPersonMapper.updateById(person);
                    }
                }

                //????????????????????????????????????
                if (StringUtils.isNotBlank(basicHousing.getId())) {
                    QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().and(i -> i.like(RelaPersonHouse::getHouseId, basicHousing.getHouseId()));
                    relaPersonHouseMapper.delete(queryWrapper);
                }

                //????????????
                return this.savePersonAndBasicHouse(basicHousing);
            } else {
                return ResultUtil.error("???????????????????????????????????????");
            }
        } catch (Exception e) {
//            logService.addErrorLog("????????????", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("????????????:" + e.getMessage());
        }
    }

    @Override
    public Result<Object> savePersonAndBasicHouse(BasicHousing basicHousing) {
        Long time = System.currentTimeMillis();
        try {
            boolean houseMore = isHouseMore(basicHousing);
            if (houseMore) {
                return ResultUtil.error("????????????");
            }
            //???????????????
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            basicHousing.setCreateTime(new Timestamp(System.currentTimeMillis()));
            basicHousing.setIsDelete(0);
            basicHousing.setId(id);
            String houseCode = setHouseCode(basicHousing);
            if (StringUtils.isNotBlank(houseCode)) {
                basicHousing.setHouseCode(houseCode);
            } else {
                return ResultUtil.error("????????????");
            }
            int row = basicHousingMapper.insert(basicHousing);
            if (row > 0) {
                return ResultUtil.success("????????????");
            } else {
                return ResultUtil.error("????????????");
            }
        } catch (Exception e) {
//            logService.addErrorLog("????????????", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("????????????:" + e.getMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @param basicHousing
     * @return
     */
    public boolean isHouseMore(BasicHousing basicHousing) {
        boolean flag = false;
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            if (StringUtils.isNotBlank(basicHousing.getBuildArchiveId())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getBuildArchiveId, basicHousing.getBuildArchiveId()));
            }
            if (StringUtils.isNotBlank(basicHousing.getUnit())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getUnit, basicHousing.getUnit()));
            }
            if (StringUtils.isNotBlank(basicHousing.getFloor())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getFloor, basicHousing.getFloor()));
            }
            if (StringUtils.isNotBlank(basicHousing.getDoorNumber())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getDoorNumber, basicHousing.getDoorNumber()));
            }
            BasicHousing bHouse = basicHousingMapper.selectOne(queryWrapper);
            if (bHouse != null) {
                flag = true;
            }
        }
        return flag;
    }


    public String setHouseCode(BasicHousing basicHousing) {
        String houseCode = "";
        if (basicHousing != null) {
            if (StringUtils.isNotBlank(basicHousing.getBuildArchiveName())) {
                int i = Integer.parseInt(basicHousing.getBuildArchiveName());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
            if (StringUtils.isNotBlank(basicHousing.getUnit())) {
                int i = Integer.parseInt(basicHousing.getUnit());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
            if (StringUtils.isNotBlank(basicHousing.getFloor())) {
                int i = Integer.parseInt(basicHousing.getFloor());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
            if (StringUtils.isNotBlank(basicHousing.getDoorNumber())) {
                int i = Integer.parseInt(basicHousing.getDoorNumber());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
        }
        return houseCode;
    }

    @Override
    public Result<Object> getBasicHousingById(String id) {
        BasicHousing basicHousing = basicHousingMapper.selectById(id);
        if (basicHousing != null) {
            return ResultUtil.data(basicHousing);
        }
        return ResultUtil.error("????????????????????????????????????????????????");
    }

    @Override
    public List<Map<String, Object>> getUnitByBuildArchiveId(String buildArchiveId) {
        return basicHousingMapper.getUnitByBuildArchiveId(buildArchiveId);
    }

    @Override
    public List<Map<String, Object>> getDoor(String buildArchiveId, String unit) {
        return basicHousingMapper.getDoor(buildArchiveId, unit);
    }

    @Override
    public List<Map<String, Object>> getMaxOfFloorAndDoor(String buildArchiveId, String unit) {
        return basicHousingMapper.getMaxOfFloorAndDoor(buildArchiveId, unit);
    }

    @Override
    public List<Map<String, Object>> getHouseNum(String buildArchiveId, String unit) {
        return basicHousingMapper.getHouseNum(buildArchiveId, unit);
    }

    @Override
    public Result<Object> queryBasicHousingListByPage(BasicHousing basicHousing, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicHousing> pageData = new Page<>(page, limit);
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            queryWrapper = LikeAllFeild(basicHousing, searchVo, null);
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc(pageVo.getSort());
            } else {
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        IPage<BasicHousing> result = basicHousingMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicHousing basicHousing, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            queryWrapper = LikeAllFeild(basicHousing, null, null);
        }
        List<BasicHousing> list = basicHousingMapper.selectByMyWrapper(queryWrapper);
        for (BasicHousing re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("????????????", re.getStreet());
            map.put("????????????", re.getHouseCommunity());
            map.put("????????????Id", re.getOwnedGridId());
            map.put("????????????", re.getOwnedGrid());
            map.put("????????????", re.getOwnedGrid());


//            map.put("??????Id", re.getHouseId());
            map.put("????????????", re.getHouseName());
            map.put("????????????", re.getBuildArchiveName().substring(0,re.getBuildArchiveName().length()-1)+"-"+re.getUnit()+"-"+dealNumber(re.getFloor())+dealNumber(re.getDoorNumber()));

            map.put("????????????", re.getBuildArchiveName());
//            map.put("??????id", re.getBuildArchiveId());
            map.put("?????????", re.getDoorNumber());
            map.put("??????", re.getUnit());
            map.put("??????", re.getFloor());
//            map.put("????????????", re.getStreetName());
//            map.put("?????????", re.getStreetNumber());
            map.put("????????????", re.getCustomerName());
            map.put("????????????", re.getHouseAddress());
//            map.put("??????????????????", re.getCustomerCard());
//            map.put("????????????", re.getCustomerMobile());
            map.put("????????????", re.getHostName());
            map.put("????????????", re.getHostCard());
            map.put("????????????", re.getHouseType());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    @Override
    public void downloadRent(BasicPerson basicPerson, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> allPersonByRela = basicHousingMapper.getAllPersonByRelaRent(basicPerson);
        for (Map<String, Object> re : allPersonByRela) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("????????????", re.get("owned_street"));
            map.put("????????????", re.get("owned_community"));
            map.put("????????????", re.get("residential_address"));
            map.put("????????????", re.get("name"));
            map.put("??????????????????", re.get("card_id"));
            map.put("????????????", re.get("phone"));
            map.put("????????????", simpleDateFormat.format(re.get("create_time")));
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * ?????????????????????????????????
     *
     * @param basicHousing ???????????????????????????
     * @return ????????????
     */
    public QueryWrapper<BasicHousing> LikeAllFeild(BasicHousing basicHousing, SearchVo searchVo, String key) {
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing.getId() != null) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getId, basicHousing.getId()));
        }
        if (StringUtils.isNotBlank(basicHousing.getStreet())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getStreet, basicHousing.getStreet()));
        }
        if (StringUtils.isNotBlank(basicHousing.getOwnedGrid())) {
            String[] streetIds = basicHousing.getOwnedGrid().split(",");
            queryWrapper.lambda().and(i -> i.in(BasicHousing::getOwnedGrid, streetIds));
            //queryWrapper.lambda().and(i -> i.eq(BasicHousing::getOwnedGrid, basicHousing.getOwnedGrid()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseCommunity())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseCommunity, basicHousing.getHouseCommunity()));
        }
        if (StringUtils.isNotBlank(basicHousing.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getCommunityId, basicHousing.getCommunityId()));
        }
        if (StringUtils.isNotBlank(basicHousing.getOwnedGridId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getOwnedGridId, basicHousing.getOwnedGridId()));
        }
        if (StringUtils.isNotBlank(basicHousing.getOwnedGrid())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getOwnedGrid, basicHousing.getOwnedGrid()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getHouseId, basicHousing.getHouseId()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseCode())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseCode, basicHousing.getHouseCode()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseName, basicHousing.getHouseName()));
        }
        if (StringUtils.isNotBlank(basicHousing.getBuildArchiveName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getBuildArchiveName, basicHousing.getBuildArchiveName()));
        }
        if (StringUtils.isNotBlank(basicHousing.getBuildArchiveId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getBuildArchiveId, basicHousing.getBuildArchiveId()));
        }
        if (StringUtils.isNotBlank(basicHousing.getDoorNumber())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getDoorNumber, basicHousing.getDoorNumber()));
        }
        if (StringUtils.isNotBlank(basicHousing.getUnit())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getUnit, basicHousing.getUnit()));
        }
        if (StringUtils.isNotBlank(basicHousing.getFloor())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getFloor, basicHousing.getFloor()));
        }
        if (StringUtils.isNotBlank(basicHousing.getStreetName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getStreetName, basicHousing.getStreetName()));
        }
        if (StringUtils.isNotBlank(basicHousing.getStreetNumber())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousing::getStreetNumber, basicHousing.getStreetNumber()));
        }
        if (StringUtils.isNotBlank(basicHousing.getCustomerName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getCustomerName, basicHousing.getCustomerName()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseAddress())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseAddress, basicHousing.getHouseAddress()));
        }
        if (StringUtils.isNotBlank(basicHousing.getCustomerCard())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getCustomerCard, basicHousing.getCustomerCard()));
        }
        if (StringUtils.isNotBlank(basicHousing.getCustomerMobile())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getCustomerMobile, basicHousing.getCustomerMobile()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHostName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHostName, basicHousing.getHostName()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHostCard())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHostCard, basicHousing.getHostCard()));
        }
        if (basicHousing.getIsMap() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getIsMap, basicHousing.getIsMap()));
        }
        if (StringUtils.isNotBlank(basicHousing.getHouseType())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseType, basicHousing.getHouseType()));
        }
        if (StringUtils.isNotBlank(basicHousing.getRentStatus())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getRentStatus, basicHousing.getRentStatus()));
        }
        if (basicHousing.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getIsDelete, basicHousing.getIsDelete()));
        }
        if (basicHousing.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getCreateId, basicHousing.getCreateId()));
        }
        if (basicHousing.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getCreateTime, basicHousing.getCreateTime()));
        }
        if (basicHousing.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getUpdateId, basicHousing.getUpdateId()));
        }
        if (basicHousing.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getUpdateTime, basicHousing.getUpdateTime()));
        }
        if (basicHousing.getArea() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getArea, basicHousing.getArea()));
        }
        if (basicHousing.getHouseForm() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseForm, basicHousing.getHouseForm()));
        }
        if (basicHousing.getHouseProperty() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseProperty, basicHousing.getHouseProperty()));
        }
        if (basicHousing.getAccName() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getAccName, basicHousing.getAccName()));
        }
        if (basicHousing.getAccNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getAccNumber, basicHousing.getAccNumber()));
        }
        if (basicHousing.getAccRelation() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getAccRelation, basicHousing.getAccRelation()));
        }
        if (basicHousing.getAccType() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousing::getAccType, basicHousing.getAccType()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(BasicHousing::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //????????????
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getHouseCode, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(BasicHousing::getIsDelete, 0));

        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(wrapper -> wrapper.like("build_archive_name", key).or().like("unit", key).or().like("door_number", key).or().like("house_address", key));
        }
        return queryWrapper;
    }

    @Override
    public Result<Object> queryHousingManage(BasicHousing basicHousing) {
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            queryWrapper = LikeAllFeild(basicHousing, null, null);
        }
        List<BasicHousing> result = basicHousingMapper.selectList(queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public List<BasicHousing> getRoomByBuildArchiveId(String buildArchiveId, String personId, String type) {
        return basicHousingMapper.getRoomByBuildArchiveId(buildArchiveId, personId, type);
    }

    @Override
    public Result<Object> queryBasicHousingListByAnyWayWhere(BasicHousing basicHousing, String key, PageVo pageVo) {

        List<BasicHousing> basicHousings = new ArrayList<>();
        IPage<BasicHousing> result=null;
        boolean isPage=true;
        int page = 1;
        int limit = 10;
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            queryWrapper = LikeAllFeild(basicHousing, null, key);
        }
        if (pageVo != null && pageVo.getPageNumber() != 0 && pageVo.getPageSize() != 0) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
            Page<BasicHousing> pageData = new Page<>(page, limit);

            result = basicHousingMapper.selectPage(pageData, queryWrapper);
            basicHousings = result.getRecords();
        }
        else {
            isPage = false;
            basicHousings = basicHousingMapper.selectList(queryWrapper);
        }

        //???????????????????????????
        QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
        List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);
        for (BasicHousing record : basicHousings) {
            List<TBuildingArchives> archivesList = tBuildingArchives.stream().filter(item -> item.getId().equals(record.getBuildArchiveId())).collect(Collectors.toList());
            if (archivesList != null && archivesList.size() > 0) {
                record.setPosition(archivesList.get(0).getPosition());
            }
        }
        if(isPage){
            return ResultUtil.data(result);
        }
        return ResultUtil.data(basicHousings);

    }

    @Override
    public List<Map<String, Object>> getRealDoor(String buildArchiveId, String unit) {
        return basicHousingMapper.getRealDoor(buildArchiveId, unit);
    }

    @Override
    public Map<String, Object> getRealDoorAndFloor(String buildArchiveId, String unit) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> realDoor = basicHousingMapper.getRealDoorInfo(buildArchiveId, unit);
        List<Map<String, Object>> realHouseInfo = basicHousingMapper.getRealHouseInfo(buildArchiveId, unit);
        result.put("realHouseInfo",realHouseInfo);
        result.put("realDoor",realDoor);
        return result;
    }
    @Override
    public Map<String, Object> getRealDoorAndPerson(String buildArchiveId, String unit) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> realDoor = basicHousingMapper.getRealDoorInfo(buildArchiveId, unit);
        List<Map<String, Object>> realHouseInfo = basicHousingMapper.getRealPersonInfo(buildArchiveId, unit);
        result.put("realHouseInfo",realHouseInfo);
        result.put("realDoor",realDoor);
        return result;
    }
    @Override
    public List<Map<String, Object>> getRealHouse(String buildArchiveId, String unit) {
        return basicHousingMapper.getRealHouse(buildArchiveId, unit);
    }

    @Override
    public List<Map<String, Object>> statisticsHousingType(String buildingArchiveId) {
        return basicHousingMapper.statisticsHousingType(buildingArchiveId);
    }

    @Override
    public List<String> getUnits(String buildArchiveId) {
        return basicHousingMapper.getUnits(buildArchiveId);
    }

    @Override
    public List<String> getFloors(String buildArchiveId, String unit) {
        return basicHousingMapper.getFloors(buildArchiveId, unit);
    }

    @Override
    public List<Map<String, Object>> getDoorNumbers(String buildArchiveId, String unit, String floor) {
        return basicHousingMapper.getDoorNumbers(buildArchiveId, unit, floor);
    }

    @Override
    public List<BasicHousing> getHouseByPersonId(String personId) {
        return basicHousingMapper.getHouseByPersonId(personId);
    }

    @Override
    public List<Map<String, Object>> selectHouseTree(Map<String,Object> powerMap) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (powerMap != null){
            mapList = basicHousingMapper.selectHouseTree(powerMap);
        }else{
            mapList = basicHousingMapper.selectHouseTree();
        }
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
                                if(mapChild.containsKey("title") && mapChild.containsKey("lel") && "4".equals(mapChild.get("lel").toString().trim()) ){
                                    String mapChildTitle  = mapChild.get("title").toString();
                                    mapChild.put("title",title+mapChildTitle+"??????");
                                }
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

    /**
     * ??????number
     * @param number
     * @return
     */
    private String dealNumber(String number){
        if(number!=null && com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(number)){
            int i = Integer.parseInt(number);
            if (i < 10) {
                number = "0" + i;
            } else {
                number = i + "";
            }
        }
        else {
            number = "";
        }
        return number;
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //???????????????????????????
        BasicHousing tZhsqGridMember = new BasicHousing();
        List<BasicHousing> readData =ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData =  matchingData(readData,result);
            if(isMatchingData)
            {
                return ResultUtil.data(result);
            }
            for(BasicHousing basicGrids:readData){

                if(basicGrids==null|| BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                basicGrids.setId(uuid);
                basicGrids.setIsDelete(0);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = basicHousingMapper.insert(basicGrids);
                if(insert!=1){
                    throw new BusinessErrorException("?????????????????????????????????");
                }
//                JSONObject houseParams = new JSONObject();
//                houseParams.put("roomNum",basicGrids.getFloor()+basicGrids.getDoorNumber());
//                houseParams.put("unitUuid",basicGrids.getUnitId());
//                houseParams.put("thirdPartyId",basicGrids.getId());
//                JSONObject result = thirdPartyClient.saveUpdateHousing(houseParams);
//                if (!"200".equals(result.getString("code"))){
//                    throw new BusinessErrorException("???????????????????????????");
//                }
            }
        }
        catch (Exception e){
            e.printStackTrace();

            throw new BusinessException(ResultCode.FAILURE);
        }

        return ResultUtil.data(result);
    }

    /**
     * ????????????
     */
    public Boolean  matchingData(List<BasicHousing> readData,List<Map<String, Object>>  result ){
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
                BasicHousing basicGrids = readData.get(i);
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if(basicGrids!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("number",i+1);
                    map.put("success","??????");

                    String msg="";
                    //????????????
                    if(StringUtils.isBlank(basicGrids.getHouseCode())){
                        msg+="??????????????????\n";
                    }
                    else{
                        if(!DictionaryUtils.isNumeric(basicGrids.getHouseCode())){
                            msg+="????????????????????????????????????\n";
                        }else if(basicGrids.getHouseCode().trim().length()!=8){
                            msg+="???????????????????????????????????????\n";
                        }
                        //?????????
                        if(StringUtils.isNotBlank(basicGrids.getBuildArchiveName())){
                            if(!DictionaryUtils.isNumeric(basicGrids.getBuildArchiveName())){
                                msg+="?????????????????????????????????\n";
                            }
                            else if(basicGrids.getBuildArchiveName().trim().length()>2){
                                msg+="????????????????????????????????????2\n";
                            }
                        }
                        else {
                            msg+="?????????????????????\n";
                        }
                        //??????
                        if(StringUtils.isNotBlank(basicGrids.getUnit())){
                            if(!DictionaryUtils.isNumeric(basicGrids.getUnit())){
                                msg+="??????????????????????????????\n";
                            }
                            else if(basicGrids.getUnit().trim().length()>2){
                                msg+="?????????????????????????????????2\n";
                            }
                        }
                        else {
                            msg+="??????????????????\n";
                        }
                        //??????
                        if(StringUtils.isNotBlank(basicGrids.getFloor())){
                            if(!DictionaryUtils.isNumeric(basicGrids.getFloor())){
                                msg+="??????????????????????????????\n";
                            }
                            else if(basicGrids.getFloor().trim().length()>2){
                                msg+="?????????????????????????????????2\n";
                            }
                        }
                        else {
                            msg+="??????????????????\n";
                        }
                        //?????????
                        if(StringUtils.isNotBlank(basicGrids.getDoorNumber())){
                            if(!DictionaryUtils.isNumeric(basicGrids.getDoorNumber())){
                                msg+="?????????????????????????????????\n";
                            }
                            else if(basicGrids.getDoorNumber().trim().length()>2){
                                msg+="????????????????????????????????????2\n";
                            }
                        }
                        else {
                            msg+="?????????????????????\n";
                        }
                        if(msg.length()==0){
                           String number = dealNumber(basicGrids.getBuildArchiveName())+dealNumber(basicGrids.getUnit())+dealNumber(basicGrids.getFloor())+dealNumber(basicGrids.getDoorNumber());
                           if(number!=null && !number.trim().equals(basicGrids.getHouseCode().trim())){
                               msg+="?????????????????????,????????????????????????????????????\n";
                           }
                           else{
                               basicGrids.setDoorNumber(dealNumber(basicGrids.getDoorNumber()));
                           }
                        }
                    }
                    //??????
                    if(StringUtils.isNotBlank(basicGrids.getArea())){
                        if(!DictionaryUtils.isNumeric(basicGrids.getArea())){
                            msg+="??????????????????????????????\n";
                        }
                    }

                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getHouseType())){
                        String s = dictionaryUtils.houseType(basicGrids.getHouseType());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setHouseType(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }
                    //??????
                    if(StringUtils.isNotBlank(basicGrids.getHouseForm())){
                        String s = dictionaryUtils.houseFormData(basicGrids.getHouseForm());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setHouseForm(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getHouseProperty())){
                        String s = dictionaryUtils.housePropertyData(basicGrids.getHouseProperty());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setHouseProperty(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getHouseClassification())){
                        String s = dictionaryUtils.houseClassification(basicGrids.getHouseClassification());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setHouseClassification(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }

                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getHouseNature())){
                        String s = dictionaryUtils.houseNature(basicGrids.getHouseNature());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setHouseNature(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }

                    //????????????
                    if (StringUtils.isNotBlank(basicGrids.getStreet())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreet(), streetUtil.streetDepts);
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
                    if (StringUtils.isNotBlank(basicGrids.getHouseCommunity())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreet(),basicGrids.getHouseCommunity(), streetUtil.gridDepts);
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
                    if(StringUtils.isNotBlank(basicGrids.getOwnedGrid())){
                        String s = GridUtil.matchingGrid(basicGrids.getOwnedGrid(),basicGrids.getHouseCommunity(),basicGrids.getStreet(),gridUtil.basicGrids);
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setOwnedGridId(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getOwnedGrid())){
                        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
                        //????????????
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getIsDelete, 0));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getName, basicGrids.getHouseName()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getCommunity, basicGrids.getHouseCommunity()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getStreet, basicGrids.getStreet()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getGrid, basicGrids.getOwnedGrid()));
                        queryWrapper.select("id");
                        List<BasicHousingEstate> basicHousingEstates = basicHousingEstateMapper.selectList(queryWrapper);
                        if(basicHousingEstates!=null && basicHousingEstates.size()>0){
                           if(basicHousingEstates.get(0)!=null && StringUtils.isNotBlank(basicHousingEstates.get(0).getId())){
                               basicGrids.setHouseId(basicHousingEstates.get(0).getId());
                               //????????????
                               QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
                               objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getIsDelete, 0));
                               objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getBuildingName, basicGrids.getBuildArchiveName()));
                               objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getHousingEstate, basicHousingEstates.get(0).getId()));
                               objectQueryWrapper.select("id","is_delete");
                               List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);
                               if(tBuildingArchives!=null &&tBuildingArchives.size()>0&& tBuildingArchives.get(0)!=null && StringUtils.isNotBlank(tBuildingArchives.get(0).getId())){
                                   basicGrids.setBuildArchiveId(tBuildingArchives.get(0).getId());
                                   basicGrids.setBuildArchiveName(basicGrids.getBuildArchiveName()+"???");

                                   QueryWrapper<BasicUnit> basicUnitWrapper = new QueryWrapper<>();
                                   basicUnitWrapper.lambda().and(item -> item.eq(BasicUnit::getIsDelete, 0));
                                   basicUnitWrapper.lambda().and(item -> item.eq(BasicUnit::getName, basicGrids.getUnit()));
                                   basicUnitWrapper.lambda().and(item -> item.eq(BasicUnit::getBuildArchiveId, basicGrids.getBuildArchiveId()));
                                   basicUnitWrapper.select("id","name");
                                   List<BasicUnit> basicUnits = basicUnitMapper.selectList(basicUnitWrapper);
                                   if(basicUnits!=null && basicUnits.size()>0 && basicUnits.get(0)!=null &&  StringUtils.isNotBlank(basicUnits.get(0).getId())){
                                       basicGrids.setUnitId(basicUnits.get(0).getId());
                                       QueryWrapper<BasicHousing> basicHousingQueryWrapper = new QueryWrapper<>();
                                       basicHousingQueryWrapper.lambda().and(item -> item.eq(BasicHousing::getIsDelete, 0));
                                       basicHousingQueryWrapper.lambda().and(item -> item.eq(BasicHousing::getFloor, basicGrids.getFloor()));
                                       basicHousingQueryWrapper.lambda().and(item -> item.eq(BasicHousing::getDoorNumber, basicGrids.getDoorNumber()));
                                       basicHousingQueryWrapper.lambda().and(item -> item.eq(BasicHousing::getUnitId, basicGrids.getUnitId()));
                                       Integer integer = basicHousingMapper.selectCount(basicHousingQueryWrapper);
                                       if(integer>0){
                                           msg+="???????????????????????????????????????????????????????????????\n";
                                       }

                                   }
                                   else{
                                       msg+="??????????????????????????????????????????????????????\n";
                                   }

                               }
                               else{
                                   msg+="??????????????????????????????????????????????????????\n";
                               }

                           }
                           else{
                               msg+="??????????????????????????????????????????????????????????????????\n";
                           }
                        }

                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getHostName())){
                        boolean chinaPhoneLegal = DictionaryUtils.checkEnglish(basicGrids.getHostName());
                        if(chinaPhoneLegal){
                            msg+="???????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //??????????????????
                    if(StringUtils.isNotBlank(basicGrids.getHostCard())){
                        String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getHostCard());
                        if(StringUtils.isNotBlank(chinaPhoneLegal)&& !chinaPhoneLegal.equals("YES")){
                            msg+="??????"+chinaPhoneLegal+"????????????\n";
                        }
                    }
                    else{
                        msg+="????????????????????????\n";
                    }
                    //????????????
                    if(StringUtils.isBlank(basicGrids.getHouseAddress())){
                        msg+="??????????????????\n";
                    }

                    //??????
                    if(StringUtils.isNotBlank(basicGrids.getAccType())){
                        String s = dictionaryUtils.accTypeData(basicGrids.getAccType());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setAccType(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="????????????\n";
                    }
                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getAccName())){
                        boolean chinaPhoneLegal = DictionaryUtils.checkEnglish(basicGrids.getAccName());
                        if(chinaPhoneLegal){
                            msg+="???????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //??????????????????
                    if(StringUtils.isNotBlank(basicGrids.getAccCard())){
                        String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getAccCard());
                        if(StringUtils.isNotBlank(chinaPhoneLegal)&& !chinaPhoneLegal.equals("YES")){
                            msg+="??????"+ chinaPhoneLegal+"????????????\n";
                        }
                    }
                    else{
                        msg+="????????????????????????\n";
                    }
                    //??????
                    if(StringUtils.isBlank(basicGrids.getAccNumber())){
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
