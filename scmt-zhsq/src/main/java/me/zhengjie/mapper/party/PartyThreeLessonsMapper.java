package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.PartyThreeLessonsListDto;
import me.zhengjie.dto.ReportThreeLessons;
import me.zhengjie.entity.party.PartyThreeLessons;

/**
 * <p>
 * 三学一课 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Repository
@Mapper
public interface PartyThreeLessonsMapper extends BaseMapper<PartyThreeLessons> {

    /**
     * 三学一课列表
     * 
     * @param page
     * @param partyCategoryId
     * @return
     */
    IPage<PartyThreeLessonsListDto> getListByCategory(IPage<PartyThreeLessons> page,
                                                      @Param("partyCategoryId") Integer partyCategoryId);

    List<ReportThreeLessons> getReportThreeLessons(@Param("partyCommitteeIds") List<Long> committeeIds);

}
