package com.gogaworm.easyjlpt.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.gogaworm.easyjlpt.R;
import com.gogaworm.easyjlpt.data.Word;
import com.gogaworm.easyjlpt.loaders.WordListLoader;
import com.gogaworm.easyjlpt.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created on 24.05.2017.
 *
 * @author ikarpova
 */
public class FlashCardsActivity extends UserSessionLoaderActivity<Word> {
    private int lessonId;

    private TextView questionView;
    private TextView kanjiView;
    private TextView readingView;
    private TextView translationView;
    private Button yesButton;
    private Button noButton;

    private boolean taskNotAnswered;

    private SimpleGameController simpleGameController;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lessonId = getIntent().getIntExtra("lessonId", 0);

        ActionBar actionBar = getSupportActionBar();

        LayoutInflater.from(this).inflate(R.layout.flash_card, (ViewGroup) findViewById(R.id.content), true);

        questionView = (TextView) findViewById(R.id.question);
        kanjiView = (TextView) findViewById(R.id.japanese);
        readingView = (TextView) findViewById(R.id.reading);
        translationView = (TextView) findViewById(R.id.translation);
        yesButton = (Button) findViewById(R.id.yeButton);
        noButton = (Button) findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserAnswer(true);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserAnswer(false);
            }
        });

        simpleGameController = new SimpleGameController();
        getSupportLoaderManager().initLoader(Constants.LOADER_ID_WORD_LIST, null, this).forceLoad();
    }

    private void onUserAnswer(boolean correct) {
        if (taskNotAnswered) {
            showAnswer();
        } else {
            simpleGameController.onAnswered(word, correct);
            showTask(); //todo: move to next
        }
        taskNotAnswered = !taskNotAnswered;
    }

    @Override
    protected Loader<List<Word>> createLoader(String folder) {
        return new WordListLoader(this, folder, lessonId);
    }

    @Override
    public void onLoadFinished(Loader<List<Word>> loader, List<Word> data) {
        simpleGameController.init(data);
        taskNotAnswered = true;
        //show first card
        showTask();
    }

    private void showTask() {
        word = simpleGameController.getNextWord();
        if (word == null) {
            finish();
        } else {
            questionView.setText(R.string.label_do_you_know_word);
            kanjiView.setText(word.japanese);
            readingView.setVisibility(View.GONE);
            translationView.setVisibility(View.GONE);
            yesButton.setText(R.string.button_check);
            noButton.setVisibility(View.GONE);
        }
    }

    private void showAnswer() {
        questionView.setText(R.string.label_is_it_correct);
        kanjiView.setText(word.japanese);
        readingView.setText(word.reading);
        translationView.setText(word.translation);

        readingView.setVisibility(View.VISIBLE);
        translationView.setVisibility(View.VISIBLE);
        yesButton.setText(R.string.button_yes);
        noButton.setVisibility(View.VISIBLE);
    }

    class SimpleGameController {
        List<Word> words;
        int index;

        void init(List<Word> words) {
            this.words = new ArrayList<>(words);
            Collections.shuffle(this.words);
            index = 0;
        }

        Word getNextWord() {
            return index == this.words.size() ? null : words.get(index++);
        }

        void onAnswered(Word word, boolean correct) {
            // put as last item and add last check time to prevent repeating one card twice
            if (!correct) {
                int insertIndex = Math.min(words.size(), index + new Random(System.currentTimeMillis()).nextInt(3) + 1);
                this.words.add(insertIndex, word);
            }
        }
    }

    class GameController {
        List<Word>[] boxes;
        int index;
        int switchTime;

        void init(List<Word> words) {
            boxes =  new List[] {
                    new ArrayList<>(words),
                    new ArrayList<>(words.size()),
                    new ArrayList<>(words.size())
            };
            index = 0;
            switchTime = 3;
        }

        Word getNextWord() {
            if (boxes[index].isEmpty() || switchTime == 0) {
                //move to other
                int newIndex = index;
                do {
                    newIndex = newIndex + 1 < boxes.length ? newIndex + 1 : 0;
                } while (newIndex != index && boxes[newIndex].isEmpty());
                if (newIndex == index) {
                    return null;
                }
                index = newIndex;
                switchTime = 3;
            }
            switchTime--;
            return boxes[index].remove(0);
        }

        void onAnswered(Word word, boolean correct) {
            // put as last item and add last check time to prevent repeating one card twice
        }
    }
}