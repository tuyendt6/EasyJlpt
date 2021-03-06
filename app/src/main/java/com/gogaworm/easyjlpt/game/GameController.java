package com.gogaworm.easyjlpt.game;

import com.gogaworm.easyjlpt.data.Word;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 17.04.2017.
 *
 * @author ikarpova
 */
public class GameController {
    int MAX_GAME_TYPE = 6;

    public enum GameType {
        FLASH_CARD,
        SELECT_TRANSLATION_BY_READING,
        SELECT_READING_BY_KANJI, //todo: add adv version that will find similar kanji
        SELECT_TRANSLATION_BY_KANJI,
        SELECT_KANJI_BY_READING, //todo: add adv version that will find similiar readings
        SELECT_KANJI_BY_TRANSLATION,
        WRITE_READING,
        MULTYSELECT_KANJI_READING,
        WRITE_KANJI_IN_KANJI
    }

    private List<Task> tasks = new LinkedList<>();
    //todo: create other arrays for second run and separate for wrong answers
    private int currentIndex;

    private OnGameStateChangedListener listener;

    public GameController() {
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks); //todo: this is correct
        //this.tasks.add(tasks.get(0));


        for (Task task : tasks) {
            task.leftGames = MAX_GAME_TYPE;
        }
        currentIndex = 0;
    }

    public void startGame() {
        showNextTask();
    }

    public void onUserAnswer(boolean correct) {
        // adjust learnStep
        Task task = tasks.get(currentIndex);
        if (correct) {
            task.leftGames--;
            if (!task.isComplete()) {
                shiftCurrentTaskForward();
            } else {
                currentIndex++;
            }
        } else {
            task.gameType = null;
            task.leftGames++;
        }
        showNextTask();
    }

    public void setOnGameStateChangedListener(OnGameStateChangedListener listener) {
        this.listener = listener;
    }

    private void showNextTask() {
        for (; currentIndex < tasks.size(); currentIndex++) {
            Task task = tasks.get(currentIndex);
            prepareTask(task);
            if (!task.isComplete()) {
                notifyNextTask(task);
                return;
            }
        }

        if (currentIndex == tasks.size()) {
            notifyGameOver();
        }
    }

    private void prepareTask(Task task) {
        if (task.gameType == null) {
            task.gameType = GameType.FLASH_CARD;
        } else {
            //find next proper gameType
            GameType[] gameTypes = GameType.values();
            for (int i = task.gameType.ordinal() + 1; i < MAX_GAME_TYPE; i++) {
                if (isValidForGame(gameTypes[i], task)) {
                    task.gameType = gameTypes[i];
                    return;
                }
                if (i == MAX_GAME_TYPE - 1) i = 0;
                if (i == task.gameType.ordinal()) return;
            }
        }
    }

    private void shiftCurrentTaskForward() {
        if (currentIndex < tasks.size()) {
            Task task = tasks.remove(currentIndex);
            int index = currentIndex + 1 + (int)Math.round(Math.random() * Math.min(tasks.size() - currentIndex - 1, 5));
            if (index >= tasks.size()) {
                tasks.add(task);
            } else {
                tasks.add(index, task);
            }
        }
    }

    private void notifyNextTask(Task task) {
        if (listener != null) {
            listener.onNextTask(task);
        }
    }

    private void notifyGameOver() {
        if (listener != null) {
            listener.onGameOver();
        }
    }

    private boolean isValidForGame(GameType gameType, Task task) {
        switch (gameType) {
            case FLASH_CARD:
                return true;
            case SELECT_KANJI_BY_TRANSLATION:
                return task instanceof WordTask;
            case SELECT_TRANSLATION_BY_READING:
                return true;
            case SELECT_READING_BY_KANJI:
            case SELECT_TRANSLATION_BY_KANJI:
            case SELECT_KANJI_BY_READING:
                if (task instanceof WordTask) {
                    Word word = (Word) task.value;
                    return word.hasKanji();
                }
                return false;
            case WRITE_READING:
                if (task instanceof WordTask) {
                    Word word = (Word) task.value;
                    return word.hasKanji();
                }
                return false;
            case MULTYSELECT_KANJI_READING:
                return false; //todo
            case WRITE_KANJI_IN_KANJI:
                return false; //todo
        }
        return false;
    }

    public interface OnGameStateChangedListener {
        void onNextTask(Task task);
        void onGameOver();
    }
}
