package me.zhengjie.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.dao.entity.TFormGenerator;
import me.zhengjie.domain.ColumnInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单自动 生成 Mapper 接口
 * </p>
 *
 * @author zenghu
 * @since 2020-11-25
 */
public interface ActivityMapper extends BaseMapper<TFormGenerator> {

    /**
     * 根据数据字段新增流程表单数据
     * @param model  数据
     * @param tableName 表
     * @return
     */
    public int AddActivity(@Param("model") Map<String,Object> model,@Param("tableName") String tableName);

    /**
     * 根据数据字段编辑流程表单数据
     * @param model  数据
     * @param tableName 表
     * @return
     */
    public int UpdateActivity(@Param("model") Map<String,Object> model,@Param("tableName") String tableName);

    /**
     * 根据Id 查询流程表单数据
     * @param id
     * @param tableName
     * @return
     */
    public Map<String ,Object> getActivityById(@Param("id")String id,@Param("tableName")String tableName ,@Param("columns") List<ColumnInfo> columns);
}
