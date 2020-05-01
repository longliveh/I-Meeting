package com.example.auth.demo.JSONApi;

import lombok.Data;

import java.util.List;

@Data
public class AddparticipantApi {
    private List<Integer> participants;
    private Integer reservation_id;
}
