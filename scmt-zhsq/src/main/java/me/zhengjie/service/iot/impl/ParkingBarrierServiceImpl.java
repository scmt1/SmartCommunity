package me.zhengjie.service.iot.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.entity.iot.ParkingBarrier;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.iot.ParkingBarrierMapper;
import me.zhengjie.service.iot.IParkingBarrierService;
import lombok.AllArgsConstructor;
import me.zhengjie.util.BusinessErrorException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Service
@AllArgsConstructor
public class ParkingBarrierServiceImpl extends ServiceImpl<ParkingBarrierMapper, ParkingBarrier> implements IParkingBarrierService {

    private final ParkingBarrierMapper parkingBarrierMapper;

    private final GridTree gridTree;


    @Override
    @InitBaseInfo(type = InitBaseType.ADD )
    public void add(ParkingBarrier parkingBarrier) {
        Integer cods = parkingBarrierMapper.selectCount(new QueryWrapper<ParkingBarrier>().lambda()
                .eq(ParkingBarrier::getCode, parkingBarrier.getCode())
                .eq(ParkingBarrier::getIsDeleted, 0));
        if (cods>0){
            throw new BusinessErrorException("设备编号不可重复");
        }

        Integer names = parkingBarrierMapper.selectCount(new QueryWrapper<ParkingBarrier>().lambda()
                .eq(ParkingBarrier::getName, parkingBarrier.getName())
                .eq(ParkingBarrier::getIsDeleted, 0));
        if (names>0){
            throw new BusinessErrorException("停车道闸名称不能重复");
        }
        parkingBarrierMapper.insert(parkingBarrier);
    }

    @Override
    public void delete(Integer parkingBarrierId) {
        parkingBarrierMapper.deleteById(parkingBarrierId);
    }

    @Override
    @InitBaseInfo(type = InitBaseType.UPDATE )
    public void modify(ParkingBarrier parkingBarrier) {
        Integer cods = parkingBarrierMapper.selectCount(new QueryWrapper<ParkingBarrier>().lambda()
                .eq(ParkingBarrier::getCode, parkingBarrier.getCode())
                .eq(ParkingBarrier::getIsDeleted, 0)
                .notIn(ParkingBarrier::getId,parkingBarrier.getId()));
        if (cods>0){
            throw new BusinessErrorException("设备编号不可重复");
        }

        Integer names = parkingBarrierMapper.selectCount(new QueryWrapper<ParkingBarrier>().lambda()
                .eq(ParkingBarrier::getName, parkingBarrier.getName())
                .eq(ParkingBarrier::getIsDeleted, 0)
                .notIn(ParkingBarrier::getId,parkingBarrier.getId()));
        if (names>0){
            throw new BusinessErrorException("停车道闸名称不能重复");
        }
        parkingBarrierMapper.updateById(parkingBarrier);
    }

    @Override
    public ParkingBarrier loadOne(Integer parkingBarrierId) {
        return parkingBarrierMapper.selectById(parkingBarrierId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        List<Map> propertys = new ArrayList<>();
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        IPage<Map<String, Object>> mapIPage = parkingBarrierMapper.selectByQuery(page,query);
        for (Map<String, Object> record : mapIPage.getRecords()) {
            record.put("propertyName","");
            GridTree.Record gridInfomation = gridTree.getGridInfomation(record.get("gridId").toString());
            record.put("gridName",gridInfomation.getName());
            record.put("communityId",gridInfomation.getCommunityId());
            record.put("communityName",gridInfomation.getCommunityName());
            record.put("streetId",gridInfomation.getStreetId());
            record.put("streetName",gridInfomation.getStreetName());
        }
//        try {
//            JSONObject allPropertys = communityClient.getAllPropertys();
//            if (allPropertys !=null && allPropertys.getString("code").equals("200") && allPropertys.getJSONArray("rows").size()>0){
//                propertys = allPropertys.getJSONArray("rows").toJavaList(Map.class);
//            }
//            for (Map<String, Object> record : mapIPage.getRecords()) {
//                for (Map property : propertys) {
//                    if (record.get("propertyId").toString().equals(property.get("propertyId").toString())){
//                        record.put("propertyName",property.get("propertyName"));
//                    }
//                }
//            }
//        }catch (Exception e){
//            log.error(e.getMessage());
//        }
        return mapIPage;
    }
}
