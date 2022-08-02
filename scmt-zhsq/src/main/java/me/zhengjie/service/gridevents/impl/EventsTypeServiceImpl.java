package me.zhengjie.service.gridevents.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.entity.TComponentmanagement;
import me.zhengjie.mapper.gridevents.EventsTypeMapper;
import me.zhengjie.mapper.gridevents.GridEventsMapper;
import me.zhengjie.entity.gridevents.EventsType;
import me.zhengjie.entity.gridevents.GridEvents;
import me.zhengjie.service.gridevents.IEventsTypeService;
import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EventsTypeServiceImpl extends ServiceImpl<EventsTypeMapper, EventsType> implements IEventsTypeService {

    private final  EventsTypeMapper eventsTypeMapper;

    private final GridEventsMapper gridEventsMapper;


    @Override
    @InitBaseInfo(type = InitBaseType.ADD )
    public void add(EventsType eventsType) {
        Integer integer = eventsTypeMapper.selectCount(new QueryWrapper<EventsType>().lambda()
                .eq(EventsType::getName, eventsType.getName()));
        if (integer>0){
            throw new BusinessErrorException("事件类型名称重复");
        }
        eventsTypeMapper.insert(eventsType);
    }

    @Override
    @InitBaseInfo(type = InitBaseType.UPDATE )
    public void modify(EventsType eventsType) {

        eventsTypeMapper.updateById(eventsType);
    }

    @Override
    public EventsType loadOne(Integer eventsTypeId) {
        return eventsTypeMapper.selectById(eventsTypeId);
    }

    @Override
    public void delete(Integer eventsTypeId) {
        Integer integer = gridEventsMapper.selectCount(new QueryWrapper<GridEvents>().lambda()
                .eq(GridEvents::getEventsTypeId, eventsTypeId));
        if (integer>0){
            EventsType eventsType = eventsTypeMapper.selectById(eventsTypeId);
            eventsType.setIsDeleted(1);
            eventsTypeMapper.updateById(eventsType);
        }else {
            eventsTypeMapper.deleteById(eventsTypeId);
        }

    }

    @Override
    public IPage<EventsType> loadAllByQuery(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        return eventsTypeMapper.loadAllByQuery(page,query);
    }

    @Override
    public List<EventsType> loadByQuery() {
        List<EventsType> eventsTypes = eventsTypeMapper.selectList(new QueryWrapper<EventsType>().lambda()
                .notIn(EventsType::getIsDeleted, 1));
        return eventsTypes;
    }

    @Override
    public IPage<EventsType> queryEventsTypeTreeByPage(JSONObject query) {
        Page<EventsType> page = new Page<>(query.getLongValue("pageNum"),query.getLongValue("pageSize"));

        Page<EventsType> eventsTypePage = eventsTypeMapper.loadByPage(page,  query);
        if(eventsTypePage!=null && eventsTypePage.getRecords()!=null && eventsTypePage.getRecords().size()>0)
            eventsTypePage.getRecords().forEach(dept -> {
                if ( dept.getId()!=0){
                    List<EventsType> eventsType = eventsTypeMapper.selectTreeByParentId(""+dept.getId());
                    dept.setChildren(eventsType);
                    getChildren(eventsType);
                }
            });
        return eventsTypePage;
    }

    @Override
    public List<EventsType> queryEventsTypeTreeNotPage(JSONObject query) {
        List<EventsType> EventsTypeNotPage = eventsTypeMapper.loadNotPage(query);
        if(EventsTypeNotPage!=null){
            EventsTypeNotPage.forEach(dept -> {
                if ( dept.getId()!=0){
                    List<EventsType> EventsTypes = eventsTypeMapper.selectTreeByParentId(""+dept.getId());
                    dept.setChildren(EventsTypes);
                    getChildren(EventsTypes);
                }
            });
        }
        return EventsTypeNotPage;
    }

    public void getChildren( List<EventsType> eventsType){
        if(eventsType!=null && eventsType.size()>0){
            eventsType.forEach(dept -> {
                if ( dept.getId()!=0){
                    List<EventsType> eventsTypeList = eventsTypeMapper.selectTreeByParentId(""+dept.getId());
                    if(eventsTypeList!=null && eventsTypeList.size()>0){
                        dept.setChildren(eventsTypeList);
                        getChildren(eventsTypeList);
                    }
                }
            });
        }

    }
}
