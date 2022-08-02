//package me.zhengjie.service.party;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.IService;
//import me.zhengjie.dto.MyMessageDto;
//import me.zhengjie.dto.UserInfomation;
//import me.zhengjie.entity.party.PartyMessage;
//import me.zhengjie.enums.MessageTypeEnum;
//
///**
// * <p>
// *  服务类
// * </p>
// *
// * @author puyongyan
// * @since 2020-08-14
// */
//public interface IPartyMessageService extends IService<PartyMessage> {
//
//    /**
//     * 我的消息
//     * @param userInfomation
//     * @param current
//     * @param size
//     * @return
//     */
//    IPage<MyMessageDto> getMyMessageList(UserInfomation userInfomation, Integer current, Integer size);
//
//    /**
//     * 添加消息
//     * 
//     * @param title
//     */
//    public void addMessage(String title, MessageTypeEnum messageType, Long partyBranchId, Long partyCommitteeId);
//
//}
