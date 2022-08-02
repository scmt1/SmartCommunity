package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.BasicHealthFile;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-08-04
 */
public interface BasicHealthFileMapper extends BaseMapper<BasicHealthFile> {
    /**
     * 根据personId查询最新的健康档案
     * @param personId
     * @return
     */
    BasicHealthFile getTopOne(@Param("personId")String personId);
}
