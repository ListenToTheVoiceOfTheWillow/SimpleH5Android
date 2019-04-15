package com.mrchen.simpleh5andorid.annotation;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * 查找控件id与设置空间点击事件
 * 

 *
 */
public class ViewHelper {
	/**
	 * 初始化fragment
	 * 
	 * @param object
	 * @param view
	 */
	public static void inject(Object object, View view) {
		injectObject(object, view);
	}

	/**
	 * 初始化activity
	 * 
	 * @param object
	 */
	public static void inject(Object object) {
		injectObject(object, null);
	}

	/**
	 * 查找控件id
	 * 
	 * @param handler
	 */
	private static void injectObject(Object handler, View view) {
		Class<?> handlerType = handler.getClass();
		// inject view
		Field[] fields = handlerType.getDeclaredFields();// 获得Activity中声明的字段
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				// 查看这个字段是否有我们自定义的注解类标志的
				if (field.isAnnotationPresent(ViewInject.class)) {
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject != null) {
						if (0 != viewInject.id()) {
							try {
								View findView = null;
								if (handler instanceof Activity) {
									findView = ((Activity) handler).findViewById(viewInject.id());
								} else if (handler instanceof Dialog) {
									findView = ((Dialog) handler).findViewById(viewInject.id());
								} else if (handler instanceof ViewGroup) {
									findView = ((ViewGroup) handler).findViewById(viewInject.id());
								} else if (null != view) {
									// view---用于fragment
									findView = view.findViewById(viewInject.id());
								}
								if (findView != null) {
									field.setAccessible(true);
									// 将handler的field赋值为findView
									field.set(handler, findView);
									if (viewInject.click()) {
										findView.findViewById(viewInject.id()).setOnClickListener((OnClickListener) handler);
									}
								}
							} catch (Throwable e) {
							}
						}
					}
				}
			}
		}
	}
}
