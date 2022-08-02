package me.zhengjie.entity.gridevents;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.BaseEntity;
import me.zhengjie.entity.TComponentmanagement;

import javax.persistence.Column;
import java.util.List;

/**
  * @Description:    事件类型
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@ApiModel
@TableName("grid_events_type")
public class EventsType extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父id")
    private String pid;

    @ApiModelProperty("关联流程id")
    private String procDefId;

    @ApiModelProperty("关联流程名")
    private String procDefName;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String label;

    @ApiModelProperty("子节点")
    @TableField(exist = false)
    private List<EventsType> children;

}
