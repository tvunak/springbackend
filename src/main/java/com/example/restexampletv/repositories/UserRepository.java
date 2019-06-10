package com.example.restexampletv.repositories;


import com.example.restexampletv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    User getUserByUsername(String  username);
    User findOneByUsername(String  username);
    User getUserById(Long  id);

}
