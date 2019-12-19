package com.buydeem.bing;

import com.buydeem.bing.api.WebService;
import com.buydeem.bing.dao.ImageInfoService;
import com.buydeem.bing.mode.Image;
import com.buydeem.bing.utils.DateTimeUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

/**
 * @author zengchao
 * Created by zengchao on 2019/12/13.
 */
@Slf4j
public class BingGetVerticle extends AbstractVerticle {
  private static final String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
  private static final String HOST = "https://cn.bing.com";
  private static final long DAY = 24 * 60 * 60 * 1000L;
  private ImageInfoService imageInfoService;
  /**
   * 文件保存路径
   */
  private String filePath;
  /**
   * 定时任务开始时间
   */
  private Date date;

  /**
   * 创建vertx
   * @param filePath 文件路径
   * @param date 时间
   */
  public BingGetVerticle(String filePath, Date date) {
    this.filePath = filePath;
    this.date = date;
  }

  @Override
  public void start() throws Exception {
    super.start();
    imageInfoService = new ImageInfoService(vertx);
    Long temp = calDelay();
    Long delay = temp > 0 ? DAY - temp : -temp;
    log.debug("还有{}分钟将执行任务",delay % (60*1000) == 0 ? delay / (60*1000) : delay / (60*1000) +1);
    vertx.setTimer(delay,l ->{
      downloadPic();
      log.debug("启动周期循环任务");
      vertx.setPeriodic(DAY,t ->downloadPic());
    });
    new WebService(vertx);
  }


  /**
   * 下载图片
   */
  public void downloadPic(){
    WebClient.create(vertx).getAbs(BING_API)
      .send(item ->{
        if (item.succeeded()){
          //成功
          log.info("请求成功");
          HttpResponse<Buffer> response = item.result();
          //解析图片信息
          Image image = processResult(response);
          //文件写出
          write2File(image);
          imageInfoService.insert(image);
        }
        if (item.failed()){
          //失败
          log.error("请求失败");
          item.cause().printStackTrace();
        }
      });
  }

  /**
   * 解析结果json
   * @param response
   * @return
   */
  private Image processResult(HttpResponse<Buffer> response){
    return response.bodyAsJsonObject()
      .getJsonArray("images")
      .getJsonObject(0)
      .mapTo(Image.class);
  }

  /**
   * 写入文件
   * @param image
   */
  private void write2File(Image image){
    FileSystem fileSystem = vertx.fileSystem();
    //解析文件名
    Map<String, String> map = parseQuery(image.getUrl());
    String fileName = map.get("id");
    String fileUrl = HOST+image.getUrl();
    String fullFileName = filePath+File.separator+fileName;
    log.debug("fileName = {}, fileUrl = {}",fileName,fileUrl);
    WebClient.create(vertx).getAbs(HOST+image.getUrl()).send(rs ->{
      if (rs.succeeded()){
        Buffer buffer = rs.result().bodyAsBuffer();
        fileSystem.writeFile(fullFileName,buffer,ar ->{
          if (ar.succeeded()){
            log.debug("文件[{}]写入完成",fullFileName);
          }else {
            ar.cause().printStackTrace();
          }
        });
      }else {
        rs.cause().printStackTrace();
        log.error(rs.toString());
      }
    });
  }

  /**
   * 解析url查询参数
   * @param url
   * @return
   */
  private Map<String,String> parseQuery(String url){
    if (Objects.isNull(url) || Objects.equals("",url)){
      throw new NullPointerException("url不能为空!");
    }
    Map<String,String> map = new LinkedHashMap<>();
    int index = url.indexOf("?");
    if (index == -1 || index == url.length()-1){
      return map;
    }
    String queryStr = url.substring(index + 1, url.length());
    String[] kvs = queryStr.split("\\&");
    for (String kv : kvs) {
      String[] temp = kv.split("=");
      String key = temp[0];
      String value = temp.length == 1 ? "" : temp[1];
      map.put(key,value);
    }
    return map;
  }

  /**
   * 计算延时时间
   * @return 如果返回值大于0 说明定时任务在当前时间之前 大于0 说明定时任务在当前时间之后
   */
  private Long calDelay(){
    Calendar now = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    now.set(Calendar.YEAR,cal.get(Calendar.YEAR));
    now.set(Calendar.MONTH,cal.get(Calendar.MONTH));
    now.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH));
    log.debug("now : {}",DateTimeUtils.date2String(now.getTime()));
    log.debug("cal : {}",DateTimeUtils.date2String(cal.getTime()));
    //求时间差
    return now.getTime().getTime()-cal.getTime().getTime();
  }
}
