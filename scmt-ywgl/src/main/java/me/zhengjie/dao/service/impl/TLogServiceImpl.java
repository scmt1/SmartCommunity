package me.zhengjie.dao.service.impl;

import me.zhengjie.dao.entity.TLog;
import me.zhengjie.dao.mapper.TLogMapper;
import me.zhengjie.dao.service.ITLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luozhen
 * @since 2020-05-21
 */
@Service
public class TLogServiceImpl extends ServiceImpl<TLogMapper, TLog> implements ITLogService {

}
