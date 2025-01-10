package cn.hush.types.utils;

/**
 * @author Hush
 * @description
 * @create 2025-01-09 上午3:52
 */

public class BaseContext {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setName(String name) {
        threadLocal.set(name);
    }

    public static String getName() {
        return threadLocal.get();
    }

    public static void removeName() {
        threadLocal.remove();
    }

}
