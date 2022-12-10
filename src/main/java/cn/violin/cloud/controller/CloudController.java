package cn.violin.cloud.controller;

import cn.violin.cloud.utils.CommonUtils;
import cn.violin.cloud.utils.Parameter;
import cn.violin.cloud.utils.RequestParameter;
import cn.violin.cloud.vo.CloudFileVo;
import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.common.utils.JedisUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RequestMapping("/api/v1")
public class CloudController {

    public static final String BAIDU_CLOUD_PROTOCOL = "https://";

    public static final String BAIDU_CLOUD_DOMAIN = "pan.baidu.com";

    public static final String BAIDU_CLOUD_PATH = "/rest/2.0/xpan/file";

    public static final String QUESTION_MARK = "?";

    @Autowired
    private JedisUtils redis;

    /**
     * 获取文件列表
     * https://pan.baidu.com/union/doc/nksg0sat9
     */
    @RequestMapping("/file/list")
    @GetMapping
    public ResponseEntity<List<CloudFileVo>> getList(
            @RequestParam(value = "dir", defaultValue = "/") String dir,
            @RequestParam(value = "order", defaultValue = "name") String order,
            @RequestParam(value = "start", defaultValue = "0") String start,
            @RequestParam(value = "limit", defaultValue = "100") String limit,
            @RequestParam(value = "web", defaultValue = "1") String web,
            @RequestParam(value = "folder", defaultValue = "0") String folder,
            @RequestParam(value = "desc ", defaultValue = "1") String desc,
            @CurrentUser Tenant tenant) {

        Optional<String> tokenOptional = redis.get(tenant.getTenantId());

        if (tokenOptional.isPresent()) {
            StringBuilder api_url = new StringBuilder();
            api_url.append(BAIDU_CLOUD_PROTOCOL)
                    .append(BAIDU_CLOUD_DOMAIN)
                    .append(BAIDU_CLOUD_PATH)
                    .append(QUESTION_MARK);
            Parameter parameters = RequestParameter.builder()
                    .method("list")
                    .dir(dir)
                    .order(order)
                    .start(start)
                    .limit(limit)
                    .web(web)
                    .folder(folder)
                    .desc(desc)
                    .access_token(tokenOptional.get())
                    .build();
            String parameters_part = CommonUtils.createRequestParameters(parameters);
            api_url.append(parameters_part);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGetToken = new HttpGet(api_url.toString());
            CloseableHttpResponse response;
            try {
                response = httpClient.execute(httpGetToken);
                if (response.getStatusLine().getStatusCode() == 200) {

                    HttpEntity entity = response.getEntity();
                    JSONObject data = JSONObject.parseObject(EntityUtils.toString(entity));
                    JSONArray filesArray = (JSONArray) data.get("list");

                    List<CloudFileVo> result = filesArray.stream().map(record -> {

                        JSONObject object = (JSONObject) record;
                        return CloudFileVo
                                .builder()
                                .path(object.getString("path"))
                                .server_filename(object.getString("server_filename"))
                                .size(object.getInteger("size"))
                                .isDir(object.getInteger("isdir"))
                                .category(object.getInteger("category"))
                                .build();

                    }).collect(Collectors.toList());
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
