package com.buydeem.bing.utils.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zengchao on 2019/12/19.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFile {
  /**
   * 字段名
   * @return
   */
  String fieldName();

  /**
   * @return
   */
  String dateFormat() default "yyyy-MM-dd HH:mm:ss";
}
