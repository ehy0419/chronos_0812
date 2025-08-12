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

/**
 * 트랜잭션 경계와 비즈니스 로직 담당
 */

@Service
@RequiredArgsConstructor
public class ScheduleService {
    ///  비즈니스 로직

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 일정 저장(생성)
     */

    @Transactional
    public ScheduleSaveResponse save(ScheduleSaveRequest scheduleSaveRequest) {
        // 유효성 검사는 컨트롤러 @Valid 에서 처리
        // 서비스에서는 비즈니스 검증만, 작성자 존재 확인 만 하자

        /**
         * 결론부터 아래 주석 코드는 널 값, 길이 체크하는 내용으로, 필요없다.
         * 이유는 ScheduleSaveRequest에 @NotBlank, @Size, @NotNull을 달았고 컨트롤러에서 @Valid로 받으면 스프링이 자동으로 400 에러를 보여주는 검증을 해준다.
         */

        // Lv1. 간단 유효성 체크: 공백/길이.
        // Lv5. 고급 Validation
//        if(scheduleSaveRequest.getAuthor() == null) {
//            throw new IllegalStateException("작성자명은 필수값입니다.");
//        }
//        if(scheduleSaveRequest.getTitle() == null) {
//            throw new IllegalStateException("일정 제목은 필수값입니다.");
//        }
//        if(scheduleSaveRequest.getContent() == null) {
//            throw new IllegalStateException("일정 내용은 필수값입니다.");
//        }
//        if(scheduleSaveRequest.getTitle().length() > 30) {
//            throw new IllegalStateException("일정 제목은 최대 30자입니다.");
//        }
//        if(scheduleSaveRequest.getContent().length() > 200) {
//            throw new IllegalStateException("일정 내용은 최대 200자입니다.");
//        }

        User author = userRepository.findById(scheduleSaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        /// Lv1. 일정 생성
        Schedule schedule = new Schedule(
                scheduleSaveRequest.getTitle(),
                scheduleSaveRequest.getContent(),
                author
                // 수정 전 scheduleSaveRequest.getAuthor()
                // 수정 후 author
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleSaveResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getAuthor().getId(),
                //1st 수정. API 응답(ScheduleSaveResponse)에서 User 타입으로 설정, 엔티티에서 User author로 설정.
                //2nd 수정. authorId 확인
                savedSchedule.getAuthor().getUsername(),
                //1st 수정. authorName 확인
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    /**
     * 일정 전체 조회 (읽기 전용 트랜잭션 → 성능 최적화)
     *
     * @param authorId
     * @return
     */

    @Transactional(readOnly = true)
    public List<ScheduleGetAllResponse> findAllSchedules(Long authorId) {     // 에러 : findAllSchedules 의 타입 User author
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleGetAllResponse> scheduleGetAllResponses = new ArrayList<>();

        if (authorId == null) {                     // authorId로 User 조회 → 해당 유저의 일정만 반환
            for (Schedule schedule : schedules) {
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
        // authorId 없으면 전체 일정 반환

        for (Schedule schedule : schedules) {
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
     *
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

        // 부분 업데이트 적용 ( null 이면 해당 필드는 수정 하지 않는다)
        // 규칙 : 제목 공란 금지
        if (scheduleUpdateRequest.getTitle() != null) {
            if (scheduleUpdateRequest.getTitle().isEmpty()) {
                throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
            }
            schedule.updateTitle(scheduleUpdateRequest.getTitle());
        }
        // 규칙 : 내용 공란 금지
        if (scheduleUpdateRequest.getContent() != null) {
            if (scheduleUpdateRequest.getContent().isEmpty()) {
                throw new IllegalArgumentException("내용은 비어 있을 수 없습니다.");
            }
            schedule.updateContent(scheduleUpdateRequest.getContent());
        }

        // save() 불필요: 영속 엔티티 변경 감지
        return ScheduleGetOneResponse.from(schedule);
    }

    /**
     * 일정 삭제
     *
     * @param scheduleId
     */

    @Transactional
    // version 1
    public void deleteSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정을 찾을 수 없습니다.")
        );
        scheduleRepository.delete(schedule);
    }
    // version 2
//    public void deleteSchedule(long scheduleId) {
//        if (!scheduleRepository.existsById(scheduleId)) {
//            throw new IllegalArgumentException("일정을 찾을 수 없습니다.");
//        }
//        scheduleRepository.deleteById(scheduleId);
//    }
}
