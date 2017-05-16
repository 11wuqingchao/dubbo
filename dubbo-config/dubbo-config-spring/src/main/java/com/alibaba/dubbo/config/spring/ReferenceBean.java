/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.config.spring;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import com.alibaba.dubbo.config.support.Parameter;

/**
 * ReferenceFactoryBean
 * 
 * @author william.liangf
 *
 */
public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean, ApplicationContextAware, InitializingBean, DisposableBean {

	private static final long serialVersionUID = 213195494150089726L;
	
	private transient ApplicationContext applicationContext;

	public ReferenceBean() {
        super();
    }

    public ReferenceBean(Reference reference) {
        super(reference);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		SpringExtensionFactory.addApplicationContext(applicationContext);
	}
    
    public Object getObject() throws Exception {
        return get();
    }

    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Parameter(excluded = true)
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings({ "unchecked"})
    public void afterPropertiesSet() throws Exception {
        if (getConsumer() == null) {
            Map<String, ConsumerConfig> consumerConfigMap = BeanHelper.get(applicationContext, ConsumerConfig.class);
            if (consumerConfigMap != null && consumerConfigMap.size() > 0) {
                ConsumerConfig consumerConfig = null;
                for (ConsumerConfig config : consumerConfigMap.values()) {
                    if (config.isDefault() == null || config.isDefault()) {
                        if (consumerConfig != null) {
                            throw new IllegalStateException("Duplicate consumer configs: " + consumerConfig + " and " + config);
                        }
                        consumerConfig = config;
                    }
                }
                if (consumerConfig != null) {
                    setConsumer(consumerConfig);
                }
            }
        }

        if (getApplication() == null && (getConsumer() == null || getConsumer().getApplication() == null)) {
            Map<String, ApplicationConfig> applicationConfigMap = BeanHelper.get(applicationContext, ApplicationConfig.class);
            if (applicationConfigMap != null && applicationConfigMap.size() > 0) {
                ApplicationConfig applicationConfig = BeanHelper.getApplicationConfig(applicationConfigMap);
                if (applicationConfig != null) {
                    setApplication(applicationConfig);
                }
            }
        }

        if (getModule() == null && (getConsumer() == null || getConsumer().getModule() == null)) {
            Map<String, ModuleConfig> moduleConfigMap = BeanHelper.get(applicationContext, ModuleConfig.class);
            if (moduleConfigMap != null && moduleConfigMap.size() > 0) {
                ModuleConfig moduleConfig = BeanHelper.getModuleConfig(moduleConfigMap);
                if (moduleConfig != null) {
                    setModule(moduleConfig);
                }
            }
        }

        if ((getRegistries() == null || getRegistries().size() == 0)
                && (getConsumer() == null || getConsumer().getRegistries() == null || getConsumer().getRegistries().size() == 0)
                && (getApplication() == null || getApplication().getRegistries() == null || getApplication().getRegistries().size() == 0)) {
            Map<String, RegistryConfig> registryConfigMap = BeanHelper.get(applicationContext, RegistryConfig.class);
            if (registryConfigMap != null && registryConfigMap.size() > 0) {
                List<RegistryConfig> registryConfigs = BeanHelper.getRegistryConfigs(registryConfigMap);
                if (registryConfigs.size() > 0) {
                    setRegistries(registryConfigs);
                }
            }
        }

        if (getMonitor() == null && (getConsumer() == null || getConsumer().getMonitor() == null)
                && (getApplication() == null || getApplication().getMonitor() == null)) {
            Map<String, MonitorConfig> monitorConfigMap = BeanHelper.get(applicationContext, MonitorConfig.class);
            if (monitorConfigMap != null && monitorConfigMap.size() > 0) {
                MonitorConfig monitorConfig = BeanHelper.getMonitorConfig(monitorConfigMap);
                if (monitorConfig != null) {
                    setMonitor(monitorConfig);
                }
            }
        }

        Boolean b = isInit();
        if (b == null && getConsumer() != null) {
            b = getConsumer().isInit();
        }
        if (b != null && b) {
            getObject();
        }
    }

}