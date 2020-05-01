package com.example.auth.demo.cache;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class PhoneCode {

    private final Map<String,Code> codemap;

    public PhoneCode() {
        this.codemap = new HashMap<>();
    }

    public void removecode(String phonenum)
    {
        codemap.remove(phonenum);
    }
}
