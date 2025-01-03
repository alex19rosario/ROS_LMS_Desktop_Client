package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.util.DynamicInvocationHandler;

import java.lang.reflect.Proxy;

public class ServiceFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> serviceInterface, T serviceImplementation) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new DynamicInvocationHandler(serviceImplementation)
        );
    }
}
