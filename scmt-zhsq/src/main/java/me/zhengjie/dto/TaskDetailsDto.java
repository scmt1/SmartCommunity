package me.zhengjie.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/*
* 用于物业app的数据展示返回
*
* */
public class TaskDetailsDto {

    @ApiModelProperty("任务名称")
    private String taskDetailsName;

    @ApiModelProperty("单号")
    private String code;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("紧急程度")
    private String urgentTypeName;

    @ApiModelProperty("内容")
    private String remark;

    @ApiModelProperty("发布人")
    private String publisher;

    @ApiModelProperty("任务照片")
    private String photos;

    @ApiModelProperty("任务类型id")
    private Integer categoryId;

    @ApiModelProperty("驳回操作人,用,隔开")
    private String overruleUserIds;

    @ApiModelProperty("驳回详情,用,隔开")
    private String overruleRemark;

    @ApiModelProperty("任务完成照片")
    private String completePhotos;

    @ApiModelProperty("任务完成详情")
    private String completeRemark;

    @ApiModelProperty("评价详情")
    private String evaluationRemark;

    @ApiModelProperty("任务完成时间")
    private String completeDate;

    @ApiModelProperty("部门ids")
    private String departmentIds;

    public String getOverruleRemark() {
        return overruleRemark;
    }

    public void setOverruleRemark(String overruleRemark) {
        this.overruleRemark = overruleRemark;
    }

    public String getEvaluationRemark() {
        return evaluationRemark;
    }

    public void setEvaluationRemark(String evaluationRemark) {
        this.evaluationRemark = evaluationRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(String departmentIds) {
        this.departmentIds = departmentIds;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getCompleteRemark() {
        return completeRemark;
    }

    public void setCompleteRemark(String completeRemark) {
        this.completeRemark = completeRemark;
    }

    public String getCompletePhotos() {
        return completePhotos;
    }

    public void setCompletePhotos(String completePhotos) {
        this.completePhotos = completePhotos;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getOverruleUserIds() {
        return overruleUserIds;
    }

    public void setOverruleUserIds(String overruleUserIds) {
        this.overruleUserIds = overruleUserIds;
    }

    public String getTaskDetailsName() {
        return taskDetailsName;
    }

    public void setTaskDetailsName(String taskDetailsName) {
        this.taskDetailsName = taskDetailsName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUrgentTypeName() {
        return urgentTypeName;
    }

    public void setUrgentTypeName(String urgentTypeName) {
        this.urgentTypeName = urgentTypeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
