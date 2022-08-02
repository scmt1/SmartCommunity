package me.zhengjie.service.party;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.party.PartyLessonsMember;

/**
 * <p>
 * 三会一课与参会人关系 服务类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
public interface IPartyLessonsMemberService extends IService<PartyLessonsMember> {
    
    void deletByLessonsId(Long partyLessonsId);

}
