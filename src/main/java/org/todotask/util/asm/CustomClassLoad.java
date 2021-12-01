package org.todotask.util.asm;

public class CustomClassLoad extends ClassLoader{

    public Class<?> defineClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }

}
