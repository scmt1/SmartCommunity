package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.mapper.RelaGridsPersonGridsMapper;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.RelaGridsPersonGrids;
import me.zhengjie.entity.TZhsqGridMember;

import me.zhengjie.service.IRelaGridsPersonGridsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网格-网格人员档案 中间表 服务实现类
 * </p>
 *
 * @author dengjie
 * @since 2020-07-22
 */
@Service
@AllArgsConstructor
public class RelaGridsPersonGridsServiceImpl extends ServiceImpl<RelaGridsPersonGridsMapper, RelaGridsPersonGrids> implements IRelaGridsPersonGridsService {

    private final RelaGridsPersonGridsMapper relaGridsPersonGridsMapper;

    private final BasicGridsMapper basicGridsMapper;
    /**
     * 功能描述：根据网格人员主键来获取数据
     * @param PersonId 主键
     * @return 返回获取结果
     */
    @Override
    public List<RelaGridsPersonGrids> getDataByPersonId(String PersonId) {
        QueryWrapper<RelaGridsPersonGrids> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(i -> i.eq(RelaGridsPersonGrids::getGridsPersonId, PersonId));
        //满足条件的字段
        List<RelaGridsPersonGrids> relaGridsPersonGrids = relaGridsPersonGridsMapper.selectList(queryWrapper);
        //处理中文名
        //获取所有网格
        List<BasicGrids> basicGrids = basicGridsMapper.selectList(new QueryWrapper<BasicGrids>());
        Map<String, BasicGrids> basicGridsMap = new HashMap<>();
        //将网格 按 id entity 拼装进map
        for(BasicGrids Grids : basicGrids){
            basicGridsMap.put(Grids.getId(),Grids);
        }
        //循环 用GridsId在map中取值 将name添加到entity
        for(RelaGridsPersonGrids gridsPersonGrids :relaGridsPersonGrids){
            if(basicGridsMap.containsKey(gridsPersonGrids.getGridsId())){
                gridsPersonGrids.setGridsName(basicGridsMap.get(gridsPersonGrids.getGridsId()).getName());
            }

        }
        return relaGridsPersonGrids;
    }

    /**
     * 功能描述：获取网格人员的网格信息
     * @param zhsqGridMembers 网格人员List
     * @return 网格人员List
     */
    @Override
    public List<TZhsqGridMember> getGridsPersonGridsData(List<TZhsqGridMember> zhsqGridMembers) {
        //获取所有中间表字段
        List<RelaGridsPersonGrids> RelaGridsPersonGrids = relaGridsPersonGridsMapper.selectList(new QueryWrapper<RelaGridsPersonGrids>());
        //将中间表信息 按 网格人员id entity 拼装进map
        Map<String, List<RelaGridsPersonGrids>> gridsPersonGridsMap = new HashMap<>();
        for(RelaGridsPersonGrids entity : RelaGridsPersonGrids){
            ArrayList<RelaGridsPersonGrids> tempArr = new ArrayList<>();
            tempArr.add(entity);
            //key已存在
            if(gridsPersonGridsMap.containsKey(entity.getGridsPersonId())){
                gridsPersonGridsMap.get(entity.getGridsPersonId()).add(entity);
            }else {//key不存在
                gridsPersonGridsMap.put(entity.getGridsPersonId(),tempArr);
            }
        }
        //获取所有网格
        List<BasicGrids> basicGrids = basicGridsMapper.selectList(new QueryWrapper<BasicGrids>());
        Map<String, BasicGrids> basicGridsMap = new HashMap<>();
        //将网格 按 网格id entity 拼装进map
        for(BasicGrids Grids : basicGrids){
            basicGridsMap.put(Grids.getId(),Grids);
        }
        //循环网格人员表
        for (TZhsqGridMember gridMember : zhsqGridMembers) {
            //网格人员表id
            String id = gridMember.getId();
            if(gridsPersonGridsMap.containsKey(id)){
                List<RelaGridsPersonGrids> relaGridsPersonGrids = gridsPersonGridsMap.get(id);
                //循环网格人员-网格中间表
                for (RelaGridsPersonGrids gridsPersonGrids: relaGridsPersonGrids) {
                    //网格id
                    String gridsId = gridsPersonGrids.getGridsId();
                    if(basicGridsMap.containsKey(gridsId)){
                        //获取网格名称
                        gridsPersonGrids.setGridsName(basicGridsMap.get(gridsId).getName());
                    }
                }
                gridMember.setGridsPersonGrids(relaGridsPersonGrids);
            }
        }
        return zhsqGridMembers;
    }


}
