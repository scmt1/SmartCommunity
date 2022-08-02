package me.zhengjie.controller.iot;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.iot.CameraInfo;
import me.zhengjie.service.iot.ICameraInfoService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 摄像头管理
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@RestController
@RequestMapping("/api/cameraInfo")
@AllArgsConstructor
public class CameraInfoController {

    private final ICameraInfoService cameraInfoService;

    private final SecurityUtil securityUtil;

    /**
     * 添加摄像头信息
     * @param cameraInfo
     * @return
     */
    @PostMapping("/add")

    public Result<Object>addCameraInfo(@RequestBody CameraInfo cameraInfo) {
        cameraInfoService.add(cameraInfo);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除摄像头信息
     * @param cameraInfoId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object>deleteCameraInfo(@RequestParam(name = "id")Integer cameraInfoId) {
        cameraInfoService.delete(cameraInfoId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改摄像头信息
     * @param cameraInfo
     * @return
     */
    @PostMapping("/modify")
    public Result<Object>modifyCameraInfo(@RequestBody CameraInfo cameraInfo) {
        cameraInfoService.modify(cameraInfo);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个摄像头
     * @param cameraInfoId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object>loadOne(@RequestParam(name = "id")Integer cameraInfoId) {
        CameraInfo cameraInfo = cameraInfoService.loadOne(cameraInfoId);
        return ResultUtil.data(cameraInfo);
    }

    /**
     * 获取所有摄像头
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
        IPage<CameraInfo> cameraInfos = cameraInfoService.loadAllByQuery(query);
        return ResultUtil.data(cameraInfos);
    }


}
