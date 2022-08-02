package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TZhsqGridMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 网格员 Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Mapper
public interface YWTZhsqGridMemberMapper  extends BaseMapper<TZhsqGridMember>{

    /**
     * 网格队伍统计
     * @param gridMember
     * @param page
     * @return
     */
    IPage<Map<String,Object>> getStatisticsData( @Param("gridMember") TZhsqGridMember gridMember, Page page);
}
