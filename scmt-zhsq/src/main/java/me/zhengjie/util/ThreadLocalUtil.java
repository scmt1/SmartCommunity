package me.zhengjie.util;

public class ThreadLocalUtil {

    private final static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void add(Integer userId) {
        threadLocal.set(userId);
    }

    public static Integer getUserId() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
