package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.zhengjie.entity.DeptType;
import me.zhengjie.mapper.DeptTypeMapper;
import me.zhengjie.service.IDeptTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DeptTypeServiceImpl extends ServiceImpl<DeptTypeMapper, DeptType> implements IDeptTypeService {

    private final DeptTypeMapper deptTypeMapper;


    @Override
    public boolean add(DeptType deptType) {
        int insert = deptTypeMapper.insert(deptType);
        return insert > 0;
    }

    @Override
    public IPage<DeptType> loadAllByQuery(JSONObject query) {
        Page<DeptType> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        QueryWrapper<DeptType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(DeptType::getName, query.getString("queryStr"));
        return deptTypeMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<JSONObject> loadAll4Select() {
        return deptTypeMapper.loadAll4Select();
    }

    @Override
    public boolean delete(Integer deptTypeId) {
        DeptType deptType = deptTypeMapper.selectById(deptTypeId);
        int i = deptTypeMapper.deleteById(deptTypeId);
        return i==1;
    }

    @Override
    public boolean modify(DeptType deptType) {
        int i = deptTypeMapper.updateById(deptType);
        return i==1;
    }
}
