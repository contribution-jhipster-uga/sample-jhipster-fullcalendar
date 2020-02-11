package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Calendar entity.
 */
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long>, JpaSpecificationExecutor<Calendar> {

    @Query("select calendar from Calendar calendar where calendar.ownedBy.login = ?#{principal.username}")
    List<Calendar> findByOwnedByIsCurrentUser();

    @Query(value = "select distinct calendar from Calendar calendar left join fetch calendar.sharedWiths",
        countQuery = "select count(distinct calendar) from Calendar calendar")
    Page<Calendar> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct calendar from Calendar calendar left join fetch calendar.sharedWiths")
    List<Calendar> findAllWithEagerRelationships();

    @Query("select calendar from Calendar calendar left join fetch calendar.sharedWiths where calendar.id =:id")
    Optional<Calendar> findOneWithEagerRelationships(@Param("id") Long id);

}
