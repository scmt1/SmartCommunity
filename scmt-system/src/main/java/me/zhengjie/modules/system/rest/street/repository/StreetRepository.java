package me.zhengjie.modules.system.rest.street.repository;

import me.zhengjie.system.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StreetRepository extends JpaRepository<Dept, Long>, JpaSpecificationExecutor<Dept> {

    /**
     * 查询街道
     * @return id,name
     */
    @Query(value = "select * from dept where pid in (select id from dept where pid = 1)",nativeQuery = true)
    List<Dept> findStreetById();

    /**
     * 根据街道id查询社区
     */
    @Query(value = "select * from dept where pid = ?1",nativeQuery = true)
    List<Dept> findCommunityById(Long pid);
}
