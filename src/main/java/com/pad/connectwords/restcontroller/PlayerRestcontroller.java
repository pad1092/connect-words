package com.pad.connectwords.restcontroller;

import com.pad.connectwords.Entity.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PlayerRestcontroller {
    @GetMapping("/player")
    public Player getPlayer(HttpSession session){
        return (Player)session.getAttribute("player");
    }
}
