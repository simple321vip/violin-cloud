package cn.violin.cloud.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CloudFileVo {

    private String path;
    private String server_filename;
    private Integer size;
    private Integer isDir;
    private Integer category;
}
