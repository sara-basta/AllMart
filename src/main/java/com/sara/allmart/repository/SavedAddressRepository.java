package com.sara.allmart.repository;

import com.sara.allmart.entity.SavedAddress;
import com.sara.allmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedAddressRepository extends JpaRepository<SavedAddress,Long> {
    List<SavedAddress> findByUser(User user);
}
