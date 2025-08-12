package com.chronos_0812.schedule.dto.save;

import com.chronos_0812.user.entity.User;
import lombok.Getter;

@Getter
public class ScheduleSaveRequest {

    private String title;
    private String content;
    private User author;
}
