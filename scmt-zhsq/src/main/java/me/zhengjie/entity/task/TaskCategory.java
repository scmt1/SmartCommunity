package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * @Description:    任务类别
  * @Author:         ljj
  * @CreateDate:     2019/5/6 13:38
  */
@Data
@ApiModel
public class TaskCategory {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("任务编码")
    private String code;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("总分")
    private Double countScore;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("状态，0是正常，1是删除")
    private Integer status;

    @TableField(exist = false)
    private List<TaskScoreDetails> taskScoreDetails = new ArrayList<TaskScoreDetails>();

}
