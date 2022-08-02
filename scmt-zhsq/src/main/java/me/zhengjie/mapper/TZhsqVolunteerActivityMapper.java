package me.zhengjie.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.entity.TZhsqVolunteerActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 志愿者活动 Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-23
 */
public interface TZhsqVolunteerActivityMapper extends BaseMapper<TZhsqVolunteerActivity> {
    /**
     * 通过志愿者Id查询志愿者活动
     * @param vtId 志愿者Id
     * @return
     */
    List<TZhsqVolunteerActivity> selectActivityByVtId(@Param(value = "vtId") String vtId);


}
