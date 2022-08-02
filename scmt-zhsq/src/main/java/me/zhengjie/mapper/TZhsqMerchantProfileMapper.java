package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.entity.TZhsqMerchantProfile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-21
 */
@Repository
public interface TZhsqMerchantProfileMapper extends BaseMapper<TZhsqMerchantProfile> {

    /**
     * 房屋类型统计
     * @param communityId
     * @return
     */
    List<Map<String, Object>> getHouseType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 建筑类型统计
     * @param communityId
     * @return
     */
    List<Map<String, Object>> getBuildingType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 商户类型统计
     * @param communityId
     * @return
     */
    List<Map<String, Object>> getMerchantType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 社会组织统计
     * @param communityId
     * @return
     */
    List<Map<String, Object>> getSocialType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     * 事件紧急程度统计
     * @param communityId
     * @return
     */
    List<Map<String, Object>> getUrgentType(@Param("communityId") String communityId, @Param("gridId") String gridId);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TZhsqMerchantProfile> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<TZhsqMerchantProfile> userWrapper);


}
