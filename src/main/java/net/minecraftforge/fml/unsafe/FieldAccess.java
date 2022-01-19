package net.minecraftforge.fml.unsafe;

public interface FieldAccess<V>
{

    V get(Object instance);

    default V getStatic()
    {
        return get(null);
    }

}
