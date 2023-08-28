/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.services;

import com.bluehabit.eureka.common.AbstractBaseService;
import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.FileUtil;
import com.bluehabit.eureka.common.PageResponse;
import com.bluehabit.eureka.component.AttachmentType;
import com.bluehabit.eureka.component.data.SubTask;
import com.bluehabit.eureka.component.data.SubTaskRepository;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskAttachment;
import com.bluehabit.eureka.component.data.TaskAttachmentRepository;
import com.bluehabit.eureka.component.data.TaskPriorityRepository;
import com.bluehabit.eureka.component.data.TaskRepository;
import com.bluehabit.eureka.component.data.TaskStatusRepository;
import com.bluehabit.eureka.component.model.PublishTaskRequest;
import com.bluehabit.eureka.component.model.UploadAttachmentRequest;
import com.bluehabit.eureka.exception.GeneralErrorException;
import com.bluehabit.eureka.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class TaskService extends AbstractBaseService {
    private static final int maxAttachment = 3;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;
    @Autowired
    private SubTaskRepository subTaskRepository;

    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTask(Pageable pageable) {
        return getAuthenticatedUser(userCredential -> {
            final Page<Task> getListTask = taskRepository.getListTaskByUser(userCredential, pageable);

            return BaseResponse.success(translate(""), new PageResponse<>(getListTask));
        }, () -> {
            throw new GeneralErrorException(HttpStatus.UNAUTHORIZED.value(), translate(""));
        });
    }

    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTaskByDate(
        String start,
        String end,
        Pageable pageable
    ) {
        return getAuthenticatedUser(userCredential -> {
            try {
                if (start.isBlank() || end.isBlank()) {
                    throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate(""));
                }
                final OffsetDateTime startDate = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(start)
                    .toInstant()
                    .atOffset(ZoneOffset.UTC);

                final OffsetDateTime endDate = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(end)
                    .toInstant()
                    .atOffset(ZoneOffset.UTC);

                if (endDate.isBefore(startDate)) {
                    throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate(""));
                }
                final Page<Task> getListTask = taskRepository.findTaskByUserBetweenDate(
                    userCredential,
                    startDate,
                    endDate,
                    pageable
                );
                return BaseResponse.success(translate(""), new PageResponse<>(getListTask));
            } catch (ParseException parseException) {
                throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), parseException.getMessage());
            }

        }, () -> {
            throw new GeneralErrorException(HttpStatus.UNAUTHORIZED.value(), translate(""));
        });
    }

    public ResponseEntity<BaseResponse<List<Task>>> searchTask(
        String query
    ) {
        return getAuthenticatedUser(userCredential -> {
            final List<Task> list = taskRepository.findByNameStartWith(query);
            return BaseResponse.success(translate(""), list);
        }, () -> {
            throw new GeneralErrorException(HttpStatus.NO_CONTENT.value(), translate(""));
        });
    }

    public ResponseEntity<BaseResponse<Task>> createTemporaryTask() {
        return getAuthenticatedUser(user -> {
            return taskRepository.findPersonalTaskTemporary(user.getId())
                .map(task -> {
                    return BaseResponse.success(translate("task.create.temp.use.existing"), task);
                })
                .orElseGet(() -> {
                    final String uuid = UUID.randomUUID().toString();
                    final OffsetDateTime date = OffsetDateTime.now();
                    final Task tempTask = new Task();
                    tempTask.setId(uuid);
                    tempTask.setCreatedBy(user);
                    tempTask.setPublish(false);
                    tempTask.setCreatedAt(date);
                    tempTask.setUpdatedAt(date);

                    final Task savedTempTask = taskRepository.save(tempTask);

                    return BaseResponse.success(translate("task.create.temp.success"), savedTempTask);
                });
        }, () -> {
            throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("task.create.temp.user.not.allowed"));
        });
    }

    public ResponseEntity<BaseResponse<TaskAttachment>> uploadAttachment(
        UploadAttachmentRequest request
    ) {
        validate(request);
        return taskRepository.findById(request.taskId())
            .map(task -> {
                if (task.getAttachments().size() >= maxAttachment) {
                    throw new GeneralErrorException(HttpStatus.FORBIDDEN.value(), translate("task.attachment.upload.failed.max"));
                }
                final String idAttachment = UUID.randomUUID().toString();
                final OffsetDateTime currentDate = OffsetDateTime.now();
                final TaskAttachment taskAttachment = new TaskAttachment();
                taskAttachment.setId(idAttachment);
                taskAttachment.setMimeType(request.file().getContentType());
                taskAttachment.setTask(task);
                taskAttachment.setType(AttachmentType.IMAGE);
                taskAttachment.setCreatedAt(currentDate);
                taskAttachment.setUpdatedAt(currentDate);

                return FileUtil.saveFile(request.file(), idAttachment).map(fileName -> {
                    taskAttachment.setName(fileName);
                    final TaskAttachment savedTaskAttachment = taskAttachmentRepository.save(taskAttachment);
                    return BaseResponse.success(translate("task.attachment.upload.success"), savedTaskAttachment);
                }).orElseThrow(() -> {
                    return new GeneralErrorException(
                        HttpStatus.BAD_REQUEST.value(),
                        translate("task.attachment.upload.file.not.uploaded")
                    );
                });
            })
            .orElseThrow(() -> new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("task.attachment.upload.task.not.found")));
    }

    public ResponseEntity<BaseResponse<TaskAttachment>> deleteAttachment(
        String attachmentId
    ) {
        return taskAttachmentRepository.findById(attachmentId)
            .map(taskAttachment -> {
                taskAttachmentRepository.deleteById(attachmentId);
                return BaseResponse.success(translate(""), taskAttachment);
            })
            .orElseThrow(() -> new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("")));
    }

    public ResponseEntity<BaseResponse<Task>> publishTask(
        PublishTaskRequest request
    ) {
        validate(request);
        final OffsetDateTime currentDate = OffsetDateTime.now();
        final OffsetDateTime startTask = OffsetDateTime.parse(request.start());
        final OffsetDateTime endTask = OffsetDateTime.parse(request.end());
        return getAuthenticatedUser(user -> {
            return taskRepository
                .findById(request.taskId())
                .map(Optional::of)
                .orElseGet(() -> {
                    final String uuid = UUID.randomUUID().toString();
                    final Task task = new Task();
                    task.setId(uuid);
                    task.setCreatedBy(user);
                    final Task savedTempTask = taskRepository.save(task);
                    return Optional.of(savedTempTask);
                })
                //then saved all
                .map(task -> {

                    taskPriorityRepository.findById(request.priorityId()).ifPresent(task::setPriority);
                    task.setName(request.taskName());
                    task.setDescription(request.taskDescription());
                    task.setTaskStart(startTask);
                    task.setTaskEnd(endTask);
                    task.setPublish(true);
                    task.setCreatedBy(user);
                    task.setCreatedAt(currentDate);
                    task.setUpdatedAt(currentDate);

                    final List<SubTask> subTasks = request.subtask().stream().map((subTaskRequest) -> {
                        final String uuid = UUID.randomUUID().toString();
                        return new SubTask(
                            uuid,
                            null,
                            task,
                            subTaskRequest.subTaskName(),
                            subTaskRequest.done(),
                            currentDate,
                            currentDate,
                            false
                        );
                    }).toList();
                    final Iterable<SubTask> savedSubTasks = subTaskRepository.saveAll(subTasks);
                    final List<SubTask> subTasksFinal = StreamSupport.stream(savedSubTasks.spliterator(), false)
                        .toList();
                    task.setSubtasks(subTasksFinal);
                    final Task savedTask = taskRepository.save(task);

                    return BaseResponse.success(translate("task.publish.success"), savedTask);
                }).orElseThrow(() -> new GeneralErrorException(HttpStatus.NO_CONTENT.value(), translate("task.publish.failed")));
        }, () -> {
            throw new UnAuthorizedException(HttpStatus.UNAUTHORIZED.value(), translate("unauthorized"));
        });
    }

    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTaskByStatus(
        String statusId,
        Pageable pageable
    ) {
        return getAuthenticatedUser(userCredential -> {
            if (statusId.isBlank()) {
                throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate(""));
            }
            return taskStatusRepository
                .findById(statusId)
                .map(taskStatus -> {
                    final Page<Task> listTaskByStatus = taskRepository.findByStatus(
                        taskStatus,
                        pageable
                    );
                    return BaseResponse.success(translate(""), new PageResponse<>(listTaskByStatus));
                })
                .orElseThrow(() -> new GeneralErrorException(HttpStatus.NOT_FOUND.value(), translate("")));
        }, () -> {
            throw new GeneralErrorException(HttpStatus.UNAUTHORIZED.value(), translate(""));
        });
    }

}
