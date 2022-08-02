//package me.zhengjie.service.party.impl;
//
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import me.zhengjie.dto.MyMessageDto;
//import me.zhengjie.dto.UserInfomation;
//import me.zhengjie.entity.party.PartyMember;
//import me.zhengjie.entity.party.PartyMessage;
//import me.zhengjie.enums.MessageTypeEnum;
//import me.zhengjie.mapper.party.PartyMessageMapper;
//import me.zhengjie.service.party.IPartyMemberService;
//import me.zhengjie.service.party.IPartyMessageService;
//
//import lombok.AllArgsConstructor;
//
///**
// * <p>
// *  服务实现类
// * </p>
// *
// * @author puyongyan
// * @since 2020-08-14
// */
//@AllArgsConstructor
//@Service
//public class PartyMessageServiceImpl extends ServiceImpl<PartyMessageMapper, PartyMessage>
//                                     implements IPartyMessageService {
//
//    private final IPartyMemberService partyMemberService;
//
//    private final PartyMessageMapper  partyMessageMapper;
//
//    @Override
//    public IPage<MyMessageDto> getMyMessageList(UserInfomation userInfomation, Integer current, Integer size) {
//        String idNumber = userInfomation.getIdNumber();
//        IPage<MyMessageDto> page = new Page<>(current, size);
//        if (StringUtils.isEmpty(idNumber)) {
//            return page;
//        }
//        PartyMember partyMember = partyMemberService.getPartyMemberByIdCardNo(idNumber);
//        if (partyMember == null) {
//            return page;
//        }
//        List<MyMessageDto> records = partyMessageMapper.getMyMessageList(page, partyMember.getPartyCommitteeId(),
//            partyMember.getPartyBranchId());
//        page.setRecords(records);
//        return page;
//    }
//
//    @Override
//    public void addMessage(String title, MessageTypeEnum messageType, Long partyBranchId, Long partyCommitteeId) {
//        PartyMessage message = new PartyMessage();
//        message.setContent(title);
//        message.setMsgType(messageType.getCode());
//        message.setCreateTime(new Date());
//        message.setPartyBranchId(partyBranchId);
//        message.setPartyCommitteeId(partyCommitteeId);
//        save(message);
//    }
//}
