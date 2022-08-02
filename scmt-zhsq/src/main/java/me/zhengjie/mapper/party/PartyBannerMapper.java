package me.zhengjie.mapper.party;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.party.PartyBanner;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Repository
@Mapper
public interface PartyBannerMapper extends BaseMapper<PartyBanner> {

}
