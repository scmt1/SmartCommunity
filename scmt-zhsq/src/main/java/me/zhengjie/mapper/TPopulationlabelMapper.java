package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.GridDept;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.entity.TPopulationlabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface TPopulationlabelMapper extends BaseMapper<TPopulationlabel> {

    //查询标签件
    List<TPopulationlabel> loadNotPage(@Param("query") JSONObject query);

    // 查询标签
    List<TPopulationlabel> selectDeptTreeByParentId(@Param("parentId") String parentId);

}
