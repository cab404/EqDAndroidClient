package com.cab404.eqd_client;

import java.lang.reflect.InvocationTargetException;
/**
 * Manages instances of converters
 *
 * @author cab404
 */
public class ConverterPool extends ConstructingPool<ViewConverter<?>> {

    @Override
    protected ViewConverter<?> makeInstance(Class<? extends ViewConverter<?>> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
