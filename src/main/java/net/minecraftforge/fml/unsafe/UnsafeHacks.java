/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.fml.unsafe;

import java.lang.reflect.Field;
import net.minecraftforge.unsafe.UnsafeFieldAccess;

/**
 * Kept for backwards compatibility until I feel like deleting it.
 * Consumers should use the proper API exposed through {@link net.minecraftforge.unsafe.UnsafeHacks}
 */
@Deprecated
public class UnsafeHacks {
    public static <T> T newInstance(Class<T> clazz) {
        return net.minecraftforge.unsafe.UnsafeHacks.newInstance(clazz);
    }

    public static <T> T getField(Field field, Object instance) {
        return net.minecraftforge.unsafe.UnsafeHacks.getField(field, instance);
    }

    public static void setField(Field field, Object instance, Object value) {
        net.minecraftforge.unsafe.UnsafeHacks.setField(field, instance, value);
    }

    public static int getIntField(Field field, Object instance) {
        return net.minecraftforge.unsafe.UnsafeHacks.getIntField(field, instance);
    }

    public static void setIntField(Field data, Object instance, int value) {
        net.minecraftforge.unsafe.UnsafeHacks.setIntField(data, instance, value);
    }

    @SuppressWarnings("rawtypes")
    private static UnsafeFieldAccess<Class, Object> enumConstantDirectory = net.minecraftforge.unsafe.UnsafeHacks.findField(Class.class, "enumConstantDirectory");
    @SuppressWarnings("rawtypes")
    private static UnsafeFieldAccess<Class, Object> enumConstants = net.minecraftforge.unsafe.UnsafeHacks.findField(Class.class, "enumConstants");

    public static void cleanEnumCache(Class<? extends Enum<?>> enumClass) {
        if (enumConstantDirectory != null) enumConstantDirectory.set(enumClass, null);
        if (enumConstants != null) enumConstants.set(enumClass, null);
    }
}
