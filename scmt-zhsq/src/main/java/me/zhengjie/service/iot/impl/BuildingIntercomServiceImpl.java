package me.zhengjie.service.iot.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.aop.annotation.InitBaseInfo;
import me.zhengjie.aop.type.InitBaseType;
import me.zhengjie.entity.iot.BuildingIntercom;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.iot.BuildingIntercomMapper;
import me.zhengjie.service.iot.IBuildingIntercomService;
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
public class BuildingIntercomServiceImpl extends ServiceImpl<BuildingIntercomMapper, BuildingIntercom> implements IBuildingIntercomService {

    private final BuildingIntercomMapper buildingIntercomMapper;

    private final GridTree gridTree;


    @Override
    @InitBaseInfo(type = InitBaseType.ADD )
    public void add(BuildingIntercom buildingIntercom) {
        Integer cods = buildingIntercomMapper.selectCount(new QueryWrapper<BuildingIntercom>().lambda()
                .eq(BuildingIntercom::getCode, buildingIntercom.getCode())
                .eq(BuildingIntercom::getIsDeleted, 0));
        if (cods>0){
            throw new BusinessErrorException("设备编号不可重复");
        }

        Integer names = buildingIntercomMapper.selectCount(new QueryWrapper<BuildingIntercom>().lambda()
                .eq(BuildingIntercom::getDoorName, buildingIntercom.getDoorName())
                .eq(BuildingIntercom::getIsDeleted, 0));
        if (names>0){
            throw new BusinessErrorException("云门禁名称不能重复");
        }
        buildingIntercomMapper.insert(buildingIntercom);
    }

    @Override
    public void delete(Integer buildingIntercomId) {
        buildingIntercomMapper.deleteById(buildingIntercomId);
    }

    @Override
    @InitBaseInfo(type = InitBaseType.UPDATE )
    public void modify(BuildingIntercom buildingIntercom) {
        Integer cods = buildingIntercomMapper.selectCount(new QueryWrapper<BuildingIntercom>().lambda()
                .eq(BuildingIntercom::getCode, buildingIntercom.getCode())
                .eq(BuildingIntercom::getIsDeleted, 0)
                .notIn(BuildingIntercom::getId,buildingIntercom.getId()));
        if (cods>0){
            throw new BusinessErrorException("设备编号不可重复");
        }

        Integer names = buildingIntercomMapper.selectCount(new QueryWrapper<BuildingIntercom>().lambda()
                .eq(BuildingIntercom::getDoorName,buildingIntercom.getDoorName())
                .eq(BuildingIntercom::getIsDeleted, 0)
                .notIn(BuildingIntercom::getId,buildingIntercom.getId()));
        if (names>0){
            throw new BusinessErrorException("云门禁名称不能重复");
        }
        buildingIntercomMapper.updateById(buildingIntercom);
    }

    @Override
    public BuildingIntercom loadOne(Integer buildingIntercomId) {
        return buildingIntercomMapper.selectById(buildingIntercomId);
    }

    @Override
    public IPage<Map<String, Object>> loadAllByQuery(JSONObject query) {
        List<Map> propertys = new ArrayList<>();
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        IPage<Map<String, Object>> mapIPage = buildingIntercomMapper.selectByQuery(page,query);
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
//                   if (record.get("propertyId").toString().equals(property.get("propertyId").toString())){
//                       record.put("propertyName",property.get("propertyName"));
//                   }
//                }
//            }
//        }catch (Exception e){
//            log.error(e.getMessage());
//        }
        return mapIPage;
    }
}
