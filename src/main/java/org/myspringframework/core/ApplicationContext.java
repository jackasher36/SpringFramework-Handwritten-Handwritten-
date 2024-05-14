package org.myspringframework.core;

public interface ApplicationContext {
    /**
     * 根据bean的名称获取bean对象
     * @param beanName myspring配置文件的bean标签id
     * @return 对应的单例bean对象
     */
    Object getBean(String beanName);
}
