package com.anthonyk.Helpdesk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonyk.Helpdesk.user.model.User;

@Repository 
public interface UserRepository extends JpaRepository<User, Long> {

}
