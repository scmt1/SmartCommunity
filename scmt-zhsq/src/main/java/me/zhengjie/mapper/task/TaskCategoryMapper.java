package me.zhengjie.mapper.task;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.task.TaskCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Repository
@Mapper
public interface TaskCategoryMapper extends BaseMapper<TaskCategory> {

    @Select("select department_ids from task_category tc where tc.property_id = #{propertyId} and tc.code = 'JCSJ-SBWH'")
    String selectDepartmentIds(@Param("propertyId") Integer propertyId);
}
