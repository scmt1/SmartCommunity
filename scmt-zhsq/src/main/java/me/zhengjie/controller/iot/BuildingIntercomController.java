package me.zhengjie.controller.iot;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.iot.BuildingIntercom;
import me.zhengjie.service.iot.IBuildingIntercomService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 楼宇对讲管理
 * </p>
 *
 * @author ly
 * @since 2019-05-06
 */
@RestController
@RequestMapping("/api/buildingIntercom")
@AllArgsConstructor
public class BuildingIntercomController {

    private final IBuildingIntercomService buildingIntercomService;

    private final SecurityUtil securityUtil;

    /**
     * 添加楼宇对讲信息
     * @param buildingIntercom
     * @return
     */
    @PostMapping("/add")

    public Result<Object>addBuildingIntercom(@RequestBody BuildingIntercom buildingIntercom) {
        buildingIntercomService.add(buildingIntercom);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除楼宇对讲信息
     * @param buildingIntercomId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object>deleteBuildingIntercom(Integer buildingIntercomId) {
        buildingIntercomService.delete(buildingIntercomId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改楼宇对讲信息
     * @param buildingIntercom
     * @return
     */
    @PostMapping("/modify")
    public Result<Object>modifyBuildingIntercom(@RequestBody BuildingIntercom buildingIntercom) {
        buildingIntercomService.modify(buildingIntercom);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个楼宇对讲
     * @param buildingIntercomId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object>loadOne(Integer buildingIntercomId) {
        BuildingIntercom buildingIntercom = buildingIntercomService.loadOne(buildingIntercomId);
        return ResultUtil.data(buildingIntercom);
    }

    /**
     * 获取所有楼宇对讲
     * @param query
     * @return
     */
    @PostMapping("/loadAllByQuery")
    public Result<Object>loadAllByQuery(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        IPage<Map<String, Object>> buildingIntercoms = buildingIntercomService.loadAllByQuery(query);
        return ResultUtil.data(buildingIntercoms);
    }


}
