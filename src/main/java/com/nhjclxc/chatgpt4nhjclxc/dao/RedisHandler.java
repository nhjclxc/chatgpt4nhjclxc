package com.nhjclxc.chatgpt4nhjclxc.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nhjclxc.chatgpt4nhjclxc.config.ApplicationConst;
import com.nhjclxc.chatgpt4nhjclxc.config.ContextHolder;
import com.nhjclxc.chatgpt4nhjclxc.exception.ProjectException;
import com.nhjclxc.chatgpt4nhjclxc.model.gpt.ChatGPTMessage;
import com.nhjclxc.chatgpt4nhjclxc.model.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoXianchao
 * @since 2023/5/21 21:12
 */
@Component
public class RedisHandler {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setStringValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setStringValue(String key, String value, long expire, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, expire, unit);
    }

    public String getStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setListValue(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public List<String> getListValue(String key) {
         return getListValue(key, 0, -1);
    }

    public List<String> getListValue(String key, long strat, long end ) {
         return stringRedisTemplate.opsForList().range(key, strat, end);
    }

    /**
     * 获取redis里面保存的CHATGPT_API_KEY
     * @return gpt API Key
     */
    public String getChatGPTAPIKey(){
        String gptAPIKey = stringRedisTemplate.opsForValue().get(ApplicationConst.CHATGPT_API_KEY_REDIS);
        if (StringUtils.isBlank(gptAPIKey)){
            throw new ProjectException(ReturnCodeEnum.CHATGPT_API_KEY_ERROR);
        }
        return gptAPIKey;
    }

    /**
     * 获取所有聊天记录
     * @param phone 手机号
     * @return 该用户的所有聊天记录
     */
    public Map<String, List<ChatGPTMessage>> getChatRecordListMap(String phone) {
        // key = hash.key，value = hash.value
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(ApplicationConst.CHATGPT_RECORD_HASH_PREFIX + phone);

        // 组装数据返回
        Map<String, List<ChatGPTMessage>> chatGPTMessageListMap = new LinkedHashMap<>(entries.size());
        entries.forEach((k, v) -> {
            List<ChatGPTMessage> chatGPTMessageList;
            if (!Objects.isNull(v)) {
                chatGPTMessageList = JSONObject.parseArray(String.valueOf(v), ChatGPTMessage.class);
            }else {
                chatGPTMessageList = new ArrayList<>();
            }
            chatGPTMessageListMap.put(String.valueOf(k), chatGPTMessageList);
        });
        return chatGPTMessageListMap;
    }


    /**
     * hash方式保存用户于gpt的聊天记录
     * @param phone 用户唯一识别码
     * @param recordSn 聊天记录序号
     * @param userContent 用户输入的内容
     * @param assistantContext gpt回复的内容
     */
    public void saveChatRecordHash(String phone, String recordSn, String userContent, String assistantContext) {
        ChatGPTMessage userMeassage = ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_USER).content(userContent).build();
        ChatGPTMessage assistantMeassage = ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_ASSISTANT).content(assistantContext).build();

        List<ChatGPTMessage> redisChatRecordList = getChatRecordHash(phone, recordSn);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(redisChatRecordList)){
            redisChatRecordList = new ArrayList<>();
        }
        redisChatRecordList.add(userMeassage);
        redisChatRecordList.add(assistantMeassage);
        doSaveChatRecordHash(phone, recordSn, redisChatRecordList);
    }


    /**
     * 保存聊天记录
     * @param phone
     * @param recordSn
     * @param redisChatRecordList
     */
    private void doSaveChatRecordHash(String phone, String recordSn, List<ChatGPTMessage> redisChatRecordList){
        stringRedisTemplate.opsForHash().put(ApplicationConst.CHATGPT_RECORD_HASH_PREFIX + phone, recordSn, JSONObject.toJSONString(redisChatRecordList));
    }

    /**
     * 获取用户的历史聊天记录
     * @param phone
     * @param recordSn
     * @return
     */
    public List<ChatGPTMessage> getChatRecordHash(String phone, String recordSn){
        Object o = stringRedisTemplate.opsForHash().get(ApplicationConst.CHATGPT_RECORD_HASH_PREFIX + phone, recordSn);
        if (Objects.isNull(o)){
            return new ArrayList<>();
        }
        return JSONObject.parseArray(String.valueOf(o), ChatGPTMessage.class);
    }


    /**
     * string方式保存用户于gpt的聊天记录
     * @param phone 用户唯一识别码
     * @param recordSn 聊天记录序号
     * @param userContent 用户输入的内容
     * @param assistantContext gpt回复的内容
     */
    public void saveChatRecord(String phone, String recordSn, String userContent, String assistantContext) {
        ChatGPTMessage userMeassage = ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_USER).content(userContent).build();
        ChatGPTMessage assistantMeassage = ChatGPTMessage.builder().role(ApplicationConst.CHATGPT.ROLE_ASSISTANT).content(assistantContext).build();

        Map<String, List<ChatGPTMessage>> redisChatRecordMap = getChatRecord(phone);
        List<ChatGPTMessage> chatGPTMessageList = redisChatRecordMap.get(recordSn);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(chatGPTMessageList)){
            chatGPTMessageList = new ArrayList<>();
            redisChatRecordMap.put(recordSn, chatGPTMessageList);
        }
        chatGPTMessageList.add(userMeassage);
        chatGPTMessageList.add(assistantMeassage);
        doSaveChatRecord(phone, redisChatRecordMap);
    }


    private void doSaveChatRecord(String phone, Map<String, List<ChatGPTMessage>> redisChatRecordMap){
        stringRedisTemplate.opsForValue().set(ApplicationConst.CHATGPT_RECORD_PREFIX + phone, JSONObject.toJSONString(redisChatRecordMap));
    }


    public Map<String, List<ChatGPTMessage>> getChatRecord(String phone){
        String s = stringRedisTemplate.opsForValue().get(ApplicationConst.CHATGPT_RECORD_PREFIX + phone);
        if (StringUtils.isBlank(s)){
            return new LinkedHashMap<>();
        }
        Map<String, List<ChatGPTMessage>> map = (Map<String, List<ChatGPTMessage>>) JSON.parseObject(s, Map.class);
        System.out.println(map);
        return map;
    }

    public String getSessionValue(String phone) {
        return stringRedisTemplate.opsForValue().get(ApplicationConst.USER_SESSION_PREFIX + phone);
    }
}
