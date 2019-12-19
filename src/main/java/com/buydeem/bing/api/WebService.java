package com.buydeem.bing.api;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by zengchao on 2019/12/19.
 */
public class WebService {
  private Vertx vertx;
  private HttpServer server;
  private Router router;

  public WebService(Vertx vertx) {
    this.vertx = vertx;
    server = vertx.createHttpServer();
    router = Router.router(vertx);
    //index
    router.route("/").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.putHeader("Content-Type","application/json;charset=UTF-8");
      JsonObject json = new JsonObject();
      json.put("code","1");
      json.put("message","请求成功");
      json.put("data","Server By Vertx 3.8.4");
      response.end(json.toString());
    });
    // 404
    router.route().failureHandler(event -> {
      if (event.statusCode() == 404){
        HttpServerResponse response = event.response();
        response.putHeader("Content-Type","application/json;charset=UTF-8");
        JsonObject json = new JsonObject();
        json.put("code","0");
        json.put("message","未找到资源");
        json.put("data","Server By Vertx 3.8.4");
        response.end(json.toString());
      }
    });
    router.route("/pic").handler(new PicQueryApi());

    server.requestHandler(router).listen(8080);
  }
}
