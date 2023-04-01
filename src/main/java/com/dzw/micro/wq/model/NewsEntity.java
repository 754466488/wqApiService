package com.dzw.micro.wq.model;

import lombok.Data;

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

    private Long publishTime;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;
}