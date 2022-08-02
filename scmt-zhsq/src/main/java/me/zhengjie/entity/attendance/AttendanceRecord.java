package me.zhengjie.entity.attendance;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.BaseEntity;

import java.util.Date;

/**
  * @Description:    考勤记录
  * @Author:         ly
  * @CreateDate:     2019/5/6 13:50
  */
@Data
@ApiModel
@TableName("grid_attendance_record")
public class AttendanceRecord extends BaseEntity {

    @ApiModelProperty("人员id")
    private Long userId;

    @ApiModelProperty("状态，1是上班，0是休假")
    private Integer status;

    @ApiModelProperty("时期")
    private Date date;

}
