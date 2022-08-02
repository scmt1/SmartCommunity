package me.zhengjie.service.party.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.PartyWorkListDto;
import me.zhengjie.entity.party.PartyWork;
import me.zhengjie.common.BusinessException;
import me.zhengjie.mapper.party.PartyWorkMapper;
import me.zhengjie.req.PartyWorkReq;
import me.zhengjie.service.party.IPartyWorkService;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 党务公开 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Service
@AllArgsConstructor
public class PartyWorkServiceImpl extends ServiceImpl<PartyWorkMapper, PartyWork> implements IPartyWorkService {

    private final PartyWorkMapper      partyWorkMapper;

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public IPage<PartyWork> getList(String title, Long partBranchId, Long partyCommitteeId, Integer partyCategoryId,
                                    Integer current, Integer size) {
        IPage<PartyWork> page = new Page<>(current, size);
        QueryWrapper<PartyWork> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(title), "title", title);
        queryWrapper.eq(partBranchId != null && partBranchId != 0, "part_branch_id", partBranchId);
        queryWrapper.eq(partyCommitteeId != null && partyCommitteeId != 0, "party_committee_id", partyCommitteeId);
        queryWrapper.eq(partyCategoryId != null && partyCategoryId != 0, "party_category_id", partyCategoryId);
        queryWrapper.orderByDesc("create_time");
        IPage<PartyWork> pageResult = partyWorkMapper.selectPage(page, queryWrapper);
        return pageResult;
    }

    @Override
    public IPage<PartyWorkListDto> getList(Long partyCommitteeId, Long branchId, Integer partyCategoryId,
                                           Integer current, Integer size) {
        IPage<PartyWorkListDto> page = new Page<>(current, size);
        List<PartyWorkListDto> records = partyWorkMapper.getPartyWorkList(partyCommitteeId, branchId, partyCategoryId,
            page);
        page.setRecords(records);
        return page;
    }

    @Override
    public IPage<PartyWork> getPartyList(Integer current, Integer size) {
        IPage<PartyWork> page = new Page<>(current, size);
        List<PartyWork> records = partyWorkMapper.getPartyList(page);
        page.setRecords(records);
        return page;
    }

    @Override
    //    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, PartyWorkReq partyWorkReq) {

        String title = partyWorkReq.getTitle();
        Long partyCommitteeId = partyWorkReq.getPartyCommitteeId();
        Long partBranchId = partyWorkReq.getPartBranchId();
        PartyWork partyWork = new PartyWork();
        partyWork.setContent(partyWorkReq.getContent());
        partyWork.setCreateTime(new Date());
        partyWork.setCreateUser(userId);
        partyWork.setPartBranchId(partBranchId);
        partyWork.setPartyCategoryId(partyWorkReq.getPartyCategoryId());
        partyWork.setPartyCategoryName(partyWorkReq.getPartyCategoryName());
        partyWork.setPartBranchName(partyWorkReq.getPartBranchName());
        partyWork.setPartyCommitteeId(partyCommitteeId);
        partyWork.setPartyCommitteeName(partyWorkReq.getPartyCommitteeName());
        partyWork.setTitle(title);
        super.save(partyWork);
        //        partyMessageService.addMessage(title, MessageTypeEnum.PARTYWORK, partBranchId, partyCommitteeId);
    }

    @Override
    public void edit(Long id, Long userId, PartyWorkReq partyWorkReq) {
        PartyWork partyWork = getById(id);
        if (partyWork == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        partyWork.setContent(partyWorkReq.getContent());
        partyWork.setUpdateTime(new Date());
        partyWork.setUpdateUser(userId);
        partyWork.setPartBranchId(partyWorkReq.getPartBranchId());
        partyWork.setPartBranchName(partyWorkReq.getPartBranchName());
        partyWork.setPartyCategoryId(partyWorkReq.getPartyCategoryId());
        partyWork.setPartyCategoryName(partyWorkReq.getPartyCategoryName());
        partyWork.setPartyCommitteeId(partyWorkReq.getPartyCommitteeId());
        partyWork.setPartyCommitteeName(partyWorkReq.getPartyCommitteeName());
        partyWork.setTitle(partyWorkReq.getTitle());
        super.updateById(partyWork);
    }

    public void getPartyWorkDetai(HttpServletResponse response, Long id) throws TemplateNotFoundException,
                                                                         MalformedTemplateNameException, ParseException,
                                                                         IOException, TemplateException {
        PartyWork partyWork = getById(id);
        if (partyWork == null) {
            throw new BusinessException(ResultCode.FAILURE);
        }
        //        PartyWorkDetail partyWorkDetail = new PartyWorkDetail();
        //        partyWorkDetail.setContent(partyWorkDetail.getContent());
        //        partyWorkDetail.setCreateTime(partyWorkDetail.getCreateTime());
        //        partyWorkDetail.setId(id);
        //        partyWorkDetail.setTitle(partyWorkDetail.getTitle());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("Title", partyWork.getTitle());
        model.put("DateTime", me.zhengjie.common.DateUtils.formatDate(partyWork.getCreateTime()));
        model.put("content", partyWork.getContent());
        Template t2 = freeMarkerConfigurer.getConfiguration().getTemplate("guide.ftl");
        String content2 = FreeMarkerTemplateUtils.processTemplateIntoString(t2, model);
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().print(content2);
    }

}
