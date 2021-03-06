package com.gogaworm.easyjlpt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.gogaworm.easyjlpt.R;
import com.gogaworm.easyjlpt.data.Lesson;
import com.gogaworm.easyjlpt.data.UserSession;
import com.gogaworm.easyjlpt.utils.UnitedKanjiKanaSpannableString;

/**
 * Created on 05.07.2017.
 *
 * @author ikarpova
 */
public class ViewLessonContentActivity extends UserSessionActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Lesson lesson = getIntent().getParcelableExtra("lesson");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = WordListFragment.getInstance(userSession, lesson);
        fragmentManager.beginTransaction().add(R.id.content, fragment).commit();

        getSupportActionBar().setTitle(UnitedKanjiKanaSpannableString.getKanjiFromReading(lesson.title.japanese));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
