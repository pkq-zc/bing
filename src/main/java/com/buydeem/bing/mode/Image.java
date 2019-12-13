package com.buydeem.bing.mode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by zengchao on 2019/12/13.
 */
@Getter
@Setter
@ToString
public class Image {
  private String startdate;
  private String fullstartdate;
  private String enddate;
  private String url;
  private String urlbase;
  private String copyright;
  private String copyrightlink;
  private String title;
  private String quiz;
  private Boolean wp;
  private String hsh;
  private Integer drk;
  private Integer top;
  private Integer bot;
  private List hs;
}
