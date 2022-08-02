package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.TZhsqGridMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
public interface TZhsqGridMemberMapper extends BaseMapper<TZhsqGridMember> {

    /**
     * 查询当前网格下的网格长
     * @param gridId
     * @return
     */
    List<TZhsqGridMember> queryAllTZhsqGridManageListByGridId(@Param("gridId") String gridId);

    /**
     * 查询当前网格下的网格员
     * @param gridId
     * @return
     */

    List<TZhsqGridMember> queryAllTZhsqGridMemberListByGridId(@Param("gridId") String gridId);

    /**
     * 查询网格是否有网格长
     * @param s
     * @return
     */
    List<TZhsqGridMember> existsGrid(@Param("s") String s);

    /**
     * 根据网格id和社区id查询网格员
     * @param communityId
     * @param gridId
     * @return
     */
    int selectByCommunityIdAndGridId(@Param(value = "communityId") String communityId, @Param(value = "gridId") String gridId);


    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<TZhsqGridMember> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqGridMember> userWrapper);

    /**
     * 处理网格人员树
     * @return
     */
    List<Map<String,Object>> selectGridMemberTree();
}
