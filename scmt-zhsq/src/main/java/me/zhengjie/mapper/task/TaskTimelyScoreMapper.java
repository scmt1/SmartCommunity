package me.zhengjie.mapper.task;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.task.TaskTimelyScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author ljj
 */
@Repository
@Mapper
public interface TaskTimelyScoreMapper extends BaseMapper<TaskTimelyScore> {

    @Select("select sum(grab_order_first_score + complete_order_score)\n" +
            "from task_timely_score ")
    int getTotalTimelyScoreByPropertyId();
}
