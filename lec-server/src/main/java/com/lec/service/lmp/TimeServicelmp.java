package com.lec.service.lmp;

import com.lec.context.BaseContext;
import com.lec.entity.Time;
import com.lec.mapper.TimeMapper;
import com.lec.service.TimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TimeServicelmp implements TimeService {

    @Resource
    private TimeMapper timeMapper;

    /**
     * 结算打卡时常
     */
    @Override
    public void endTime() {
        Long id = BaseContext.getCurrentId();
        Time lasttime = timeMapper.getTime(id);//获取Time数据库中的值
        Long during = ChronoUnit.MINUTES.between(lasttime.getTemptime(), LocalDateTime.now());
        timeMapper.update(during+lasttime.getTime(), id);
    }

    /**
     * 记录当前时间
     */
    @Override
    public void startTime() {
        Long id = BaseContext.getCurrentId();
        Time time = timeMapper.getTime(id);
        if(time!=null) {
            timeMapper.keepTime(LocalDateTime.now(), id);
        }
        else{
            timeMapper.insert(new Time(id, 0L, LocalDateTime.now()));
        }
    }
}
