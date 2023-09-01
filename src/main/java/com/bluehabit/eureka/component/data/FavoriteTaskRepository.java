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

public interface FavoriteTaskRepository extends PagingAndSortingRepository<FavoriteTask, String>, CrudRepository<FavoriteTask, String> {
    @Query("select f from FavoriteTask f where f.user = ?1")
    Page<FavoriteTask> findStarredTaskByUser(UserCredential user, Pageable pageable);

}
