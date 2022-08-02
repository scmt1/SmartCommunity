package me.zhengjie.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务明细表
 * @author liujingjie
 */
@Data
@ApiModel
public class TaskDetails  {

    @ApiModelProperty("数据id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("主单id")
    private Long masterId;

    @ApiModelProperty("单号")
    private String code;

    @ApiModelProperty("无效时间")
    private Date invalidDate;

    @ApiModelProperty("类别 （TaskCategory）")
    private Integer categoryId;

    @ApiModelProperty("提交人")
    private Integer submitUserId;

    @ApiModelProperty("派单时间")
    private Date assignDate;

    @ApiModelProperty("派单人")
    private Integer operateUserId;

    @ApiModelProperty("周期 1：固定；2：零时")
    private Integer cycleFixed;

    @ApiModelProperty("紧急程度")
    private Integer urgentType;

    @ApiModelProperty("建议执行时间")
    private String repairTime;

    @ApiModelProperty("实际工时")
    private BigDecimal workingHours;

    @ApiModelProperty("")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("接单时间")
    private Date receiveDate;

    @ApiModelProperty("执行人")
    private String executeUserId;

    @ApiModelProperty("详情")
    private String remark;

    @ApiModelProperty("保修照片")
    private String repairPhotos;

    @ApiModelProperty("完成图片，每次用%隔开")
    private String completePhotos;

    @ApiModelProperty("提交时间，yyyy-MM-dd HH:mm:ss,多个用,隔开")
    private String completeDate;

    @ApiModelProperty("完成描述，每次用#%&隔开")
    private String completeRemark;

    @ApiModelProperty("审核人ids，多个用，隔开")
    private String reviewUserId;

    @ApiModelProperty("审核时间，yyyy-MM-dd HH:mm:ss,多个用,隔开")
    private String reviewDate;

    @TableField(exist = false)
    @ApiModelProperty("驳回操作人,用,隔开")
    private String overruleUserIds;

    @ApiModelProperty("驳回理由说明，多个用#%&隔开")
    private String overruleRemark;

    @ApiModelProperty("系统评分id")
    private String systemScoreIds;

    @ApiModelProperty("选择条件评分id")
    private String taskScoreIds;

    @ApiModelProperty("评价照片")
    private String evaluationPhotos;

    @ApiModelProperty("评分星级")
    private Double evaluationStarLevel;

    @ApiModelProperty("管理者评价")
    private Integer evaluationUserId;

    @ApiModelProperty("评价")
    private String evaluationRemark;

    @ApiModelProperty("总分")
    private Double totalScore;

    @ApiModelProperty("管理者评价时间")
    private Date evaluateDate;

    @TableField(exist = false)
    private MultipartFile[] files;

    @ApiModelProperty("网格id")
    private String gridId;

}
