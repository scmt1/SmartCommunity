package me.zhengjie.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DictionaryMapper {

    @Select("select value,label,sort,dict_id from grid_dictionary where status = 1 order by id")
    List<Map<Integer, String>> loadAll();

    @Select("select id,value,label from dict_detail where dict_id = #{dictId} and status = 1 order by number")
    List<Map<String, Object>> loadAllByFieldName(@Param("dictId") String dictId);

    @Select("select label from dict_detail where value = #{number} and dict_id = #{dictId}")
    String loadOneByFieldNameAndNumber(@Param("number") Integer number, @Param("dictId") String dictId);
}
