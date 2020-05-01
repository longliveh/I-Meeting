package com.example.auth.demo.cache.msg;


import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import lombok.Data;

import java.util.Date;

@Data
public class Message {

    private Reservation reservation;
    private Room room;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
