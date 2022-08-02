package me.zhengjie.service.party;

import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.dto.Userinfo;
import me.zhengjie.entity.party.PartyInOutApply;
import me.zhengjie.req.ArchivesInReq;
import me.zhengjie.req.AuditReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-14
 */
public interface IPartyInOutApplyService extends IService<PartyInOutApply> {

    /**
     * 装入档案申请
     * 
     * @param userInfomation
     * @param archivesInReq
     */
    void inApply(UserInfomation userInfomation, ArchivesInReq archivesInReq);

    /**
     * 转出档案申请
     * @param userId
     * @param archivesInReq
     */
    void outApply(UserInfomation userInfomation);

    /**
     * 
     * 通过
     * @param userId
     * @param id
     */
    void adopt(Long userId, AuditReq audit, Long id);

    /**
     * 驳回
     * 
     * @param userId
     * @param id
     * @param rejectReason
     */
    void reject(Long userId, Long id, String rejectReason);

    /**
     * 分页查询申请列表
     * 
     * @param name  姓名
     * @param gender 性别
     * @param educationId  文化程度id
     * @param idCardNo   身份证号码
     * @param phoneNumber   电话号码
     * @param applyStatus   申请状态
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param current
     * @param size
     * @return
     */
    IPage<PartyInOutApply> getList(String name, Integer gender, Integer educationId, String idCardNo,
                                   String phoneNumber, Integer applyStatus, Date startTime, Date endTime,
                                   Integer current, Integer size);

    /**
     * 根据身份证号码查询
     * @param idCardNo
     * @return
     */
    PartyInOutApply getByIdCardNo(String idCardNo);

    Userinfo getUserInfo(UserInfomation userInfomation);

    void deleteIdCardNo(String idCardNo);

}
