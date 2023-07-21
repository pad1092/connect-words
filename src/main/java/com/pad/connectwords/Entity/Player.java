package com.pad.connectwords.Entity;

import com.pad.connectwords.ConnectWordsApplication;
import lombok.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Player {
    private Long id;
    private String name;
    private String color;

    public static Long generateId(){
        Long idGenerate =  ThreadLocalRandom.current().nextLong(1L, 100000L);
        ConnectWordsApplication.listPlayer.forEach(player -> {
            if (idGenerate == player.getId())
                generateId();
        });
        return idGenerate;
    }
}
