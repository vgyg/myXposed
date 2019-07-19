package com.example.zheng.zygxposeddemo;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Test implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.e("xposed-log","====================");
        Log.e("xposed-log",lpparam.packageName);
        if("com.eg.android.AlipayGphone".contains(lpparam.packageName)){

        }
        XposedHelpers.findAndHookMethod(Runtime.class,"doload",String.class,ClassLoader.class,());

    }
}
