package com.gogaworm.easyjlpt.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.gogaworm.easyjlpt.R;
import com.gogaworm.easyjlpt.data.Exam;
import com.gogaworm.easyjlpt.utils.UnitedKanjiKanaSpannableString;

/**
 * Created on 18.08.2017.
 *
 * @author ikarpova
 */
public class WordSelectExamFragment extends Fragment {
    private Button[] answerButtons;
    private TextView sentenceJapaneseView;
    private TextView sentenceTranslationView;
    private Exam exam;
    private boolean answered;

    private ExamListener examListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_exam_word_select, container, false);
        sentenceJapaneseView = (TextView) parentView.findViewById(R.id.sentenceJapanese);
        sentenceTranslationView = (TextView) parentView.findViewById(R.id.sentenceTranslation);

        answerButtons = new Button[]{
                (Button) parentView.findViewById(R.id.answer1),
                (Button) parentView.findViewById(R.id.answer2),
                (Button) parentView.findViewById(R.id.answer3),
                (Button) parentView.findViewById(R.id.answer4)
        };
        makeButtonsVisible(0);
        for (Button answerButton : answerButtons) {
            answerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAnswerSelected(v.getId());
                }
            });
        }

        parentView.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (examListener != null) {
                    examListener.nextSentence();
                }
            }
        });

        return parentView;
    }

    void initExam(Exam exam) {
        this.exam = exam;
        answered = false;
        sentenceJapaneseView.setText(new UnitedKanjiKanaSpannableString(exam.japanese, false));
        sentenceTranslationView.setVisibility(View.INVISIBLE);
        makeButtonsVisible(exam.answers.length);
        for (int i = 0; i < exam.answers.length; i++) {
            resetButton(answerButtons[i]);
            answerButtons[i].setText(new UnitedKanjiKanaSpannableString(exam.answers[i].japanese));
        }
    }

    private void makeButtonsVisible(int count) {
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setVisibility(i < count ? View.VISIBLE : View.GONE);
        }
    }

    private void onAnswerSelected(int id) {
        //todo
        int answerIndex = 0;
        switch (id) {
            case R.id.answer1:
                answerIndex = 1;
                break;
            case R.id.answer2:
                answerIndex = 2;
                break;
            case R.id.answer3:
                answerIndex = 3;
                break;
            case R.id.answer4:
                answerIndex = 4;
                break;
        }
        if (answered) {
            //todo: show translation
        } else {
            answered = true;
            boolean correctAnswer = exam.correct == answerIndex;
            sentenceTranslationView.setVisibility(View.VISIBLE);
            sentenceJapaneseView.setText(new UnitedKanjiKanaSpannableString(exam.japanese.replace("_", exam.answers[exam.correct - 1].japanese)));
            sentenceTranslationView.setText(exam.translation);
            makeButtonsVisible(1);
            setCorrect(answerButtons[0], correctAnswer);
            answerButtons[0].setText(new UnitedKanjiKanaSpannableString(exam.answers[answerIndex - 1].japanese));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExamListener) {
            examListener = (ExamListener) context;
        }
    }

    @Override
    public void onDetach() {
        examListener = null;
        super.onDetach();
    }

    private void resetButton(Button button) {
        //todo: reset button
    }

    private void setCorrect(Button button, boolean isCorrect) {
        //todo: set correct
    }

    public interface ExamListener {
        void nextSentence();
    }
}
