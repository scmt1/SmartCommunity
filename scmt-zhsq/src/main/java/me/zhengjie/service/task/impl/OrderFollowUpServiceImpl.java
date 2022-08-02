package me.zhengjie.service.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import me.zhengjie.mapper.task.OrderFollowUpMapper;
import me.zhengjie.entity.task.OrderFollowUp;
import me.zhengjie.service.task.IOrderFollowUpService;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.util.ThreadLocalUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljj
 * @since 2019-05-06
 */
@Service
@AllArgsConstructor
public class OrderFollowUpServiceImpl extends ServiceImpl<OrderFollowUpMapper, OrderFollowUp> implements IOrderFollowUpService {

    private final OrderFollowUpMapper orderFollowUpMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(OrderFollowUp orderFollowUp) {
        if (!"BXMK".equals(orderFollowUp.getCood()) &&!"SBWX".equals(orderFollowUp.getCood()) && !"RWLB".equals(orderFollowUp.getCood()) ){
            throw new BusinessErrorException("工单模块cood错误，请联系管理人员");
        }
        orderFollowUp.setPublisher(ThreadLocalUtil.getUserId());
        orderFollowUp.setCreateDate(new Date());
        orderFollowUpMapper.insert(orderFollowUp);
    }

    @Override
    public void delete(Integer orderFollowUpId) {
        orderFollowUpMapper.deleteById(orderFollowUpId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(OrderFollowUp orderFollowUp) {
        orderFollowUpMapper.updateById(orderFollowUp);
    }

    @Override
    public OrderFollowUp loadOne(Integer orderFollowUpId) {
        return orderFollowUpMapper.selectById(orderFollowUpId);
    }

    @Override
    public List<OrderFollowUp> loadByQuery(JSONObject query) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String cood = query.getString("cood");
        Integer detailsId = query.getInteger("detailsId");
        Integer publisher = query.getInteger("publisher");
        String gridId = query.getString("gridId");
        QueryWrapper<OrderFollowUp> orderFollowUpQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(cood)){
            orderFollowUpQueryWrapper.lambda().eq(OrderFollowUp::getCood,cood);
        }
        if (detailsId !=null){
            orderFollowUpQueryWrapper.lambda().eq(OrderFollowUp::getDetailsId,detailsId);
        }
        if (publisher !=null){
            orderFollowUpQueryWrapper.lambda().eq(OrderFollowUp::getPublisher,publisher);
        }
        if (gridId !=null){
            orderFollowUpQueryWrapper.lambda().eq(OrderFollowUp::getGridId,gridId);
        }
        orderFollowUpQueryWrapper.lambda().orderByDesc(OrderFollowUp::getCreateDate);
        List<OrderFollowUp> orderFollowUps = orderFollowUpMapper.selectList(orderFollowUpQueryWrapper);
        for (OrderFollowUp orderFollowUp : orderFollowUps) {
            String time = format.format(orderFollowUp.getCreateDate());
            orderFollowUp.setTime(time);
        }
        return orderFollowUps ;
    }


}
