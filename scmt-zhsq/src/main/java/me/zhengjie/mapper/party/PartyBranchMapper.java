package me.zhengjie.mapper.party;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.party.PartyBranch;

/**
 * <p>
 * 党支部 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Repository
@Mapper
public interface PartyBranchMapper extends BaseMapper<PartyBranch> {

}
