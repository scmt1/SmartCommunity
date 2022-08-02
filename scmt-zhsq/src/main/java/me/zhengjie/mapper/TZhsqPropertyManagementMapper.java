package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.TZhsqGridMember;
import me.zhengjie.entity.TZhsqPropertyManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.TZhsqVolunteerActivity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
@Repository
public interface TZhsqPropertyManagementMapper extends BaseMapper<TZhsqPropertyManagement> {
    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TZhsqPropertyManagement> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqPropertyManagement> userWrapper);
}
