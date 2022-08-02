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
            map.put("姓名", re.getName());
            map.put("性别", re.getSex());
            map.put("身份证号", re.getIdCard());
            map.put("民族", re.getNation());
            map.put("手机号", re.getPhone());
            map.put("所属街道", re.getStreet());
            map.put("所属社区", re.getHouseCommunity());
            map.put("是否为党员", re.getIsPartyMember());
            map.put("家庭住址", re.getHomeAddress());
            map.put("审核状态（0、审核中；1、已审核，2、审核不通过）", re.getState());
            map.put("排序", re.getOrderNumber());
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
     * 功能描述：构建模糊查询
     *
     * @param tZhsqVolunteer 需要模糊查询的信息
     * @return 返回查询
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
            //搜索条件
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
     * 根据社区id和网格id查询志愿者人数
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
        //读取一个对象的信息
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
                    throw new BusinessErrorException("保存失败，请联系管理员");
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
                    map.put("success","成功");

                    String msg="";
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
                    //是否为党员
                    if(StringUtils.isBlank(basicGrids.getIsPartyMember())){
                        msg+="是否为党员为空\n";
                    }
                    else{
                        //是否为党员
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getIsPartyMember())) {
                            if (!DictionaryUtils.IsTrue(basicGrids.getIsPartyMember())) {
                                msg += "是否为党员必须是 '是'或者'否'\n";
                            }
                        }
                    }
                    //婚姻状况
                    if(StringUtils.isNotBlank(basicGrids.getMaritalStatus())){
                        String s = dictionaryUtils.maritalStatus(basicGrids.getMaritalStatus());
                        if(StringUtils.isNotBlank(s)){
                            //basicGrids.setMaritalStatus(s);
                        }
                        else{
                            msg+="婚姻状况未匹配上，请检查当前婚姻状况（字典）是否入库\n";
                        }
                    }
                    else{
                        msg+="婚姻状况为空\n";
                    }

                    //手机号
                    if(StringUtils.isNotBlank(basicGrids.getPhone())){
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPhone());
                        if(!chinaPhoneLegal){
                            msg+="手机号格式不正确，请检查\n";
                        }
                        else{
                            //根据手机号获取信息
                            QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getPhone, basicGrids.getPhone()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIsDelete, 0));
                            int res = tZhsqVolunteerMapper.selectCount(queryWrapper);
                            //数据库重复
                            if (res >= 1) {
                                msg+="手机号重复,请检查\n";
                            }
                            //map 重复
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqVolunteer basicGrids1 = readData.get(j);
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
                            QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIdCard, basicGrids.getIdCard()));
                            queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIsDelete, 0));
                            int res = tZhsqVolunteerMapper.selectCount(queryWrapper);
                            //数据库重复
                            if (res >= 1) {
                                msg+="身份证号码重复,请检查\n";
                            }
                            //map 重复
                            else{
                                for(int j=i-1;j>=0;j--){
                                    TZhsqVolunteer basicGrids1 = readData.get(j);
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
                    //籍贯
                    if(StringUtils.isBlank(basicGrids.getNativePlace())){
                        msg+="籍贯为空\n";
                    }
                    //所属街道
                    if (StringUtils.isNotBlank(basicGrids.getStreet())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreet(), streetUtil.streetDepts);
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
                    if (StringUtils.isNotBlank(basicGrids.getHouseCommunity())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreet(),basicGrids.getHouseCommunity(), streetUtil.gridDepts);
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
                    if(StringUtils.isNotBlank(basicGrids.getOwnedGridName())){
                        String s = GridUtil.matchingGrid(basicGrids.getOwnedGridName(),basicGrids.getHouseCommunity(),basicGrids.getStreet(),gridUtil.basicGrids);
                        if(StringUtils.isNotBlank(s)){
                            basicGrids.setOwnedGrid(s);
                        }
                        else{
                            msg+="所属网格未匹配上，请检查当前所属网格是否入库\n";
                        }
                    }
                    else{
                        msg+="所属网格为空\n";
                    }

                    //学历
                    if(StringUtils.isNotBlank(basicGrids.getEducation())){
                        String s = dictionaryUtils.education(basicGrids.getEducation());
                        if(StringUtils.isNotBlank(s)){
                           // basicGrids.setEducation(s);
                        }
                        else{
                            msg+="学历未匹配上，请检查当前学历（字典）是否入库\n";
                        }
                    }
                    else{
                        msg+="学历为空\n";
                    }
                    //宗教信仰
                    if(StringUtils.isNotBlank(basicGrids.getReligiousBelief())){
                        String s = dictionaryUtils.religious(basicGrids.getReligiousBelief());
                        if(StringUtils.isNotBlank(s)){
                            //basicGrids.setReligiousBelief(s);
                        }
                        else{
                            msg+="宗教信仰未匹配上，请检查当前宗教信仰（字典）是否入库\n";
                        }
                    }
                    else{
                        msg+="宗教信仰为空\n";
                    }

                    //申请时间
                    if(StringUtils.isBlank(basicGrids.getApplicationTime())){
                        msg+="申请时间为空\n";
                    }
                    else{
                        boolean rqFormat =DictionaryUtils.IsDate(basicGrids.getApplicationTime());
                        if(!rqFormat){
                            msg+="申请时间格式错误，不为日期格式\n";
                        }
                    }

                    //工作单位
                    if(StringUtils.isBlank(basicGrids.getWorkAddress())){
                        msg+="工作单位为空\n";
                    }
                    //居住房屋
                    if(StringUtils.isBlank(basicGrids.getHomeAddress())){
                        msg+="居住房屋为空\n";
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
