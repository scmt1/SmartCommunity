package me.zhengjie.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.dao.entity.TFormGenerator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单自动 生成 Mapper 接口
 * </p>
 *
 * @author zenghu
 * @since 2020-11-25
 */
public interface TFormGeneratorMapper extends BaseMapper<TFormGenerator> {

    /**
     * 分页列表
     * @param wrapper
     * @param page
     * @return
     */
    IPage<TFormGenerator> selectListPage(@Param(Constants.WRAPPER) QueryWrapper<TFormGenerator> wrapper, Page page);

}
