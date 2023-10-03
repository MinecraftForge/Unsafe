/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.unsafe;

public class UnsafeFieldAccess<Owner, Type> {
    protected final long index;

    UnsafeFieldAccess(long index) {
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public <I extends Owner> Type get(I instance) {
        return (Type)UnsafeHacks.theUnsafe().getObject(instance, index);
    }

    public <I extends Owner> void set(I instance, Type value) {
        UnsafeHacks.theUnsafe().putObject(instance, index, value);
    }

    public static class Int<Owner> extends UnsafeFieldAccess<Owner, Integer> {
        Int(long index) {
            super(index);
        }

        @Override
        public <I extends Owner> Integer get(I instance) {
            return getInt(instance);
        }

        public <I extends Owner> int getInt(I instance) {
            return UnsafeHacks.theUnsafe().getInt(instance, index);
        }

        @Override
        public <I extends Owner> void set(I instance, Integer value) {
            setInt(instance, value);
        }

        public <I extends Owner> void setInt(I instance, int value) {
            UnsafeHacks.theUnsafe().putInt(instance, index, value);
        }
    }

    public static class Bool<Owner> extends UnsafeFieldAccess<Owner, Boolean> {
        Bool(long index) {
            super(index);
        }

        @Override
        public <I extends Owner> Boolean get(I instance) {
            return getBoolean(instance);
        }

        public <I extends Owner> boolean getBoolean(I instance) {
            return UnsafeHacks.theUnsafe().getBoolean(instance, index);
        }

        @Override
        public <I extends Owner> void set(I instance, Boolean value) {
            setBoolean(instance, value);
        }

        public <I extends Owner> void setBoolean(I instance, boolean value) {
            UnsafeHacks.theUnsafe().putBoolean(instance, index, value);
        }
    }
}
