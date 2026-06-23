package com.marv.taskmanagement.servise;

import com.marv.taskmanagement.model.dto.request.LoginRequest;
import com.marv.taskmanagement.model.dto.request.RegisterRequest;
import com.marv.taskmanagement.model.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
