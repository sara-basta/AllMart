package com.sara.allmart.service;

import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.UserMapper;
import com.sara.allmart.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
}
