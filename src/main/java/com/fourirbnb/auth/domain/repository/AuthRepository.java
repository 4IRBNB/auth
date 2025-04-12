package com.fourirbnb.auth.domain.repository;

import com.fourirbnb.auth.domain.entity.Auth;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {

}
