package com.minimrpc.rpcregistry.constants;

import org.springframework.util.StringUtils;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 14:50
 * @since V1.0.0
 */
public enum RegistryType {
    ZOOKEEPER("ZOOKEEPER"),
    EUREKA("EUREKA");
    private final String name;

    RegistryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RegistryType getTypeByName(String name){
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("注册类型为空！");
        }
        for (RegistryType typeEnum : RegistryType.values()) {
            if (StringUtils.pathEquals(typeEnum.getName(), name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
