package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TEarlywarning;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TEarlywarningMapper extends BaseMapper<TEarlywarning> {

    /**
     * 分页查询数据
     *
     * @param wrapper
     * @param page
     * @return
     */
    IPage<TEarlywarning> selectTEarlywarningPageList(@Param(Constants.WRAPPER) QueryWrapper<TEarlywarning> wrapper, Page page);

}
