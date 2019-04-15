package com.github.lzyzsd.jsbridge;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * WebView 工具类
 *
 * @Author: GaiQS
 * @E-mail: gaiqs@sina.com
 * @Date: 2017-08-13
 * @Time: 11:01
 * @DateModified: 2017-09-27
 * @Hint:
 * @Param <T>
 * FIXME
 */
public class WebViewHelper {
    /**
     * WebView Setting init
     *
     * @param webView
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void initWebviewSetting(Context context, final WebView webView) {
        webView.clearCache(true);
        //支持获取手势焦点，输入用户名、密码或其他
        webView.requestFocusFromTouch();
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8"); // 设置文本编码
        webSettings.setJavaScriptEnabled(true);// 启用JavaScript支持
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 允许js弹出窗口,支持通过JS打开新窗口

//        启用或禁用文件访问。默认启用文件访问
        webSettings.setAllowFileAccess(true);
//        WebView是否下载图片资源，默认为true。注意，该方法控制所有图片的下载，包括使用URI嵌入的图片（使用setBlockNetworkImage(boolean) 只控制使用网络URI的图片的下载）。
//        如果该设置项的值由false变为true，WebView展示的内容所引用的所有的图片资源将自动下载。
        webSettings.setLoadsImagesAutomatically(true);

        // 设置滚动条位置，一般处理webview显示网页白边问题
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//      调用requestFocus(int, Android.graphics.Rect)时是否需要设置节点获取焦点，默认值为true。
        webSettings.setNeedInitialFocus(true);

//        提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

//        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
//        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
//        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        // LayoutAlgorithm是一个枚举，用来控制html的布局，总共有三种类型：
        // NORMAL：正常显示，没有渲染变化。
        // SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。
        // NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 设置WebView是否启用视口支持，设置自适应屏幕，两者合用
        // false=布局宽度总是被设置为在设备无关的WebView空间宽度cdd像素
        // true==会使用网页标签中制定的宽度，如果该页不包含标记或者宽度，将使用广泛的视口
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 这方法可以让你的页面适应手机屏幕的分辨率，完整的显示在屏幕上，可以放大缩小, 两种方法都试过，推荐使用第二种方法

        webSettings.setBlockNetworkImage(false);//是否禁止从网络（通过http和https URI schemes访问的资源）下载图片资源
        webSettings.setBlockNetworkLoads(false);//是否禁止从网络下载数据，如果app有INTERNET权限，默认值为false，否则默认为true

//        WebView中Http和Https混合问题 ,在Android 5.0上 Webview 默认不允许加载 Http 与 Https 混合内容：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
//            MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
//            MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
//            在5.0以下 Android 默认是 全允许，但是到了5.0以上，就是不允许，实际情况下很我们很难确定所有的网页都是https的，所以就需要这一步的操作。
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        WebView是否支持使用屏幕上的缩放控件和手势进行缩放，默认值true。
//        设置setBuiltInZoomControls(boolean)可以使用特殊的缩放机制。该项设置不会影响zoomIn() and zoomOut () 的缩放操作。
        webSettings.setSupportZoom(false);//支持缩放，默认为true。是下面那个的前提。
//        是否使用内置的缩放机制。内置的缩放机制包括屏幕上的缩放控件（浮于WebView内容之上）和缩放手势的运用。
//        通过setDisplayZoomControls(boolean)可以控制是否显示这些控件，默认值为false。
        webSettings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false);// 设置WebView是否显示缩放控件按钮，默认是true
        }
        webSettings.setTextZoom(100);//设置文本的缩放倍数，默认为 100

        // 通过加载完成后启动硬件渲染加速
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        String dir = context.getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);

        initSameDomainPolicy(webView);
        newWin(webSettings);
        saveData(context, webSettings);
    }

    /**
     * 允许跨域
     * http://blog.csdn.net/super_spy/article/details/52766411
     * http://stackoverflow.com/questions/11318703/access-control-allow-origin-error-at-android-4-1
     *
     * @param webView
     */
    private static void initSameDomainPolicy(final WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多窗口的问题
     * 设置这里是为了解决百度广告iframe跳转的问题,主要用在Android4.4.2以下版本 在Android
     * 4.4以下(不包含4.4)系统WebView底层实现是采用WebKit(http://www.webkit.org/)内核，
     * 而在Android 4.4及其以上Google
     * 采用了chromium(http://www.chromium.org/)作为系统WebView的底层内核支持
     * setSupportMultipleWindows默认的是false，也就是说WebView默认不支持新窗口，
     * 但是这个不是说WebView不能打开多个页面了，
     * 只是你点击页面上的连接，当它的target属性是_blank时。它会在当前你所看到的页面继续加载那个连接。而不是重新打开一个窗口。
     * 当你设置为true时，就代表你想要你的WebView支持多窗口，但是一旦设置为true，
     * 必须要重写WebChromeClient的onCreateWindow方法。
     *
     * @param mWebSettings
     */
    private static void newWin(WebSettings mWebSettings) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下,然后 复写 WebChromeClient的onCreateWindow方法
            mWebSettings.setSupportMultipleWindows(false);
        }
    }

    /**
     * HTML5数据存储,WebView的缓存可以分为页面缓存和数据缓存
     * 说明参考http://blog.csdn.net/a345017062/article/details/8703221
     *
     * @param webSettings
     */
    private static void saveData(Context context, WebSettings webSettings) {
//        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
//        if (NetStatusUtil.isConnected(App.getContext())) {
//            // 默认加载方式，使用这种方式，会实现快速前进后退，在同一个标签打开几个网页后，关闭网络时，可以通过前进后退来切换已经访问过的数据，同时新建网页需要网络
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
//        } else {
//            // 这个方式不论如何都会从缓存中加载，除非缓存中的网页过期，出现的问题就是打开动态网页时，不能时时更新，会出现上次打开过的状态，除非清除缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
//        }
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            String appCachePath = cacheDir.getAbsolutePath();
            webSettings.setDomStorageEnabled(true);// 开启DOM storage API 功能
            webSettings.setDatabaseEnabled(true);// 开启database storage API功能
            webSettings.setAppCacheEnabled(true);// 开启Application Cache功能
            // 设置数据库缓存路径,已废弃，数据库路径由实现（implementation）管理，调用此方法无效。
//            webSettings.setDatabasePath(appCachePath); // API 19 deprecated
            // 设置Application caches缓存目录
            webSettings.setAppCachePath(appCachePath);
        }
    }

    /**
     * 清空所有Cookie
     *
     * @param paramContext
     * @param bridgeWebView
     */
    public static void clearCache(Context paramContext, BridgeWebView bridgeWebView) {
        if (paramContext != null && bridgeWebView != null) {
            try {
                CookieSyncManager.createInstance(paramContext);  //Create a singleton CookieSyncManager within a context
                CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
                cookieManager.removeAllCookie();// Removes all cookies.
                CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
                bridgeWebView.setWebChromeClient(null);
                bridgeWebView.setWebViewClient(null);
                bridgeWebView.getSettings().setJavaScriptEnabled(false);
                bridgeWebView.clearCache(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置WebView跳转浏览器下载
     *
     * @param webView
     */
    public static void initDownloadListener(final WebView webView) {
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                webView.getContext().startActivity(intent);
            }
        });
    }
}
