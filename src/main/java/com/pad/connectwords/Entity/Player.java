package com.pad.connectwords.Entity;

import lombok.*;

import java.security.Principal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Player implements Principal {
    private UUID id;
    private String name;
    private String color;

    public static UUID generateId(){
        return UUID.randomUUID();
    }
}
