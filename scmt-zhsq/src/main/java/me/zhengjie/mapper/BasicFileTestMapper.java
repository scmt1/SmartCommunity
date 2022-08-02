package me.zhengjie.mapper;

import me.zhengjie.entity.BasicFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-21
 */
@Mapper
public interface BasicFileTestMapper {

    /**
     * 查询所有
     * @return
     */
    List<BasicFile> selectAll();
}
