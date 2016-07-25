package com.ftd.schaepher.wordbook.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.wordbook.R;
import com.ftd.schaepher.wordbook.adapter.DefinitionAdapter;
import com.ftd.schaepher.wordbook.pojo.Word;
import com.ftd.schaepher.wordbook.tools.DataBaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class WordDisplayActivity extends BaseActivity {
    private List<Word> words;
    private DefinitionAdapter definitionAdapter;
    private Dao<Word, Integer> wordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_display);
        init();
    }

    private void init() {
        TextView tvWord = (TextView) findViewById(R.id.tv_word);
        TextView tvPron = (TextView) findViewById(R.id.tv_pron);
        ListView lvDefs = (ListView) findViewById(R.id.lv_def);
        assert tvWord != null;
        assert tvPron != null;
        assert lvDefs != null;
        lvDefs.setItemsCanFocus(true);

        Intent intent = getIntent();
        String wordToQuery = intent.getStringExtra("wordToQuery");

        if (wordToQuery != null) {
            DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(WordDisplayActivity.this);
            wordDao = dataBaseHelper.getWordDao();
            QueryBuilder<Word, Integer> queryBuilder = wordDao.queryBuilder();
            try {
                queryBuilder.where().eq("word", wordToQuery);
                words = queryBuilder.query();
                if (words.size() != 0) {
                    tvWord.setText(words.get(0).getWord());
                    tvPron.setText(words.get(0).getPron());

                    definitionAdapter = new DefinitionAdapter(this,
                            R.layout.list_item_definition, words);
                    lvDefs.setAdapter(definitionAdapter);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            tvWord.setText(R.string.word_not_found);
        }
    }


    /**
     * clear focus and hide soft keyboard when touch the outside of focused view.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                definitionAdapter.setEditTextEditable(true);
                hideOption(R.id.menu_edit);
                showOption(R.id.menu_save);
                break;
            case R.id.menu_save:
                definitionAdapter.setEditTextEditable(false);
                hideOption(R.id.menu_save);
                showOption(R.id.menu_edit);
                updateWords();
            case R.id.menu_comment:
                break;
            case R.id.menu_change_log:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Word word : words) {
                        wordDao.update(word);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }


}
