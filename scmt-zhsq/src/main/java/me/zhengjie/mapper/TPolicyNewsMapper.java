package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TPolicyNews;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TPolicyNewsMapper extends BaseMapper<TPolicyNews> {

    /**
     * 分页查询数据
     *
     * @param wrapper
     * @param page
     * @return
     */
    IPage<TPolicyNews> selectTPolicyNewsPageList(@Param(Constants.WRAPPER) QueryWrapper<TPolicyNews> wrapper, Page page);

}
