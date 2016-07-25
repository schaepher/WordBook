package com.ftd.schaepher.wordbook.tools;

import android.content.Context;

import com.ftd.schaepher.wordbook.R;
import com.ftd.schaepher.wordbook.pojo.Word;
import com.j256.ormlite.dao.Dao;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Schaepher on 2016/6/30.
 */
public class XmlManager {

    public void parse(Context context) throws XmlPullParserException, IOException {
        Word word = null;
        String wordName = null;
        String wordPron = null;

        DataBaseHelper helper = DataBaseHelper.getInstance(context);
        Dao<Word, Integer> wordDao = helper.getWordDao();

        XmlPullParser parser = context.getResources().getXml(R.xml.words);
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    eventType = parser.next();
                    break;
                case XmlPullParser.START_TAG:
                    switch (parser.getName()) {
                        case "word":
                            parser.next();
                            wordName = parser.getText();
                            break;
                        case "pron":
                            parser.next();
                            wordPron = "/" + parser.getText() + "/";
                            break;
                        case "def":
                            word = new Word();
                            word.setWord(wordName);
                            word.setPron(wordPron);
                            break;
                        case "en":
                            parser.next();
                            assert word != null;
                            word.setEnglish(parser.getText());
                            break;
                        case "exmp":
                            parser.next();
                            String example = parser.getText();
                            if (example == null) {
                                example = "%例子为空%";
                            }
                            assert word != null;
                            word.setExample(getFirstExample(example));
                            break;
                        case "cn":
                            parser.next();
                            String cn = parser.getText();
                            if (cn == null) {
                                cn = "%对应中文词为空%";
                            }
                            assert word != null;
                            word.setCnWord(cn);
                            break;
                        case "trans":
                            parser.next();
                            String trans = parser.getText();
                            if (trans == null) {
                                trans = "%翻译为空%";
                            }
                            assert word != null;
                            word.setChinese(trans);
                            break;
                        default:
                            break;
                    }

                    eventType = parser.next();
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("def")) {
                        try {
                            wordDao.create(word);
                            Loger.d("Tag", word.getWord());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        word = null;
                    }
                    eventType = parser.next();
                    break;
                default:
                    eventType = parser.next();
                    break;
            }

        }
    }

    public String getFirstExample(String str) {
        if (str != null && str.contains("\n")) {

            String[] strings = str.split("\\.");
            return "Example:\n" + strings[0] + "\n";

        }
        return str;
    }

}
