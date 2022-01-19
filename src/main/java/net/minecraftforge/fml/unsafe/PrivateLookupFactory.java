package net.minecraftforge.fml.unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodType.methodType;

/**
 * Allows creating {@link java.lang.invoke.MethodHandles.Lookup} objects with private access in a given class.
 * This privileged Lookup object can now access private members in the target class.
 *
 * This abstracts behavior across Java 8 and Java 9+.
 * In Java 8 reflective access to MethodHandles.Lookup.IMPL_LOOKUP is used.
 * In Java 9 and above MethodHandles.privateLookupIn is used, which requires that if the target class is in a module
 * --add-opens arguments are added so that the target module exports the package of the target class to the module of PrivateLookupFactory, which is
 * ALL-UNNAMED by default.
 */
public final class PrivateLookupFactory
{

    private static final MethodHandles.Lookup MY_LOOKUP = MethodHandles.lookup();
    private static final MethodHandle IMPL;

    static {
        MethodHandle impl;
        try
        {
            try
            {
                // Java 9+
                impl = MethodHandles.publicLookup().findStatic(MethodHandles.class, "privateLookupIn", methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
            }
            catch (NoSuchMethodException e)
            {
                try
                {
                    // java 8
                    Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    field.setAccessible(true);
                    impl = MethodHandles.dropArguments(
                            MethodHandles.publicLookup().unreflectGetter(field),
                            0, Class.class, MethodHandles.Lookup.class
                    );
                }
                catch (NoSuchFieldException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Necessary MethodHandles fields/methods not public or not as expected", e);
        }
        IMPL = impl;
    }

    public static MethodHandles.Lookup privateLookupIn(Class<?> targetClass)
    {
        try
        {
            return (MethodHandles.Lookup) IMPL.invokeExact(targetClass, MY_LOOKUP);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

}
