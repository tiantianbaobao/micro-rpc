package com.mini.rpc.rpcconsumer.inject;

import com.mini.rpc.rpcconsumer.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * @description {@link RpcReferenceBean} processor
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 23:31
 * @since V1.0.0
 */
@Component
@Slf4j
public class RpcConsumerPostProcessor  implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private ApplicationContext applicationContext;
    private ClassLoader classLoader;
    private final Map<String, BeanDefinition> beanDefinitionMap = new LinkedHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                ReflectionUtils.doWithFields(beanClass, this::parseFiled);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.beanDefinitionMap.forEach((beanName, definition) -> {
            if (applicationContext.containsBeanDefinition(beanName)) {
                throw new IllegalArgumentException("当前beanDefinition已经被定义！");
            }
            registry.registerBeanDefinition(beanName, beanDefinitionMap.get(beanName));
            System.out.println("注册@RpcReference bean : " + beanName +"成功!");
        });
    }

    /***
     * 解析属性bean
     * @param field
     */
    private void parseFiled(Field field) {
        RpcReference rpcReference = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (rpcReference != null) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            beanDefinitionBuilder.setInitMethodName("init");
            beanDefinitionBuilder.addPropertyValue("interfaceClass", field.getType());
            beanDefinitionBuilder.addPropertyValue("serviceVersion", rpcReference.version());
            beanDefinitionBuilder.addPropertyValue("registryAddr", rpcReference.registryAddr());
            beanDefinitionBuilder.addPropertyValue("registryType", rpcReference.registryType());
            beanDefinitionBuilder.addPropertyValue("timeout", rpcReference.timeout());
            BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            beanDefinitionMap.put(field.getName(), beanDefinition);
        }
    }


}
