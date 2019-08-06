package com.example.zheng.zygxposeddemo;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Test implements IXposedHookLoadPackage {
    public static final String TAG = "alilog-H5RpcUtil";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.e("xposed-log", "====================");
        if ("com.eg.android.AlipayGphone".contains(lpparam.packageName)) {
            final ClassLoader loader = lpparam.classLoader;
            Class clazz = loader.loadClass("com.alipay.mobile.nebula.util.H5Log");
            if (clazz != null) {
                XposedHelpers.findAndHookMethod(clazz, "d", String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.i("alilog", param.args[0] + " " + param.args[1]);
                    }

                });
            }

            final Class<?> m_H5RpcUtil = loader.loadClass("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil");
            final Class<?> m_H5Page = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            final Class<?> m_JSONObject = loader.loadClass("com.alibaba.fastjson.JSONObject");
            Log.i(TAG, "H5RpcUtil : " + m_H5RpcUtil + " ");
            Log.i(TAG, "m_H5Page : " + m_H5Page + " ");
            Log.i(TAG, "m_JSONObject : " + m_JSONObject + " ");
            if (m_H5RpcUtil != null && m_H5Page != null && m_JSONObject != null) {
                //String str, String str2, String str3, boolean z, JSONObject jSONObject, String str4, boolean z2, H5Page h5Page, int i, String str5, boolean z3, int i2, String str6
                XposedHelpers.findAndHookMethod(m_H5RpcUtil, "rpcCall", String.class, String.class, String.class, boolean.class,
                        m_JSONObject, String.class, boolean.class, m_H5Page, int.class, String.class, boolean.class, int.class, String.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                Log.i(TAG, "param :" + param.args[0] + "," + param.args[1] + "," +
                                        param.args[2] + "," + param.args[3] + "," + param.args[4] + "," +
                                        param.args[5] + "," + param.args[6] + "," + param.args[7] + "," +
                                        param.args[8] + "," + param.args[9] + "," + param.args[10] + "," +
                                        param.args[11]);
                                Log.i(TAG, "result :" + param.getResult());
                                Log.i(TAG, "object :" + param.thisObject);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                Object resp = param.getResult();
                                Method method = resp.getClass().getMethod("getResponse", new Class<?>[]{});
                                String response = (String) method.invoke(resp, new Object[]{});
                                Log.i(TAG, "response: " + response);

                                JSONObject jsonObject = new JSONObject(response);
                                jsonObject.has("friendRanking");
                                if (AliMobileAutoCollectEnergyUtils.isRankList(response)) {
                                    AliMobileAutoCollectEnergyUtils.autoGetCanCollectUserIdList(loader, response);
                                }

//                                AliMobileAutoCollectEnergyUtils.isUserDetail(response);


                                rpcCall_collectEnergy();
                                rpcCall_queryNextAction();
                                rpcCall_pageQueryDynamics(loader, );


//                                AliMobileAutoCollectEnergyUtils.autoCollectEnergy(loader, response);
                            }
                        });

            }


            Class<?> H5FragmentManager_clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5FragmentManager");
            Class<?> H5Fragment_clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Fragment");
            if (H5FragmentManager_clazz != null) {
                XposedHelpers.findAndHookMethod(H5FragmentManager_clazz, "pushFragment", H5Fragment_clazz, boolean.class, Bundle.class, boolean.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.i(TAG, "cur fragment " + param.args[0]);
                        AliMobileAutoCollectEnergyUtils.curH5Fragment = param.args[0];
                    }
                });

            }

        }

    }


    /*
     收取能量
     loader
     bubbleIds
     userId
     */
    public static void rpcCall_collectEnergy(ClassLoader loader, Integer bubbleIds, String userId) {
        try {
            Class<?> H5RpcUtilclazz = loader.loadClass("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil");
            final Class<?> m_H5Page = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            final Class<?> m_JSONObject = loader.loadClass("com.alibaba.fastjson.JSONObject");
            if (H5RpcUtilclazz != null) {
                Method rpcCall = AliMobileAutoCollectEnergyUtils.getRpcCallMethod(loader);
                JSONArray jsonArray = new JSONArray();
                JSONArray bubbleId = new JSONArray();
                bubbleId.put(bubbleIds);
                JSONObject jo = new JSONObject();
                jo.put("userId", userId);
                jsonArray.put(bubbleId);
                jsonArray.put(jo);
                Object resp = rpcCall.invoke(null, "alipay.antmember.forest.h5.collectEnergy",
                        jsonArray.toString(), "", true, "{}", null, false, AliMobileAutoCollectEnergyUtils.curH5PageImpl, 0, "", false, -1);

                Method method = resp.getClass().getMethod("getResponse", new Class<?>[]{});
                String response = (String) method.invoke(resp, new Object[]{});
                Log.i(TAG, "response: " + response);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //alipay.antmember.forest.h5.queryNextAction,[{"canRobFlags":"T,F,F","userId":"2088312508547030","version":"20181220"}]
// ,,true,{},null,false,com.alipay.mobile.nebulacore.core.H5PageImpl@aca4894,0,,false,-1
    public static void rpcCall_queryNextAction(ClassLoader loader, String userId) {
        try {
            Class<?> H5RpcUtilclazz = loader.loadClass("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil");
            final Class<?> m_H5Page = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            final Class<?> m_JSONObject = loader.loadClass("com.alibaba.fastjson.JSONObject");
            if (H5RpcUtilclazz != null) {
                Method rpcCall = AliMobileAutoCollectEnergyUtils.getRpcCallMethod(loader);
                JSONArray jsonArray = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("canRobFlags", "T,F,F");
                jo.put("userId", userId);
                jo.put("version", "20181220");
                jsonArray.put(jo);
                rpcCall.invoke(null, "alipay.antmember.forest.h5.queryNextAction",
                        jsonArray.toString(), "", true, "{}", null, false, AliMobileAutoCollectEnergyUtils.curH5PageImpl, 0, "", false, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void rpcCall_pageQueryDynamics(ClassLoader loader, String userId) {
        try {
            Class<?> H5RpcUtilclazz = loader.loadClass("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil");
            final Class<?> m_H5Page = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            final Class<?> m_JSONObject = loader.loadClass("com.alibaba.fastjson.JSONObject");
            if (H5RpcUtilclazz != null) {
                Method rpcCall = H5RpcUtilclazz.getDeclaredMethod("rpcCall", String.class, String.class, String.class, boolean.class,
                        m_JSONObject, String.class, boolean.class, m_H5Page, int.class, String.class, boolean.class, int.class, String.class);

                JSONArray jsonArray = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("pageSize", 10);
                jo.put("startIndex", 0);
                jo.put("userId", userId);
                jsonArray.put(jo);
                rpcCall.invoke(null, "alipay.antmember.forest.h5.pageQueryDynamics",
                        jsonArray.toString(), "", true, "{}", null, false, AliMobileAutoCollectEnergyUtils.curH5PageImpl, 0, "", false, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
