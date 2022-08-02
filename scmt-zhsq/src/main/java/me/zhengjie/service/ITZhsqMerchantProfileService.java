package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqMerchantProfile;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *@author
 **/
public interface ITZhsqMerchantProfileService extends IService<TZhsqMerchantProfile> {

    /**
     * 功能描述：根据主键来获取数据
     * @param id 主键
     * @return 返回获取结果
     */
    public Result<Object> getTZhsqMerchantProfileById(String id);

    /**
     * 功能描述：实现分页查询
     * @param tZhsqMerchantProfile 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryTZhsqMerchantProfileListByPage(TZhsqMerchantProfile  tZhsqMerchantProfile, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     * @param tZhsqMerchantProfile 查询参数
     * @param response response参数
     */
    public void download(TZhsqMerchantProfile  tZhsqMerchantProfile, HttpServletResponse response) ;

    /**
     * 分页查询商户数据数据
     * @param tZhsqMerchantProfile
     * @param key
     * @param pageVo
     * @return
     */
    Result<Object> queryTZhsqMerchantProfileListByAnyWayWhere(TZhsqMerchantProfile tZhsqMerchantProfile, String key, PageVo pageVo);

    /**
     * 统计各个专题数据
     * @param communityId
     * @return
     */
    public Map<String,Object> selectByCommunity(String communityId,String gridId);
}
