package me.zhengjie.service.party.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.PartyLearningCenterDto;
import me.zhengjie.dto.ReportLearn;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyLearnCenterEnroll;
import me.zhengjie.entity.party.PartyLearningCenter;
import me.zhengjie.entity.party.PartyMember;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyLearnCenterEnrollMapper;
import me.zhengjie.mapper.party.PartyLearningCenterMapper;
import me.zhengjie.mapper.party.PartyMemberMapper;
import me.zhengjie.req.PartyLearningCenterSeq;
import me.zhengjie.service.party.IPartyLearningCenterService;
import me.zhengjie.service.party.IPartyMemberService;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.AllArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */

@AllArgsConstructor
@Service
public class PartyLearningCenterServiceImpl extends ServiceImpl<PartyLearningCenterMapper, PartyLearningCenter>
                                            implements IPartyLearningCenterService {

    private final PartyLearningCenterMapper    partyLearningCenterMapper;

    private final IPartyMemberService          partyMemberService;

    private final PartyLearnCenterEnrollMapper partyLearnCenterEnrollMapper;

    private final FreeMarkerConfigurer         freeMarkerConfigurer;

    private final PartyMemberMapper            partyMemberMapper;

    @Override
    public IPage<PartyLearningCenter> getList(String title, Integer current, Integer size) {
        IPage<PartyLearningCenter> page = new Page<>(current, size);
        QueryWrapper<PartyLearningCenter> queryWrapper = new QueryWrapper<PartyLearningCenter>();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        queryWrapper.orderByDesc("create_time");
        return partyLearningCenterMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void add(Long userId, PartyLearningCenterSeq partyLearningCenterSeq) {
        PartyLearningCenter plc = seq2entity(partyLearningCenterSeq);
        plc.setCreateTime(Calendar.getInstance().getTime());
        plc.setCreateUser(userId);
        plc.setUpdateUser(userId);
        partyLearningCenterMapper.insert(plc);
    }

    private PartyLearningCenter seq2entity(PartyLearningCenterSeq seq) {
        PartyLearningCenter plc = new PartyLearningCenter();
        plc.setContent(seq.getContent());
        plc.setRemark(seq.getRemark());
        plc.setTitle(seq.getTitle());
        plc.setUpdateTime(Calendar.getInstance().getTime());
        return plc;

    }

    @Override
    public void edit(Long userId, Long id, PartyLearningCenterSeq partyLearningCenterSeq) {
        PartyLearningCenter ptc = seq2entity(partyLearningCenterSeq);
        ptc.setId(id);
        ptc.setUpdateUser(userId);
        partyLearningCenterMapper.updateById(ptc);

    }

    @Override
    public IPage<PartyLearningCenterDto> getLearnCenterList(Long userId, Integer current, Integer size) {

        IPage<PartyLearningCenterDto> page = new Page<>(current, size);
        List<PartyLearningCenterDto> list = partyLearningCenterMapper.getLearnCenterList(page, userId);
        page.setRecords(list);
        return page;
    }

    @Override
    public void complete(UserInfomation userInfomation, Long id) {
        PartyLearnCenterEnroll partyLearnCenterEnroll = new PartyLearnCenterEnroll();

        partyLearnCenterEnroll.setLearnCenterId(id);
        partyLearnCenterEnroll.setUserId(userInfomation.getId());
        partyLearnCenterEnroll.setUserName(userInfomation.getRealName());
        PartyMember member = partyMemberService.getPartyMemberByIdCardNo(userInfomation.getIdNumber());
        if (member != null) {
            partyLearnCenterEnroll.setUserCommitteeId(member.getPartyCommitteeId());
            partyLearnCenterEnroll.setUserCommitteeName(member.getPartyCommitteeName());
            partyLearnCenterEnroll.setUserName(member.getName());
        }
        partyLearnCenterEnroll.setUpdateTime(new Date());
        partyLearnCenterEnroll.setUserPhoneNumber(userInfomation.getPhone());
        QueryWrapper<PartyLearnCenterEnroll> query = new QueryWrapper<PartyLearnCenterEnroll>(partyLearnCenterEnroll);
        Integer selectCount = partyLearnCenterEnrollMapper.selectCount(query);
        if (selectCount > 0) {
            //已经完成过学习
            return;
        }
        partyLearnCenterEnrollMapper.insert(partyLearnCenterEnroll);
    }

    @Override
    public void getLearningDetail(HttpServletResponse response, Long id) throws TemplateNotFoundException,
                                                                         MalformedTemplateNameException, ParseException,
                                                                         IOException, TemplateException {

        PartyLearningCenter partyLearningCenter = getById(id);
        if (partyLearningCenter == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("Title", partyLearningCenter.getTitle());
        model.put("DateTime", me.zhengjie.common.DateUtils.formatDate(partyLearningCenter.getCreateTime()));
        model.put("content", partyLearningCenter.getContent());
        Template t2 = freeMarkerConfigurer.getConfiguration().getTemplate("guide.ftl");
        String content2 = FreeMarkerTemplateUtils.processTemplateIntoString(t2, model);
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().print(content2);

    }

    @Override
    public IPage<PartyLearnCenterEnroll> completeLearningMember(Long learnId, Integer current, Integer size) {

        IPage<PartyLearnCenterEnroll> page = new Page<>(current, size);
        QueryWrapper<PartyLearnCenterEnroll> queryWrapper = new QueryWrapper<PartyLearnCenterEnroll>();
        queryWrapper.eq("learn_center_id", learnId);
        queryWrapper.orderByAsc(false, "create_time");
        IPage<PartyLearnCenterEnroll> pageResult = partyLearnCenterEnrollMapper.selectPage(page, queryWrapper);
        return pageResult;
    }

    @Override
    public ReportLearn getLearnanalysis(List<Long> committeeIds) {
        ReportLearn reportLearn = new ReportLearn();
        reportLearn.setArticlesCount(0);
        reportLearn.setComplete(0d);
        QueryWrapper<PartyLearningCenter> queryWrapper = new QueryWrapper<PartyLearningCenter>();
        List<PartyLearningCenter> partyLearningCenters = partyLearningCenterMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(partyLearningCenters)) {
            return reportLearn;
        }
        Integer articlesCount = partyLearningCenters.size();
        reportLearn.setArticlesCount(articlesCount);
        QueryWrapper<PartyLearnCenterEnroll> wrapper = new QueryWrapper<PartyLearnCenterEnroll>();

        if (!CollectionUtils.isEmpty(committeeIds)) {
            wrapper.in("user_committee_id", committeeIds);
        }
        List<PartyLearnCenterEnroll> partyLearnCenterEnrolls = partyLearnCenterEnrollMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(partyLearnCenterEnrolls)) {
            reportLearn.setComplete(0d);
            return reportLearn;
        }
        QueryWrapper<PartyMember> partyMember = new QueryWrapper<PartyMember>();
        if (!CollectionUtils.isEmpty(committeeIds)) {
            partyMember.in("party_committee_id", committeeIds);
        }
        List<PartyMember> partyMembers = partyMemberMapper.selectList(partyMember);
        if (CollectionUtils.isEmpty(partyMembers)) {
            reportLearn.setComplete(0d);
            return reportLearn;
        }
        int total = articlesCount * partyMembers.size();
        int learnCount = partyLearnCenterEnrolls.size();
        double complete = (double) learnCount / total * 100;
        reportLearn.setComplete(get2Double(complete));
        return reportLearn;
    }

    public static double get2Double(Double d) {
        if (d == null || d.doubleValue() == 0) {
            return 0d;
        }
        BigDecimal b = new BigDecimal(d);
        d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

}
