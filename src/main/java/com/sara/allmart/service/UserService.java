package com.sara.allmart.service;

import com.sara.allmart.dto.request.AddressRequest;
import com.sara.allmart.dto.request.ChangePasswordRequest;
import com.sara.allmart.dto.request.UpdateProfileRequest;
import com.sara.allmart.dto.request.UserRequest;
import com.sara.allmart.dto.response.AddressResponse;
import com.sara.allmart.dto.response.UserResponse;
import com.sara.allmart.entity.SavedAddress;
import com.sara.allmart.entity.User;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.AddressMapper;
import com.sara.allmart.mapper.UserMapper;
import com.sara.allmart.repository.SavedAddressRepository;
import com.sara.allmart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SavedAddressRepository savedAddressRepository;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, UserMapper userMapper, SavedAddressRepository savedAddressRepository, AddressMapper addressMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.savedAddressRepository = savedAddressRepository;
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public AddressResponse addAddress(String email, AddressRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        SavedAddress newAddress = new SavedAddress();
        newAddress.setCity(request.city());
        newAddress.setStreet(request.street());
        newAddress.setZipCode(request.zipCode());
        newAddress.setUser(user);
        return addressMapper.toResponse(savedAddressRepository.save(newAddress));
    }

    public List<AddressResponse> getAllAddresses(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return savedAddressRepository.findByUser(user)
                .stream().map(addressMapper::toResponse).toList();
    }

    public void deleteAddress(String email, Long addressId){
        SavedAddress address = savedAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!"));
        if (!address.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access Denied: You cannot delete an address that isn't yours!");
        }
        savedAddressRepository.delete(address);
    }

    public AddressResponse updateAddress(String email, Long addressId, AddressRequest request) {
        SavedAddress address = savedAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found!"));

        if (!address.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access Denied: You cannot edit an address that isn't yours!");
        }

        if (request.street() != null && !request.street().isBlank()) {
            address.setStreet(request.street());
        }
        if (request.city() != null && !request.city().isBlank()) {
            address.setCity(request.city());
        }
        if (request.zipCode() != null && !request.zipCode().isBlank()) {
            address.setZipCode(request.zipCode());
        }

        return addressMapper.toResponse(savedAddressRepository.save(address));
    }

    public Page<UserResponse> getUsers(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return userRepository.searchUsers(id,pageable)
                .map(userMapper::toResponse);
    }

    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return userMapper.toResponse(user);
    }

    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if(request.firstName()!=null && !request.firstName().isBlank()){
            user.setFirstName(request.firstName());
        }
        if(request.lastName()!=null && !request.lastName().isBlank()){
            user.setLastName(request.lastName());
        }
       userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect.");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
