package com.buydeem.bing.utils;

import com.buydeem.bing.mode.Image;
import com.buydeem.bing.utils.sql.FieldIgnore;
import com.buydeem.bing.utils.sql.Table;
import com.buydeem.bing.utils.sql.TableFile;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zengchao on 2019/12/19.
 */
public class SqlUtils {

  /**
   * 创建插入sql
   *
   * @param entity 实体
   * @return
   */
  public static String insertSql(Object entity) {
    try {
      Class<?> clazz = entity.getClass();
      String tableName = getTableName(clazz);
      Field[] fields = clazz.getDeclaredFields();
      Map<String,String> map = new LinkedHashMap<>();
      for (Field field : fields) {
        field.setAccessible(true);
        if (field.getAnnotation(FieldIgnore.class) == null && field.get(entity) != null){
          String fileName = getFileName(field);
          String fileValue = getFileValue(field, entity);
          map.put(fileName,fileValue);
        }
      }
      if (map.isEmpty()){
        throw new RuntimeException("没有需要插入的数据");
      }
      StringBuilder builder = new StringBuilder("INSERT INTO ").append(tableName).append(" ( ");
      map.keySet().forEach(key -> builder.append(key).append(","));
      builder.deleteCharAt(builder.length()-1).append(") VALUES (");
      map.keySet().forEach(key -> builder.append(map.get(key)).append(","));
      builder.deleteCharAt(builder.length()-1).append(")");
      return builder.toString();
    } catch (Exception e){
      throw new RuntimeException("构建sql异常");
    }
  }

  /**
   * 获取表名
   *
   * @param clazz
   * @return
   */
  public static String getTableName(Class<?> clazz) {
    Table table = clazz.getAnnotation(Table.class);
    return Objects.isNull(table) ? clazz.getSimpleName() : table.tableName();
  }

  /**
   * 获取字段名称
   *
   * @param field
   * @return
   */
  public static String getFileName(Field field) {
    TableFile annotation = field.getAnnotation(TableFile.class);
    return Objects.isNull(annotation) ? field.getName() : annotation.fieldName();
  }

  /**
   * 获取对象字段值
   * @param field
   * @param o
   * @return
   */
  public static String getFileValue(Field field, Object o) throws IllegalAccessException {
    String typeName = field.getType().getName();
    TableFile annotation = field.getAnnotation(TableFile.class);
    String format = Objects.isNull(annotation) ? "yyyy-MM-dd HH:mm:ss" : annotation.dateFormat();
    switch (typeName) {
      case "java.lang.Integer":
      case "int": {
        return String.valueOf(field.get(o));
      }
      case "java.lang.Double":
      case "double": {
        return String.valueOf(field.get(o));
      }
      case "java.lang.Float":
      case "float": {
        return String.valueOf(field.get(o));
      }
      case "java.lang.Long":
      case "long": {
        return String.valueOf(field.get(o));
      }
      case "java.lang.Short":
      case "short": {
        return String.valueOf(field.get(o));
      }
      case "java.lang.Boolean":
      case "boolean": {
        return (boolean)field.get(o) ? "1" : "0";
      }
      case "java.lang.Character":{
        return "'"+String.valueOf(field.getChar(o))+"'";
      }
      case "java.lang.String":{
        return "'"+field.get(o)+"'";
      }
      case "java.util.Date":{
        return "'"+DateTimeUtils.date2String((Date) field.get(o),format)+"'";
      }
      case "java.math.BigDecimal":{
        return field.get(o).toString();
      }
      default: {
        throw new UnsupportedOperationException("不支持的字段类型 : "+typeName);
      }
    }
  }

}
