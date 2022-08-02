package me.zhengjie.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author luozhen
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TLog对象", description="")
public class TLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    private String createBy;

    private LocalDateTime createTime;

    private Integer delFlag;

    private String updateBy;

    private LocalDateTime updateTime;

    private Integer costTime;

    private String ip;

    private String ipInfo;

    private Integer logType;

    private String name;

    private String requestParam;

    private String requestType;

    private String requestUrl;

    private String username;


}
