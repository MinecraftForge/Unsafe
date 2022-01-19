package net.minecraftforge.fml.unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Hashtable;
import java.util.jar.JarFile;

import static java.lang.invoke.MethodType.methodType;
import sun.security.util.ECUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnsafeTest {

    @Test
    public void testPrivateLookupIn() throws Throwable
    {
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(ECUtil.class);
        MethodHandle constructor = privateLookup.findConstructor(ECUtil.class, methodType(void.class));
        ECUtil instance = (ECUtil)constructor.invoke();
        Assertions.assertNotEquals(instance, null);
    }

    @Test
    public void testConstructorAccess() throws Throwable
    {
        Class<?> jarVerifierCls = Class.forName("java.util.jar.JarVerifier");
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(jarVerifierCls);
        MethodAccess<?> constructor = UnsafeHacks.findConstructor(privateLookup, jarVerifierCls, String.class, byte[].class);
        constructor.invokeStatic(JarFile.MANIFEST_NAME, new byte[0] );
    }

    @Test
    public void testStaticFieldAccess() throws Throwable
    {
        Class<?> jarVerifierCls = Class.forName("java.util.jar.JarVerifier");
        Class<?> debugCls = Class.forName("sun.security.util.Debug");
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(jarVerifierCls);
        FieldAccess<?> fieldAccess = UnsafeHacks.findStaticField(privateLookup, jarVerifierCls, "debug", debugCls);
        fieldAccess.getStatic();
    }

    @Test
    public void testNonStaticFieldAccess() throws Throwable
    {
        Class<?> jarVerifierCls = Class.forName("java.util.jar.JarVerifier");
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(jarVerifierCls);

        MethodAccess<?> constructor = UnsafeHacks.findConstructor(privateLookup, jarVerifierCls, String.class, byte[].class);
        Object instance = constructor.invokeStatic(JarFile.MANIFEST_NAME, new byte[0]);

        FieldAccess<String> fieldAccess = UnsafeHacks.findField(privateLookup, jarVerifierCls, "manifestName", String.class);
        Assertions.assertEquals(fieldAccess.get(instance), JarFile.MANIFEST_NAME);
    }

    @Test
    public void testStaticMethodAccess() throws Throwable
    {
        Class<?> jarVerifierCls = Class.forName("java.util.jar.JarVerifier");
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(jarVerifierCls);
        MethodAccess<Boolean> method = UnsafeHacks.findStaticMethod(privateLookup, jarVerifierCls, "isSigningRelated", boolean.class, String.class);
        method.invokeStatic("foo");
    }

    @Test
    public void testNonStaticMethodAccess() throws Throwable
    {
        Class<?> jarVerifierCls = Class.forName("java.util.jar.JarVerifier");
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(jarVerifierCls);

        MethodAccess<?> constructor = UnsafeHacks.findConstructor(privateLookup, jarVerifierCls, String.class, byte[].class);
        Object instance = constructor.invokeStatic(JarFile.MANIFEST_NAME, new byte[0]);

        MethodAccess<Boolean> method = UnsafeHacks.findMethod(privateLookup, jarVerifierCls, "isTrustedManifestEntry", boolean.class, String.class);
        method.invoke(instance, "foo");
    }

}
