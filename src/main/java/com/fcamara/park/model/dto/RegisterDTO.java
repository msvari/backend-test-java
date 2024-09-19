package com.fcamara.park.model.dto;

import com.fcamara.park.model.enumeration.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
