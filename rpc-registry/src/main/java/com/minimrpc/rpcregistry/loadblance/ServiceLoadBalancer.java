package com.minimrpc.rpcregistry.loadblance;

import java.util.List;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 23:30
 * @since V1.0.0
 */
public interface ServiceLoadBalancer<T> {
    /***
     * 抽象的负载选择
     * @param serverList
     * @param hashCode
     * @return
     */
    T select(List<T> serverList, int hashCode);
}
