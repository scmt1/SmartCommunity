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
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.entity.TZhsqVolunteerActivity;
import me.zhengjie.mapper.*;
import me.zhengjie.service.ITZhsqVolunteerService;
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

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class TZhsqVolunteerServiceImpl extends ServiceImpl<TZhsqVolunteerMapper, TZhsqVolunteer> implements ITZhsqVolunteerService {

    private final TZhsqVolunteerMapper tZhsqVolunteerMapper;

    private final TZhsqVolunteerActivityMapper tZhsqVolunteerActivityMapper;

    private final BasicPersonMapper basicPersonMapper;

    private final BasicGridsMapper basicGridsMapper;

    private final GridDeptMapper deptMapper;

    public TZhsqVolunteer getTZhsqVolunteerById(String id) {
        TZhsqVolunteer tZhsqVolunteer = tZhsqVolunteerMapper.selectById(id);
        if (tZhsqVolunteer != null) {
            List<TZhsqVolunteerActivity> tZhsqVolunteerActivities = tZhsqVolunteerActivityMapper.selectActivityByVtId(id);
            tZhsqVolunteer.setTZhsqGridMembers(tZhsqVolunteerActivities);

            if (StringUtils.isNotEmpty(tZhsqVolunteer.getPersonId())) {
                BasicPerson basicPerson = basicPersonMapper.selectById(tZhsqVolunteer.getPersonId());
                tZhsqVolunteer.setBasicPerson(basicPerson);
            }
            if (StringUtils.isNotEmpty(tZhsqVolunteer.getOwnedGrid())) {
                BasicGrids basicGrids = basicGridsMapper.selectById(tZhsqVolunteer.getOwnedGrid());
                if (basicGrids != null) {
                    tZhsqVolunteer.setOwnedGridName(basicGrids.getName());
                }

            }
            return tZhsqVolunteer;
        }
        return null;
    }

    @Override
    public Result<Object> queryTZhsqVolunteerListByPage(TZhsqVolunteer tZhsqVolunteer, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqVolunteer> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteer != null) {
            queryWrapper = LikeAllFeild(tZhsqVolunteer, searchVo);
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

        IPage<TZhsqVolunteer> result = tZhsqVolunteerMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqVolunteer tZhsqVolunteer, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteer != null) {
            queryWrapper = LikeAllFeild(tZhsqVolunteer, null);
        }
        List<TZhsqVolunteer> list = tZhsqVolunteerMapper.selectByMyWrapper(queryWrapper);
        for (TZhsqVolunteer re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("??????", re.getName());
            map.put("??????", re.getSex());
            map.put("????????????", re.getIdCard());
            map.put("??????", re.getNation());
            map.put("?????????", re.getPhone());
            map.put("????????????", re.getStreet());
            map.put("????????????", re.getHouseCommunity());
            map.put("???????????????", re.getIsPartyMember());
            map.put("????????????", re.getHomeAddress());
            map.put("???????????????0???????????????1???????????????2?????????????????????", re.getState());
            map.put("??????", re.getOrderNumber());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    @Override
    public Result<Object> queryAllList(TZhsqVolunteer tZhsqVolunteer) {
        QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteer != null) {
            queryWrapper = LikeAllFeild(tZhsqVolunteer, null);
        }
        List<TZhsqVolunteer> result = tZhsqVolunteerMapper.selectList(queryWrapper);
        return ResultUtil.data(result);
    }

    /**
     * ?????????????????????????????????
     *
     * @param tZhsqVolunteer ???????????????????????????
     * @return ????????????
     */
    public QueryWrapper<TZhsqVolunteer> LikeAllFeild(TZhsqVolunteer tZhsqVolunteer, SearchVo searchVo) {
        QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper<>();
        if (tZhsqVolunteer.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getId, tZhsqVolunteer.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getName, tZhsqVolunteer.getName()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getSex())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getSex, tZhsqVolunteer.getSex()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getIdCard())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getIdCard, tZhsqVolunteer.getIdCard()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getPhone())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getPhone, tZhsqVolunteer.getPhone()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getStreet())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getStreet, tZhsqVolunteer.getStreet()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getOwnedGrid())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getOwnedGrid, tZhsqVolunteer.getOwnedGrid()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getHouseCommunity())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getHouseCommunity, tZhsqVolunteer.getHouseCommunity()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getCommunityId, tZhsqVolunteer.getCommunityId()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getOwnedGrid())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getOwnedGrid, tZhsqVolunteer.getOwnedGrid()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getOwnedGridName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getOwnedGridName, tZhsqVolunteer.getOwnedGridName()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getIsPartyMember())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getIsPartyMember, tZhsqVolunteer.getIsPartyMember()));
        }
        if (StringUtils.isNotBlank(tZhsqVolunteer.getHomeAddress())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getHomeAddress, tZhsqVolunteer.getHomeAddress()));
        }
        if (tZhsqVolunteer.getState() != null && StringUtils.isNotBlank(tZhsqVolunteer.getState())) {
            queryWrapper.lambda().and(i -> i.in(TZhsqVolunteer::getState, tZhsqVolunteer.getState().split(",")));
        }
        if (tZhsqVolunteer.getPosition() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getPosition, tZhsqVolunteer.getPosition()));
        }
        if (tZhsqVolunteer.getOrderNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getOrderNumber, tZhsqVolunteer.getOrderNumber()));
        }
        if (tZhsqVolunteer.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getIsDelete, tZhsqVolunteer.getIsDelete()));
        }
        if (tZhsqVolunteer.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getCreateId, tZhsqVolunteer.getCreateId()));
        }
        if (tZhsqVolunteer.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getCreateTime, tZhsqVolunteer.getCreateTime()));
        }
        if (tZhsqVolunteer.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getUpdateId, tZhsqVolunteer.getUpdateId()));
        }
        if (tZhsqVolunteer.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getUpdateTime, tZhsqVolunteer.getUpdateTime()));
        }
        if (tZhsqVolunteer.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getDeleteId, tZhsqVolunteer.getDeleteId()));
        }
        if (tZhsqVolunteer.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getDeleteTime, tZhsqVolunteer.getDeleteTime()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TZhsqVolunteer::getApplicationTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //????????????
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(TZhsqVolunteer::getName, searchVo.getSearchInfo())
                        .or().like(TZhsqVolunteer::getPhone, searchVo.getSearchInfo())
                        .or().like(TZhsqVolunteer::getIdCard, searchVo.getSearchInfo())
                        .or().like(TZhsqVolunteer::getHomeAddress, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getIsDelete, 0));
        return queryWrapper;

    }

    /**
     * ????????????id?????????id?????????????????????
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @Override
    public int selectByCommunityAndGrid(String communityId, String gridId){
     return tZhsqVolunteerMapper.selectByCommunityAndGrid(communityId,gridId);
    }


    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //???????????????????????????
        TZhsqVolunteer tZhsqGridMember = new TZhsqVolunteer();
        List<TZhsqVolunteer> readData =ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData =  matchingData(readData,result);
            if(isMatchingData)
            {
                return ResultUtil.data(result);
            }
            for(TZhsqVolunteer basicGrids:readData){
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                basicGrids.setIsDelete(0);
                basicGrids.setState("0");
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = tZhsqVolunteerMapper.insert(basicGrids);
                if(insert!=1){
                    throw new BusinessErrorException("?????????????????????????????????");
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
     * ????????????
     */
    public Boolean  matchingData(List<TZhsqVolunteer> readData,List<Map<String, Object>>  result ){
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
                TZhsqVolunteer basicGrids = readData.get(i);
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if(basicGrids!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("number",i+1);
                    map.put("success","??????");

                    String msg="";
                    //??????
                    if(StringUtils.isBlank(basicGrids.getName())){
                        msg+="????????????\n";
                    }
                    else{
                        if(!DictionaryUtils.isChinese(basicGrids.getName())){
                            msg+="????????????????????????????????????????????????????????????\n";
                        }
                    }
                    //??????
                    if(StringUtils.isBlank(basicGrids.getSex())){
                        msg+="????????????\n";
                    }
                    //???????????????
                    if(StringUtils.isBlank(basicGrids.getIsPartyMember())){
                        msg+="?????????????????????\n";
                    }
                    else{
                        //???????????????
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsPartyMember())) {
                            if (!DictionaryUtils.IsTrue(basicGrids.getIsPartyMember())) {
                                msg += "???????????????????????? '???'??????'???'\n";
                            }
                        }
                    }
                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getMaritalStatus())){
                        String s = dictionaryUtils.maritalStatus(basicGrids.getMaritalStatus());
                        if(StringUtils.isNotBlank(s)){
                            //basicGrids.setMaritalStatus(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //?????????
                    if(StringUtils.isNotBlank(basicGrids.getPhone())){
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPhone());
                        if(!chinaPhoneLegal){
                            msg+="????????????????????????????????????\n";
                        }
                        else{
                            //???????????????????????????
                            QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getPhone, basicGrids.getPhone()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIsDelete, 0));
                            int res = tZhsqVolunteerMapper.selectCount(queryWrapper);
                            //???????????????
                            if (res >= 1) {
                                msg+="???????????????,?????????\n";
                            }
                            //map ??????
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqVolunteer basicGrids1 = readData.get(j);
                                    if(basicGrids1!=null && basicGrids.getPhone().equals(basicGrids1.getPhone()) ){
                                        msg+="??????"+(j+1)+"????????????????????????\n";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        msg+="???????????????\n";
                    }

                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getIdCard())){
                        String chinaPhoneLegal = IdCardUtil.IDCardValidate(basicGrids.getIdCard());
                        if(StringUtils.isNotBlank(chinaPhoneLegal)&& !chinaPhoneLegal.equals("YES")){
                            msg+=chinaPhoneLegal+"????????????\n";
                        }
                        else{
                            //??????id????????????????????????
                            QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIdCard, basicGrids.getIdCard()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIsDelete, 0));
                            int res = tZhsqVolunteerMapper.selectCount(queryWrapper);
                            //???????????????
                            if (res >= 1) {
                                msg+="?????????????????????,?????????\n";
                            }
                            //map ??????
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqVolunteer basicGrids1 = readData.get(j);
                                    if(basicGrids1!=null && basicGrids.getIdCard().equals(basicGrids1.getIdCard()) ){
                                        msg+="??????"+(j+1)+"???????????????????????????\n";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //??????
                    if(StringUtils.isNotBlank(basicGrids.getNation())){
                        String s = dictionaryUtils.nationType(basicGrids.getNation());
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setNation(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="????????????\n";
                    }
                    //??????
                    if(StringUtils.isBlank(basicGrids.getNativePlace())){
                        msg+="????????????\n";
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
                    if(StringUtils.isNotBlank(basicGrids.getOwnedGridName())){
                        String s = GridUtil.matchingGrid(basicGrids.getOwnedGridName(),basicGrids.getHouseCommunity(),basicGrids.getStreet(),gridUtil.basicGrids);
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setOwnedGrid(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //??????
                    if(StringUtils.isNotBlank(basicGrids.getEducation())){
                        String s = dictionaryUtils.education(basicGrids.getEducation());
                        if(StringUtils.isNotBlank(s)){
                           // basicGrids.setEducation(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="????????????\n";
                    }
                    //????????????
                    if(StringUtils.isNotBlank(basicGrids.getReligiousBelief())){
                        String s = dictionaryUtils.religious(basicGrids.getReligiousBelief());
                        if(StringUtils.isNotBlank(s)){
                            //basicGrids.setReligiousBelief(s);
                        }
                        else{
                            msg+="??????????????????????????????????????????????????????????????????????????????\n";
                        }
                    }
                    else{
                        msg+="??????????????????\n";
                    }

                    //????????????
                    if(StringUtils.isBlank(basicGrids.getApplicationTime())){
                        msg+="??????????????????\n";
                    }
                    else{
                        boolean rqFormat =DictionaryUtils.IsDate(basicGrids.getApplicationTime());
                        if(!rqFormat){
                            msg+="?????????????????????????????????????????????\n";
                        }
                    }

                    //????????????
                    if(StringUtils.isBlank(basicGrids.getWorkAddress())){
                        msg+="??????????????????\n";
                    }
                    //????????????
                    if(StringUtils.isBlank(basicGrids.getHomeAddress())){
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
