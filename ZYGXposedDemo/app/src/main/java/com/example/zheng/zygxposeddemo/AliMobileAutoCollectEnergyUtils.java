package com.example.zheng.zygxposeddemo;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

class AliMobileAutoCollectEnergyUtils {

    public static Object curH5Fragment;
    public static Object curH5PageImpl;

    public static boolean isRankList(String response) {


        return false;
    }

    public static boolean isUserDetail(String response) {
        return false;
    }

    public static void autoGetCanCollectUserIdList(ClassLoader loader, String response) {
       boolean isSuccess =  paseFriendRankPageDataResponse(response);
    }

    private static boolean paseFriendRankPageDataResponse(String response) {
        return false;
    }

    public static void autoGetCanCollectBubbleIdList(ClassLoader loader, String response) {

    }

    public static Method getRpcCallMethod(ClassLoader loader) {

        try {
            Field aF = curH5Fragment.getClass().getDeclaredField("a");
            aF.setAccessible(true);
            Object viewHolder = aF.get(curH5Fragment);
            Field hF = viewHolder.getClass().getDeclaredField("h");
            hF.setAccessible(true);
            curH5PageImpl = hF.get(viewHolder);
            if (curH5PageImpl != null) {
                Class<?> H5RpcUtilclazz = loader.loadClass("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil");
                final Class<?> m_H5Page = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
                final Class<?> m_JSONObject = loader.loadClass("com.alibaba.fastjson.JSONObject");

                Method method = H5RpcUtilclazz.getMethod("rpcCall", String.class, String.class, String.class, boolean.class,
                        m_JSONObject, String.class, boolean.class, m_H5Page, int.class, String.class, boolean.class, int.class, String.class);
                return method;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
