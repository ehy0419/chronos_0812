package com.chronos_0812.schedule.controller;

import com.chronos_0812.schedule.dto.get.ScheduleGetAllResponse;
import com.chronos_0812.schedule.dto.get.ScheduleGetOneResponse;
import com.chronos_0812.schedule.dto.save.ScheduleSaveRequest;
import com.chronos_0812.schedule.dto.save.ScheduleSaveResponse;
import com.chronos_0812.schedule.dto.update.ScheduleUpdateRequest;
import com.chronos_0812.schedule.service.ScheduleService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody ScheduleSaveRequest scheduleSaveRequest // @Valid 추가
    ) {
        return ResponseEntity.ok(scheduleService.save(scheduleSaveRequest));
    }

    /**
     * 전체 일정 조회
     * 예) /schedules                -> 전체
     * 예) /schedules?authorId=1     -> 특정 작성자
     */
    @GetMapping
    public ResponseEntity<List<ScheduleGetAllResponse>> getAllSchedules(
            @RequestParam(value = "authorId", required = false) Long authorId
            // 수정 전 : @RequestBody(required = false) String author
            // 수정 후 : @RequestParam(value = "authorId", required = false) Long authorId
            // 수정 이유 : GET에 @RequestBody 사용 제거: 조회 필터는 @RequestParam이 적절
    ) {
        return ResponseEntity.ok(scheduleService.findSchedules(authorId));
    }
    // 오류 : return ResponseEntity.ok(scheduleService.findSchedules(author));
    // 오류 수정 : return ResponseEntity.ok(scheduleService.findSchedules(authorId));
    // 오류 원인 : 메서드 시그니처/매핑 불일치
    // findSchedules(String) → findSchedules(Long authorId)로 맞춤

    /**
     * 단건 일정 조회
     * 예) /schedules/10
     */

    @GetMapping
    // 수정 전 :@GetMapping
    // 수정 후 :@GetMapping("/{scheduleId}")
    // 수정 이유 : 중복 @GetMapping(경로 동일) 제거: 단건 조회는 @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleGetOneResponse> getSchedule(
            @PathVariable long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    /**
     * 일정 수정 (부분 업데이트)
     * 예) PATCH /schedules/10
     * Body(JSON) : {"title":"새 제목"} 또는 {"content":"새 내용"}
     */

    @PutMapping
    public ResponseEntity<ScheduleGetOneResponse> updateSchedule(
            @PathVariable long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, scheduleUpdateRequest));
    }
    // 오류 : return ResponseEntity.ok(scheduleService.save(scheduleSaveRequest, scheduleId));
    // 오류 수정 : return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, scheduleUpdateRequest));
    // 오류 원인 : 메서드 시그니처/매핑 불일치
    // 수정은 save(..., scheduleId)가 아니라 updateSchedule(scheduleId, ...) 호출


    /**
     * 일정 삭제
     * 예) DELETE /schedules/10
     */
    @DeleteMapping
    public void deleteSchedule(
            @PathVariable long scheduleId
    ) {
        scheduleService.deleteSchedule(scheduleId);
    }
}
