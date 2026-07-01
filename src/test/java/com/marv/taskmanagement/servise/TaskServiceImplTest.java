package com.marv.taskmanagement.servise;

import com.marv.taskmanagement.constants.ErrorMessages;
import com.marv.taskmanagement.exceptions.ResourceNotFoundException;
import com.marv.taskmanagement.mapper.TaskMapper;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.entity.UserEntity;
import com.marv.taskmanagement.repository.TaskRepository;
import com.marv.taskmanagement.repository.UserRepository;
import com.marv.taskmanagement.servise.impl.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UserEntity currentUser;

    @BeforeEach
    void setUp() {

        currentUser = new UserEntity();
        currentUser.setId(1L);
        currentUser.setUsername("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken("testUser", null));
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(currentUser));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getById_shouldReturnTask_whenTaskExists() {

        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setTitle("Test task");
        taskEntity.setUser(currentUser);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Test task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toResponse(taskEntity)).thenReturn(taskResponse);

        TaskResponse result = taskService.getById(taskId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getTitle()).isEqualTo("Test task");
    }

    @Test
    void getById_shouldThrowException_whenTaskNotFound() {

        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getById(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorMessages.TASK_NOT_FOUND);
    }
}
