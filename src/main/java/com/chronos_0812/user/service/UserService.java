package com.chronos_0812.user.service;

import com.chronos_0812.user.dto.get.UserGetAllResponse;
import com.chronos_0812.user.dto.get.UserGetOneResponse;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.dto.update.UserUpdateRequest;
import com.chronos_0812.user.entity.User;
import com.chronos_0812.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 유저 비즈니스 로직
 * Lv3: password 저장 로직 포함
 * - 현재는 평문 저장 (테스트/학습용)
 * Lv6에서 PasswordEncoder 주입 예정.
 */

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;

    /** 회원가입 및 유저 생성 */
    @Transactional
    public Long save(UserSaveRequest userSaveRequest) {
        if (userSaveRequest.getUsername() == null || userSaveRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("유저명은 필수입니다.");
        }
        if (userSaveRequest.getEmail() == null || userSaveRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        ///  암호화 ///
        // Lv6에서: String encoded = passwordEncoder.encode(req.getPassword());
        User user = new User(userSaveRequest.getUsername(), userSaveRequest.getEmail(),  userSaveRequest.getPassword());
        return userRepository.save(user).getId();
    }

    /** 유저 전체 조회 */
    @Transactional(readOnly = true)
    public List<UserGetAllResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserGetAllResponse::from)
                .toList();
    }

    /** 유저 단건 조회 */
    @Transactional(readOnly = true)
    public UserGetOneResponse findOne(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + id));
        return UserGetOneResponse.from(u);
    }

    /** 유저 수정
     * 비밀번호를 제외한 일반 정보(이름, 이메일 변경)
     * */
    @Transactional
    public void update(Long id, UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.getUsername() == null || userUpdateRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("유저명은 필수입니다.");
        }
        if (userUpdateRequest.getEmail() == null || userUpdateRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + id));

        // 이메일이 바뀌는 경우 중복 방지
        if (!user.getEmail().equals(userUpdateRequest.getEmail()) && userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        user.updateUsername(userUpdateRequest.getUsername());
        user.updateEmail(userUpdateRequest.getEmail());
    }

    /** 유저 삭제 */
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 유저가 존재하지 않습니다. id=" + id);
        }
        userRepository.deleteById(id);
    }
}
