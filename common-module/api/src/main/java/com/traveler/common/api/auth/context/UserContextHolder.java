package com.traveler.common.api.auth.context;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> USER_CONTEXT = new ThreadLocal<>();

    public static void setContext(UserContext context) {
        USER_CONTEXT.set(context);
    }

    public static UserContext getContext() {
        return USER_CONTEXT.get();
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }
}
