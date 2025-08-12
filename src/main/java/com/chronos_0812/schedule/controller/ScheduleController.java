package com.chronos_0812.schedule.controller;

import com.chronos_0812.schedule.dto.get.ScheduleGetAllResponse;
import com.chronos_0812.schedule.dto.get.ScheduleGetOneResponse;
import com.chronos_0812.schedule.dto.save.ScheduleSaveRequest;
import com.chronos_0812.schedule.dto.save.ScheduleSaveResponse;
import com.chronos_0812.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
// @RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * 기능 : 일정 등록
     * HTTP Method : POST
     * URL : /schedules
     * Body(JSON) : {"userId":1, "title":"할 일", "content":"내용"}
     */

    @PostMapping
    public ResponseEntity<ScheduleSaveResponse> saveSchedule(
            @RequestBody ScheduleSaveRequest scheduleSaveRequest
    ) {
        return ResponseEntity.ok(scheduleService.save(scheduleSaveRequest));
    }

    /**
     * 전체 일정 조회
     */
    @GetMapping
    public ResponseEntity<List<ScheduleGetAllResponse>> getAllSchedules(
            @RequestBody(required = false) String author
    ) {
        return ResponseEntity.ok(scheduleService.findSchedules(author));
    }

    /**
     * 단건 일정 조회
     */

    @GetMapping
    public ResponseEntity<ScheduleGetOneResponse> getSchedule(
            @PathVariable long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    /**
     * 일정 수정
     */

    @PutMapping
    public ResponseEntity<ScheduleSaveResponse> updateSchedule(
            @RequestBody ScheduleSaveRequest scheduleSaveRequest,
            @PathVariable long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.save(scheduleSaveRequest, scheduleId));
    }

    @DeleteMapping
    public void deleteSchedule(
            @PathVariable long scheduleId
    ) {
        scheduleService.deleteSchedule(scheduleId);
    }
}
