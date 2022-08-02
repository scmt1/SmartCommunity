package me.zhengjie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.BasicPerson;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface IBasicHousingService extends IService<BasicHousing> {

    /**
     * 功能描述：根据主键来获取数据
     * @param id 主键
     * @return 返回获取结果
     */
    public Result<Object> getBasicHousingById(String id);

    /**
     * 功能描述：实现分页查询
     * @param basicHousing 需要模糊查询的信息
     * @param searchVo 排序参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    public Result<Object> queryBasicHousingListByPage(BasicHousing basicHousing, SearchVo searchVo, PageVo pageVo);

    /**
     * 功能描述： 导出
     * @param basicHousing 查询参数
     * @param response response参数
     */
    public void download(BasicHousing basicHousing, HttpServletResponse response) ;
    /**
     * 功能描述： 导出出租
     * @param basicPerson 查询参数
     * @param response response参数
     */
    public void downloadRent(BasicPerson basicPerson, HttpServletResponse response) ;

    /**
     * 根据楼栋id，查询单元
     * @param buildArchiveId
     * @return
     */
    List<Map<String, Object>> getUnitByBuildArchiveId(String buildArchiveId);

    /**
     * 查询门牌号和楼层
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getDoor(String buildArchiveId,String unit);

    /**
     * 获取最大楼层数和楼层中最大门牌数
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getMaxOfFloorAndDoor(String buildArchiveId,String unit);

    /**
     * 获取房屋中的人数
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getHouseNum(String buildArchiveId,String unit);

    /**
     * 保存人员和出租房
     * @param basicHousing
     */
    Result<Object> savePersonAndBasicHouse(BasicHousing basicHousing);

    /**
     * 更新人员出租房
     * @param basicHousing
     */
    Result<Object> updatePersonAndBasicHouse(BasicHousing basicHousing);

    /**
     * 功能描述：根据房屋人员中间表查询人员
     * gridMember 查询数据
     * pageVo 分页数据
     * @return 统计数据
     */
    IPage<Map<String, Object>> getAllPersonByRela(BasicPerson basicPerson, PageVo pageVo);


    /**
     * 查询门牌号和楼层
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealDoor(String buildArchiveId,String unit);

    /**
     * 查询门牌号和楼层
     * @param buildArchiveId
     * @param unit
     * @return
     */
    Map<String,Object> getRealDoorAndFloor(String buildArchiveId,String unit);

    /**
     * 查询门牌号和楼层
     * @param buildArchiveId
     * @param unit
     * @return
     */
    Map<String,Object> getRealDoorAndPerson(String buildArchiveId,String unit);

    /**
     * 获取房屋中的人数
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<Map<String,Object>> getRealHouse(String buildArchiveId,String unit);


    /**
     * 功能描述：模糊查询所有信息
     * @return 返回获取结果
     */
    Result<Object> queryHousingManage(BasicHousing basicHousing);

    /**
     * 根据楼栋id查询房屋信息
     * @param buildArchiveId
     *  @param personId
     * @return
     */
    List<BasicHousing> getRoomByBuildArchiveId(String buildArchiveId,String personId,String type);

    /**
     * 模糊查询所有
     * @param basicHousing
     * @param key
     * @param pageVo
     * @return
     */
    Result<Object> queryBasicHousingListByAnyWayWhere(BasicHousing basicHousing, String key, PageVo pageVo);

    /**
     * 统计各房屋类型数据
     * @return
     */
    List<Map<String,Object>> statisticsHousingType(String buildingArchiveId);

    /**
     * 根据楼栋查询单元
     * @param buildArchiveId
     * @return
     */
    List<String> getUnits(String buildArchiveId);

    /**
     * 根据楼栋单元查询楼层
     * @param buildArchiveId
     * @param unit
     * @return
     */
    List<String> getFloors(String buildArchiveId, String unit);

    /**
     * 根据楼栋单元楼层查询门牌号
     * @param buildArchiveId
     * @param unit
     * @param floor
     * @return
     */
    List<Map<String,Object>> getDoorNumbers(String buildArchiveId, String unit, String floor);


    /**
     * 根据personId查询房屋信息
     * @param personId
     * @return
     */
    List<BasicHousing> getHouseByPersonId(String personId);

    /**
     * 处理房屋结构图左侧树
     * @param map 用户数据权限
     * @return
     */
    List<Map<String,Object>> selectHouseTree(Map<String,Object> map);

    /**
     * 功能描述：Excel导入信息数据
     *
     * @return 返回获取结果
     */
    Result<Object> importExcel(MultipartFile file) throws Exception;

}
