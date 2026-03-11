package com.ruoyi.web.controller.newweb;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


@RestController
public class webController {

    @GetMapping("/tets")
    public AjaxResult test(@RequestParam String message){
        String key = UUID.randomUUID().toString() + message + new Date();
        return AjaxResult.success();
    }
}

