package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    String QUERY_USER_CHECK_EMAIL_PASSWORD = "select * from user where name = :nameUser and password = :pass";

    @Query(value = QUERY_USER_CHECK_EMAIL_PASSWORD,
            nativeQuery = true)
    List<User> findUser(@Param("nameUser") String name, @Param("pass") String password);

    String QUERY_USERS = "select * from user where is_operator in (0,1) order by id asc";

    @Query(value = QUERY_USERS,
            nativeQuery = true)
    Page<User> findUsers(Pageable pageable);

    String QUERY_COUNT_USERS = "select count(*) from user where is_operator in (0,1) order by id asc";

    @Query(value = QUERY_COUNT_USERS,
            nativeQuery = true)
    int countUsers();

}
