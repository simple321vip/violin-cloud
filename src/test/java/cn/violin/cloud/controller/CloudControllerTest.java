package cn.violin.cloud.controller;

import cn.violin.cloud.ViolinCloudApplication;
import cn.violin.common.utils.JedisUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.event.annotation.PrepareTestInstance;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViolinCloudApplication.class)
@AutoConfigureMockMvc
public class CloudControllerTest {

    public final static String REQUEST_PREFIX_PATH = "/cloud/api/v1/file/";

    public final static String TENANT_ID = "test_user1";

    public final static String TMP_TOKEN = "121.d144a43adf1860eea1f86a8de06d8f2d.Y5cMrfKlC6wj_ckYM64jUYsn9-4Q3yuQcIJNJu-.staGfw";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JedisUtils redis;

    @Before
    public void setup(){
        redis.set(TENANT_ID, TMP_TOKEN, 0, null);
    }

    @Test
    public void test_getList() {

        RequestBuilder requestBuilder;
        requestBuilder = get(REQUEST_PREFIX_PATH + "list")
                .header("Authorization", "Bearer:" + TMP_TOKEN)
                .header("tenantId", TENANT_ID)
                .param("dir", "/");

        try {
            String responseJsonString = mvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            System.out.println(responseJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
