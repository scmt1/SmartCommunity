package me.zhengjie.service.gridevents.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.mapper.gridevents.GridRecordMapper;
import me.zhengjie.entity.gridevents.GridRecord;
import me.zhengjie.service.gridevents.IGridRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GridRecordServiceImpl extends ServiceImpl<GridRecordMapper, GridRecord> implements IGridRecordService {

    private final  GridRecordMapper gridRecordMapper;


    @Override
    public void add(GridRecord gridRecord) {
        gridRecordMapper.insert(gridRecord);
    }

    @Override
    public void modify(GridRecord gridRecord) {
        gridRecordMapper.updateById(gridRecord);
    }

    @Override
    public GridRecord loadOne(Integer gridRecordId) {
        return gridRecordMapper.selectById(gridRecordId);
    }

    @Override
    public void delete(Integer gridRecordDetailsId) {
        gridRecordMapper.deleteById(gridRecordDetailsId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        return gridRecordMapper.loadAllByQuery(page,query);
    }
}
