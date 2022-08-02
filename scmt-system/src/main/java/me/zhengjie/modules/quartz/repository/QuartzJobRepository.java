package me.zhengjie.modules.quartz.repository;

import me.zhengjie.modules.quartz.domain.QuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2019-01-07
 */
public interface QuartzJobRepository extends JpaRepository<QuartzJob,Long>, JpaSpecificationExecutor<QuartzJob> {

    /**
     * 查询启用的任务
     * @return List
     */
    @Query(value = "SELECT  * FROM quartz_job WHERE is_pause = 0", nativeQuery = true)
    List<QuartzJob> findByIsPauseIsFalse();

    /**
     * 根据 “行” 模糊查询现有银行
     * @param
     * @return
     */
    @Query(value = "SELECT * FROM job WHERE name LIKE CONCAT('%',?1,'%')", nativeQuery = true)
    List<QuartzJob>  findByFirstnameLisk(String listName);
}
