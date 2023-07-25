package com.pad.connectwords.service;

import com.pad.connectwords.ConnectWordsApplication;
import com.pad.connectwords.Entity.Gameplay;
import com.pad.connectwords.Entity.Message;
import com.pad.connectwords.Entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GameplayService {
    private static final String GAMEPLAY_DES = "/gameplay/room/";
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public Gameplay createNewGame(Player hostPlayer){
        Gameplay gameplay = new Gameplay (Gameplay.generateId(),  hostPlayer, null, "Waiting");
        ConnectWordsApplication.listGameplay.add(gameplay);
        return gameplay;
    }

    public Gameplay getGameplayById(Long id){
        Gameplay res = new Gameplay();
        System.out.println("ID REQUETS: " + id);
        for (Gameplay gameplay : ConnectWordsApplication.listGameplay){
            System.out.println("GAMEPLAY: " + gameplay.getId());
            if (gameplay.getId().equals(id)) {
                res = gameplay;
            }
        }
        return res;
    }

    public boolean exitGameProcess(Gameplay gameplay, Player exitPlayer){
        Player host = gameplay.getHost();
        Player player = gameplay.getPlayer();

//        if exit player not in the game, then do not anything
        if (!checkPlayerOfGame(exitPlayer, gameplay))
            return false;

        ConnectWordsApplication.listGameplay.remove(gameplay);
        return true;
    }

    public boolean joinGame(Long id, HttpSession session){
        Gameplay gameplay =getGameplayById(id);
        if (gameplay.getId() == null)
            return false;
        if (gameplay.getPlayer() != null && gameplay.getPlayer() != null)
            return false;
        gameplay.setPlayer((Player) session.getAttribute("player"));
        gameplay.setStatus("Full");
        handleSocketJoinGame(id, session);
        return true;
    }

    private boolean checkPlayerOfGame(Player player, Gameplay gameplay){
        if (player == gameplay.getPlayer() || player == gameplay.getHost())
            return true;
        return false;
    }

    public void handleSendingWords(Long idGame, String word, HttpSession session){
        Player sender = (Player) session.getAttribute("player");
        Gameplay gameplay = getGameplayById(idGame);
        Player receiver = gameplay.getHost().getId() == sender.getId() ? gameplay.getPlayer() : gameplay.getHost();

        Message message = new Message();
        message.setMessage(word);
        message.setToWhom(receiver);
        message.setFromWho(sender);
        message.setType(Message.MesssageType.GAME);

        addMessageToQueue(GAMEPLAY_DES + idGame, message);
    }
    public void handleSocketJoinGame(Long idGame, HttpSession session){
        Player sender = (Player) session.getAttribute("player");
        Gameplay gameplay = getGameplayById(idGame);
        Player receiver = gameplay.getHost().getId() == sender.getId() ? gameplay.getPlayer() : gameplay.getHost();

        Message message = new Message();
        message.setMessage("A player joined game");
        message.setToWhom(receiver);
        message.setFromWho(sender);
        message.setType(Message.MesssageType.JOIN);

        addMessageToQueue(GAMEPLAY_DES + idGame, message);
    }

    public void handleSocketStartGame(Long idGame, HttpSession session){
        if (getGameplayById(idGame).getPlayer() == null)
            return;

        Message message = new Message();
        message.setType(Message.MesssageType.START);
        message.setFromWho((Player)session.getAttribute("player"));
        addMessageToQueue(GAMEPLAY_DES + idGame, message);
    }

    public void handleChat(Long idGame, HttpSession session, String content){
        System.out.println(content);
        Player sender = (Player) session.getAttribute("player");
        Gameplay gameplay = getGameplayById(idGame);
        Player receiver = gameplay.getHost().getId() == sender.getId() ? gameplay.getPlayer() : gameplay.getHost();

        Message message = new Message();
        message.setMessage(content);
        message.setToWhom(receiver);
        message.setFromWho(sender);
        message.setType(Message.MesssageType.CHAT);

        addMessageToQueue(GAMEPLAY_DES + idGame, message);
    }

    public void handleWords(Long idGame, HttpSession session, String content){
        Player sender = (Player) session.getAttribute("player");
        Gameplay gameplay = getGameplayById(idGame);
        Player receiver = gameplay.getHost().getId() == sender.getId() ? gameplay.getPlayer() : gameplay.getHost();

        Message message = new Message();
        message.setMessage(content);
        message.setToWhom(receiver);
        message.setFromWho(sender);
        message.setType(Message.MesssageType.GAME);

        addMessageToQueue(GAMEPLAY_DES + idGame, message);
    }


    private void addMessageToQueue(String destination, Message message){
        simpMessagingTemplate.convertAndSend(destination, message);
    }
}
