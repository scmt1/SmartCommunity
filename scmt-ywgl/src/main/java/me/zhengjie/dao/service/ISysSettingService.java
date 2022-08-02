package me.zhengjie.dao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.dao.entity.SysSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.Setting;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dengjie
 * @since 2020-06-10
 */
public interface ISysSettingService extends IService<SysSetting> {

    /**
     * 功能描述：根据主键来获取数据
     * @param id 主键
     * @return 返回获取结果
     */
    public SysSetting getSysSettingById(String id);

    /**
     * 功能描述：实现分页查询
     * @param search 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public IPage<SysSetting> querySysSettingListByPage(String search, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     * @param likeValue 查询参数
     * @param response response参数
     */
    public void download(String likeValue, HttpServletResponse response) ;
}
