package me.zhengjie.entity.party;

import java.util.Calendar;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import me.zhengjie.entity.BaseEntity;
import me.zhengjie.enums.PartyThreeLessonsCategoryEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 三学一课
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PartyThreeLessons对象", description = "三学一课")
public class PartyThreeLessons extends BaseEntity {

    @ApiModelProperty(value = "议题")
    private String  topic;

    @ApiModelProperty(value = "发起人")
    private String  initiator;

    @ApiModelProperty(value = "课程时间")
    private Date    lessonsTime;

    @ApiModelProperty(value = "课程地点")
    private String  lessonsSite;

    @ApiModelProperty(value = "课程要求")
    private String  lessonsRequire;

    @ApiModelProperty(value = "党委名称")
    private String  partyCommitteeName;

    @ApiModelProperty(value = "党委id")
    private Long    partyCommitteeId;

    @ApiModelProperty(value = "分类id")
    private Integer partyCategoryId;

    @ApiModelProperty(value = "支部名称")
    private String  partyBranchName;

    @ApiModelProperty(value = "支部id")
    private Long    partyBranchId;

    @ApiModelProperty(value = "会议结果")
    private String  meetingResults;

    @TableField(exist=false)
    @ApiModelProperty(value = "分类名称")
    private String  partyCategoryName;
    
    
    @TableField(exist=false)
    @ApiModelProperty(value = "课程状态：1 未开始 ，2 已结束")
    private Integer  lessonsStatus;

    public String getPartyCategoryName() {
        Integer categoryId = this.getPartyCategoryId();
        return PartyThreeLessonsCategoryEnum.getNameByCode(categoryId);
    }
    public Integer getLessonsStatus() {
        return Calendar.getInstance().getTime().after(this.getLessonsTime())?2:1;
    }
}
