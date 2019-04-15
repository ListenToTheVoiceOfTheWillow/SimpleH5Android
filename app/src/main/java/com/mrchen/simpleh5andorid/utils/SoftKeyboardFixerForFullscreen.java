package com.mrchen.simpleh5andorid.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class SoftKeyboardFixerForFullscreen {
    public static  void assistActivity(Activity activity){
        new SoftKeyboardFixerForFullscreen(activity);
    }
    private View mContentView;//我们设置的contentView

    private FrameLayout.LayoutParams mFrameLayoutParams;//我们设置的contentView的layoutParams

    private int barStatusHeight = 0;//状态栏高度

    private int lastUsableHeight = 0;//上一次的可用高度

    private int lastUsableWidth = 0;//上一次的可用宽度

    private SoftKeyboardFixerForFullscreen(final Activity activity){
        barStatusHeight = getStatusBarHeight(activity);
      final FrameLayout content=activity.findViewById(android.R.id.content);
      mContentView=content.getChildAt(0);
      mFrameLayoutParams= (FrameLayout.LayoutParams) mContentView.getLayoutParams();
      mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
              int heightRoot=content.getRootView().getHeight();
              int heightDecor=content.getHeight();
              int usableHeight=computeUsableHeight();
              if (usableHeight!=lastUsableHeight){
                  lastUsableHeight=usableHeight;
                  int heightDifference=heightDecor-usableHeight;
                  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N&&activity.isInMultiWindowMode()){
                      if (heightDifference>0){
                          setHeight(heightDecor-heightDifference);
                      }else {
                          setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
                      }
                  }else {
                      if (heightDifference>(heightDecor/4)){
                          if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                              setHeight(heightDecor-heightDifference+barStatusHeight);
                          }else {
                              setHeight(heightDecor-heightDifference);
                          }
                      }else {
                          setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
                      }


                  }

              }


          }
      });


    }
    /**     * 获取状态栏高度     *
     *  * @param context context
     *  * @return 状态栏高度
     *  */
    private static int getStatusBarHeight(Activity context) {
        // 获得状态栏高度
        return getBarHeight(context, STATUS_BAR_HEIGHT_RES_NAME);
    }

    /**     * 获取Bar高度
     *  *
     *  * @param context context
     *  * @param barName 名称
     *  * @return Bar高度
     *  */
    private static int getBarHeight(Context context, String barName) {
        // 获得状态栏高度
            int resourceId = context.getResources().getIdentifier(barName, "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
    }
    //下面相关代码来自：https://github.com/yy1300326388/AndroidBarUtils/blob/master/app/src/main/java/cn/zsl/androidbarutils/utils/AndroidBarUtils.java
    // 完整代码，全屏时有问题。
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";


    private int computeUsableHeight() {
        Rect r = new Rect();
        mContentView.getWindowVisibleDisplayFrame(r);
        // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
             return (r.bottom - r.top);
    }
    private void setHeight(int height) {
        if (mFrameLayoutParams.height != height) {
            //不必要的更新就不要了
               mFrameLayoutParams.height = height;
               mContentView.requestLayout();//触发布局更新
        }
    }

}
