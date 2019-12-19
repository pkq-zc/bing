package com.buydeem.bing.api;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zengchao on 2019/12/19.
 */
@Slf4j
public class PicQueryApi implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext event) {
    event.response().end("1111");
  }
}
