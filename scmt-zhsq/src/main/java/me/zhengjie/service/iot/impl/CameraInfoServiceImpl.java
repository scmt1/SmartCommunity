package me.zhengjie.service.iot.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.entity.iot.CameraInfo;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.iot.CameraInfoMapper;
import me.zhengjie.service.iot.ICameraInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
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
public class CameraInfoServiceImpl extends ServiceImpl<CameraInfoMapper, CameraInfo> implements ICameraInfoService {

    private final CameraInfoMapper cameraInfoMapper;

    private final GridTree gridTree;

    private final SecurityUtil securityUtil;

    @Override
    public void add(CameraInfo cameraInfo) {
        Integer userId = securityUtil.getCurrUser().getId().intValue();
        cameraInfo.setCreateDate(new Date());
        cameraInfo.setPublisher(userId);
        if (cameraInfo.getStatus()==null){
            cameraInfo.setStatus(0);
        }
        cameraInfoMapper.insert(cameraInfo);
    }

    @Override
    public void delete(Integer cameraInfoId) {
        cameraInfoMapper.deleteById(cameraInfoId);
    }

    @Override
    public void modify(CameraInfo cameraInfo) {
        cameraInfoMapper.updateById(cameraInfo);
    }

    @Override
    public CameraInfo loadOne(Integer cameraInfoId) {
        return cameraInfoMapper.selectById(cameraInfoId);
    }

    @Override
    public IPage<CameraInfo> loadAllByQuery(JSONObject query) {
        Page<Map<String, Object>> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        IPage<CameraInfo> mapIPage = cameraInfoMapper.selectByQuery(page,query);
        for (CameraInfo record : mapIPage.getRecords()) {

            GridTree.Record gridInfomation = gridTree.getGridInfomation(record.getGridId());
            record.setGridName(gridInfomation.getName());
           /*     record.put("gridId", record.get("gridId").toString());
                record.put("communityName", gridInfomation.getCommunityName());
                record.put("communityId", gridInfomation.getCommunityId());
                record.put("streetName", gridInfomation.getStreetName());
                record.put("streetId", gridInfomation.getStreetId());*/
        }
        return mapIPage;
    }
}
