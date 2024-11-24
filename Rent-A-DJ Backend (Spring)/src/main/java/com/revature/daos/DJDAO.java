package com.revature.daos;

import com.revature.models.DJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*By extending JpaRepository, we get access to various DAO methods that we don't need to write

    JpaRepository takes two generics:
    -The Java Model we intend to perform DB operations with (DJ => DJs table in this case)
    -The datatype of the Primary Key field in the Model (Integer => DJId in this case */
@Repository //1 of the 4 stereotype annotations. It registers this class as a Spring Bean
public interface DJDAO extends JpaRepository<DJ, Integer> {
    DJ findByDjId(int djId);

    List<DJ> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<DJ> findByUsername(String username);

//    List<DJ> findByDJname(String DJname);
//
//    List<DJ> findByDJnameStartingWith(String DJname);

}