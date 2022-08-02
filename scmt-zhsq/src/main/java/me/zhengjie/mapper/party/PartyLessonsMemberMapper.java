package me.zhengjie.mapper.party;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.party.PartyLessonsMember;

/**
 * <p>
 * 三会一课与参会人关系 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyLessonsMemberMapper extends BaseMapper<PartyLessonsMember> {

    /**
    * 删除
    * 
    * @param partyLessonsId
    */

    void deletByLessonsId(@Param("partyLessonsId") Long partyLessonsId);

}
