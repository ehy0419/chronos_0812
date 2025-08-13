package com.chronos_0812.user.service;

import com.chronos_0812.common.config.PasswordEncoder;
import com.chronos_0812.user.dto.get.UserGetAllResponse;
import com.chronos_0812.user.dto.get.UserGetOneResponse;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.dto.save.UserSaveResponse;
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
 * PasswordEncoder 참고 코드는 노션 과제 발제에서 확인.
 */

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 회원가입 및 유저 생성 */

    // 수정 전
    // public Long save(UserSaveRequest userSaveRequest) {
    // 수정 이유
    // Long id 만 반환하는 것이 아니라 UserSaveResponse DTO로 반환해서
    // 다른 정보도 전달이 가능, 비밀번호는 제외!


    /** 회원가입 */
    @Transactional
    public UserSaveResponse save(UserSaveRequest userSaveRequest) {
        // 수정 전: username/email만 검사
        if (userSaveRequest.getUsername() == null || userSaveRequest.getUsername().trim().isEmpty()
        ) {
            throw new IllegalArgumentException("유저명은 필수입니다.");
        }
        if (userSaveRequest.getEmail() == null || userSaveRequest.getEmail().trim().isEmpty()
        ) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        // 수정 후: password도 추가해서 검사 (nullable=false 이므로 필수)
        if (userSaveRequest.getPassword() == null || userSaveRequest.getPassword().trim().isEmpty()
        ) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (userRepository.existsByEmail(userSaveRequest.getEmail())
        ) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        ///  암호화 ///
        // Lv6에서: String encoded = passwordEncoder.encode(userSaveRequest.getPassword());
        // PasswordEncoder 클래스 생성 ->>>> 클래스 생성 확인

        /**  build.gradle 에 아래의 의존성을 추가
         implementation 'at.favre.lib:bcrypt:0.10.2'
         */

        /**  PasswordEncoder 클래스 생성
         import at.favre.lib.crypto.bcrypt.BCrypt;
         import org.springframework.stereotype.Component;

         @Component
         public class PasswordEncoder {

         public String encode(String rawPassword) {
         return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
         }

         public boolean matches(String rawPassword, String encodedPassword) {
         BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
         return result.verified;
         }
         }
         */

        // 수정 전 : return saved.getId();
//        User user = new User(
//                userSaveRequest.getUsername(),
//                userSaveRequest.getEmail(),
//                userSaveRequest.getPassword()
//        );
//        return userRepository.save(user).getId();

        ///  lv.6 비밀번호 암호화(도전)
        // 평문을 해시로 변경!
        String encoded = passwordEncoder.encode(userSaveRequest.getPassword());

        // 수정 후 응답 DTO로 변환
        User saved = userRepository.save(
                new User(
                        userSaveRequest.getUsername(),
                        userSaveRequest.getEmail(),
//                        userSaveRequest.getPassword(),        //비밀번호 암호화 encoded
                        encoded                                 //비밀번호 해시만 저장
                )
        );
        return UserSaveResponse.from(saved);                // 당연히,응답에 비번은 없음
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + id));
        return UserGetOneResponse.from(user);
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

        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + id));

        // 이메일이 바뀌는 경우 중복 방지
        if (!user.getEmail().equals(userUpdateRequest.getEmail())
                && userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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
