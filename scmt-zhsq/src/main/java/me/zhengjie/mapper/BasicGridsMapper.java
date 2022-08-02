package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.BasicHousingEstate;
import me.zhengjie.entity.BasicPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-20
 */
@Repository
@Mapper
public interface BasicGridsMapper extends BaseMapper<BasicGrids> {

    /**
     * 根据人员Id查询所管理的网格
     * @param personId
     * @return
     */
    List<BasicGrids> queryMyManagedGridsList(@Param("personId")String personId);

    /**
     * 根据网格Id查询该网格下个网格员、建筑、房屋等。。
     * @param gridsId
     * @return
     */
    List<Map<String, Object>> queryGridsOwnInformation(@Param("gridsId")String gridsId);

    /**
     * 查询该网格下有多少个网格员
     * @param gridsId
     * @return
     */
    List<Map<String, Object>> queryGridmanList(@Param("gridsId")String gridsId);

    /**
     *
     * 使用MP提供的Wrapper条件构造器，
     *
     * @param userWrapper
     * @return
     */
    List<BasicGrids> selectByMyWrapper(@org.apache.ibatis.annotations.Param(Constants.WRAPPER) Wrapper<BasicGrids> userWrapper);

    /**
     * 查询该网格区域树
     * @return
     */
	List<Map<String, Object>> queryAllGridsTree();

    /**
     * 根据Id查询网格
     * @param id
     * @return
     */
    BasicGrids getGridById(@Param("id")String id);
}
