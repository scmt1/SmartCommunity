package me.zhengjie.entity.iot;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.entity.BaseEntity;

/**
 * 停车道闸信息
 */
@Data
public class ParkingBarrier extends BaseEntity {

    @ApiModelProperty("网格id")
    private String gridId;

    @ApiModelProperty("楼盘id")
    private String propertyId;

    @ApiModelProperty("道闸名称")
    private String name;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("0是启用，1是停用")
    private Integer status;


}
