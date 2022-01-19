package net.minecraftforge.fml.unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;
import sun.security.util.ECUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnsafeTest {
    @Test
    public void test() throws Throwable
    {
        MethodHandles.Lookup privateLookup = PrivateLookupFactory.privateLookupIn(ECUtil.class);
        MethodHandle constructor = privateLookup.findConstructor(ECUtil.class, methodType(void.class));
        ECUtil instance = (ECUtil)constructor.invoke();
        Assertions.assertNotEquals(instance, null);
    }

}
