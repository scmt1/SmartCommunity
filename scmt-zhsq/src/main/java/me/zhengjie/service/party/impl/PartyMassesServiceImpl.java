package me.zhengjie.service.party.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.AppPartyMassessListDto;
import me.zhengjie.dto.PartyMassessDetail;
import me.zhengjie.dto.ReportPartyMasses;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyMasses;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.enums.MassesStatusEnum;
import me.zhengjie.enums.PartyCategoryEnum;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyMassesMapper;
import me.zhengjie.req.PartyMassesReq;
import me.zhengjie.service.party.IPartyMassesService;

import freemarker.template.Template;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党群  服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyMassesServiceImpl extends ServiceImpl<PartyMassesMapper, PartyMasses> implements IPartyMassesService {

    private final PartyMassesMapper      partyMassesMapper;

    private final PartyMemberServiceImpl partyMemberServiceImpl;

    private final FreeMarkerConfigurer   freeMarkerConfigurer;

    @Override
    public IPage<PartyMasses> getList(String title, Long partBranchId, Long partyCommitteeId, Integer partyCategoryId,
                                      Integer current, Integer size) {
        this.doUpdateMassesStatus();
        IPage<PartyMasses> page = new Page<>(current, size);
        QueryWrapper<PartyMasses> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(title), "title", title);
        queryWrapper.eq(partBranchId != null && partBranchId != 0, "part_branch_id", partBranchId);
        queryWrapper.eq(partyCommitteeId != null && partyCommitteeId != 0, "party_committee_id", partyCommitteeId);
        queryWrapper.eq(partyCategoryId != null && partyCategoryId != 0, "party_category_id", partyCategoryId);
        queryWrapper.orderByDesc("create_time");
        IPage<PartyMasses> pageResult = partyMassesMapper.selectPage(page, queryWrapper);
        return pageResult;
    }

    @Override
    public void edit(Long id, Long userId, PartyMassesReq partyMassesReq) {
        PartyMasses partyMasses = getById(id);
        if (partyMasses == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyMasses.setActivityDate(partyMassesReq.getActivityDate());
        partyMasses.setContent(partyMassesReq.getContent());
        partyMasses.setUpdateTime(new Date());
        partyMasses.setCreateUser(userId);
        partyMasses.setMassesStatus(MassesStatusEnum.ONGOING.getCode());
        partyMasses.setPartyBranchId(partyMassesReq.getPartyBranchId());
        partyMasses.setPartyBranchName(partyMassesReq.getPartyBranchName());
        partyMasses.setPartyCategoryId(partyMassesReq.getPartyCategoryId());
        partyMasses.setPartyCategoryName(PartyCategoryEnum.getNameByCode(partyMassesReq.getPartyCategoryId()));
        partyMasses.setPartyCommitteeId(partyMassesReq.getPartyCommitteeId());
        partyMasses.setPartyCommitteeName(partyMassesReq.getPartyCommitteeName());
        partyMasses.setTitle(partyMassesReq.getTitle());
        super.updateById(partyMasses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, PartyMassesReq partyMassesReq) {
        PartyMasses partyMasses = new PartyMasses();
        String title = partyMassesReq.getTitle();
        //        Long partyBranchId = partyMassesReq.getPartyBranchId();
        //        Long partyCommitteeId = partyMassesReq.getPartyCommitteeId();
        partyMasses.setActivityDate(partyMassesReq.getActivityDate());
        partyMasses.setContent(partyMassesReq.getContent());
        partyMasses.setCreateTime(new Date());
        partyMasses.setCreateUser(userId);
        partyMasses.setMassesStatus(MassesStatusEnum.ONGOING.getCode());
        partyMasses.setPartyBranchId(partyMassesReq.getPartyBranchId());
        partyMasses.setPartyBranchName(partyMassesReq.getPartyBranchName());
        partyMasses.setPartyCategoryId(partyMassesReq.getPartyCategoryId());
        partyMasses.setPartyCategoryName(PartyCategoryEnum.getNameByCode(partyMassesReq.getPartyCategoryId()));
        partyMasses.setPartyCommitteeId(partyMassesReq.getPartyCommitteeId());
        partyMasses.setPartyCommitteeName(partyMassesReq.getPartyCommitteeName());
        partyMasses.setTitle(title);
        super.save(partyMasses);
        //        partyMessageService.addMessage(title, MessageTypeEnum.PARTYMASSES, partyBranchId, partyCommitteeId);
    }

    @Override
    public void cancel(Long id) {
        QueryWrapper<PartyMasses> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("id", id);
        PartyMasses partyMasses = new PartyMasses();
        partyMasses.setMassesStatus(MassesStatusEnum.CANCEL.getCode());
        super.update(partyMasses, updateWrapper);
    }

    /**
     * 
     * @see me.zhengjie.service.party.IPartyMassesService#getPartyMassessDetailById(javax.servlet.http.HttpServletResponse, java.lang.Long)
     */
    @Override
    public void getPartyMassessDetailById(HttpServletResponse response, Long id) throws Exception {
        PartyMasses partyMasses = getById(id);
        if (partyMasses == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        PartyMassessDetail detail = new PartyMassessDetail();
        detail.setActivityDate(partyMasses.getActivityDate());
        detail.setContent(partyMasses.getContent());
        detail.setId(id);
        detail.setTitle(partyMasses.getTitle());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("Title", partyMasses.getTitle());
        model.put("DateTime", me.zhengjie.common.DateUtils.formatDate(partyMasses.getActivityDate()));
        model.put("content", partyMasses.getContent());
        Template t2 = freeMarkerConfigurer.getConfiguration().getTemplate("guide.ftl");
        String content2 = FreeMarkerTemplateUtils.processTemplateIntoString(t2, model);
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().print(content2);
    }

    @Override
    public IPage<AppPartyMassessListDto> getPartyMassessList(UserInfomation userInfomation, Integer current,
                                                             Integer size) {

        Long userId = userInfomation.getId();
        String idNumber = userInfomation.getIdNumber();
        PartyMember partyMember = partyMemberServiceImpl.getPartyMemberByIdCardNo(idNumber);
        IPage<AppPartyMassessListDto> page = new Page<>(current, size);
        if (partyMember == null) {
            return page;
        }
        Long partyBranchId = partyMember.getPartyBranchId();
        Long partyCommitteeId = partyMember.getPartyCommitteeId();
        Integer partyCategoryId = partyMember.getPartyCategoryId();
        List<AppPartyMassessListDto> records = partyMassesMapper.getPartyMassessList(page, userId, partyCommitteeId,
            partyBranchId, partyCategoryId);
        page.setRecords(records);
        return page;
    }

    /**
     * 更新活动状态
     */
    private void doUpdateMassesStatus() {
        QueryWrapper<PartyMasses> updateWrapper = new QueryWrapper<>();
        updateWrapper.lt("activity_date", new Date());
        PartyMasses partyMasses = new PartyMasses();
        partyMasses.setMassesStatus(MassesStatusEnum.END.getCode());
        super.update(partyMasses, updateWrapper);
    }

    @Override
    public List<ReportPartyMasses> getPartyMassesStatistics() {

        return partyMassesMapper.getpartyMassesStatistics();

    }

}
