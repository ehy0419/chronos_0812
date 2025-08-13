package com.chronos_0812.schedule.controller;

import com.chronos_0812.schedule.dto.get.ScheduleGetAllResponse;
import com.chronos_0812.schedule.dto.get.ScheduleGetOneResponse;
import com.chronos_0812.schedule.dto.save.ScheduleSaveRequest;
import com.chronos_0812.schedule.dto.save.ScheduleSaveResponse;
import com.chronos_0812.schedule.dto.update.ScheduleUpdateRequest;
import com.chronos_0812.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일정 CRUD API
 *  - POST   /schedules                 : 생성
 *  - GET    /schedules                 : 전체/작성자별 조회 (?userId=)
 *  - GET    /schedules/{scheduleId}    : 단건 조회
 *  - PUT    /schedules/{scheduleId}    : 수정
 *  - DELETE /schedules/{scheduleId}    : 삭제
 */

/**
 * Lv2: 저장 시 userId 필요
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 기능 : 일정 등록
     * HTTP Method : POST
     * URL : /schedules
     * Body(JSON) : {"userId":1, "title":"할 일", "content":"내용"}
     */

    /** 일정 등록 */
    @PostMapping
    public ResponseEntity<ScheduleSaveResponse> saveSchedule(
            @Valid @RequestBody ScheduleSaveRequest scheduleSaveRequest     // @Valid 추가
    ) {
        return ResponseEntity.ok(scheduleService.save(scheduleSaveRequest));
    }

    /**
     * 전체 일정 조회
     * 예) /schedules              -> 전체
     * 예) /schedules?userId=1     -> 특정 작성자
     */
    @GetMapping
    public ResponseEntity<List<ScheduleGetAllResponse>> getAllSchedules(
            @RequestParam(value = "userId", required = false) Long userId
            ///  쿼리 파라미터 변경 ///
            // 수정 전 : @RequestBody(required = false) String user
            // 수정 후 : @RequestParam(value = "userId", required = false) Long userId
            // 수정 이유 : GET에 @RequestBody 사용 제거: 조회 필터는 @RequestParam이 적절
    ) {
        return ResponseEntity.ok(scheduleService.findAllSchedules(userId));     // 에러 : findAllSchedules(scheduleId) 의 타입 Long
    }
    // 오류 : return ResponseEntity.ok(scheduleService.findSchedules(user));
    // 오류 수정 : return ResponseEntity.ok(scheduleService.findSchedules(userId));
    // 오류 원인 : scheduleService.findAllSchedules(...) 메서드 시그니처와 getAllSchedules() 컨트롤러에서 넘겨주는 인자의 타입이 맞지 않아서
    // 에러 메시지 : 'com.chronos_0812.schedule.service.ScheduleService'의 'findAllSchedules(com.chronos_0812.user.entity.User)'을(를) '(java.lang.Long)'에 적용할 수 없습니다

    /** 단건 조회 */
    @GetMapping("/{scheduleId}")
    // 수정 전 :@GetMapping
    // 수정 후 :@GetMapping("/{scheduleId}")
    // 수정 이유 : 중복 @GetMapping(경로 동일) 제거: 단건 조회는 @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleGetOneResponse> getSchedule(
            @PathVariable long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    /** 부분 수정 */
    @PatchMapping("/{scheduleId}")
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


    /** 일정 삭제 */
    // version2
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();              // 204 No Content
    }

    // version 1
//    @DeleteMapping("/{scheduleId}")
//    public void deleteSchedule(
//            @PathVariable long scheduleId
//    ) {
//        scheduleService.deleteSchedule(scheduleId);
//    }
}