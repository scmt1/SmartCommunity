package me.zhengjie.service.party;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.PartyThreeLessonsDto;
import me.zhengjie.dto.PartyThreeLessonsListDto;
import me.zhengjie.dto.ReportThreeLessons;
import me.zhengjie.entity.party.PartyThreeLessons;
import me.zhengjie.req.PartyThreeLessonsSeq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
public interface IPartyThreeLessonsService extends IService<PartyThreeLessons> {

    /**
     * 
     * @param topic  议题
     * @param partBranchId  支部id
     * @param partyCommitteeId  党委id
     * @param partyCategoryId   分类id
     * @param current       当前页
     * @param size          每页大小
     * @return
     */
    IPage<PartyThreeLessons> getList(String topic, Long partBranchId, Long partyCommitteeId, Integer partyCategoryId,
                                     Integer current, Integer size);

    /**
     * 编辑三学一课
     * 
     * @param id
     * @param partyThreeLessonsReq
     */
    void edit(Long userId, Long id, PartyThreeLessonsSeq partyThreeLessonsReq);

    /**
     * 获取三学一做详细数据
     * @param id
     * @return
     */
    PartyThreeLessonsDto detail(Long id);

    /**
     * 
     * 根据三学一会分类id查询
     * @param partyCategoryId
     * @param current
     * @param size
     * @return
     */
    IPage<PartyThreeLessonsListDto> getListByCategory(Integer partyCategoryId, Integer current, Integer size);

    /**
     * 新增加三学一课
     * @param partyThreeLessonsReq
     */
    void add(Long userId, PartyThreeLessonsSeq partyThreeLessonsReq);

    List<ReportThreeLessons> reportThreeLessons(List<Long> committeeIds);

}
