package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dengjie
 * @since 2020-07-21
 */
public interface IBasicFileService extends IService<BasicFile> {
    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    public Result<Object> getBasicFileById(String id);

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 排序参数
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryBasicFileListByPage(BasicFile basicFile, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     *
     * @param basicFile 查询参数
     * @param response  response参数
     */
    public void download(BasicFile basicFile, HttpServletResponse response);
}
