package com.example.example5test.repository;

import com.example.example5test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name like %?1%" +
            "or u.email like %?1%"
            + "or u.password like %?1%")
    List<User> search(String keyword);

    Optional<User> findByEmail(String email);




}
