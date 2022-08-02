package me.zhengjie.controller.iot;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.entity.iot.VideoWarning;
import me.zhengjie.service.iot.IVideoWarningService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 视频预警管理
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@RestController
@RequestMapping("/api/videoWarning")
@AllArgsConstructor
public class VideoWarningController {

    private final IVideoWarningService videoWarningService;

    private final SecurityUtil securityUtil;

    /**
     * 添加视频预警信息
     * @param videoWarning
     * @return
     */
    @PostMapping("/add")

    public Result<Object>addVideoWarning(@RequestBody VideoWarning videoWarning) {
        videoWarningService.add(videoWarning);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 删除视频预警信息
     * @param videoWarningId
     * @return
     */
    @GetMapping("/delete")
    public Result<Object>deleteVideoWarning(Integer videoWarningId) {
        videoWarningService.delete(videoWarningId);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 修改视频预警信息
     * @param videoWarning
     * @return
     */
    @PostMapping("/modify")
    public Result<Object>modifyVideoWarning(@RequestBody VideoWarning videoWarning) {
        videoWarningService.modify(videoWarning);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    /**
     * 获取单个视频预警
     * @param videoWarningId
     * @return
     */
    @GetMapping("/loadOne")
    public Result<Object>loadOne(Integer videoWarningId) {
        VideoWarning videoWarning = videoWarningService.loadOne(videoWarningId);
        return ResultUtil.data(videoWarning);
    }

    /**
     * 获取所有视频预警
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
        IPage<VideoWarning> videoWarnings = videoWarningService.loadAllByQuery(query);
        return ResultUtil.data(videoWarnings);
    }
}
