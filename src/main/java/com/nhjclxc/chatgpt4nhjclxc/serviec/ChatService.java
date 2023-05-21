package com.nhjclxc.chatgpt4nhjclxc.serviec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nhjclxc.chatgpt4nhjclxc.config.ApplicationConst;
import com.nhjclxc.chatgpt4nhjclxc.config.ContextHolder;
import com.nhjclxc.chatgpt4nhjclxc.dao.RedisHandler;
import com.nhjclxc.chatgpt4nhjclxc.exception.ProjectException;
import com.nhjclxc.chatgpt4nhjclxc.model.dto.ChatParamDTO;
import com.nhjclxc.chatgpt4nhjclxc.model.gpt.*;
import com.nhjclxc.chatgpt4nhjclxc.model.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import com.nhjclxc.chatgpt4nhjclxc.utils.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoXianchao
 * @since 2023/5/21 14:02
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final RedisHandler redisHandler;

    /**
     * 测试系统环境方法
     */
    public Object test(TbUser tbUser, String param, HttpServletRequest request, HttpServletResponse response){
        System.out.println(tbUser);

        String data = "参数：" + param;
        System.out.println(data);

        // 获取token
        String token = request.getHeader("token");
        System.out.println("token：" + token);

        // 测试jwt
        Map<String, Object> info = new HashMap<>();
        info.put("phone", tbUser.getPhone());
        String token1 = JWTUtils.getToken(info);
        System.out.println(token1);
        response.setHeader("token", token1);

        // 测试redis
        redisHandler.setStringValue(ApplicationConst.USER_SESSION_PREFIX + tbUser.getPhone(), JSONObject.toJSONString(tbUser));
        System.out.println("redis数据：" + redisHandler.getStringValue(ApplicationConst.USER_SESSION_PREFIX + tbUser.getPhone()));


        TbUser user = ContextHolder.getUser();
        System.out.println(user);

        // 模拟异常
        // System.out.println(1/0);
        // throw new ProjectException("666", "项目业务异常");

        return user;
    }

    /**
     * 用户做登录
     *
     * @param phone 用户唯一身份识别码 （手机号）
     * @param response 响应，用于放token
     * @return
     */
    public Object login(String phone, HttpServletResponse response) {
        Map<String, Object> info = new HashMap<>();
        info.put("phone", phone);
        String token = JWTUtils.getToken(info);
        response.setHeader("token", token);
        TbUser tbUser = TbUser.builder().phone(phone).name("").build();
        redisHandler.setStringValue(ApplicationConst.USER_SESSION_PREFIX + phone, JSONObject.toJSONString(tbUser));
        log.info("redis数据：" + redisHandler.getStringValue(ApplicationConst.USER_SESSION_PREFIX + phone));

        response.setHeader("token", token);
        // 登录成功之后返回用户的聊天记录
        return redisHandler.getChatRecordListMap(phone);
    }

    /**
     * 用户于chatGPT聊天
     * @param chatParamDTO 聊天请求数据
     * @return gpt返回的聊天数据
     * @throws IOException
     * @throws URISyntaxException
     * @throws ParseException
     */
    public Object chat(ChatParamDTO chatParamDTO) throws IOException, URISyntaxException, ParseException {
        /*
            curl https://api.openai.com/v1/chat/completions \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $OPENAI_API_KEY" \
            -d '{
                "model": "gpt-3.5-turbo",
                "messages": [{"role": "user", "content": "Hello!"}]
             }'
         */
        String userContent = chatParamDTO.getContent();
        if (StringUtils.isBlank(userContent)){
            return "入参为空";
        }

        // 构造请求入参
        ChatGPTParamDTO chatGPTParamDTO = ChatGPTParamDTO.builder().model(ApplicationConst.CHATGPT.MODEL)
                .temperature(ApplicationConst.CHATGPT.TEMPERATURE).messages(
                        Collections.singletonList(ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_USER).content(userContent).build())
                ).build();

        // 发请求
        String stringResponse = doRequest2GPT(chatGPTParamDTO);

        // 响应返回
        ChatGPTVO chatGPTVO = JSON.parseObject(stringResponse, ChatGPTVO.class);
        ChatGPTError error = chatGPTVO.getError();
        ChatParamDTO retChatParamDTO;
        String assistantContext;
        if (!Objects.isNull(error)) {
/*请求失败

{
    "error": {
        "message": "",
        "type": "invalid_request_error",
        "param": null,
        "code": "invalid_api_key"
    }
}
*/
            throw new ProjectException(ReturnCodeEnum.CHATGPT_ERROR, error.getMessage());
        }else {
/*请求成功
{
    "id": "chatcmpl-7IXJHrvq2qQ0uu7ebAyQLxai7lite",
    "object": "chat.completion",
    "created": 1684652303,
    "model": "gpt-3.5-turbo-0301",
    "usage": {
        "prompt_tokens": 12,
        "completion_tokens": 29,
        "total_tokens": 41
    },
    "choices": [
        {
            "message": {
                "role": "assistant",
                "content": "I am an AI language model created by OpenAI, designed to assist with various tasks such as answering questions, generating text, and providing information."
            },
            "finish_reason": "stop",
            "index": 0
        }
    ]
}
 */
            assistantContext = chatGPTVO.getChoices().stream().map(ChatGPTChoices::getMessage)
                    .map(ChatGPTMessage::getContent).reduce("", String::concat);
            // messages.add(ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_ASSISTANT).content(context).build());
            retChatParamDTO = ChatParamDTO.builder().content(assistantContext).build();
        }
        // 数据库记录调用日志
        String s = saveChatGPTRecord(chatParamDTO.getRecordSn(), userContent, assistantContext);
        retChatParamDTO.setRecordSn(s);

        return retChatParamDTO;
    }

    /**
     * 保存用户于gpt的聊天记录
     *
     * @param recordSn 聊天记录序号
     * @param userContent 用户请求入参
     * @param assistantContext gpt响应返回数据
     */
    private String saveChatGPTRecord(String recordSn, String userContent, String assistantContext) {
        // 保存本次用户的聊天记录于redis
        String phone = ContextHolder.getUser().getPhone();
        if (StringUtils.isBlank(recordSn)){
            recordSn = UUID.randomUUID().toString().replaceAll("-", "");
        }

        // 保存用户于gpt的聊天记录于redis
        // saveChatRecord(phone, recordSn, userContent, assistantContext);
        redisHandler.saveChatRecordHash(phone, recordSn, userContent, assistantContext);
        return recordSn;
    }

    /**
     * 向GPT发起请求
     *
     * @param chatGPTParamDTO 请求入参
     * @return 请求响应
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    private String doRequest2GPT(ChatGPTParamDTO chatGPTParamDTO) throws URISyntaxException, IOException, ParseException {
        URL url = new URL(ApplicationConst.CHATGPT.ADDRESS);
        URI uri = new URIBuilder()
                .setScheme(url.getProtocol())
                .setHost(url.getHost())
                .setPort(url.getPort())
                .setPath(url.getPath()).build();

        //创建Post请求
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());//MediaType.APPLICATION_JSON ContentType.APPLICATION_JSON
        httpPost.addHeader("Authorization", redisHandler.getChatGPTAPIKey());

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间
                .setConnectTimeout(Timeout.of(30000, TimeUnit.MILLISECONDS))
                // 设置响应超时时间
                .setResponseTimeout(30000, TimeUnit.MILLISECONDS)
                // 设置从连接池获取链接的超时时间
                .setConnectionRequestTimeout(30000, TimeUnit.MILLISECONDS)
                .build();
        httpPost.setConfig(requestConfig);

/*
{
     "model": "gpt-3.5-turbo",
     "messages": [
         {"role": "system", "content": "who are you?"}
         ],
     "temperature": 0.7
}
*/
        // 将数据放入entity中
        log.info("chat请求入参：{}", chatGPTParamDTO);
        HttpEntity httpEntity = new StringEntity(JSON.toJSONString(chatGPTParamDTO), StandardCharsets.UTF_8);
        httpPost.setEntity(httpEntity);


        // 响应模型
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String stringResponse = EntityUtils.toString(response.getEntity());

        log.info("chat请求响应：\n{}", stringResponse);
        return stringResponse;
    }


    /*
    //  import org.springframework.http.HttpHeaders; //导入方法依赖的package包/类
        // 1设置请求头
        // 2添加请求头和json数据
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());//MediaType.APPLICATION_JSON ContentType.APPLICATION_JSON
        headers.add("Authorization", getChatGPTAPIKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(chatGPTParamDTO), headers);

        // 3发请求
        RestTemplate restTemplate = new RestTemplate();
        String stringResponse = restTemplate.postForObject(ApplicationConst.CHATGPT.ADDRESS, httpEntity, String.class);// 返回的是json数据

     */

}
