package com.lec.controller;

import com.lec.context.BaseContext;
import com.lec.result.Result;
import com.lec.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/time")
public class TimeController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TimeService timeService;

//    private static String key = "打卡人id：";


    /**
     * 获取打卡状态
     * @return
     */
    @GetMapping("/status")
    public Result<String> getStatus(){
        Long id = BaseContext.getCurrentId();
        //只要redis里面没有，那么就显示未打卡
        if(redisTemplate.keys(id.toString()).isEmpty()){
            log.info("员工{}的打卡状态：未打卡", id);
            return Result.success("0");
        }
        log.info("员工{}的打卡状态：打卡中", id);
        return Result.success("1");
    }


    /**
     * 设置打卡状态
     * @return
     */
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable String status){
        Long id = BaseContext.getCurrentId();
        log.info("设置打卡状态:{}",status.equals("1")?"打卡中":"未打卡");
        redisTemplate.opsForValue().set(id.toString(), status);
        if(status.equals("0")){
            timeService.endTime();
            redisTemplate.delete(id.toString());
        }
        else{
            timeService.startTime();
        }
        return Result.success();
    }
}
