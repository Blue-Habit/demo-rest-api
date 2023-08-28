/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends PagingAndSortingRepository<Task, String>, CrudRepository<Task, String> {
    @Query("select t from Task t where t.name like concat(?1, '%')")
    List<Task> findByNameStartWith(String name);

    @Query("select t from Task t where t.createdBy = ?1 and t.taskEnd between ?2 and ?3")
    Page<Task> findTaskByUserBetweenDate(UserCredential createdBy, OffsetDateTime taskEndStart, OffsetDateTime taskEndEnd, Pageable pageable);

    @Query("select t from Task t where t.createdBy = ?1")
    Page<Task> getListTaskByUser(UserCredential createdBy, Pageable pageable);

    @Query("select t from Task t where t.createdBy.id = ?1 and t.isPublish = false")
    Optional<Task> findPersonalTaskTemporary(String id);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
}
