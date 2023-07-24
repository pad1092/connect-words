package com.pad.connectwords.Entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private Player toWhom;
    private Player fromWho;
    private String message;
    private MesssageType type;
    public enum MesssageType {
        CHAT,
        GAME,
        QUIT,
        JOIN,
        START
    }
}
