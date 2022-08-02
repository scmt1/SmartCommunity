package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TArmedequipment;
import me.zhengjie.entity.TEarlywarning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TArmedequipmentMapper extends BaseMapper<TArmedequipment> {

    /**
     * 分页查询数据
     *
     * @param wrapper
     * @param page
     * @return
     */
    IPage<TArmedequipment> selectTArmedequipmentPageList(@Param(Constants.WRAPPER) QueryWrapper<TArmedequipment> wrapper, Page page);

}
