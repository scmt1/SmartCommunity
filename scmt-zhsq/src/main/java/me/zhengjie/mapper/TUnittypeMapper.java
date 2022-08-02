package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TUnittype;
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
public interface TUnittypeMapper extends BaseMapper<TUnittype> {

    //查询单位(树级)
    List<TUnittype> loadNotPage(@Param("query") JSONObject query);

    // 查询单位
    List<TUnittype> selectDeptTreeByParentId(@Param("parentId") String parentId);

}
