package me.zhengjie.mapper;

import me.zhengjie.entity.BasicResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-08-14
 */
public interface BasicResumeMapper extends BaseMapper<BasicResume> {

    /**
     * 查询当前社区干部最后一个履历
     * @param id
     * @return
     */
    BasicResume queryLastOneData(@Param("id") String id);

    /**
     * 根据社区干部Id,查询履历
     * @param personId
     * @return
     */
    List<BasicResume> queryBasicResumeListByPersonId(@Param("personId")String personId);

}
