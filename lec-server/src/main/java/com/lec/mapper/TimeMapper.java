package com.lec.mapper;

import com.lec.entity.Time;
import org.apache.ibatis.annotations.Mapper;


import java.time.LocalDateTime;

@Mapper
public interface TimeMapper {
    Time getTime(Long id);

    void update(Long during, Long id);

    void keepTime(LocalDateTime now, Long id);

    void insert(Time time);
}
