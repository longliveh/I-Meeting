package com.example.auth.demo.entity.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private Long id;
    private String name;
    private String nameZh;
}
