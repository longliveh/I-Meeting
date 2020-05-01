package com.example.auth.demo.cache;

import lombok.Data;

@Data
public class Code {
    private String code;
    private long begintime;

    public Code(String code, long begintime) {
        this.code = code;
        this.begintime = begintime;
    }
}
