/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.unsafe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import sun.misc.Unsafe;

public class UnsafeHacks {
    private static final Unsafe UNSAFE = getUnsafe();
    @SuppressWarnings("rawtypes")
    private static final UnsafeFieldAccess<Class, Object> module = findField(Class.class, "module");
    private static final Consumer<AccessibleObject> SETACCESSIBLE = getSetAccessible();

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return cast(UNSAFE.allocateInstance(clazz));
        } catch (InstantiationException e) {
            return sneak(e);
        }
    }

    public static <T> T getField(Field field, Object instance) {
        return cast(UNSAFE.getObject(instance, UNSAFE.objectFieldOffset(field)));
    }

    public static void setField(Field field, Object instance, Object value) {
        UNSAFE.putObject(instance, UNSAFE.objectFieldOffset(field), value);
    }

    public static int getIntField(Field field, Object instance) {
        return UNSAFE.getInt(instance, UNSAFE.objectFieldOffset(field));
    }

    public static void setIntField(Field field, Object instance, int value) {
        UNSAFE.putInt(instance, UNSAFE.objectFieldOffset(field), value);
    }

    public static <O, T> UnsafeFieldAccess<O, T> findField(Class<O> clazz, String name) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equals(name))
                return new UnsafeFieldAccess<O, T>(UNSAFE.objectFieldOffset(f));
        }
        return null;
    }

    public static <O> UnsafeFieldAccess.Int<O> findIntField(Class<O> clazz, String name) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equals(name))
                return new UnsafeFieldAccess.Int<O>(UNSAFE.objectFieldOffset(f));
        }
        return null;
    }

    public static <O> UnsafeFieldAccess.Bool<O> findBooleanField(Class<O> clazz, String name) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equals(name))
                return new UnsafeFieldAccess.Bool<O>(UNSAFE.objectFieldOffset(f));
        }
        return null;
    }

    public static void setAccessible(AccessibleObject target) {
        SETACCESSIBLE.accept(target);
    }

    /* =======================================================================
     * =======================================================================
     */
    static Unsafe theUnsafe() {
        return UNSAFE;
    }

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe)theUnsafe.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return sneak(e);
        }
    }

    private static Consumer<AccessibleObject> getSetAccessible() {
        try {
            try {
                Method setAccessible0 = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
                setAccessibleFallback(setAccessible0);
                return acc -> {
                    try {
                        setAccessible0.invoke(acc, true);
                    } catch (Exception e) {
                        sneak(e);
                    }
                };
            } catch (NoSuchMethodException nm) {
                // In java 8 it was a static method
                Method setAccessible0 = AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class);
                setAccessibleFallback(setAccessible0);
                return acc -> {
                    try {
                        setAccessible0.invoke(acc, true);
                    } catch (Exception e) {
                        sneak(e);
                    }
                };
            }
        } catch (Exception e) {
            return sneak(e);
        }
    }

    private static void setAccessibleFallback(AccessibleObject obj) {
        if (module != null) { // Java 9+ Lets pretend to be the java.base module
            Object old = module.get(UnsafeHacks.class);
            Object base = module.get(Object.class);
            module.set(UnsafeHacks.class, base);

            obj.setAccessible(true);

            // Put ourselves back just to be nice
            module.set(UnsafeHacks.class, old);
        } else { // lets just hope there is no security manager installed;
            obj.setAccessible(true);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable, R> R sneak(Throwable e) throws E {
        throw (E)e;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object inst) {
        return (T)inst;
    }
}
