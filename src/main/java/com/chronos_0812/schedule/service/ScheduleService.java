package com.chronos_0812.schedule.service;

import com.chronos_0812.schedule.dto.get.ScheduleGetAllResponse;
import com.chronos_0812.schedule.dto.get.ScheduleGetOneResponse;
import com.chronos_0812.schedule.dto.save.ScheduleSaveRequest;
import com.chronos_0812.schedule.dto.save.ScheduleSaveResponse;
import com.chronos_0812.schedule.dto.update.ScheduleUpdateRequest;
import com.chronos_0812.schedule.entity.Schedule;
import com.chronos_0812.schedule.repository.ScheduleRepository;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 일정 저장(생성)
     * @param scheduleSaveResponse
     * @return
     */

    @Transactional
    public ScheduleSaveResponse save(ScheduleSaveRequest scheduleSaveResponse) {
        if(scheduleSaveResponse.getAuthor() == null) {
            throw new IllegalStateException("작성자명은 필수값입니다.");
        }
        if(scheduleSaveResponse.getTitle() == null) {
            throw new IllegalStateException("일정 제목은 필수값입니다.");
        }
        if(scheduleSaveResponse.getContent() == null) {
            throw new IllegalStateException("일정 내용은 필수값입니다.");
        }

        if(scheduleSaveResponse.getTitle().length() > 30) {
            throw new IllegalStateException("일정 제목은 최대 30자입니다.");
        }
        if(scheduleSaveResponse.getContent().length() > 200) {
            throw new IllegalStateException("일정 내용은 최대 200자입니다.");
        }

        Schedule schedule = new Schedule(
                scheduleSaveResponse.getTitle(),
                scheduleSaveResponse.getContent(),
                scheduleSaveResponse.getAuthor()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleSaveResponse(            // 왜 6개 인수이지??
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getAuthor(),          //API 응답(ScheduleSaveResponse)에서 User 타입으로 설정, 엔티티에서 User author로 설정.
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    /**
     * 일정 전체 조회 (읽기 전용 트랜잭션 → 성능 최적화)
     * @param author
     * @return
     */

    @Transactional(readOnly = true)
    public List<ScheduleGetAllResponse> findSchedules(User author) {
        List<Schedule> schedules =scheduleRepository.findAll();
        List<ScheduleGetAllResponse> scheduleGetAllResponses = new ArrayList<>();

        if(author == null) {
            for(Schedule schedule : schedules) {
                ScheduleGetAllResponse scheduleGetAllResponse = new ScheduleGetAllResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getAuthor().getUsername(),         //  정적 메소드 활용??
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                );
                scheduleGetAllResponses.add(scheduleGetAllResponse);
            }
            return scheduleGetAllResponses;
        }

        for(Schedule schedule : schedules) {
            ScheduleGetAllResponse scheduleGetAllResponse = new ScheduleGetAllResponse(
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getAuthor().getUsername(),             //  정적 메소드 활용??
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            );
            scheduleGetAllResponses.add(scheduleGetAllResponse);
        }
        return scheduleGetAllResponses;
    }

    /**
     * 일정 단건 조회
     * @param scheduleId
     * @return
     */

    @Transactional(readOnly = true)
    public ScheduleGetOneResponse findSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        return ScheduleGetOneResponse.from(schedule);
    }

//    @Transactional(readOnly = true)
//    public ScheduleGetOneResponse findSchedule(long scheduleId) {
//        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
//                () -> new IllegalArgumentException("일정을 찾을 수 없습니다.")
//        );
//        return new ScheduleGetOneResponse.from(schedule);
//    }

    /**
     * 일정 수정 (부분 업데이트)
     * @param scheduleId
     * @return
     */

    /**
     * 일정 수정 (부분 업데이트)
     */
    @Transactional
    public ScheduleGetOneResponse updateSchedule(long scheduleId, ScheduleUpdateRequest scheduleUpdateRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        // 부분 업데이트 적용
        if (scheduleUpdateRequest.getTitle() != null) {
            if (scheduleUpdateRequest.getTitle().isEmpty()) {
                throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
            }
            schedule.changeTitle(scheduleUpdateRequest.getTitle());
        }

        if (scheduleUpdateRequest.getContent() != null) {
            if (scheduleUpdateRequest.getContent().isEmpty()) {
                throw new IllegalArgumentException("내용은 비어 있을 수 없습니다.");
            }
            schedule.changeContent(scheduleUpdateRequest.getContent());
        }

        // save() 불필요: 영속 엔티티 변경 감지
        return ScheduleGetOneResponse.from(schedule);
    }

    /**
     * 일정 삭제
     * @param scheduleId
     */

    @Transactional
    public void deleteSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정을 찾을 수 없습니다.")
        );
        scheduleRepository.delete(schedule);
    }
}
