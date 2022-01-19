package net.minecraftforge.fml.unsafe;

public interface MethodAccess<R>
{

    R invoke(Object instance, Object... args);

    default R invokeStatic(Object... args)
    {
        return invoke(null, args);
    }

}
