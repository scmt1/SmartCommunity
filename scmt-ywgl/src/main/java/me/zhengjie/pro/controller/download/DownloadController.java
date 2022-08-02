package me.zhengjie.pro.controller.download;


import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.utils.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/download")

public class DownloadController {


    @PostMapping(value = "/fileUpload")
    public Result<Object> fileUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        try {
            String upload = UploadFile.uploadFile(multipartFile);
            return ResultUtil.data(upload);
        }catch (Exception e){
            return ResultUtil.error("文件上传失败！");
        }
    }

    @GetMapping(value = "/fileDelete")
    public Result<Object> fileDelete(String url) {
        try {
            UploadFile.deleteFile(url);
            return ResultUtil.success(ResultCode.SUCCESS);
        }catch (Exception e){
            return ResultUtil.error("删除失败！");
        }
    }

    /**
     * 多文件上传
     * @param
     * @param multipartFiles
     * @return
     */
    @PostMapping("/batchUpload")
    public Result<Object> batchUpload(MultipartFile[] multipartFiles) {
        try {
            String uploads = UploadFile.batchUpload(multipartFiles);
            return ResultUtil.data(uploads);
        }catch (Exception e){
            return ResultUtil.error("文件上传失败！");
        }

    }
}
