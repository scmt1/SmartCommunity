package me.zhengjie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.TBuildingArchives;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface IBasicPersonService extends IService<BasicPerson> {

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    public Result<Object> getBasicPersonById(String id);

    /**
     * 功能描述：实现分页查询
     *
     * @param basicPerson 需要模糊查询的信息
     * @param searchVo    排序参数
     * @param pageVo      分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryBasicPersonListByPage(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     *
     * @param basicPerson 查询参数
     * @param response    response参数
     */
    public void download(BasicPerson basicPerson, HttpServletResponse response) throws Exception;

    /**
     * 功能描述： 导出
     *
     * @param map 参数
     * @param response    response参数
     */
    public void downloadDynamics(Map<String, Object> map, HttpServletResponse response) throws Exception;

    /**
     * 自写插入
     *
     * @param basicPerson
     * @return
     */
    int insert1(BasicPerson basicPerson);

    /**
     * 人员统计
     *
     * @return
     */
    List<Map<String, Object>> getBasicPersonCount(BasicPerson basicPerson);

    /**
     * 特殊人员统计
     * @param basicPerson 用户数据权限
     * @return
     */
    List<Map<String, Object>> getSpecialPersonCount(BasicPerson basicPerson);

    /**
     * 根据小区id查询楼栋
     *
     * @param estateId
     * @return
     */
    List<TBuildingArchives> getBuildArchiveByEstateId(String estateId);

    /**
     * 统计各网格人口数据
     *
     * @return
     */
    Result<Object> statisticsGridPerson();


    /**
     * 统计人口数据
     *
     * @return
     */
    Result<Object> statisticsGridPerson2(String community);

    /**
     * 模糊查询所有
     * @param basicPerson
     * @param key
     * @param pageVo
     * @return
     */
    Result<Object> queryBasicPersonListByAnyWayWhere(BasicPerson basicPerson, String key, PageVo pageVo);

    /**
     * 统计各人口类型数据
     *
     * @return
     */
    List<Map<String, Object>> statisticsPersonType(String buildingArchiveId);

    /**
     * 通过身份证号查询所有
     * @param cardId
     * @return
     */
    List<Map<String, Object>> getPersonByCardId(String cardId);

    /**
     * 查询身份证号
     * @return
     */
    List<Map<String, Object>> getCardId();

    /**
     * 房屋id查询人口信息
     *
     * @param houseId
     * @return
     */
    List<BasicPerson> getPersonByHouseId(String houseId);

    /**
     * 对外查询户籍人口
     *
     * @param basicPerson
     * @param pageVo
     * @return
     */
    Page<BasicPerson> queryAccPerson(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo);


    /**
     * 对外根据户号查询人口
     *
     * @param accNumber
     * @return
     */
    List<BasicPerson> queryBasicPersonNoPage(String accNumber, String id);

    /**
     * 功能描述：Excel导入小区信息数据
     *
     * @return 返回获取结果
     */
    Result<Object> importExcel(MultipartFile file) throws Exception;

    /**
     * 统计人员性别
     * @param communityId
     * @param gridId
     * @return
     */
    Map<String, Object> getPersonDataMaleToFemaleratio(String communityId, String gridId);


    /**
     * 统计人口类型数量
     *
     * @param communityId
     * @param gridId
     * @return
     */
    List<Map<String, Object>> getBasicPersonCountByPersonType(String communityId, String gridId);

    /**
     * 统计人员类型（老人、精神病人....）
     *
     * @param communityId
     * @param gridId
     * @return
     */
    List<Map<String, Object>> getBasicPersonCountByPopulation(String communityId, String gridId);

    /**
     * 分页动态查询
     * @param basicPerson
     * @param searchVo
     * @param pageVo
     * @return
     */
    IPage<Map<String, Object>> queryBasicPersonDynamicList(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo);
}
