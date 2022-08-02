package me.zhengjie.mapper;

import me.zhengjie.entity.BasicUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-08-11
 */
public interface BasicUnitMapper extends BaseMapper<BasicUnit> {

    /***
     * 查询当前建筑设施下的单元
     * @param archiveId
     * @return
     */
    List<BasicUnit> queryBasicUnitListByArchiveId(@Param("archiveId")String archiveId);

    /**
     * 查询最大的单元数
     * @param id
     * @return
     */
    List<Map<String, Object>> getMaxUnit(@Param("id")String id);

    /**
     * 查询单元中楼层数和层户数
     * @param id
     * @param name
     * @return
     */
    List<Map<String, Object>> getFloorAndDoor(@Param("id")String id, @Param("name")Integer name);
}
