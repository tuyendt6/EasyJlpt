package com.gogaworm.easyjlpt.game;

import com.gogaworm.easyjlpt.data.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 17.04.2017.
 *
 * @author ikarpova
 */
public class TaskCreator {
    private List<WordTask> tasks = new ArrayList<>();

    public void addWords(List<Word> words) {
        for (Word word : words) {
            tasks.add(new WordTask(word));
        }
    }

    public List<WordTask> generateLearnSession() {
        Collections.shuffle(tasks);
        return tasks;
    }
}
