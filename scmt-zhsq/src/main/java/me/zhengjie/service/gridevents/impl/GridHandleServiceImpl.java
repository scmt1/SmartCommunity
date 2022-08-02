package me.zhengjie.service.gridevents.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.gridevents.GridEventsMapper;
import me.zhengjie.mapper.gridevents.GridHandleMapper;
import me.zhengjie.mapper.gridevents.GridRecordMapper;
import me.zhengjie.entity.gridevents.GridEvents;
import me.zhengjie.entity.gridevents.GridHandle;
import me.zhengjie.entity.gridevents.GridRecord;
import me.zhengjie.service.gridevents.IGridHandleService;
import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class GridHandleServiceImpl extends ServiceImpl<GridHandleMapper, GridHandle> implements IGridHandleService {

    private final GridHandleMapper gridHandleMapper;

    private final GridEventsMapper gridEventsMapper;

    private final GridRecordMapper gridRecordMapper;

//    private final UserClient userClient;

    private final GridTree gridTree;
    private final SecurityUtil securityUtil;

    @Override
    public void add(GridHandle gridHandle) {
        gridHandleMapper.insert(gridHandle);
    }

    @Override
    public void modify(GridHandle gridHandle) {
        gridHandleMapper.updateById(gridHandle);
    }

    @Override
    public GridHandle loadOne(Integer gridHandleId) {
        return gridHandleMapper.selectById(gridHandleId);
    }

    @Override
    public void delete(Integer gridHandleDetailsId) {
        gridHandleMapper.deleteById(gridHandleDetailsId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        return gridHandleMapper.loadAllByQuery(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void isAuthenticity(JSONObject query) {
        //1是属实派给自己，2是派给网格长，3是不属实
        Integer type = query.getInteger("type");
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        Integer urgentType = query.getInteger("urgentType");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId = securityUtil.getCurrUser().getId();
        Date date = new Date();
        if (gridEvents.getStatus() != 1) {
            throw new BusinessErrorException("该工单已处理");
        }
        if (type == 1) {    //派给自己
            gridEvents.setStatus(3);
            gridEvents.setExecutor("" + userId);
            if(urgentType !=null){
                gridEvents.setUrgentType(urgentType);
            }

            GridHandle gridHandle = new GridHandle();
            gridHandle.setBeUserId(userId);
            gridHandle.setStatus(1);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格员" + userName + "判断" + gridEvents.getTitle() + "事件属实,并派遣至网格员" + userName);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else if (type == 2) {
            gridEvents.setStatus(2);
            if(urgentType !=null){
                gridEvents.setUrgentType(urgentType);
            }


            GridHandle gridHandle = new GridHandle();
            gridHandle.setBeUserId(0L);
            gridHandle.setCreateUser(userId);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setStatus(1);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格员" + userName + "判断" + gridEvents.getTitle() + "事件属实,并派遣至网格长");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            if (StringUtils.isEmpty(remark)) {
                throw new BusinessErrorException("请上传不属实理由");
            }
            gridEvents.setStatus(9);
            gridEvents.setExecutor("" + userId);
            if(urgentType !=null){
                gridEvents.setUrgentType(urgentType);
            }

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(2);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(0);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);
            gridHandle.setOrderStatus(9);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
//            String userName = userClient.baseUserInfoById(userId).getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格员" + userName + "判断" + gridEvents.getTitle() + "事件不属实");
            gridEventsMapper.updateById(gridEvents);
            gridRecordMapper.insert(gridRecord);
            gridHandleMapper.insert(gridHandle);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processing(JSONObject query) {
        //1是处理反馈，2是异常转单申请
        Integer type = query.getInteger("type");
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        String remark = query.getString("remark");
        String oldAudioMediaAddress = query.getString("oldAudioMediaAddress");
        String oldMediaAddress = query.getString("oldMediaAddress");
        String audioMediaAddress = query.getString("audioMediaAddress");
        String mediaAddress = query.getString("mediaAddress");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        if (gridEvents.getExecutor() != ("" + userId)) {
            throw new BusinessErrorException("操作失败，你没有该单的权限");
        }
        Date date = new Date();
        if (type == 1) {
            gridEvents.setStatus(5);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(2);
            gridHandle.setOldAudioMediaAddress(oldAudioMediaAddress);
            gridHandle.setAudioMediaAddress(audioMediaAddress);
            gridHandle.setOldMediaAddress(oldMediaAddress);
            gridHandle.setMediaAddress(mediaAddress);
            gridHandle.setRemark(remark);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(3);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格员" + userName + "处理了" + gridEvents.getTitle() + "事件");

            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setStatus(4);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setCreateUser(userId);
            gridHandle.setStatus(8);
            gridHandle.setBeUserId(0L);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(3);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格员" + userName + "对" + gridEvents.getTitle() + "事件申请异常转派");
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferExamine(JSONObject query) {
        //1是通过，2是驳回
        Integer type = query.getInteger("type");
        //JSONObject jsonObject = userClient.currentUser();
        Long userId = securityUtil.getCurrUser().getId();
        Long beUserId = query.getLongValue("beUserId");
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();
        if (type == 1) {
            gridEvents.setStatus(3);
            gridEvents.setExecutor("" + beUserId);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setBeUserId(beUserId);
            gridHandle.setStatus(1);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            String beUserName = userClient.baseUserInfoById(beUserId).getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "异常转派" + gridEvents.getTitle() + "事件给网格员" + beUserName);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setStatus(3);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(3);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "驳回异常转派信息");
            gridRecord.setCreateUser(userId);
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);

            gridRecordMapper.insert(gridRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examine(JSONObject query) {
        //1是通过，2是驳回
        Integer type = query.getInteger("type");
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        String remark = query.getString("remark");
        String mediaAddress = query.getString("mediaAddress");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();

        if (type == 1) {
            if (gridEvents.getDataFrom() == 1) { //1是居民app，2是街道app
                gridEvents.setStatus(6);

                GridHandle gridHandle = new GridHandle();
                gridHandle.setStatus(3);
                gridHandle.setGridEventsId(detailsId);
                gridHandle.setCreateUser(userId);
                gridHandle.setReviewStatus(1);
                gridHandle.setMediaAddress(mediaAddress);
                gridHandle.setRemark(remark);
                gridHandle.setCreateTime(date);

                GridRecord gridRecord = new GridRecord();
                gridRecord.setCreateTime(date);
                gridRecord.setStatus(4);
//                String userName = jsonObject.getJSONObject("data").getString("realName");
//                gridRecord.setContent("网格长" + userName + "办结审核通过了对" + gridEvents.getTitle() + "事件的处理结果");
                gridRecord.setCreateUser(userId);
                gridRecord.setKeyWords(gridEvents.getTitle());
                gridRecord.setGridEventsId(detailsId);
                gridEventsMapper.updateById(gridEvents);
                gridHandleMapper.insert(gridHandle);
                gridRecordMapper.insert(gridRecord);
            } else {
                gridEvents.setStatus(10);

                GridRecord gridRecord = new GridRecord();
                gridRecord.setCreateTime(date);
                gridRecord.setStatus(3);
//                String userName = jsonObject.getJSONObject("data").getString("realName");
//                gridRecord.setContent("网格长" + userName + "通过了审核并提交" + gridEvents.getTitle() + "事件给管理员");
                gridRecord.setCreateUser(userId);
                gridRecord.setKeyWords(gridEvents.getTitle());
                gridRecord.setGridEventsId(detailsId);
                gridEventsMapper.updateById(gridEvents);
                gridRecordMapper.insert(gridRecord);
            }


        } else {
            gridEvents.setStatus(3);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(3);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(0);
            gridHandle.setMediaAddress(mediaAddress);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(4);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "办结审核驳回了对" + gridEvents.getTitle() + "事件的处理结果");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void residentAudit(JSONObject query) {
        //1是发起异议，2是通过
        Integer type = query.getInteger("type");
        Integer starRating = query.getInteger("starRating");
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();
        if (type == 1) {
            gridEvents.setStatus(7);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(4);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(5);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("居民" + userName + "对" + gridEvents.getTitle() + "事件的处理发起异议");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setStatus(8);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(6);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setStarRating(starRating);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(6);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("居民" + userName + "对" + gridEvents.getTitle() + "做出评价");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void objectionReview(JSONObject query) {
        //1是驳回异议，2是通过
        Integer type = query.getInteger("type");
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Long beUserId = query.getLongValue("beUserId");
        Integer eventsTypeId = query.getInteger("eventsTypeId");
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();
        if (type == 1) {
            gridEvents.setStatus(8);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(5);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(2);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(7);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("管理员" + userName + "驳回了" + gridEvents.getTitle() + "事件的异议");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setStatus(3);
            gridEvents.setEventsTypeId(eventsTypeId);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(7);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(1);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(7);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("管理员" + userName + "通过了" + gridEvents.getTitle() + "事件的异议");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);

            {         //派遣
                JSONObject jsonT = new JSONObject();
                jsonT.put("beUserId", beUserId);
                jsonT.put("detailsId", detailsId);
                jsonT.put("type", 1);
                jsonT.put("userTy", 0);
                distribute(jsonT);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeEventsType(JSONObject query) {
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Integer eventsTypeId = query.getInteger("eventsTypeId");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();
        gridEvents.setEventsTypeId(eventsTypeId);

        GridRecord gridRecord = new GridRecord();
        gridRecord.setCreateTime(date);
        gridRecord.setStatus(2);
//        String userName = jsonObject.getJSONObject("data").getString("realName");
//        gridRecord.setContent("网格员" + userName + "修改了事件分类");
        gridRecord.setCreateUser(userId);
        gridRecord.setGridEventsId(detailsId);
        gridEventsMapper.updateById(gridEvents);
        gridRecordMapper.insert(gridRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distribute(JSONObject query) {
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        Long beUserId = query.getLongValue("beUserId");
        Integer type = query.getInteger("type");
        Integer userTy = query.getInteger("userTy");
        //1是派单，2是驳回
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Date date = new Date();
        if (type == 1) {
            gridEvents.setStatus(3);
            gridEvents.setExecutor("" + beUserId);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setBeUserId(beUserId);
            gridHandle.setStatus(1);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateUser(userId);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
//            JSONObject data = jsonObject.getJSONObject("data");
//            String userName = data.getString("realName");
//            JSONObject beUserData = userClient.baseUserInfoById(beUserId).getJSONObject("data");
//            String beUserName = beUserData.getString("realName");
//            gridRecord.setContent("网格长" + userName + "将" + gridEvents.getTitle() + "事件派遣至网格员" + beUserName);
//            if (userTy != null && StringUtils.isNotEmpty(userTy.toString()) && userTy == 0) {
//                gridRecord.setContent("管理员" + "将" + gridEvents.getTitle() + "事件派遣至网格员" + beUserName);
//            }

            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            GridHandle ogridHandle = gridHandleMapper.selectOne(new QueryWrapper<GridHandle>().lambda()
                    .eq(GridHandle::getStatus, 1)
                    .orderByDesc(GridHandle::getCreateTime)
                    .last("limit 1"));
            gridEvents.setStatus(3);
            gridEvents.setExecutor("" + ogridHandle.getCreateUser());

            GridHandle gridHandle = new GridHandle();
            gridHandle.setBeUserId(ogridHandle.getCreateUser());
            gridHandle.setStatus(1);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateUser(userId);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(2);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            String beUserName = userClient.baseUserInfoById(ogridHandle.getCreateUser()).getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "将" + gridEvents.getTitle() + "事件派遣至网格员" + beUserName);
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        }

    }

    @Override
    public void beVerified(JSONObject query) {
        //1是驳回，2是通过
        Integer type = query.getInteger("type");
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
     //   Long beUserId = query.getLongValue("beUserId");

        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
//        JSONObject jsonObject = userClient.currentUser();
//
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Date date = new Date();
        if (type == 1) {    //驳回
            gridEvents.setStatus(3);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(3);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(0);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(4);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "办结审核驳回了对" + gridEvents.getTitle() + "事件的处理结果");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setStatus(6);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(3);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setCreateUser(userId);
            gridHandle.setReviewStatus(1);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(4);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("网格长" + userName + "办结审核通过了对" + gridEvents.getTitle() + "事件的处理结果");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);

            /*{              //派遣
                JSONObject jsonT = new JSONObject();
                jsonT.put("beUserId", beUserId);
                jsonT.put("detailsId", detailsId);
                jsonT.put("type", 1);
                jsonT.put("userTy", 0);
                distribute(jsonT);
            }*/
        }
    }

    @Override
    public void endEvents(JSONObject query) {

//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
        Date date = new Date();
        gridEvents.setStatus(8);

        GridHandle gridHandle = new GridHandle();
        gridHandle.setStatus(5);
        gridHandle.setGridEventsId(detailsId);
        gridHandle.setCreateUser(userId);
        gridHandle.setRemark(remark);
        gridHandle.setCreateTime(date);

        GridRecord gridRecord = new GridRecord();
        gridRecord.setCreateTime(date);
        gridRecord.setStatus(3);
//        String userName = jsonObject.getJSONObject("data").getString("realName");
//        gridRecord.setContent("管理员" + userName + "办结审核通过了" + gridEvents.getTitle() + "事件");
        gridRecord.setCreateUser(userId);
        gridRecord.setKeyWords(gridEvents.getTitle());
        gridRecord.setGridEventsId(detailsId);
        gridEventsMapper.updateById(gridEvents);
        gridHandleMapper.insert(gridHandle);
        gridRecordMapper.insert(gridRecord);
    }

    @Override
    public void hierarchical(JSONObject query) {
        String remark = query.getString("remark");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Date date = new Date();
        gridEvents.setStatus(11);
        gridEvents.setExecutor("" + userId);

        GridHandle gridHandle = new GridHandle();
        gridHandle.setBeUserId(-1L);
        gridHandle.setStatus(1);
        gridHandle.setRemark(remark);
        gridHandle.setGridEventsId(detailsId);
        gridHandle.setCreateUser(userId);
        gridHandle.setCreateTime(date);

        GridRecord gridRecord = new GridRecord();
        gridRecord.setCreateTime(date);
        gridRecord.setStatus(2);
//        String userName = jsonObject.getJSONObject("data").getString("realName");
//        gridRecord.setContent("网格长" + userName + "分级上报" + gridEvents.getTitle() + "事件，上报至后台管理员");
        gridRecord.setCreateUser(userId);
        gridRecord.setKeyWords(gridEvents.getTitle());
        gridRecord.setGridEventsId(detailsId);
        gridEventsMapper.updateById(gridEvents);
        gridHandleMapper.insert(gridHandle);
        gridRecordMapper.insert(gridRecord);
    }

    @Override
    public void examineHierarchical(JSONObject query) {
        //1是驳回，2是通过
        Integer type = query.getInteger("type");
        String remark = query.getString("remark");
        String gridId = query.getString("gridId");
        String beUserId = query.getString("beUserId");
        Long detailsId = query.getLongValue("detailsId");
        GridEvents gridEvents = gridEventsMapper.selectById(detailsId);
//        JSONObject jsonObject = userClient.currentUser();
//        Long userId = jsonObject.getJSONObject("data").getLongValue("id");
        Long userId =securityUtil.getCurrUser().getId();
        Date date = new Date();
        if (type == 1) {    //驳回
            gridEvents.setStatus(2);

            GridHandle gridHandle = new GridHandle();
            gridHandle.setStatus(3);
            gridHandle.setGridEventsId(detailsId);
            gridHandle.setReviewStatus(0);
            gridHandle.setCreateUser(userId);
            gridHandle.setRemark(remark);
            gridHandle.setCreateTime(date);

            GridRecord gridRecord = new GridRecord();
            gridRecord.setCreateTime(date);
            gridRecord.setStatus(4);
//            String userName = jsonObject.getJSONObject("data").getString("realName");
//            gridRecord.setContent("管理员" + userName + "办结审核驳回了对" + gridEvents.getTitle() + "事件的处理结果");
            gridRecord.setCreateUser(userId);
            gridRecord.setKeyWords(gridEvents.getTitle());
            gridRecord.setGridEventsId(detailsId);
            gridEventsMapper.updateById(gridEvents);
            gridHandleMapper.insert(gridHandle);
            gridRecordMapper.insert(gridRecord);
        } else {
            gridEvents.setGridId(gridId);
            gridEventsMapper.updateById(gridEvents);
            JSONObject jsonT = new JSONObject();
            jsonT.put("beUserId", beUserId);
            jsonT.put("detailsId", detailsId);
            jsonT.put("type", 1);
            jsonT.put("userTy", 0);
            distribute(jsonT);
        }
    }

    @Override
    public List<Map<String, Object>> getUserGrid() {
        List<Map<String, Object>> result = new ArrayList<>();
//        JSONObject jsonObject = userClient.currentUser();
//        String gridId = jsonObject.getJSONObject("data").getString("gridId");
        String gridId =securityUtil.getCurrUser().getId().toString();
        if (StringUtils.isNotEmpty(gridId)) {
            String[] split = gridId.split(",");
            for (String s : split) {
                Map<String, Object> map = new HashMap<>();
                GridTree.Record gridInfomation = gridTree.getGridInfomation(s);
                map.put("gridId", gridInfomation.getId());
                map.put("gridName", gridInfomation.getName());
                map.put("streetName", gridInfomation.getStreetName());
                map.put("streetId", gridInfomation.getStreetId());
                map.put("communityName", gridInfomation.getCommunityName());
                map.put("communityId", gridInfomation.getCommunityId());
                result.add(map);
            }
        }
        return result;
    }
}
