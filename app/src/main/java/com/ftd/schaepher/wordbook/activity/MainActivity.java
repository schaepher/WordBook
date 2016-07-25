package com.ftd.schaepher.wordbook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ftd.schaepher.wordbook.R;
import com.ftd.schaepher.wordbook.pojo.Word;
import com.ftd.schaepher.wordbook.tools.DataBaseHelper;
import com.ftd.schaepher.wordbook.tools.XmlManager;
import com.j256.ormlite.dao.Dao;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends BaseActivity {
    Button btnSearch;
    EditText edtWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = (Button) findViewById(R.id.btn_search);
        edtWord = (EditText) findViewById(R.id.edt_word);
        assert btnSearch != null;
        btnSearch.setOnClickListener(new ParseOnclickListener());
        hideOptionMenu();
        initDb();
    }

    private void initDb() {
        DataBaseHelper helper = DataBaseHelper.getInstance(this);
        Dao<Word, Integer> wordDao = helper.getWordDao();

        try {
            if (wordDao.queryForId(1) == null) {
                final ProgressDialog proDialog = new ProgressDialog(MainActivity.this);
                proDialog.setMessage(getString(R.string.initData));
                proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        xmlToDb();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                proDialog.dismiss();
                            }
                        });
                    }
                }).run();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void xmlToDb() {
        try {
            XmlManager xml = new XmlManager();
            xml.parse(MainActivity.this);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    class ParseOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String word = edtWord.getText().toString().toLowerCase().trim();
            if (!word.equals("")) {
                Intent intent = new Intent(MainActivity.this, WordDisplayActivity.class);
                intent.putExtra("wordToQuery", word);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, R.string.input_can_no_empty, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
