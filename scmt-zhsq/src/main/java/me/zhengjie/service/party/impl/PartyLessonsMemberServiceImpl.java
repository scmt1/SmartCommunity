package me.zhengjie.service.party.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.entity.party.PartyLessonsMember;
import me.zhengjie.mapper.party.PartyLessonsMemberMapper;
import me.zhengjie.service.party.IPartyLessonsMemberService;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 三会一课与参会人关系 服务实现类
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Service
@AllArgsConstructor
public class PartyLessonsMemberServiceImpl extends ServiceImpl<PartyLessonsMemberMapper, PartyLessonsMember>
                                           implements IPartyLessonsMemberService {

    private final PartyLessonsMemberMapper partyLessonsMemberMapper;

    @Override
    public void deletByLessonsId(Long partyLessonsId) {
        partyLessonsMemberMapper.deletByLessonsId(partyLessonsId);
    }

}
