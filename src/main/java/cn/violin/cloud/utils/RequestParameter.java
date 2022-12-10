package cn.violin.cloud.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RequestParameter implements Parameter {

    private String method;
    private String dir;
    private String order;
    private String start;
    private String limit;
    private String web;
    private String folder;
    private String access_token;
    private String desc;

}
