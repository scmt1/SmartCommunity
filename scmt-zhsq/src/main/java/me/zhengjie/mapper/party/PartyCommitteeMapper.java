package me.zhengjie.mapper.party;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.dto.PartyCommitteeDto;
import me.zhengjie.dto.ReportPartyOrganization;
import me.zhengjie.entity.party.PartyCommittee;

/**
 * <p>
 * 党委 Mapper 接口
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-12
 */
@Repository
@Mapper
public interface PartyCommitteeMapper extends BaseMapper<PartyCommittee> {

    /**
     * 党委列表
     * 
     * @param page
     * @param name
     * @param jurisdictionId
     * @param secretary
     * @param phoneNumber
     * @return
     */
    List<PartyCommitteeDto> getList(IPage<PartyCommitteeDto> page, @Param("name") String name,
                                    @Param("jurisdictionId") Long jurisdictionId, @Param("secretary") String secretary,
                                    @Param("phoneNumber") String phoneNumber);

    List<ReportPartyOrganization> getReportPartyOrganization();

}
