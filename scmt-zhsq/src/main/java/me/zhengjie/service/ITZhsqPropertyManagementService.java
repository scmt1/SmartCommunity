package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqPropertyManagement;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *@author
 **/
public interface ITZhsqPropertyManagementService extends IService<TZhsqPropertyManagement> {

    /**
     * 功能描述：根据主键来获取数据
     * @param id 主键
     * @return 返回获取结果
     */
    public Result<Object> getTZhsqPropertyManagementById(String id);

    /**
     * 功能描述：实现分页查询
     * @param tZhsqPropertyManagement 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryTZhsqPropertyManagementListByPage(TZhsqPropertyManagement  tZhsqPropertyManagement, SearchVo searchVo, PageVo pageVo);
    /**
     * 功能描述：实现分页查询
     * @param tZhsqPropertyManagement 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryTZhsqPropertyManagementListByPageWithGridId(TZhsqPropertyManagement  tZhsqPropertyManagement,String gridId, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     * @param tZhsqPropertyManagement 查询参数
     * @param response response参数
     */
    public void download(TZhsqPropertyManagement  tZhsqPropertyManagement, HttpServletResponse response) ;

    /**
     * 模糊查询全部物业信息数据
     * @param propertyManagement
     * @return
     */
    Result<Object> queryAllList(TZhsqPropertyManagement propertyManagement);

    /**
     * 功能描述：Excel导入小区信息数据
     * @return 返回获取结果
     */
    Result<Object> importExcel(MultipartFile file) throws Exception;

    /**
     * 根据物业名称判断是否有相同数据
     * @param propertyName
     * @return
     */
    Boolean isSame(String propertyName,String id);

    /**
     * 自写插入
     * @param propertyManagement
     * @return
     */
    int insert(TZhsqPropertyManagement propertyManagement);
}
