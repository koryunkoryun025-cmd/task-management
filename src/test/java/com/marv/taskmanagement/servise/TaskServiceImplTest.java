package com.marv.taskmanagement.servise;

import com.marv.taskmanagement.constants.ErrorMessages;
import com.marv.taskmanagement.exceptions.ResourceNotFoundException;
import com.marv.taskmanagement.mapper.TaskMapper;
import com.marv.taskmanagement.model.dto.request.CreateTaskRequest;
import com.marv.taskmanagement.model.dto.request.UpdateTaskRequest;
import com.marv.taskmanagement.model.dto.response.TaskResponse;
import com.marv.taskmanagement.model.entity.TaskEntity;
import com.marv.taskmanagement.model.entity.UserEntity;
import com.marv.taskmanagement.model.enums.TaskStatus;
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

    @Test
    void getById_shouldThrowException_whenTaskBelongsToAnotherUser() {
        Long taskId = 1L;

        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L);
        anotherUser.setUsername("anotherUser");

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setUser(anotherUser);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        assertThatThrownBy(() -> taskService.getById(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorMessages.TASK_NOT_FOUND);
    }

    @Test
    void create_shouldReturnTaskResponse_whenValidRequest() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("New task")
                .build();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("New task");

        TaskEntity savedEntity = new TaskEntity();
        savedEntity.setId(1L);
        savedEntity.setTitle("New task");
        savedEntity.setUser(currentUser);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("New task");

        when(taskMapper.toEntity(request)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedEntity);
        when(taskMapper.toResponse(savedEntity)).thenReturn(taskResponse);

        TaskResponse result = taskService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New task");
    }

    @Test
    void update_shouldReturnUpdatedTask_whenTaskExists() {
        Long taskId = 1L;

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .title("Updated task")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        TaskEntity existing = new TaskEntity();
        existing.setId(taskId);
        existing.setTitle("Updated task");
        existing.setUser(currentUser);

        TaskEntity saved = new TaskEntity();
        saved.setId(taskId);
        saved.setTitle("Updated task");
        saved.setStatus(TaskStatus.IN_PROGRESS);
        saved.setUser(currentUser);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Updated task");
        taskResponse.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(saved);
        when(taskMapper.toResponse(saved)).thenReturn(taskResponse);

        TaskResponse result = taskService.update(taskId, request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated task");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void update_shouldThrowException_whenTaskNotFound() {
        Long taskId = 999L;
        UpdateTaskRequest request = UpdateTaskRequest.builder().build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(taskId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorMessages.TASK_NOT_FOUND);
    }

    @Test
    void delete_shouldDeleteTask_whenTaskExists() {
        Long taskId = 1L;

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setUser(currentUser);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        taskService.delete(taskId);

        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void delete_shouldThrowException_whenTaskNotFound() {
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorMessages.TASK_NOT_FOUND);
    }
}
