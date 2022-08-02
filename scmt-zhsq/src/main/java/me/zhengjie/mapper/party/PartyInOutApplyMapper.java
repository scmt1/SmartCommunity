package me.zhengjie.mapper.party;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.party.PartyInOutApply;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-14
 */
@Mapper
public interface PartyInOutApplyMapper extends BaseMapper<PartyInOutApply> {

    @Delete("Delete From party_in_out_apply WHERE id_card_no=#{idCardNo}")
    void deleteIdCardNo(@Param("idCardNo") String idCardNo);

}
