package com.dzw.micro.wq.model;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class SysStaffEntity {
    private String staffId;

    private String name;

    private String pass;

    private String phone;

    private String idCard;

    private String positions;

    private Byte status;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;
}