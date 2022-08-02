package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.entity.*;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.*;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITBuildingArchivesService;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class TBuildingArchivesServiceImpl extends ServiceImpl<TBuildingArchivesMapper, TBuildingArchives> implements ITBuildingArchivesService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TBuildingArchivesMapper tBuildingArchivesMapper;

    private final BasicHousingEstateMapper basicHousingEstateMapper;

    private final GridDeptMapper deptMapper;

    private final BasicGridsMapper basicGridsMapper;

    private final BasicUnitMapper basicUnitMapper;

    @Override
    public Result<Object> getTBuildingArchivesById(String id) {
        TBuildingArchives tBuildingArchives = tBuildingArchivesMapper.selectById(id);
        if (tBuildingArchives != null) {
            return ResultUtil.data(tBuildingArchives);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public List<TBuildingArchives> queryTBuildingArchivesAll(TBuildingArchives tBuildingArchives) {

        QueryWrapper<TBuildingArchives> queryWrapper = new QueryWrapper<>();
        if (tBuildingArchives != null) {
            queryWrapper = LikeAllFeild(tBuildingArchives, null, null);
        }
        queryWrapper.orderByAsc("building_name*1");
        List<TBuildingArchives> result = tBuildingArchivesMapper.selectList(queryWrapper);
        return result;
    }

    @Override
    public Page<TBuildingArchives> queryTBuildingArchivesListByPage(TBuildingArchives tBuildingArchives, SearchVo searchVo, PageVo pageVo) {
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
        Page<TBuildingArchives> pageData = new Page<>(page, limit);
        QueryWrapper<TBuildingArchives> queryWrapper = new QueryWrapper<>();
        if (tBuildingArchives != null) {
            queryWrapper = LikeAllFeild(tBuildingArchives, searchVo, null);
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
        return tBuildingArchivesMapper.selectByPageWrapper(queryWrapper, pageData);
    }

    @Override
    public void download(TBuildingArchives tBuildingArchives, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TBuildingArchives> queryWrapper = new QueryWrapper<>();
        if (tBuildingArchives != null) {
            queryWrapper = LikeAllFeild(tBuildingArchives, null, null);
        }
        List<TBuildingArchives> list = tBuildingArchivesMapper.selectByMyWrapper(queryWrapper);
        for (TBuildingArchives re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("建筑类型", re.getBuildingType());
            if (StringUtils.isNotBlank(re.getHousingEstateName())) {
                map.put("建筑名称", re.getHousingEstateName() + re.getBuildingName() + "号楼");
            } else {
                map.put("建筑名称", re.getBuildingName());
            }
            map.put("建筑地址", re.getBuildingAddress());
            map.put("所属街道", re.getStreet());
            map.put("所属社区", re.getCommunity());
            map.put("所属网格", re.getGridName());
//            map.put("地图标注（地理坐标）", re.getLocation());
            map.put("所属小区", re.getHousingEstateName());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    @Override
    public Result<Object> queryTBuildingArchivesListByAnyWayWhere(TBuildingArchives tBuildingArchives, String key, PageVo pageVo) {
        List<TBuildingArchives> tBuildingArchives1 = new ArrayList<>();
        IPage<TBuildingArchives> result = null;
        boolean isPage = true;
        int page = 1;
        int limit = 10;
        QueryWrapper<TBuildingArchives> queryWrapper = new QueryWrapper<>();
        if (tBuildingArchives != null) {
            queryWrapper = LikeAllFeild(tBuildingArchives, null, key);
        }
        queryWrapper.orderByAsc("building_name*1");
        if (pageVo != null && pageVo.getPageNumber() != 0 && pageVo.getPageSize() != 0) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
            Page<TBuildingArchives> pageData = new Page<>(page, limit);

            result = tBuildingArchivesMapper.selectByPageWrapper(queryWrapper, pageData);
            tBuildingArchives1 = result.getRecords();
        } else {
            isPage = false;
            tBuildingArchives1 = tBuildingArchivesMapper.selectByMyWrapper(queryWrapper);
        }
        for (TBuildingArchives buildingArchives : tBuildingArchives1) {
            BasicHousingEstate basicHousingEstate = basicHousingEstateMapper.selectById(buildingArchives.getHousingEstate());
            if (basicHousingEstate != null) {
                buildingArchives.setHousingEstateName(basicHousingEstate.getName() + buildingArchives.getBuildingName() + "号楼");
            }
        }
        if (isPage) {
            return ResultUtil.data(result);
        }

        return ResultUtil.data(tBuildingArchives1);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tBuildingArchives 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TBuildingArchives> LikeAllFeild(TBuildingArchives tBuildingArchives, SearchVo searchVo, String key) {
        QueryWrapper<TBuildingArchives> queryWrapper = new QueryWrapper<>();
        if (tBuildingArchives.getId() != null) {
            queryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getId, tBuildingArchives.getId()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getBuildingType())) {
            String[] types = tBuildingArchives.getBuildingType().split(",");
            queryWrapper.lambda().and(i -> i.in(TBuildingArchives::getBuildingType, types));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getBuildingName())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBuildingName, tBuildingArchives.getBuildingName()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getBuildingAddress())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBuildingAddress, tBuildingArchives.getBuildingAddress()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getCommunity())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getCommunity, tBuildingArchives.getCommunity()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getCommunityId, tBuildingArchives.getCommunityId()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getGrid())) {
            queryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getGrid, tBuildingArchives.getGrid()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getLocation())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getLocation, tBuildingArchives.getLocation()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getHousingEstate())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getHousingEstate, tBuildingArchives.getHousingEstate()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getStreet())) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getStreet, tBuildingArchives.getStreet()));
        }
        if (StringUtils.isNotBlank(tBuildingArchives.getStreetId())) {
            String[] streetIds = tBuildingArchives.getStreetId().split(",");
            queryWrapper.lambda().and(i -> i.in(TBuildingArchives::getStreetId, streetIds));
            //queryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getStreetId, tBuildingArchives.getStreetId()));
        }
        if (tBuildingArchives.getStreetNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getStreetNumber, tBuildingArchives.getStreetNumber()));
        }
        if (tBuildingArchives.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getIsDelete, tBuildingArchives.getIsDelete()));
        }
        if (tBuildingArchives.getPosition() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getPosition, tBuildingArchives.getPosition()));
        }
        if (tBuildingArchives.getBak2() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBak2, tBuildingArchives.getBak2()));
        }
        if (tBuildingArchives.getBak3() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBak3, tBuildingArchives.getBak3()));
        }
        if (tBuildingArchives.getBak4() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBak4, tBuildingArchives.getBak4()));
        }
        if (tBuildingArchives.getBak5() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getBak5, tBuildingArchives.getBak5()));
        }
        if (tBuildingArchives.getRemark() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getRemark, tBuildingArchives.getRemark()));
        }
        if (tBuildingArchives.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getCreateTime, tBuildingArchives.getCreateTime()));
        }
        if (tBuildingArchives.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getCreateId, tBuildingArchives.getCreateId()));
        }
        if (tBuildingArchives.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getUpdateTime, tBuildingArchives.getUpdateTime()));
        }
        if (tBuildingArchives.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getUpdateId, tBuildingArchives.getUpdateId()));
        }
        if (tBuildingArchives.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getDeleteId, tBuildingArchives.getDeleteId()));
        }
        if (tBuildingArchives.getManagerName() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getManagerName, tBuildingArchives.getManagerName()));
        }
        if (tBuildingArchives.getManagerPhone() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getManagerPhone, tBuildingArchives.getManagerPhone()));
        }
        if (tBuildingArchives.getParkPlaceNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(TBuildingArchives::getParkPlaceNumber, tBuildingArchives.getParkPlaceNumber()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TBuildingArchives::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //搜索条件
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.and(wrapper -> wrapper.like("concat(housing_estate_Name ,building_name,'号楼')", searchVo.getSearchInfo()).or().like("building_address", searchVo.getSearchInfo()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getIsDelete, 0));


        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(wrapper -> wrapper.like("concat(housing_estate_Name ,building_name,'号楼')", key).or().like("building_address", key));
        }
        return queryWrapper;

    }

    @Override
    public List<Map<String, Object>> getMaxOfFloorAndDoorAndUnit(String id) {
        return tBuildingArchivesMapper.getMaxOfFloorAndDoorAndUnit(id);
    }

    @Override
    public boolean insert(TBuildingArchives tBuildingArchives) {
        return tBuildingArchivesMapper.insert(tBuildingArchives) > 0;
    }

    /**
     * 查询除当前标绘面以外的所有数据
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> getOtherPolygonData(String id) {
        return tBuildingArchivesMapper.getOtherPolygonData(id);
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //读取一个对象的信息
        TBuildingArchives tZhsqGridMember = new TBuildingArchives();
        List<TBuildingArchives> readData = ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData = matchingData(readData, result);
            if (isMatchingData) {
                return ResultUtil.data(result);
            }
            int i=0;
            for (TBuildingArchives basicGrids : readData) {
                if (basicGrids == null || BeanUtil.checkObjAllFieldsIsNull(basicGrids)) {
                    continue;
                }
                Boolean isTrue = false;//楼栋信息是否重复
                for (int j = i - 1; j >= 0; j--) {
                    TBuildingArchives basicGrids1 = readData.get(j);
                    if (basicGrids1 != null && StringUtils.isNotBlank(basicGrids.getBuildingName()) && StringUtils.isNotBlank(basicGrids.getGridName()) &&  StringUtils.isNotBlank(basicGrids.getCommunity()) &&  StringUtils.isNotBlank(basicGrids.getStreet()) &&  StringUtils.isNotBlank(basicGrids.getHousingEstateName())) {
                        if (basicGrids.getBuildingName().equals(basicGrids1.getBuildingName()) && basicGrids.getCommunity().equals(basicGrids1.getCommunity()) && basicGrids.getStreet().equals(basicGrids1.getStreet()) && basicGrids.getHousingEstateName().equals(basicGrids1.getHousingEstateName())) {
                            isTrue=true;
                            break;
                        }
                    }

                }
                String uuid = "";
                synchronized (uuid) {
                    uuid = UUID.randomUUID().toString().replaceAll("-", "");
                }
                if(!isTrue){
                    basicGrids.setId(uuid);
                    basicGrids.setIsDelete(0);
                    basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    int insert = tBuildingArchivesMapper.insert(basicGrids);
                    if (insert != 1) {
                        throw new BusinessErrorException("保存失败，请联系管理员");
                    }
                }
                else{
                    //查询楼栋
                    QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
                    objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getIsDelete, 0));
                    objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getBuildingName, basicGrids.getBuildingName()));
                    objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getHousingEstate, basicGrids.getHousingEstate()));
                    objectQueryWrapper.select("id");
                    List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);
                    if (tBuildingArchives != null && tBuildingArchives.size() > 0 && tBuildingArchives.get(0) != null && StringUtils.isNotBlank(tBuildingArchives.get(0).getId())) {
                        uuid = tBuildingArchives.get(0).getId();
                    }
                    else{
                        throw new BusinessErrorException("查询楼栋失败，请联系管理员");
                    }
                }

                BasicUnit basicUnit = new BasicUnit();
                basicUnit.setBuildArchiveId(uuid);
                basicUnit.setCreateTime(new Timestamp(System.currentTimeMillis()));
                basicUnit.setIsDelete(0);
                basicUnit.setName(Integer.parseInt(basicGrids.getUnit()) );
                basicUnit.setFloorNumber(Integer.parseInt(basicGrids.getFloor()));
                basicUnit.setHouseholdsNumber(Integer.parseInt(basicGrids.getDoorNumber()));
                String basicUnitId="";
                synchronized (basicUnitId) {
                    basicUnitId = UUID.randomUUID().toString().replaceAll("-", "");
                }
                basicUnit.setId(basicUnitId);
                int insert = basicUnitMapper.insert(basicUnit);
                if(insert!=1){
                    throw new BusinessErrorException("新增单元失败，请联系管理员！！");
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILURE);
        }

        return ResultUtil.data(result);
    }

    /**
     * 匹配数据
     */
    public Boolean matchingData(List<TBuildingArchives> readData, List<Map<String, Object>> result) throws ParseException {
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
                TBuildingArchives basicGrids = readData.get(i);
                if (basicGrids == null || BeanUtil.checkObjAllFieldsIsNull(basicGrids)) {
                    continue;
                }
                if (basicGrids != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("number", i + 1);
                    map.put("success", "成功");

                    String msg = "";

                    //楼栋号
                    if (StringUtils.isNotBlank(basicGrids.getBuildingName())) {
                        if (!DictionaryUtils.isNumeric(basicGrids.getBuildingName())) {
                            msg += "楼栋号不正确，不为数字\n";
                        } else if (basicGrids.getBuildingName().trim().length() > 2) {
                            msg += "楼栋号不正确，长度最大为2\n";
                        }
                    } else {
                        msg += "楼栋号不能为空\n";
                    }
                    //单元名称
                    if (StringUtils.isNotBlank(basicGrids.getUnit())) {

                    } else {
                        msg += "单元名称不能为空\n";
                    }
                    //楼层数
                    if (StringUtils.isNotBlank(basicGrids.getFloor())) {
                        if (!DictionaryUtils.isNumeric(basicGrids.getFloor())) {
                            msg += "楼层数不正确，不为数字\n";
                        }
                    } else {
                        msg += "楼层数不能为空\n";
                    }
                    //层户数
                    if (StringUtils.isNotBlank(basicGrids.getDoorNumber())) {
                        if (!DictionaryUtils.isNumeric(basicGrids.getDoorNumber())) {
                            msg += "层户数不正确，不为数字\n";
                        } else if (basicGrids.getDoorNumber().trim().length() > 2) {
                            msg += "层户数不正确，长度最大为2\n";
                        }
                    } else {
                        msg += "层户数不能为空\n";
                    }


                    //建筑面积
                    if (StringUtils.isNotBlank(basicGrids.getArea())) {
                        if (!DictionaryUtils.isNumeric(basicGrids.getArea())) {
                            msg += "建筑面积不正确，不为数字\n";
                        }
                    }
                    else {
                        msg += "建筑面积为空\n";
                    }

                    //建筑用途
                    if (StringUtils.isNotBlank(basicGrids.getBuildingTypeName())) {
                        String s = dictionaryUtils.buildingType(basicGrids.getBuildingTypeName());
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setBuildingType(s);
                        } else {
                            msg += "建筑用途未匹配上，请检查当前建筑用途（字典）是否入库\n";
                        }
                    }
                    //建筑结构
                    if (StringUtils.isNotBlank(basicGrids.getBuildingStructure())) {
                        String s = dictionaryUtils.buildingStructureData(basicGrids.getBuildingStructure());
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setBuildingStructure(s);
                        } else {
                            msg += "建筑结构未匹配上，请检查当前建筑结构（字典）是否入库\n";
                        }
                    }
                    //建筑性质
                    if (StringUtils.isNotBlank(basicGrids.getBuildingNature())) {
                        String s = dictionaryUtils.buildingNatureData(basicGrids.getBuildingNature());
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setBuildingNature(s);
                        } else {
                            msg += "建筑性质未匹配上，请检查当前建筑性质（字典）是否入库\n";
                        }
                    }
                    //所属街道
                    if (StringUtils.isNotBlank(basicGrids.getStreet())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreet(), streetUtil.streetDepts);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setStreetId(s);
                        } else {
                            msg += "所属街道未匹配上，请检查所属街道是否入库\n";
                        }
                    } else {
                        msg += "所属街道为空\n";
                    }
                    //所属社区
                    if (StringUtils.isNotBlank(basicGrids.getCommunity())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreet(), basicGrids.getCommunity(), streetUtil.gridDepts);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setCommunityId(s);
                        } else {
                            msg += "所属社区未匹配上，请检查所属社区是否入库\n";
                        }
                    } else {
                        msg += "所属社区为空\n";
                    }

                    //所属网格
                    if (StringUtils.isNotBlank(basicGrids.getGridName())) {
                        String s = GridUtil.matchingGrid(basicGrids.getGridName(), basicGrids.getCommunity(), basicGrids.getStreet(), gridUtil.basicGrids);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setGrid(s);
                        } else {
                            msg += "所属网格未匹配上，请检查当前所属网格是否入库\n";
                        }
                    } else {
                        msg += "所属网格为空\n";
                    }

                    //所属小区
                    if (StringUtils.isNotBlank(basicGrids.getHousingEstateName())) {
                        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
                        //查询小区
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getIsDelete, 0));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getName, basicGrids.getHousingEstateName()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getCommunity, basicGrids.getCommunity()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getStreet, basicGrids.getStreet()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getGrid, basicGrids.getGridName()));
                        queryWrapper.select("id");
                        List<BasicHousingEstate> basicHousingEstates = basicHousingEstateMapper.selectList(queryWrapper);
                        if (basicHousingEstates != null && basicHousingEstates.size() > 0) {
                            if (basicHousingEstates.get(0) != null && StringUtils.isNotBlank(basicHousingEstates.get(0).getId())) {
                                basicGrids.setHousingEstate(basicHousingEstates.get(0).getId());
                                if(StringUtils.isNotBlank(basicGrids.getUnit()) && basicGrids.getUnit().length()>=2){
                                    basicGrids.setUnit(basicGrids.getUnit().substring(0,basicGrids.getUnit().length()-2));
                                }
                                //查询楼栋
                                QueryWrapper<TBuildingArchives> objectQueryWrapper = new QueryWrapper<>();
                                objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getIsDelete, 0));
                                objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getBuildingName, basicGrids.getBuildingName()));
                                objectQueryWrapper.lambda().and(item -> item.eq(TBuildingArchives::getHousingEstate, basicHousingEstates.get(0).getId()));
                                objectQueryWrapper.select("id");
                                List<TBuildingArchives> tBuildingArchives = tBuildingArchivesMapper.selectList(objectQueryWrapper);
                                if (tBuildingArchives != null && tBuildingArchives.size() > 0 && tBuildingArchives.get(0) != null && StringUtils.isNotBlank(tBuildingArchives.get(0).getId())) {
                                    msg += "楼栋号重复，当前小区下已有该楼栋，请检查\n";
                                } else {

                                    for (int j = i - 1; j >= 0; j--) {
                                        TBuildingArchives basicGrids1 = readData.get(j);
                                        if (basicGrids1 != null && StringUtils.isNotBlank(basicGrids.getBuildingName()) && StringUtils.isNotBlank(basicGrids.getGridName()) &&  StringUtils.isNotBlank(basicGrids.getCommunity()) &&  StringUtils.isNotBlank(basicGrids.getStreet()) &&  StringUtils.isNotBlank(basicGrids.getUnit())) {
                                            if (basicGrids.getBuildingName().equals(basicGrids1.getBuildingName()) && basicGrids.getCommunity().equals(basicGrids1.getCommunity()) && basicGrids.getStreet().equals(basicGrids1.getStreet()) && basicGrids.getUnit().equals(basicGrids1.getUnit())) {
                                                msg += "与第" + (j + 1) + "条数据小区名称重复\n";
                                                break;
                                            }
                                        }

                                    }


                                }

                            } else {
                                msg += "所属小区未匹配上，请检查当前所属小区是否入库\n";
                            }
                        } else {
                            msg += "所属小区未匹配上，请检查当前所属小区是否入库\n";
                        }
                    } else {
                        msg += "所属小区为空\n";
                    }

                    //*建筑地址
                    if (StringUtils.isBlank(basicGrids.getBuildingAddress())) {
                        msg += "建筑地址为空\n";
                    }
                    //建筑年代
                    if (StringUtils.isBlank(basicGrids.getBuildingYearString())) {
                        msg += "建筑年代为空\n";
                    }
                    else{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

                        Date date = null; //初始化date

                        date = sdf.parse(basicGrids.getBuildingYearString());
                        basicGrids.setBuildingYear(new Timestamp(date.getTime()));
                    }
                    //楼主姓名
                    if (StringUtils.isNotBlank(basicGrids.getManagerName())) {
                        boolean chinaPhoneLegal = DictionaryUtils.checkEnglish(basicGrids.getManagerName());
                        if (chinaPhoneLegal) {
                            msg += "楼主姓名格式不正确，请检查\n";
                        }
                    }

                    //楼主联系方式
                    if (StringUtils.isNotBlank(basicGrids.getManagerPhone())) {
                        boolean phoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getManagerPhone());
                        if (!phoneLegal) {
                            msg += "楼主联系方式格式不正确，请检查\n";
                        }
                    }


                    if (msg.length() > 0) {
                        isMatchingData = true;
                        map.put("success", "失败");
                    }
                    map.put("msg", msg);
                    result.add(map);
                }
            }
        }

        return isMatchingData;
    }


}
