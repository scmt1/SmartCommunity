package me.zhengjie.entity.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 摄像头信息
 */
@Data
public class CameraInfo {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("楼盘id")
    private String propertyId;

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("1是共有，2是私有")
    private Integer isPrivate;

    @ApiModelProperty("设备头名称，即位置")
    private String name;

    @ApiModelProperty("地址")
    private String url;

    @ApiModelProperty("坐标")
    private String position;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("应用密钥")
    private String appKey;

    @ApiModelProperty("应用")
    private String appSecret;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("发布人")
    private Integer publisher;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("0是启用，1是停用")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty("网格名")
    private String gridName;

}
