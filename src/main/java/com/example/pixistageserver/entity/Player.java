package com.example.pixistageserver.entity;

import lombok.Data;

@Data
public class Player {
    String id;
    Integer x;
    Integer y;
    String src;
    String nickname;
    String direction;
}
