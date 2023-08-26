/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TaskRepository extends PagingAndSortingRepository<Task, String>, CrudRepository<Task, String> {
    @Query("select t from Task t where t.createdBy.id = ?1 and t.isPublish = false")
    Optional<Task> findPersonalTaskTemporary(String id);

}
