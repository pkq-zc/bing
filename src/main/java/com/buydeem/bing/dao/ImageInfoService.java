package com.buydeem.bing.dao;

import com.buydeem.bing.mode.Image;
import com.buydeem.bing.utils.SqlUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zengchao on 2019/12/19.
 */
@Slf4j
public class ImageInfoService{
  private Vertx vertx;
  private SQLClient sqlClient;

  public ImageInfoService(Vertx vertx){
    this.vertx = vertx;
    InputStream in = ImageInfoService.class.getResourceAsStream("/db.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StringBuilder sb = new StringBuilder();
    reader.lines().forEach(sb::append);
    JsonObject config = new JsonObject(sb.toString());
    sqlClient = MySQLClient.createShared(vertx,config);
    log.debug("创建数据源成功... {}",config.toString());
  }

  public void insert(Image image){
    sqlClient.getConnection(getConnEvent -> {
      if (getConnEvent.succeeded()){
        SQLConnection connection = getConnEvent.result();
        String sql = SqlUtils.insertSql(image);
        log.debug(sql);
        connection.execute(sql, insertOver -> {
          if (insertOver.succeeded()){
            connection.commit(non -> log.debug("提交事务成功"));
          }else {
            connection.rollback(non -> log.debug("提交事务失败"));
          }
        });
      }else {
        log.error("获取数据库连接失败!");
      }
    }).close();
  }
}
