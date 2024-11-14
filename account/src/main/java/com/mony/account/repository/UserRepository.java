package com.mony.account.repository;

import com.mony.account.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByCpf(String cpf);

    Optional<UserModel> findByEmail(String email);

    @Query("SELECT u FROM UserModel u")
    Page<UserModel> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
    Optional<UserModel> deleteByCpf(String cpf);

}
