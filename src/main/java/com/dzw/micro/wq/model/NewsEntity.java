package com.dzw.micro.wq.model;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class NewsEntity {
    private Long id;

    private String title;

    private Long menuId;

    private Integer clickNum;

    private String linkUrl;

    private String coverPicUrl;

    private Integer isTop;

    private Integer type;

    private Integer status;

    private String publishTime;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;
}