package me.zhengjie.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 党务公开
 * </p>
 *
 * @author puyongyan
 * @since 2020-08-13
 */
@Data
public class PartyWorkListDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long              id;
    @ApiModelProperty(value = "标题")
    private String            title;

    @ApiModelProperty(value = "创建时间")
    private Date              createTime;

}
