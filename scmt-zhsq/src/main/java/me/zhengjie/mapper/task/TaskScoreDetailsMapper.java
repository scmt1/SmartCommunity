package me.zhengjie.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.task.TaskScoreDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TaskScoreDetailsMapper extends BaseMapper<TaskScoreDetails> {
}
