package com.buydeem.bing;

import com.buydeem.bing.utils.DateTimeUtils;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystem;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Created by zengchao on 2019/12/13.
 */
@Slf4j
public class AppStart {
  public static void main(String[] args) {
    Map<String,String> map = new HashMap<>();
    for (String arg : args) {
      log.debug(arg);
    }
    try {
      for (int i = 0; i < 2; i++) {
        String key = args[2*i];
        String value = args[2*i+1];
        map.put(key,value);
      }
      VertxOptions options = new VertxOptions()
        .setWorkerPoolSize(40);
      Vertx vertx = Vertx.vertx(options);
      Date date = DateTimeUtils.string2Date(Optional.of(map.get("-t")).get(), DateTimeUtils.TIME);
      String path = Optional.of(map.get("-p")).get();
      createPath(vertx.fileSystem(),path);
      vertx.deployVerticle(new BingGetVerticle(path,date));
      System.out.println("vertx = " + vertx);
    }catch (Exception e){
      errorInfo();
      return;
    }
  }

  /**
   * 打印错误信息
   */
  private static void errorInfo(){
    InputStream in = AppStart.class.getClassLoader().getResourceAsStream("how_to_use_info.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    reader.lines().forEach(log::error);
  }

  /**
   * 创建目录
   * @param fileSystem
   * @param filePath
   */
  private static void createPath(FileSystem fileSystem,String filePath){
    boolean exist = fileSystem.existsBlocking(filePath);
    if (!exist){
      log.debug("{}不存在,创建目录");
      fileSystem.mkdirBlocking(filePath);
    }
  }
}
