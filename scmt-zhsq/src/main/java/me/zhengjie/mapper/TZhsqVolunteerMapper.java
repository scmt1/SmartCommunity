package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.entity.TZhsqVolunteer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 街道志愿者档案 Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TZhsqVolunteerMapper extends BaseMapper<TZhsqVolunteer> {

    /**
     * 根据社区id和网格id查询志愿者人数
     * @param communityId
     * @param gridId
     * @return
     */
    int selectByCommunityAndGrid(@Param(value = "communityId") String communityId, @Param(value = "gridId") String gridId);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TZhsqVolunteer> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqVolunteer> userWrapper);
}
