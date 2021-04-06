package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    String QUERY_MY_REQUEST = "select * from request where user_id = :idUser and moderation_status = :statusString and time <= :date group by id order by time desc";

    @Query(value = QUERY_MY_REQUEST,
            nativeQuery = true)
    Page<Request> findMyRequests(@Param("date") Date date, @Param("idUser") int idUser, @Param("statusString") String statusString, Pageable pageable);

    String QUERY_COUNT_MOD_STATUS_USER = "select count(*) from request where moderation_status = :status and user_id = :userId";

    @Query(value = QUERY_COUNT_MOD_STATUS_USER,
            nativeQuery = true)
    int countModStatusUser(@Param("status") String status, @Param("userId") int userId);

    String QUERY_COUNT_MOD_STATUS = "select count(*) from request where moderation_status = :status";

    @Query(value = QUERY_COUNT_MOD_STATUS,
            nativeQuery = true)
    int countModStatus(@Param("status") String status);

    String QUERY_MODERATION_REQUEST_NEW = "select * from request where moderation_status = :statusString and time <= :date group by id order by time desc";

    @Query(value = QUERY_MODERATION_REQUEST_NEW,
            nativeQuery = true)
    Page<Request> findModerationRequestsNew(@Param("date") Date date, @Param("statusString") String statusString, Pageable pageable);

    String QUERY_MODERATION_REQUEST = "select * from request where moderator_id = :isModerator and is_active = 1 and moderation_status = :statusString and time <= :date group by id order by time desc";

    @Query(value = QUERY_MODERATION_REQUEST,
            nativeQuery = true)
    Page<Request> findModerationRequests(@Param("date") Date date, @Param("isModerator") int isModerator, @Param("statusString") String statusString, Pageable pageable);
}
