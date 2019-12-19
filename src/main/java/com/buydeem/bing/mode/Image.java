package com.buydeem.bing.mode;

import com.buydeem.bing.utils.sql.FieldIgnore;
import com.buydeem.bing.utils.sql.Table;
import com.buydeem.bing.utils.sql.TableFile;
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
@Table(tableName = "t_image")
public class Image {
  @TableFile(fieldName = "start_date")
  private String startdate;
  @TableFile(fieldName = "full_start_date")
  private String fullstartdate;
  @TableFile(fieldName = "end_date")
  private String enddate;
  private String url;
  @TableFile(fieldName = "url_base")
  private String urlbase;
  private String copyright;
  @TableFile(fieldName = "copyright_link")
  private String copyrightlink;
  private String title;
  @FieldIgnore
  private String quiz;
  @FieldIgnore
  private Boolean wp;
  @FieldIgnore
  private String hsh;
  @FieldIgnore
  private Integer drk;
  @FieldIgnore
  private Integer top;
  @FieldIgnore
  private Integer bot;
  @FieldIgnore
  private List hs;
}
