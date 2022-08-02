package me.zhengjie.entity.gridevents;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.zhengjie.entity.BaseEntity;
import me.zhengjie.entity.BasicGrids;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
  * @Description:    网格事件表-基本事件
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GridEvents对象")
public class GridEvents implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("创建人")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("更新人")
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否已删除，1是删除，0是正常")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty("数据来源，1是居民app，2是街道app")
    private Integer dataFrom;

    @ApiModelProperty("事件类型")
    private Integer eventsTypeId;

    @ApiModelProperty("紧急程度")
    private Integer urgentType;

    @ApiModelProperty("所属网格id")
    private String gridId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发生日期")
    private Date happenDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束日期")
    private Date endDate;

    @ApiModelProperty("事件编号")
    private String code;

    @ApiModelProperty("状态：1.是发现上报，2.是处理反馈，3.是事件接收，4.是事件审核，5.办结审核，6.异议审核，7.事件结束，8.分级上报，9.办理完成")
    private Integer status;

    @ApiModelProperty("执行人/当前处理人(流程审批人)")
    private String executor;

    @ApiModelProperty("标题/事件名称")
    private String title;

    @ApiModelProperty("事件地址")
    private String address;

    @ApiModelProperty("地图标注")
    private String position;

    @ApiModelProperty("提交内容/事件描述")
    private String remark;

    @ApiModelProperty("音频地址/相关语音")
    private String audioMediaAddress;

    @ApiModelProperty("照片-视频地址")
    private String mediaAddress;

    @ApiModelProperty("涉及人数")
    private Integer persons;

    @ApiModelProperty("涉及单位")
    private String unitsInvolved;

    @ApiModelProperty("规模")
    private Integer scale;

    @ApiModelProperty("事件等级")
    private Integer eventsGrade;

    @ApiModelProperty("当事人名")
    private String partyName;

    @ApiModelProperty("上报类型")
    private Integer reportType;

    @ApiModelProperty("网格员")
    private String gridMember;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("要求/办结时间")
    private Date requirementDate;

    @ApiModelProperty("联系电话")
    private String telephone;

    @ApiModelProperty("办结人(自办自结，处理人)")
    private Long handleSelf ;

    @ApiModelProperty("自结意见")
    private String handleSelfOpinion ;

    @ApiModelProperty("是否是自办自结 0不是 1是")
    private Integer isHandleSelf ;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("流程实例id")
    private String procInstId;

    @ApiModelProperty("事件类型实体")
    @TableField(exist = false)
    private EventsType eventsTypes;



    @ApiModelProperty("记录状态")
    @TableField(exist = false)
    private Integer recordStatus;

    @ApiModelProperty("网格名称")
    @TableField(exist = false)
    private String gridName;

    @ApiModelProperty("事件类型名")
    @TableField(exist = false)
    private String eventsTypeName;

    @ApiModelProperty("紧急程度名")
    @TableField(exist = false)
    private String urgentTypeName;

    @ApiModelProperty("用户id")
    @TableField(exist = false)
    private Long userId;

    @ApiModelProperty("规模")
    @TableField(exist = false)
    private String scaleName;

    @ApiModelProperty("责任人名")
    @TableField(exist = false)
    private String executorName;

    @ApiModelProperty("事件等级")
    @TableField(exist = false)
    private String eventsGradeName;

    @ApiModelProperty("事件记录")
    @TableField(exist = false)
    private List<GridRecord> gridRecords = new ArrayList<>();

    @ApiModelProperty("事件处理列表")
    @TableField(exist = false)
    private List<Map<String, Object>> gridHandles = new ArrayList<>();

    @ApiModelProperty("社区名")
    @TableField(exist = false)
    private String communityName;

    @ApiModelProperty("街道名")
    @TableField(exist = false)
    private String streetName;

    @ApiModelProperty("网格")
    @TableField(exist = false)
    private BasicGrids basicGrid;

    @ApiModelProperty("统计 全部")
    @TableField(exist = false)
    private Integer allCount;

    @ApiModelProperty("统计 待处理")
    @TableField(exist = false)
    private Integer waitCount;

    @ApiModelProperty("统计 已办结")
    @TableField(exist = false)
    private Integer finishCount;

    @ApiModelProperty("统计 已超时")
    @TableField(exist = false)
    private Integer overtimeCount;
}
