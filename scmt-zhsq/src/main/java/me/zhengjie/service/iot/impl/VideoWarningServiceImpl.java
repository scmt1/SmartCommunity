package me.zhengjie.service.iot.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.entity.iot.CameraInfo;
import me.zhengjie.entity.iot.VideoWarning;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.DictionaryMapper;
import me.zhengjie.mapper.iot.CameraInfoMapper;
import me.zhengjie.mapper.iot.VideoWarningMapper;
import me.zhengjie.service.iot.IVideoWarningService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

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
public class VideoWarningServiceImpl extends ServiceImpl<VideoWarningMapper, VideoWarning> implements IVideoWarningService {

    private final VideoWarningMapper videoWarningMapper;

    private final SecurityUtil securityUtil;

    private final GridTree gridTree;

    private final DictionaryMapper dictionaryMapper;

    private final CameraInfoMapper cameraInfoMapper;


    @Override
    public void add(VideoWarning videoWarning) {
        videoWarning.setCreateDate(new Date());
        videoWarningMapper.insert(videoWarning);
    }

    @Override
    public void delete(Integer videoWarningId) {
        videoWarningMapper.deleteById(videoWarningId);
    }

    @Override
    public void modify(VideoWarning videoWarning) {
        videoWarningMapper.updateById(videoWarning);
    }

    @Override
    public VideoWarning loadOne(Integer videoWarningId) {
        return videoWarningMapper.selectById(videoWarningId);
    }

    @Override
    public IPage<VideoWarning> loadAllByQuery(JSONObject query) {
        Page<VideoWarning> page = new Page<>(query.getLongValue("pageNum"), query.getLongValue("pageSize"));
        QueryWrapper<VideoWarning> videoWarningQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty( query.getString("queryStr"))){
            videoWarningQueryWrapper.lambda().like(VideoWarning::getChannelName,query.getString("queryStr"));
        }
        if (StringUtils.isNotEmpty( query.getString("gridId"))){
            videoWarningQueryWrapper.lambda().like(VideoWarning::getGridId,query.getString("gridId"));
        }
        if (query.getInteger("propertyId") !=null){
            videoWarningQueryWrapper.lambda().like(VideoWarning::getPropertyId,query.getInteger("propertyId"));
        }
        if (query.getInteger("alarmType") !=null){
            videoWarningQueryWrapper.lambda().like(VideoWarning::getAlarmType,query.getInteger("alarmType"));
        }

        IPage<VideoWarning> mapIPage = videoWarningMapper.selectPage(page, videoWarningQueryWrapper);
        for (VideoWarning record : mapIPage.getRecords()) {
            String alarmTypeName = dictionaryMapper.loadOneByFieldNameAndNumber(record.getAlarmType(), "alarmType");
            record.setAlarmTypeName(alarmTypeName);
            CameraInfo cameraInfo = cameraInfoMapper.selectById(record.getId());
            if (cameraInfo !=null){
                record.setCameraInfoName(cameraInfo.getName());
            }
            GridTree.Record gridInfomation = gridTree.getGridInfomation(record.getGridId());
            record.setGridName(gridInfomation.getName());

        }
        return mapIPage;
    }

}
