package me.zhengjie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.entity.RelaGridsPersonGrids;
import me.zhengjie.entity.TZhsqGridMember;

import java.util.List;

/**
 * <p>
 * 网格-网格人员档案 中间表 服务类
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
public interface IRelaGridsPersonGridsService extends IService<RelaGridsPersonGrids> {

    /**
     * 功能描述：根据网格人员主键来获取数据
     * @param PersonId 主键
     * @return 返回获取结果
     */
    List<RelaGridsPersonGrids> getDataByPersonId(String PersonId);

    /**
     * 功能描述：获取网格人员的网格信息
     * @param zhsqGridMembers 网格人员List
     * @return 网格人员List
     */
    List<TZhsqGridMember> getGridsPersonGridsData(List<TZhsqGridMember> zhsqGridMembers);


}
