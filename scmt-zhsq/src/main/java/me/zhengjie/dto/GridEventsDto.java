package me.zhengjie.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.gridevents.EventsType;
import me.zhengjie.entity.gridevents.GridRecord;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
* 消息列表Dto
* */
@Data
public class GridEventsDto {

    @ApiModelProperty("数据id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("记录状态")
    private Integer recordStatus;

    @ApiModelProperty("数据来源，1是居民app，2是街道app")
    private Integer dataFrom;

    @ApiModelProperty("状态名")
    private Integer statusName;

    @ApiModelProperty("所属网格id")
    private String gridId;

    @ApiModelProperty("街道名")
    private String streetName;

    @ApiModelProperty("创建时间")
    private String createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发生时间")
    private Date happenDate;

    @ApiModelProperty("网格名称")
    private String gridName;

    @ApiModelProperty("事件编码")
    private String code;

    @ApiModelProperty("事件类型名")
    private String eventsTypeName;

    @ApiModelProperty("事件类型")
    private Integer eventsTypeId;

    @ApiModelProperty("紧急程度名")
    private String urgentTypeName;

    @ApiModelProperty("紧急程度")
    private Integer urgentType;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("联系电话")
    private String userPhone;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("事件地址")
    private String address;

    @ApiModelProperty("地址-经度")
    private Double longitude;

    @ApiModelProperty("地址-纬度")
    private Double latitude;

    @ApiModelProperty("提交内容")
    private String remark;

    @ApiModelProperty("社区名")
    private String communityName;

    @ApiModelProperty("音频地址")
    private String audioMediaAddress;

    @ApiModelProperty("照片-视频地址")
    private String mediaAddress;

    @ApiModelProperty("涉及人数")
    private Integer persons;

    @ApiModelProperty("涉及单位")
    private String unitsInvolved;

    @ApiModelProperty("规模")
    private String scaleName;

    @ApiModelProperty("责任人名")
    private String executorName;

    @ApiModelProperty("责任人电话")
    private String executorPhone;

    @ApiModelProperty("规模")
    private Integer scale;

    @ApiModelProperty("事件等级")
    private Integer eventsGrade;

    @ApiModelProperty("事件等级")
    private String eventsGradeName;

    @ApiModelProperty("当事人姓名")
    private String partyName;

    @ApiModelProperty("事件记录")
    private List<GridRecord> gridRecords = new ArrayList<>();

    @ApiModelProperty("事件处理列表")
    private List<Map<String, Object>> gridHandles = new ArrayList<>();

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("流程实例id")
    private String procInstId;

    @ApiModelProperty("执行者")
    private Long executor;

    @ApiModelProperty("事件类型实体")
    @TableField(exist = false)
    private List<EventsType> eventsTypeList = new ArrayList<>();

    @ApiModelProperty("流程定义id")
    @TableField(exist = false)
    private String procDefId;

}
