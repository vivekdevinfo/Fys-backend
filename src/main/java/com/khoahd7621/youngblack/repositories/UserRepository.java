package com.khoahd7621.youngblack.repositories;

import java.util.Optional;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khoahd7621.youngblack.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByPhone(String phone);

    public Page<User> findAllByRoleAndStatus(ERoles role, EAccountStatus status, Pageable pageable);
}
