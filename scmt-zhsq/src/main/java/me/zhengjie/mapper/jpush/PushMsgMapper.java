package me.zhengjie.mapper.jpush;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import me.zhengjie.entity.jpush.PushMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PushMsgMapper extends BaseMapper<PushMsg> {

    @Select("select pm.message_type messageType,pm.type,pm.details_id detailsId,concat(p.name,':',pm.title) title,pm.content," +
            "pm.data,pm.create_date createDate,pm.is_read isRead ,pm.user_type,pm.status ,pm.is_master, pm.device_status " +
            "from push_msg pm left join property p on p.id = pm.property_id " +
            "where  FIND_IN_SET(#{userId},pm.user_ids)  and pm.app_type = #{appType} order by pm.create_date desc limit 20")
    List<PushMsg> loadPushMsg(JSONObject param);

    @Select("select count(1) from push_msg where FIND_IN_SET(#{userId},user_ids) and app_type = #{appType} and is_read = 1")
    Integer loadUnreadCount(JSONObject param);

    @Select("update push_msg set is_read = 2 where FIND_IN_SET(#{userId},user_ids) and app_type = #{appType}")
    void setReadStatus(JSONObject param);


    List<Map<String, Object>> loadMsgInWeb(@Param("param") JSONObject param);

    Map<String, Object> loadOneMsgInWeb(@Param("param")JSONObject param);

    String getBasicHousingInfo(@Param("housingId") String housingId);

    Map<String, Object> getUserPhone(@Param("personUuid") String personUuid);
}
