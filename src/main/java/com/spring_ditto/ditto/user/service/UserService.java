package com.spring_ditto.ditto.user.service;

import com.spring_ditto.ditto.user.User;
import com.spring_ditto.ditto.user.UserDTO;
import com.spring_ditto.ditto.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUser(UserDTO userDTO, Long userId){
        log.info(String.valueOf(userId));
        log.info(userDTO.getNickname());
        log.info(userDTO.getType());
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당사용자가없습니다"));
        user.authorizeUser();
        user.setType(userDTO.getType());
        user.setNickname(userDTO.getNickname());
    }


}
