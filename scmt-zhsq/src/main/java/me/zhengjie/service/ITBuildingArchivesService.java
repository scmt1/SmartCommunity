package me.zhengjie.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TBuildingArchives;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface ITBuildingArchivesService extends IService<TBuildingArchives> {

	/**
	 * 功能描述：根据主键来获取数据
	 * @param id 主键
	 * @return 返回获取结果
	 */
	public Result<Object> getTBuildingArchivesById(String id);

	/**
	 * 功能描述：实现分页查询
	 * @param searchVo 排序参数
	 * @param pageVo 分页参数
	 * @return 返回获取结果
	 */
	public Page<TBuildingArchives> queryTBuildingArchivesListByPage(TBuildingArchives tBuildingArchives, SearchVo searchVo, PageVo pageVo);

	/**
	 * 查询所有
	 * @param tBuildingArchives
	 * @return
	 */
	public List<TBuildingArchives> queryTBuildingArchivesAll(TBuildingArchives tBuildingArchives);

	/**
	 * 功能描述： 导出
	 * @param tBuildingArchives 查询参数
	 * @param response response参数
	 */
	public void download(TBuildingArchives tBuildingArchives, HttpServletResponse response) ;

	/**
	 * 模糊查询所有
	 * @param tBuildingArchives
	 * @param key
	 * @param pageVo
	 * @return
	 */
	Result<Object> queryTBuildingArchivesListByAnyWayWhere(TBuildingArchives tBuildingArchives, String key, PageVo pageVo);

	/**
	 * 获取最大单元、楼层、层户数
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getMaxOfFloorAndDoorAndUnit(String id);

	/**
	 * 新增
	 * @param tBuildingArchives
	 * @return
	 */
	boolean insert(TBuildingArchives tBuildingArchives);

	/**
	 * 查询除当前标绘面以外的所有数据
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getOtherPolygonData(String id);

	/**
	 * 功能描述：Excel导入信息数据
	 *
	 * @return 返回获取结果
	 */
	Result<Object> importExcel(MultipartFile file) throws Exception;

}
