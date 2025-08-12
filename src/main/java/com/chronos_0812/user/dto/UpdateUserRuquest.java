package com.chronos_0812.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRuquest(
        @Nullable
        @Size(max = 30)
        String username,

        @Nullable
        @Email
        @Size(max = 100)
        String email,

        @Nullable
        @Size(max = 100) 
        String password
) { }
