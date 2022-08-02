package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGridPersonPoint;
import me.zhengjie.entity.TZhsqGridMember;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *@author
 **/
public interface ITZhsqGridMemberService extends IService<TZhsqGridMember> {

	/**
	* 功能描述：根据主键来获取数据
	* @param id 主键
	* @return 返回获取结果
	*/
	public Result<Object> getTZhsqGridMemberById(String id);

	/**
	* 功能描述：实现分页查询
	* @param tZhsqGridMember 需要模糊查询的信息
	* @param searchVo 排序参数
	* @param pageVo 分页参数
	* @return 返回获取结果
	*/
	public Result<Object> queryTZhsqGridMemberListByPage(TZhsqGridMember tZhsqGridMember, String gridId, SearchVo searchVo, PageVo pageVo);

	/**
	* 功能描述： 导出
	* @param tZhsqGridMember 查询参数
	* @param response response参数
	*/
	public void download(TZhsqGridMember tZhsqGridMember, HttpServletResponse response, SearchVo searchVo) ;

	/**
	 * 功能描述： 自定义id 单条保存（用于获取ID）
	 * @param tZhsqGridMember 需保存数据
	 * @return 返回是否成功
	 */
    boolean insertEntity(TZhsqGridMember tZhsqGridMember);

	/**
	 * 功能描述：更新
	 * @param tZhsqGridMember 需更新数据
	 * @return 返回是否成功
	 */
	boolean updateByEntity(TZhsqGridMember tZhsqGridMember);

	/**
	 * 功能描述：获取统计数据
	 * gridMember 查询数据
	 * pageVo 分页数据
	 * @return 统计数据
	 */
	Result<Object> getStatisticsData(TZhsqGridMember gridMember, PageVo pageVo);

	/**
	 * 网格干部 模糊查询所有
	 * @param tZhsqGridMember
	 * @return
	 */
	Result<Object> queryGridMemberList(TZhsqGridMember tZhsqGridMember, String gridId);

	/**
	 * 查询当前网格下的网格长
	 * @param gridId
	 * @return
	 */
	Result<Object> queryAllTZhsqGridMemberListByGridId(String gridId);

	/**
	 * 查询全部网格人口档案
	 * @param tZhsqGridMember
	 * @param key
	 * @return
	 */
    Result<Object> queryAllTZhsqGridMemberListByAnyWayWhere(TZhsqGridMember tZhsqGridMember, String key);

	/**
	 * 查询网格人员轨迹
	 * @param gridPersonId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<BasicGridPersonPoint> gridPersonTrackQuery(String gridPersonId, String startTime, String endTime);

	/**
	 * 查询当前网格下的网格员
	 * @param gridId
	 * @return
	 */
	Result<Object> queryAllGridMemberListByGridId(String gridId);

	/**
	 * 判断网格是否已有网格长
	 * @param s
	 * @return
	 */
    List<TZhsqGridMember> existsGrid(String s);

	/**
	 * 根据社区id和网格id查询网格
	 * @param communityId
	 * @param gridId
	 * @return
	 */
    int selectByCommunityIdAndGridId(String communityId, String gridId);

	/**
	 * 处理网格人员树
	 * @return
	 */
	List<Map<String,Object>> selectGridMemberTree();

	/**
	 * 功能描述：Excel导入信息数据
	 *
	 * @return 返回获取结果
	 */
	Result<Object> importExcel(MultipartFile file,Boolean isGridLength) throws Exception;

}
