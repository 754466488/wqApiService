package com.dzw.micro.wq.model;

import lombok.Data;

@Data
public class SysStaffEntity {
    private String staffId;

    private String name;

    private String pass;

    private String phone;

    private String idCard;

    private String positions;

    private Byte status;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;
}