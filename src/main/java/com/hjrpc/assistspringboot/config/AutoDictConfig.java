package com.hjrpc.assistspringboot.config;

import com.hjrpc.assistspringboot.DictService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AutoDictConfig {
    private static Map<Integer, String> statusMap = new HashMap<>();
    private static Map<Integer, String> ageMap = new HashMap<>();

    static {
        statusMap.put(0, "正常");
        statusMap.put(1, "失败");
        statusMap.put(2, "其他");
        ageMap.put(0, "0-10岁");
        ageMap.put(1, "10-20岁");
        ageMap.put(2, "20-30岁");
    }

    @Bean
    public DictService dictService() {
        return (field, dictCode) -> {
            if ("DICT_STATUS".equals(dictCode)) {
                return statusMap.get((Integer) field);
            }
            return ageMap.get((Integer) field);
        };
    }
}
