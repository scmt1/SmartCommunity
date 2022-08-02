package me.zhengjie.global;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import me.zhengjie.common.vo.Result;
import me.zhengjie.service.IBasicGridsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GridTree {

    @Autowired
    private IBasicGridsService basicGridsService;

    private List<Record> result=new ArrayList<>();

    private long lastTime = 0;

    public static final int TIMEOUT = 10 * 1000;

   // 以自己实例为返回值的静态的公有方法，静态工厂方法
    private void initData() {
        if (System.currentTimeMillis() - TIMEOUT > lastTime || result == null) {
            //请求接口
            //数据检查

            Result<Object> objectResult = basicGridsService.queryAllList(null);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(objectResult);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            result = JSON.parseArray(JSON.toJSONString(jsonArray),Record.class);
         //   result = JSON.parseObject(records.toJSONString(), Record.class);

            if (!"200".equals(objectResult.getCode().toString())) {
                //log数据异常

            } else {
                lastTime = System.currentTimeMillis();
            }
        }
    }


    public Record getGridInfomation(String gridId) {
        //已过期
        if (gridId == null || gridId.length() == 0) {
            //log. //参数不正确
            return Record.NULL;
        }
        initData();
        for (Record record : result) {
            if (gridId.equals(record.id)) {

                return record;
            }
        }
        return Record.NULL;
    }

    @lombok.Data
    public static class Data {
       // private String code;
        private List<Record> data;
    }
    @lombok.Data
    public static final class Record {
        public static final Record NULL = new Record();
        private String id;
        private String name;
        private String streetId;
        private String streetName;
        private String communityId;
        private String communityName;
    }
}
