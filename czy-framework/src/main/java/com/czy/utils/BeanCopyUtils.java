package com.czy.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: BeanCopyUtils
 * Package: com.czy.utils
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
// 对Bean进行拷贝的工具
public class BeanCopyUtils {
    // 私有构造，不对外暴露
    private BeanCopyUtils(){

    }

    // 1.对单个对象进行拷贝
    public static <V> V copyBean(Object object, Class<V> clazz){
        V instance = null;
        try {
            // 1.对要返回的对象进行实例化
            instance = clazz.newInstance();
            // 2.对对象进行拷贝
            BeanUtils.copyProperties(object, instance);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3.返回拷贝后的结果
        return instance;
    }

    // 2.对集合进行拷贝
    public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
