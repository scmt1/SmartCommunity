package me.zhengjie.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TComponentmanagement;
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
public interface TComponentmanagementMapper extends BaseMapper<TComponentmanagement> {

    //分页查询部件
    Page<TComponentmanagement> loadByPage(@Param("page") Page page, @Param("query") JSONObject query);

    //查询全部部件
    List<TComponentmanagement> loadNotPage(@Param("query") JSONObject query);

    // 查询部件
    List<TComponentmanagement> selectDeptTreeByParentId(@Param("parentId") String parentId);

    /**
     * 通过Id查询图片地址
     * @param id
     * @return
     */
    TComponentmanagement selectImgUrlById(@Param(value = "id")String id );
}
