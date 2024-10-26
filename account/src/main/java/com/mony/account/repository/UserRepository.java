package com.mony.account.repository;

import com.mony.account.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    @Query("SELECT DISTINCT u.cpf FROM UserModel u")
    List<String> findCpfs();

    @Query("SELECT u.email FROM UserModel u")
    List<String> findEmails();

    @Query("SELECT u FROM UserModel u")
    Page<UserModel> findAll(Pageable pageable);

    Optional<UserModel> findByEmail(String email);

    UserModel findByEmailAndPassword(String email, String password);
}
