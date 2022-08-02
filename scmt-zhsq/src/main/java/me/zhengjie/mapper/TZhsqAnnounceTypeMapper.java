package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.TZhsqAnnounceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2021-08-25
 */
@Mapper
public interface TZhsqAnnounceTypeMapper extends BaseMapper<TZhsqAnnounceType> {

    /**
     * 通过Id查询公告类型
     * @param id
     * @return
     */
    TZhsqAnnounceType selectAnnounceTypeById(@Param(value = "id")String id );
}
