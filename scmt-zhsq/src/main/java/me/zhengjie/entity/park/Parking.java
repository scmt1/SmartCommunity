package me.zhengjie.entity.park;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dengjie
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Parking对象")
public class Parking implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;

    @ApiModelProperty(value = "停车场名称")
    private String            parkingName;

    @ApiModelProperty(value = "地址")
    private String            address;

    @ApiModelProperty(value = "网格id")
    private String            gridId;

    private String            streetId;

    private String            streetName;

    private String            communityId;

    private String            communityName;

    @ApiModelProperty(value = "网格名称")
    private String            gridName;

    @ApiModelProperty(value = "经度")
    private Double            longitude;

    @ApiModelProperty(value = "纬度")
    private Double            latitude;

    @ApiModelProperty("经纬度范围")
    private String            region;

    @ApiModelProperty(value = "备注")
    private String            remark;

    private LocalDateTime     createTime;

    private LocalDateTime     updateTime;

    @ApiModelProperty(value = "0、正常 1、删除")
    private Boolean           isDeleted;

}
