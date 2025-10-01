package com.example.mypost.user;

import com.example.mypost._core.errors.exception.Exception400;
import com.example.mypost._core.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public User signUp(UserRequest.SignUpDTO signUpDTO) {
        userJpaRepository.findByUsername(signUpDTO.getUsername())
                .ifPresent(user1 -> {
                    throw new Exception400("이미 존재하는 사용자명입니다");
                });

        return userJpaRepository.save(signUpDTO.toEntity());
    }

    public User login(UserRequest.LoginDTO loginDTO) {

        return userJpaRepository
                .findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                   return new Exception400("사용자 명 또는 비밀번호가 틀렸어요");
                });
    }

    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> {
            log.warn("사용자 조회 실패 - ID {}", id);
            return new Exception404("사용자를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public User updateById(Long userId, UserRequest.UpdateDTO updateDTO){
        User user = findById(userId);
        user.setPassword(updateDTO.getPassword());
        return user;
    }


}
