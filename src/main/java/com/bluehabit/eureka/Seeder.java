/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka;

import com.bluehabit.eureka.component.AuthProvider;
import com.bluehabit.eureka.component.UserStatus;
import com.bluehabit.eureka.component.data.SubTaskRepository;
import com.bluehabit.eureka.component.data.TaskPriority;
import com.bluehabit.eureka.component.data.TaskPriorityRepository;
import com.bluehabit.eureka.component.data.TaskStatus;
import com.bluehabit.eureka.component.data.TaskStatusRepository;
import com.bluehabit.eureka.component.data.UserCredential;
import com.bluehabit.eureka.component.data.UserCredentialRepository;
import com.bluehabit.eureka.component.data.UserProfile;
import com.bluehabit.eureka.component.data.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder implements ApplicationRunner {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String id = "e3daf232-2b2c-4720-9e87-7d23e869f8e8";
        //no empty
        final UserCredential userCredential = new UserCredential();
        userCredential.setId(id);
        userCredential.setAuthProvider(AuthProvider.BASIC);
        userCredential.setPassword(encoder.encode("12345678"));
        userCredential.setEmail("triandamai@gmail.com");
        userCredential.setStatus(UserStatus.ACTIVE);
        userCredential.setCreatedAt(OffsetDateTime.now());
        userCredential.setUpdatedAt(OffsetDateTime.now());

        final UserProfile profile = new UserProfile();
        profile.setId(id);
        profile.setUserId(id);
        profile.setKey("fullName");
        profile.setValue("Trian Damai");

        if (userCredentialRepository.findByEmail(userCredential.getId()).isEmpty()) {
            final UserProfile savedProfile = userProfileRepository.save(profile);
            final List<UserProfile> profiles = new ArrayList<>();
            profiles.add(savedProfile);
            userCredential.setUserInfo(profiles);
            userCredentialRepository.save(userCredential);
        }

        addPriority();
        addStatus();
    }

    private void addPriority() {
        //task priority

        final List<TaskPriority> priorities = new ArrayList<>();
        priorities.add(
            new TaskPriority(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e1",
                "High",
                "Priority high",
                "#FFFFFF",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            ));
        priorities.add(
            new TaskPriority(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e2",
                "Medium",
                "Priority Medium",
                "#FFFFFF",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            ));
        priorities.add(
            new TaskPriority(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e3",
                "Normal",
                "Priority Normal",
                "#FFFFFF",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            ));
        priorities.add(
            new TaskPriority(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e4",
                "Low",
                "Priority Low",
                "#FFFFFF",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            ));

        taskPriorityRepository.saveAll(priorities);

    }

    private void addStatus() {
        //status task

        final List<TaskStatus> taskStatuses = new ArrayList<>();

        taskStatuses.add(
            new TaskStatus(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e4",
                "To-Do",
                "todo",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            )
        );
        taskStatuses.add(
            new TaskStatus(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e5",
                "In Progress",
                "progress",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            )
        );
        taskStatuses.add(
            new TaskStatus(
                "e3daf232-2b2c-4720-9e87-7d23e869f8e6",
                "Selesai",
                "finish",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                false
            )
        );

        taskStatusRepository.saveAll(taskStatuses);
    }
}
