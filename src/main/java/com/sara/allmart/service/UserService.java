package com.sara.allmart.service;

import com.sara.allmart.dto.request.AddressRequest;
import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.SavedAddress;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.UserMapper;
import com.sara.allmart.repository.SavedAddressRepository;
import com.sara.allmart.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SavedAddressRepository savedAddressRepository;


    public UserService(UserRepository userRepository, UserMapper userMapper, SavedAddressRepository savedAddressRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.savedAddressRepository = savedAddressRepository;
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

    public SavedAddress addAddress(String email, AddressRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        SavedAddress newAddress = new SavedAddress();
        newAddress.setCity(request.city());
        newAddress.setStreet(request.street());
        newAddress.setZipCode(request.zipCode());
        newAddress.setUser(user);
        return savedAddressRepository.save(newAddress);
    }

    public List<SavedAddress> getAllAddresses(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return savedAddressRepository.findByUser(user);
    }

    public void deleteAddress(String email, Long addressId){
        SavedAddress address = savedAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!"));
        if (!address.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access Denied: You cannot delete an address that isn't yours!");
        }
        savedAddressRepository.delete(address);
    }
}
