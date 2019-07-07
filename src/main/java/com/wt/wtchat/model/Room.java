package com.wt.wtchat.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 游戏房间
 *
 * @author Zed
 */
@Entity
@Table(name = "room")
public class Room implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "game_type", length = 10)
    private int gameType;
    @Column(name = "game_name", length = 10)
    private String gameName;

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
