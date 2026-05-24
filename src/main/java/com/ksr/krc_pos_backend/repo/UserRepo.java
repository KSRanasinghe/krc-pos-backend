package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUsername(String username);
}
