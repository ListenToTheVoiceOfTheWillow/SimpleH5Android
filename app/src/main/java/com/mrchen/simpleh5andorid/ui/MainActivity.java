package com.mrchen.simpleh5andorid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.WebViewHelper;
import com.mrchen.simpleh5andorid.R;
import com.mrchen.simpleh5andorid.annotation.ViewHelper;
import com.mrchen.simpleh5andorid.annotation.ViewInject;
import com.mrchen.simpleh5andorid.base.BaseActivity;
import com.mrchen.simpleh5andorid.configure.NetConfigure;
import com.mrchen.simpleh5andorid.netdeal.H5CallAppDeal;
import com.mrchen.simpleh5andorid.utils.SoftKeyboardFixerForFullscreen;

import org.json.JSONException;
import org.json.JSONObject;

public class  MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.bridgeWebView, click = true)
    private BridgeWebView bridgeWebView;
    @ViewInject(id = R.id.tv_setting, click = true)
    private TextView tv_setting;



    private CallBackFunction function;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewHelper.inject(this);
        initView(savedInstanceState);
        SoftKeyboardFixerForFullscreen.assistActivity(this);
    }

    @Override
    public Activity getContext() {
        return this;
    }

    //初始化组件

    @SuppressLint("NewApi")
    private void initView(Bundle savedInstanceState) {
        bridgeWebView.registerHandler("jsCallApp", (data, function) -> initJsCallData(data, function));
        bridgeWebView.setVerticalScrollBarEnabled(true);
        if (savedInstanceState != null) {
            bridgeWebView.restoreState(savedInstanceState);
        }else {
            bridgeWebView.loadUrl(NetConfigure.INDEXURL);
        }
    }



    //h5调用原生
    private void initJsCallData(String data, CallBackFunction function) {
         Log.i("h5CallAndroid","拿到值"+data);
        H5CallAppDeal.dealData(data,function);

    }
    //原生调用h5
    public synchronized void notiH5(String content) {
        bridgeWebView.callHandler("appCallJs", content, data -> h5CallBack(data));
    }

    //原生调用h5的回调
    private void h5CallBack(String data) {

    }


    @SuppressLint({"HandlerLeak", "NewApi"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:

                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        bridgeWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bridgeWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewHelper.clearCache(mContext, bridgeWebView);
        if (null != bridgeWebView) {
            bridgeWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            bridgeWebView.clearHistory();
            ((ViewGroup) bridgeWebView.getParent()).removeView(bridgeWebView);
            bridgeWebView.removeAllViews();
            bridgeWebView.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



//    private long firstTime = 0l;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            String url = bridgeWebView.getUrl();
//
//            if (url != null ) {
//                    long secondTime = System.currentTimeMillis();
//                    if (secondTime - firstTime > 3000) {
//                        Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
//                        firstTime = secondTime;
//                        return true;
//                    }
//                 else {
//                    try {
//                        if (null != bridgeWebView && bridgeWebView.canGoBack())
//                            bridgeWebView.goBack();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//            }


                    try {
                        if (null != bridgeWebView && bridgeWebView.canGoBack())
                            bridgeWebView.goBack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
        }
            return super.onKeyDown(keyCode, event);

    }
}