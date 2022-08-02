package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TZhsqCommunityCadres;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 社区干部档案 Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TZhsqCommunityCadresMapper extends BaseMapper<TZhsqCommunityCadres> {
    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TZhsqCommunityCadres> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqCommunityCadres> userWrapper);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    IPage<TZhsqCommunityCadres> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqCommunityCadres> userWrapper, Page page);
}
