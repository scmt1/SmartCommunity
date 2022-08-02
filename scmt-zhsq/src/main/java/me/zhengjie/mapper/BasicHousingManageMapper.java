package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.BasicHousingManage;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
public interface BasicHousingManageMapper extends BaseMapper<BasicHousingManage> {
    /**
     * 查询所有
     * @return
     */
    List<Object> selectAll();
}
