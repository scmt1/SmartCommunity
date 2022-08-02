package me.zhengjie.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.entity.task.OrderFollowUp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface OrderFollowUpMapper extends BaseMapper<OrderFollowUp> {

    List<OrderFollowUp> selectByQuery(@Param("gridId") String gridId);
}
