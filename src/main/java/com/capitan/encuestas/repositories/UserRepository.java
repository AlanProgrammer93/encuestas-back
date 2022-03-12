package com.capitan.encuestas.repositories;

import com.capitan.encuestas.entities.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    public UserEntity findByEmail(String email);

    public UserEntity findById(long id);
}
