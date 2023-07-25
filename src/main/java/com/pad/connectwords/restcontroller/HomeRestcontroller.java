package com.pad.connectwords.restcontroller;

import com.pad.connectwords.Entity.Player;
import com.pad.connectwords.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1")

public class HomeRestcontroller {
    @Autowired
    IndexService indexService;

    @GetMapping("new-player/{name}/{color}")
    public Player newPlayer (@PathVariable("name") String name, @PathVariable("color") String color, HttpSession session){
        session.setAttribute("player", indexService.createPlayer(name, color));
        return (Player)(session.getAttribute("player"));
    }
}
