package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.RelaPersonHouse;
import me.zhengjie.entity.TBuildingArchives;
import me.zhengjie.mapper.*;
import me.zhengjie.service.IBasicPersonService;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
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
public class BasicPersonServiceImpl extends ServiceImpl<BasicPersonMapper, BasicPerson> implements IBasicPersonService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicPersonMapper basicPersonMapper;


    private final TBuildingArchivesMapper tBuildingArchivesMapper;


    private final RelaPersonHouseMapper relaPersonHouseMapper;


    private final BasicHousingMapper basicHousingMapper;


    private final BasicGridsMapper basicGridsMapper;


    private final GridDeptMapper deptMapper;

    @Override
    public List<TBuildingArchives> getBuildArchiveByEstateId(String estateId) {
        return basicPersonMapper.getBuildArchiveByEstateId(estateId);
    }


    @Override
    public List<Map<String, Object>> getBasicPersonCount(BasicPerson basicPerson) {
        return basicPersonMapper.getBasicPersonCount(basicPerson);
    }

    @Override
    public List<Map<String, Object>> getSpecialPersonCount(BasicPerson basicPerson) {
        return basicPersonMapper.getSpecialPersonCount(basicPerson);
    }

    @Override
    public int insert1(BasicPerson basicPerson) {
        return basicPersonMapper.insert(basicPerson);
    }

    @Override
    public Result<Object> getBasicPersonById(String id) {
        BasicPerson basicPerson = basicPersonMapper.selectById(id);
        if (basicPerson != null) {
            return ResultUtil.data(basicPerson);
        }
        return ResultUtil.error("????????????????????????????????????????????????");
    }

    @Override
    public Result<Object> queryBasicPersonListByPage(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicPerson> pageData = new Page<>(page, limit);
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
        if (basicPerson != null) {
            queryWrapper = LikeAllFeild(basicPerson, searchVo, null);
        }
        queryWrapper.orderByDesc("create_time");
        IPage<BasicPerson> result = basicPersonMapper.selectPage(pageData, queryWrapper);

        //???????????????????????????
        QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
        List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);
        for (BasicPerson record : result.getRecords()) {
            List<TBuildingArchives> archivesList = tBuildingArchives.stream().filter(item -> item.getId().equals(record.getBuildingArchiveId())).collect(Collectors.toList());
            List<BasicHousing> houseByPersonId = basicHousingMapper.getHouseByPersonId(record.getId());
            if (archivesList != null && archivesList.size() > 0) {
                record.setPosition(archivesList.get(0).getPosition());
            }
            if (houseByPersonId != null) {
                record.setIsBind(houseByPersonId.size());
            }
        }
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicPerson basicPerson, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
        List<BasicPerson> list = basicPersonMapper.selectByMyWrapper(queryWrapper);
        for (BasicPerson re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("??????", re.getName());
            map.put("??????", re.getSex());
            map.put("????????????", re.getBirthDate());
            map.put("??????", re.getNation());
            map.put("????????????", re.getCardId());
            map.put("????????????", re.getPersonType());
            map.put("????????????", re.getPoliticalFace());
            map.put("????????????", re.getResidenceAddress());
            map.put("????????????", re.getResidentialAddress());
            map.put("????????????", re.getStreetName());
            map.put("????????????", re.getCommunityName());
            map.put("????????????", re.getOwnedGrid());
            map.put("????????????", re.getOwnedHousing());
            map.put("????????????", re.getTableTypeName());
            map.put("?????????????????? 1???0???", re.getResidentGrid());
            map.put("????????????", re.getHousingName());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    @Override
    public void downloadDynamics(Map<String, Object> map, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicPerson basicPerson = new BasicPerson();
        if (map != null && map.containsKey("searchForm") && map.get("searchForm") != null) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) map.get("searchForm");
            basicPerson = MapUtil.parseMap2Object(linkedHashMap, BasicPerson.class);
        }
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
        if (basicPerson != null) {
            queryWrapper = LikeAllFeild(basicPerson, null, null);
        }
        List<Map<String, Object>> list = basicPersonMapper.selectInfoByMyWrapper(basicPerson);
        for (Map<String, Object> re : list) {
            if (re != null) {
//                if(re.containsKey("id") && re.get("id")!=null){
//                    Map<String, Object> resMap = basicPersonMapper.getOneHouseByPersonId(re.get("id").toString().trim());
//                    if(resMap!=null){
//                        re.putAll(resMap);
//                    }
//                }
                Map<String, Object> listMap = new LinkedHashMap<>();
                if (map != null && map.containsKey("Field") && map.get("Field") != null) {
                    LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) map.get("Field");
                    for (String key : linkedHashMap.keySet()) {
                        if (re.containsKey(key) && linkedHashMap.get(key) != null) {
                            listMap.put(linkedHashMap.get(key).toString(), re.get(key) == null ? "" : re.get(key));
                        }
                    }
                }
                mapList.add(listMap);
            }


//            listMap.put("??????", re.getName());
//            listMap.put("??????", re.getSex());
//            listMap.put("????????????", re.getBirthDate());
//            listMap.put("??????", re.getNation());
//            listMap.put("????????????", re.getCardId());
//            listMap.put("????????????", re.getPersonType());
//            listMap.put("????????????", re.getPoliticalFace());
//            listMap.put("????????????", re.getResidenceAddress());
//            listMap.put("????????????", re.getResidentialAddress());
//            listMap.put("????????????", re.getStreetName());
//            listMap.put("????????????", re.getCommunityName());
//            listMap.put("????????????", re.getOwnedGrid());
//            listMap.put("????????????", re.getOwnedHousing());
//            listMap.put("????????????", re.getTableTypeName());
//            listMap.put("?????????????????? 1???0???", re.getResidentGrid());
//            listMap.put("????????????", re.getHousingName());

        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * ?????????????????????????????????
     *
     * @param basicPerson ???????????????????????????
     * @return ????????????
     */
    public QueryWrapper<BasicPerson> LikeAllFeild(BasicPerson basicPerson, SearchVo searchVo, String key) {
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
        if (basicPerson.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getId, basicPerson.getId()));
        }
        if (StringUtils.isNotBlank(basicPerson.getName())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getName, basicPerson.getName()));
        }
        if (StringUtils.isNotBlank(basicPerson.getSex())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getSex, basicPerson.getSex()));
        }
        if (basicPerson.getBirthDate() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getBirthDate, basicPerson.getBirthDate()));
        }
        if (StringUtils.isNotBlank(basicPerson.getNation())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getNation, basicPerson.getNation()));
        }
        if (StringUtils.isNotBlank(basicPerson.getCardId())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getCardId, basicPerson.getCardId()));
        }
        if (StringUtils.isNotBlank(basicPerson.getPersonType())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getPersonType, basicPerson.getPersonType()));
        }
        if (StringUtils.isNotBlank(basicPerson.getBuildingArchiveId())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getBuildingArchiveId, basicPerson.getBuildingArchiveId()));
        }
        if (StringUtils.isNotBlank(basicPerson.getPoliticalFace())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getPoliticalFace, basicPerson.getPoliticalFace()));
        }
        if (StringUtils.isNotBlank(basicPerson.getResidenceAddress())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getResidenceAddress, basicPerson.getResidenceAddress()));
        }
        if (StringUtils.isNotBlank(basicPerson.getResidentialAddress())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getResidentialAddress, basicPerson.getResidentialAddress()));
        }
        if (StringUtils.isNotBlank(basicPerson.getStreetId())) {
            String[] streetIds = basicPerson.getStreetId().split(",");
            queryWrapper.lambda().and(i -> i.in(BasicPerson::getStreetId, streetIds));
        }
        if (StringUtils.isNotBlank(basicPerson.getStreetName())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getStreetName, basicPerson.getStreetName()));
        }


        if (StringUtils.isNotBlank(basicPerson.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getCommunityId, basicPerson.getCommunityId()));
        }
        if (StringUtils.isNotBlank(basicPerson.getCommunityName())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getCommunityName, basicPerson.getCommunityName()));
        }

        if (StringUtils.isNotBlank(basicPerson.getOwnedGrid())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getOwnedGrid, basicPerson.getOwnedGrid()));
        }
        if (StringUtils.isNotBlank(basicPerson.getOwnedGridId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getOwnedGridId, basicPerson.getOwnedGridId()));
        }
        if (StringUtils.isNotBlank(basicPerson.getOwnedHousing())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getOwnedHousing, basicPerson.getOwnedHousing()));
        }
        if (StringUtils.isNotBlank(basicPerson.getSpecialPopulation())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getSpecialPopulation, basicPerson.getSpecialPopulation()));
        }
        if (StringUtils.isNotBlank(basicPerson.getResidentGrid())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getResidentGrid, basicPerson.getResidentGrid()));
        }
        if (basicPerson.getOwnedHouseid() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getOwnedHouseid, basicPerson.getOwnedHouseid()));
        }
        if (StringUtils.isNotBlank(basicPerson.getHousingName())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getHousingName, basicPerson.getHousingName()));
        }
        if (StringUtils.isNotBlank(basicPerson.getRelationShip())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getRelationShip, basicPerson.getRelationShip()));
        }
        if (basicPerson.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getIsDelete, basicPerson.getIsDelete()));
        }
        if (basicPerson.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getCreateId, basicPerson.getCreateId()));
        }
        if (basicPerson.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getCreateTime, basicPerson.getCreateTime()));
        }
        if (basicPerson.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getUpdateId, basicPerson.getUpdateId()));
        }
        if (basicPerson.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getUpdateTime, basicPerson.getUpdateTime()));
        }
        if (basicPerson.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getDeleteId, basicPerson.getDeleteId()));
        }
        if (basicPerson.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getDeleteTime, basicPerson.getDeleteTime()));
        }
        if (basicPerson.getImgPath() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getImgPath, basicPerson.getImgPath()));
        }
        if (basicPerson.getPosition() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getPosition, basicPerson.getPosition()));
        }
        if (basicPerson.getTableType() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getTableType, basicPerson.getTableType()));
        }
        if (basicPerson.getHobby() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getHobby, basicPerson.getHobby()));
        }

        if (basicPerson.getStartDate() != null && basicPerson.getEndDate() != null) {
            queryWrapper.lambda().and(i -> i.between(BasicPerson::getCreateTime, basicPerson.getStartDate(), basicPerson.getEndDate()));
        }
        //????????????
        if (StringUtils.isNotEmpty(basicPerson.getSearchInfo())) {
            queryWrapper.lambda().and(i -> i.like(BasicPerson::getName, basicPerson.getSearchInfo())
                    .or().like(BasicPerson::getPhone, basicPerson.getSearchInfo())
                    .or().like(BasicPerson::getCardId, basicPerson.getSearchInfo())
            );
        }

        queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));

        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(wrapper -> wrapper.like("name", key).or().like("card_id", key).or().like("phone", key));
        }
        return queryWrapper;

    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public Result<Object> statisticsGridPerson() {
        List<Map> objectResult = basicPersonMapper.statisticsGridPerson();
        int total = 0;
        for (Map map : objectResult) {
            if (map.containsKey("number")) {
                String number = map.get("number").toString();
                int i = Integer.parseInt(number);
                total += i;
            }
        }
        List<Map> locations = basicPersonMapper.statisticsPersonLocation();

        HashMap<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("locations", locations);
        result.put("data", objectResult);
        return ResultUtil.data(result);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public Result<Object> statisticsGridPerson2(String community) {
        //??????
        List<Map> objectResult = basicPersonMapper.statisticsPersonPoliticaloutlook(community);
        //??????
        List<Map> objectResult2 = basicPersonMapper.statisticsPersoncommunity(community);
        //?????????
        List<Map> objectResult3 = basicPersonMapper.statisticsPersongridmember(community);
        //?????????
        List<Map> objectResult4 = basicPersonMapper.statisticsPersonvolunteer(community);
        //????????????
        List<Map> objectResult5 = basicPersonMapper.statisticspersontype(community);
        //????????????
        List<Map> objectResult6 = basicPersonMapper.statisticspersonteventlist(community);
        HashMap<String, Object> result = new HashMap<>();
        result.put("party_member", objectResult.size());
        result.put("cadre", objectResult2.size());
        result.put("Grid_people", objectResult3.size());
        result.put("volunteer", objectResult4.size());
        result.put("people_type", objectResult5);
        result.put("event_list", objectResult6);
        result.put("event_list2", community);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> queryBasicPersonListByAnyWayWhere(BasicPerson basicPerson, String key, PageVo pageVo) {
        List<BasicPerson> basicPeople = null;
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
        if (basicPerson != null) {
            queryWrapper = LikeAllFeild(basicPerson, null, key);
        }
        IPage<BasicPerson> result = null;
        boolean isPage = true;
        int page = 1;
        int limit = 10;
        if (pageVo != null && pageVo.getPageNumber() != 0 && pageVo.getPageSize() != 0) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
            Page<BasicPerson> pageData = new Page<>(page, limit);


            result = basicPersonMapper.selectPage(pageData, queryWrapper);
            basicPeople = result.getRecords();
        } else {
            isPage = false;
            basicPeople = basicPersonMapper.getAllPersonPosition(basicPerson);
            return ResultUtil.data(basicPeople);
        }

        //???????????????????????????
        QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
        List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);

        for (BasicPerson record : basicPeople) {
            RelaPersonHouse toponeHouseId = relaPersonHouseMapper.getToponeHouseId(record.getId());
            if (toponeHouseId != null) {
                BasicHousing basicHousing = basicHousingMapper.selectById(toponeHouseId.getHouseId());
                if (basicHousing != null) {
                    List<TBuildingArchives> archivesList = tBuildingArchives.stream().filter(item -> item.getId().equals(basicHousing.getBuildArchiveId())).collect(Collectors.toList());
                    if (archivesList != null && archivesList.size() > 0) {
                        record.setPosition(archivesList.get(0).getPosition());
                    }
                }
            }
        }
        return ResultUtil.data(result);


    }

    @Override
    public List<Map<String, Object>> statisticsPersonType(String buildingArchiveId) {
        return basicPersonMapper.statisticsPersonType(buildingArchiveId);
    }

    @Override
    public List<Map<String, Object>> getPersonByCardId(String cardId) {
        return basicPersonMapper.getPersonByCardId(cardId);
    }

    @Override
    public List<Map<String, Object>> getCardId() {
        return basicPersonMapper.getCardId();
    }

    @Override
    public List<BasicPerson> getPersonByHouseId(String houseId) {
        return basicPersonMapper.getPersonByHouseId(houseId);
    }

    /**
     * ??????????????????
     *
     * @param basicPerson
     * @param searchVo
     * @param pageVo
     * @return
     */
    @Override
    public Page<BasicPerson> queryAccPerson(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicPerson> pageData = new Page<>(page, limit);
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().and(i -> i.eq(BasicPerson::getPersonType, "1"));
        queryWrapper.lambda().and(i -> i.eq(BasicPerson::getAccRelation, "1"));
        queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
        if (basicPerson != null) {
            if (StringUtils.isNoneEmpty(basicPerson.getAccCard())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getAccCard, basicPerson.getAccCard()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getAccName())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getAccName, basicPerson.getAccName()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getAccNumber())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getAccNumber, basicPerson.getAccNumber()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getAccType())) {
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getAccType, basicPerson.getAccType()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getAccRelation())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getAccRelation, basicPerson.getAccRelation()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getCommunityId())) {
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getCommunityId, basicPerson.getCommunityId()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getCommunityName())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getCommunityName, basicPerson.getCommunityName()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getOwnedGrid())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getOwnedGrid, basicPerson.getOwnedGrid()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getOwnedGridId())) {
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getOwnedGridId, basicPerson.getOwnedGridId()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getStreetId())) {
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getStreetId, basicPerson.getStreetId()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getStreetName())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getStreetName, basicPerson.getStreetName()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getOwnedHousing())) {
                queryWrapper.lambda().and(i -> i.like(BasicPerson::getOwnedHousing, basicPerson.getOwnedHousing()));
            }
            if (StringUtils.isNoneEmpty(basicPerson.getOwnedHouseid())) {
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getOwnedHouseid, basicPerson.getOwnedHouseid()));
            }
        }
        queryWrapper.groupBy("acc_number");//??????
        Page<BasicPerson> pageResult = basicPersonMapper.selectPage(pageData, queryWrapper);
        return pageResult;
    }

    /**
     * @param accNumber
     * @return
     */
    @Override
    public List<BasicPerson> queryBasicPersonNoPage(String accNumber, String id) {
        QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(accNumber)) {
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getAccNumber, accNumber));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getPersonType, "1"));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            queryWrapper.lambda().and(i -> i.ne(BasicPerson::getId, id));
        }
        return basicPersonMapper.selectList(queryWrapper);
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //???????????????????????????
        BasicPerson basicPerson = new BasicPerson();
        List<BasicPerson> readData = ImportExeclUtil.readDateListT(wb, basicPerson, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData = matchingData(readData, result);
            if (isMatchingData) {
                return ResultUtil.data(result);
            }
            for (BasicPerson basicGrids : readData) {
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                basicGrids.setIsDelete(0);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = basicPersonMapper.insert(basicGrids);
                if (insert != 1) {
                    throw new BusinessErrorException("?????????????????????????????????");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILURE);
        }
        return ResultUtil.data(result);
    }

    /**
     * ????????????
     */
    public Boolean matchingData(List<BasicPerson> readData, List<Map<String, Object>> result) {
        if (result == null) {
            result = new ArrayList<>();
        }
        Boolean isMatchingData = false;
        StreetUtil streetUtil = new StreetUtil(deptMapper);
        streetUtil.Init();

        DictionaryUtils dictionaryUtils = new DictionaryUtils(deptMapper);
        dictionaryUtils.init();

        GridUtil gridUtil = new GridUtil(basicGridsMapper);
        gridUtil.init();

        if (readData != null && readData.size() > 0) {
            for (int i = 0; i < readData.size(); i++) {
                BasicPerson basicGrids = readData.get(i);
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if (basicGrids != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("number", i + 1);
                    map.put("success", "??????");

                    String msg = "";
                    //??????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicGrids.getName())) {
                        msg += "????????????\n";
                    } else {
                        if (!DictionaryUtils.isChinese(basicGrids.getName())) {
                            msg += "????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //??????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicGrids.getSex())) {
                        msg += "????????????\n";
                    }
                    //????????????
                    if (basicGrids.getBirthDate() == null) {
                        msg += "??????????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getCardId())) {
                        String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getCardId());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(chinaPhoneLegal) && !chinaPhoneLegal.equals("YES")) {
                            msg += chinaPhoneLegal + "????????????\n";
                        } else {
                            //??????id????????????????????????
                            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getCardId, basicGrids.getCardId()));
                            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getIsDelete, 0));
                            int res = basicPersonMapper.selectCount(queryWrapper);
                            //???????????????
                            if (res >= 1) {
                                msg += "?????????????????????,?????????\n";
                            }
                            //map ??????
                            else {
                                for (int j = i - 1; j >= 0; j--) {
                                    BasicPerson basicGrids1 = readData.get(j);
                                    if (basicGrids1 != null && basicGrids.getCardId().equals(basicGrids1.getCardId())) {
                                        msg += "??????" + (j + 1) + "???????????????????????????\n";
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        msg += "??????????????????\n";
                    }
                    //??????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getNation())) {
                        String s = dictionaryUtils.nationType(basicGrids.getNation());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setNation(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "????????????\n";
                    }

                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPoliticalFace())) {
                        String s = dictionaryUtils.politicalFaceType(basicGrids.getPoliticalFace());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setPoliticalFace(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "??????????????????\n";
                    }

                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getStreetName())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreetName(), streetUtil.streetDepts);
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setStreetId(s);
                        } else {
                            msg += "????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "??????????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getCommunityName())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreetName(), basicGrids.getCommunityName(), streetUtil.gridDepts);
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setCommunityId(s);
                        } else {
                            msg += "????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "??????????????????\n";
                    }

                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getOwnedGrid())) {
                        String s = GridUtil.matchingGrid(basicGrids.getOwnedGrid(), basicGrids.getCommunityName(), basicGrids.getStreetName(), gridUtil.basicGrids);
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setOwnedGridId(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "??????????????????\n";
                    }
                    //?????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPhone())) {
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPhone());
                        if (!chinaPhoneLegal) {
                            msg += "????????????????????????????????????\n";
                        } else {
                            //???????????????????????????
                            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getPhone, basicGrids.getPhone()));
                            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getIsDelete, 0));
                            int res = basicPersonMapper.selectCount(queryWrapper);
                            //???????????????
                            if (res >= 1) {
                                msg += "???????????????,?????????\n";
                            }
                            //map ??????
                            else {
                                for (int j = i - 1; j >= 0; j--) {
                                    BasicPerson basicGrids1 = readData.get(j);
                                    if (basicGrids1 != null && basicGrids.getPhone().equals(basicGrids1.getPhone())) {
                                        msg += "??????" + (j + 1) + "????????????????????????\n";
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        msg += "???????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getSpecialPopulation())) {
                        String s = dictionaryUtils.specialPopulationType(basicGrids.getSpecialPopulation());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setSpecialPopulation(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //??????????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getResidentGrid())) {
                        if (!DictionaryUtils.IsTrue(basicGrids.getResidentGrid())) {
                            msg += "??????????????????????????? '???'??????'???'\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getHobby())) {
                        String[] split = basicGrids.getHobby().split(",");
                        String hobby = "[";
                        if (split != null && split.length > 0) {
                            for (String r : split) {
                                String s = dictionaryUtils.hobby(r);
                                if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                                    hobby = hobby + "\""+s + "\",";
                                } else {
                                    msg += "??????????????????????????????????????????????????????????????????????????????\n";
                                    break;
                                }
                            }
                            if(hobby.length()>1){
                                hobby=hobby.substring(0,hobby.length() - 1);
                                hobby = hobby+"]";
                                basicGrids.setHobby(hobby);
                            }
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getTableTypeName())) {
                        String[] split = basicGrids.getTableTypeName().split(",");
                        String hobby = "";
                        if (split != null && split.length > 0) {
                            for (String r : split) {
                                int s = dictionaryUtils.tableType(r);
                                if (s != 0) {
                                    hobby = hobby + s + ",";
                                } else {
                                    msg += "??????????????????????????????????????????????????????????????????????????????\n";
                                    break;
                                }
                            }
                            if(hobby.length()>1){
                                hobby=hobby.substring(0,hobby.length() - 1);
                                basicGrids.setTableType(hobby);
                            }


                        }
                    } else {
                        msg += "??????????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPersonType())) {
                        String s = dictionaryUtils.personType(basicGrids.getPersonType());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setPersonType(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    } else {
                        msg += "??????????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicGrids.getResidentialAddress())) {
                        msg += "??????????????????\n";
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getReligiousBelief())) {
                        String s = dictionaryUtils.religious(basicGrids.getReligiousBelief());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setReligiousBelief(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getMaritalStatus())) {
                        String s = dictionaryUtils.maritalStatus(basicGrids.getMaritalStatus());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setMaritalStatus(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getMilitaryService())) {
                        String s = dictionaryUtils.militaryService(basicGrids.getMilitaryService());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setMilitaryService(s);
                        } else {
                            msg += "??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }

                    //??????????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsPreferentialTreatment())) {
                        if (!DictionaryUtils.IsTrue(basicGrids.getIsPreferentialTreatment())) {
                            msg += "??????????????????????????? '???'??????'???'\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsLonely())) {
                        if (!DictionaryUtils.IsTrue(basicGrids.getIsLonely())) {
                            msg += "????????????????????? '???'??????'???'\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsMinimumLiving())) {
                        if (!DictionaryUtils.IsTrue(basicGrids.getIsMinimumLiving())) {
                            msg += "????????????????????? '???'??????'???'\n";
                        }
                    }
                    //????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsDisabled())) {
                        if (!DictionaryUtils.IsTrue(basicGrids.getIsDisabled())) {
                            msg += "????????????????????? '???'??????'???'\n";
                        }
                    }
                    //?????????????????????
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPartyRelationshipManagemen())) {
                        String s = dictionaryUtils.partyRelationshipManagemen(basicGrids.getPartyRelationshipManagemen());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setPartyRelationshipManagemen(s);
                        } else {
                            msg += "????????????????????????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }

                    if (msg.length() > 0) {
                        isMatchingData = true;
                        map.put("success", "??????");
                    }
                    map.put("msg", msg);
                    result.add(map);
                }
            }
        }

        return isMatchingData;
    }

    @Override
    public Map<String, Object> getPersonDataMaleToFemaleratio(String communityId, String gridId) {
        return basicPersonMapper.getPersonDataMaleToFemaleratio(communityId, gridId);
    }

    /**
     * ????????????????????????
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @Override
    public List<Map<String, Object>> getBasicPersonCountByPersonType(String communityId, String gridId) {
        return basicPersonMapper.getBasicPersonCountByPersonType(communityId, gridId);
    }

    /**
     * ??????????????????????????????????????????....???
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @Override
    public List<Map<String, Object>> getBasicPersonCountByPopulation(String communityId, String gridId) {
        return basicPersonMapper.getBasicPersonCountByPopulation(communityId, gridId);
    }

    @Override
    public IPage<Map<String, Object>> queryBasicPersonDynamicList(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {

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
        Page<BasicPerson> pageData = new Page<>(page, limit);

        IPage<Map<String, Object>> result = basicPersonMapper.selectByMyWrapperByPage(basicPerson, pageData);
        if (result != null) {
            for (Map<String, Object> map : result.getRecords()) {
                if (map != null && map.containsKey("id") && map.get("id") != null) {
                    Map<String, Object> resMap = basicPersonMapper.getOneHouseByPersonId(map.get("id").toString().trim());
                    if (resMap != null) {
                        map.putAll(resMap);
                    }

                }
            }
        }

        return result;
    }
}
