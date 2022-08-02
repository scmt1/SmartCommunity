package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.entity.TZhsqAnounceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dengjie
 * @since 2021-08-26
 */
@Mapper
public interface TZhsqAnounceInfoMapper extends BaseMapper<TZhsqAnounceInfo> {

	/**
	 *
	 * 使用MP提供的Wrapper条件构造器，
	 *
	 * @param userWrapper
	 * @return
	 */
	List<TZhsqAnounceInfo> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqAnounceInfo> userWrapper);

	/**
	 *
	 * 使用MP提供的Wrapper条件构造器（分页查询），
	 *
	 * @param userWrapper
	 * @return
	 */
	IPage<TZhsqAnounceInfo> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TZhsqAnounceInfo> userWrapper, @Param(value = "page") Page page);

}
