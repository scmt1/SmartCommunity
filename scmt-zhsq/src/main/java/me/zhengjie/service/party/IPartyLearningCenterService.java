package me.zhengjie.service.party;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.dto.PartyLearningCenterDto;
import me.zhengjie.dto.ReportLearn;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.entity.party.PartyLearnCenterEnroll;
import me.zhengjie.entity.party.PartyLearningCenter;
import me.zhengjie.req.PartyLearningCenterSeq;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyLearningCenterService extends IService<PartyLearningCenter> {

    /**
     * 分页查询学习中心数据
     * 
     * @param title   标题
     * @param current
     * @param size
     * @return
     */
    IPage<PartyLearningCenter> getList(String title, Integer current, Integer size);

    /**
     * 新增学习中心数据
     * 
     * @param partyLearningCenterSeq
     */
    void add(Long userId, PartyLearningCenterSeq partyLearningCenterSeq);

    /**
     * 编辑学习中心
     * 
     * @param id
     * @param partyLearningCenterSeq
     */
    void edit(Long userId, Long id, PartyLearningCenterSeq partyLearningCenterSeq);

    /**
     * app分页查询学习中心列表
     * 
     * @param current
     * @param size
     * @return
     */
    IPage<PartyLearningCenterDto> getLearnCenterList(Long userId, Integer current, Integer size);

    /**
     * 
     * 完成学习
     * @param userInfomation
     * @param id
     */
    void complete(UserInfomation userInfomation, Long id);

    /**
     * 获取
     * 
     * @param id
     * @param size 
     * @param current 
     * @return
     */
    IPage<PartyLearnCenterEnroll> completeLearningMember(Long id, Integer current, Integer size);

    void getLearningDetail(HttpServletResponse response, Long id) throws TemplateNotFoundException,
                                                                  MalformedTemplateNameException, ParseException,
                                                                  IOException, TemplateException;

    ReportLearn getLearnanalysis(List<Long> committeeIds);

}
