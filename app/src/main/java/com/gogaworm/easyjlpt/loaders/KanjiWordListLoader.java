package com.gogaworm.easyjlpt.loaders;

import android.content.Context;
import com.gogaworm.easyjlpt.data.JlptLevel;
import com.gogaworm.easyjlpt.data.JlptSection;
import com.gogaworm.easyjlpt.data.Kanji;
import com.gogaworm.easyjlpt.data.Word;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created on 07.06.2017.
 *
 * @author ikarpova
 */
public class KanjiWordListLoader extends WordListLoader {
    public KanjiWordListLoader(Context context, JlptSection section, JlptLevel level, int lessonId) {
        super(context, section, level, lessonId);
    }

    @Override
    protected void parseJson(List<Word> results, String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray jsonKanji = root.optJSONArray("kanji");
        if (jsonKanji != null) {
            for (int i = 0; i < jsonKanji.length(); i++) {
                JSONObject jsonKanjiItem = jsonKanji.getJSONObject(i);

                // add kanji as word, it's ok for flash cards
                results.add(new Kanji(
                        jsonKanjiItem.getString("japanese"),
                        jsonKanjiItem.optString("on"),
                        jsonKanjiItem.optString("kun"),
                        jsonKanjiItem.getString("translation")));

                JSONArray jsonWords = jsonKanjiItem.optJSONArray("words");
                if (jsonWords != null) {
                    parseWords(jsonWords, results);
                }
            }
        }
    }
}
