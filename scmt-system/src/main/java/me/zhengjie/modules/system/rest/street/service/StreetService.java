package me.zhengjie.modules.system.rest.street.service;

import me.zhengjie.system.domain.Dept;

import java.util.List;

public interface StreetService {

    /**
     * 查询街道
     * @return /
     */
    List<Dept> findStreetById();

    /**
     * 根据街道id查询社区
     * @return /
     */
    List<Dept> findCommunityById(Long pid);
}
