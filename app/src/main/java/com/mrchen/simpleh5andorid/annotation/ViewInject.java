package com.mrchen.simpleh5andorid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElementType、RetentionPolicy是两个枚举类，摘自JDK1.6文档
 * ElementType.FIELD表示我们需要注解的是一个字段(包括枚举常量)
 * RetentionPolicy.RUNTIME表示编译器将把注释记录在类文件中，在运行时VM将保留注释，因此可以反射性地读取。
 * 

 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
	/** 控件 id */
	int id();// 定义部分的id() 表示注解接受一个int类型的数据作为id所对应的值（就如使用中的id = R.id.xxx）;

	/** 是否注册点击事件 */
	boolean click() default false;// 定义部分的click表示接受一个Boolean类型的数据作为click对应的值，还可以设置一个默认值使用default修饰；

}