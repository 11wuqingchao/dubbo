package com.alibaba.dubbo.config.spring;

import com.alibaba.dubbo.config.*;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by woodle on 17/5/16.
 * 辅助工具类
 */
public class BeanHelper {

    public static <T> Map<String, T> get(ApplicationContext applicationContext, Class<T> clz) {
        return applicationContext == null ? null  :
                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, clz, false, false);
    }

    public static ApplicationConfig getApplicationConfig(Map<String, ApplicationConfig> applicationConfigMap) {
        ApplicationConfig applicationConfig = null;
        for (ApplicationConfig config : applicationConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault()) {
                if (applicationConfig != null) {
                    throw new IllegalStateException("Duplicate application configs: " + applicationConfig + " and " + config);
                }
                applicationConfig = config;
            }
        }
        return applicationConfig;
    }

    public static ModuleConfig getModuleConfig(Map<String, ModuleConfig> moduleConfigMap) {
        ModuleConfig moduleConfig = null;
        for (ModuleConfig config : moduleConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault()) {
                if (moduleConfig != null) {
                    throw new IllegalStateException("Duplicate module configs: " + moduleConfig + " and " + config);
                }
                moduleConfig = config;
            }
        }
        return moduleConfig;
    }

    public static List<RegistryConfig> getRegistryConfigs(Map<String, RegistryConfig> registryConfigMap) {
        List<RegistryConfig> registryConfigs = new ArrayList<>();
        for (RegistryConfig config : registryConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault()) {
                registryConfigs.add(config);
            }
        }
        return registryConfigs;
    }

    public static MonitorConfig getMonitorConfig(Map<String, MonitorConfig> monitorConfigMap) {
        MonitorConfig monitorConfig = null;
        for (MonitorConfig config : monitorConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault()) {
                if (monitorConfig != null) {
                    throw new IllegalStateException("Duplicate monitor configs: " + monitorConfig + " and " + config);
                }
                monitorConfig = config;
            }
        }
        return monitorConfig;
    }

    public static List<ProtocolConfig> getProtocolConfigs(Map<String, ProtocolConfig> protocolConfigMap) {
        List<ProtocolConfig> protocolConfigs = new ArrayList<>();
        for (ProtocolConfig config : protocolConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault()) {
                protocolConfigs.add(config);
            }
        }
        return protocolConfigs;
    }


}
