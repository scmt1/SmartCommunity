package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqVolunteer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author
 **/
public interface ITZhsqVolunteerService extends IService<TZhsqVolunteer> {

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    public TZhsqVolunteer getTZhsqVolunteerById(String id);

    /**
     * 功能描述：实现分页查询
     *
     * @param tZhsqVolunteer 需要模糊查询的信息
     * @param searchVo       排序参数
     * @param pageVo         分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryTZhsqVolunteerListByPage(TZhsqVolunteer tZhsqVolunteer, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     *
     * @param tZhsqVolunteer 查询参数
     * @param response       response参数
     */
    public void download(TZhsqVolunteer tZhsqVolunteer, HttpServletResponse response);

    /**
     * 功能描述：模糊查询所有信息
     *
     * @return 返回获取结果
     */
    public Result<Object> queryAllList(TZhsqVolunteer tZhsqVolunteer);

    /**
     * 根据社区id和网格id查询志愿者人数
     * @param communityId
     * @param gridId
     * @return
     */
    int selectByCommunityAndGrid(String communityId, String gridId);

    /**
     * 功能描述：Excel导入信息数据
     *
     * @return 返回获取结果
     */
    Result<Object> importExcel(MultipartFile file) throws Exception;
}
