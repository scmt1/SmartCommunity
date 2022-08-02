package me.zhengjie.modules.system.rest.street.service.impl;

import me.zhengjie.modules.system.rest.street.repository.StreetRepository;
import me.zhengjie.modules.system.rest.street.service.StreetService;
import me.zhengjie.system.domain.Dept;
import me.zhengjie.system.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "street")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StreetServiceImpl implements StreetService {

    @Autowired
    private StreetRepository streetRepository;


    @Override
    public List<Dept> findStreetById() {
        List<Dept> streetById = streetRepository.findStreetById();
        for (int i =0;i<streetById.size();i++){
            streetById.get(i).setCreateTime(null);
            streetById.get(i).setEnabled(null);
            streetById.get(i).setPid(null);
            streetById.get(i).setRoles(null);
        }
        return streetById;
    }

    @Override
    public List<Dept> findCommunityById(Long pid) {
        List<Dept> communityById = streetRepository.findCommunityById(pid);
        for (int i =0;i<communityById.size();i++){
            communityById.get(i).setCreateTime(null);
            communityById.get(i).setEnabled(null);
            communityById.get(i).setPid(null);
            communityById.get(i).setRoles(null);
        }
        return communityById;
    }
}
