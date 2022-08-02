package me.zhengjie.service.party;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.AppPartyMassessListDto;
import me.zhengjie.dto.ReportPartyMasses;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyMasses;
import me.zhengjie.req.PartyMassesReq;

/**
 * <p>
 * 党群  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyMassesService extends IService<PartyMasses> {

    /**
     * 党群活动列表
     * 
     * @param title
     * @param partBranchId
     * @param partyCommitteeId
     * @param partyCategoryId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyMasses> getList(String title, Long partBranchId, Long partyCommitteeId, Integer partyCategoryId,
                               Integer current, Integer size);

    /**
     * 修改党群活动
     * 
     * @param id
     * @param userId
     * @param partyMassesReq
     */
    void edit(Long id, Long userId, PartyMassesReq partyMassesReq);

    /**
     * 新增党群活动
     * 
     * @param userId
     * @param partyMassesReq
     */
    void add(Long userId, PartyMassesReq partyMassesReq);

    /**
     * 取消
     * 
     * @param id
     */
    void cancel(Long id);

    /**
     * 党群活动详情
     * 
     * @param id
     * @return
     */
    void getPartyMassessDetailById(HttpServletResponse response, Long id) throws Exception;

    /**
     * 党群列表
     * 
     * @param userId
     * @param current
     * @param size
     * @return
     */
    IPage<AppPartyMassessListDto> getPartyMassessList(UserInfomation userInfomation, Integer current, Integer size);

    List<ReportPartyMasses> getPartyMassesStatistics();

}
