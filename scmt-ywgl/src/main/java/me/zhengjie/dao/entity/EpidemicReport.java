package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author dengjie
 * @since 2021-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EpidemicReport对象", description="")
public class EpidemicReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "姓名")
    private String personName;

    @ApiModelProperty(value = "身份证号")
    private String personIdCard;

    @ApiModelProperty(value = "电话号码")
    private String personMobile;

    @ApiModelProperty(value = "申报人身份")
    private String personIdentity;

    @ApiModelProperty(value = "从事行业")
    private String personIndustry;

    @ApiModelProperty(value = "备用手机号")
    private String spareMobile;

    @ApiModelProperty(value = "来源地")
    private String personOrigin;

    @ApiModelProperty(value = "来源地省")
    private String originProvince;

    @ApiModelProperty(value = "来源地市")
    private String originCity;

    @ApiModelProperty(value = "来源地区")
    private String originArea;

    @ApiModelProperty(value = "来源地详细地址")
    private String originAddress;

    @ApiModelProperty(value = "到泸详细地址")
    private String toAddress;

    @ApiModelProperty(value = "到泸街道")
    private String toStree;

    @ApiModelProperty(value = "到泸社区")
    private String toCommunity;

    @ApiModelProperty(value = "泸州本地联系人姓名")
    private String contactPersonName;

    @ApiModelProperty(value = "泸州本地联系人电话")
    private String contactPersonMobile;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "离开日期")
    private Date leaveDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "到泸日期")
    private Date arriveDate;

    @ApiModelProperty(value = "交通方式")
    private String transportation;

    @ApiModelProperty(value = "健康码颜色")
    private String healthCodeColor;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "承诺日期")
    private Date promiseDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加日期")
    private Date createTime;

    @ApiModelProperty(value = "省市区")
    private String provinceAndCity;
}
