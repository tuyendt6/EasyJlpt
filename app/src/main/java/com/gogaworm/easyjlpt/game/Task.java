package com.gogaworm.easyjlpt.game;

import java.util.Date;

/**
 * Created on 17.04.2017.
 *
 * @author ikarpova
 */
public class Task <T> {
    public long id;
    public T value;
    public int progress;
    public GameController.GameType gameType;
    public Date lastPractice;

    public Game game;
    public int leftGames;

    public void setupGame(GameController.GameType gameType, Game game) {
        this.gameType = gameType;
        this.game = game; //todo: pass this task to the game
    }

    public boolean isComplete() {
        return leftGames == 0;
    }
}
