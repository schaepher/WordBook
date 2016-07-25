package com.ftd.schaepher.wordbook.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Schaepher on 2016/6/30.
 */
@DatabaseTable(tableName = "tb_def")
public class Word {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "word")
    private String word;
    @DatabaseField(columnName = "pron")
    private String pron;
    @DatabaseField(columnName = "english")
    private String english;
    @DatabaseField(columnName = "chinese")
    private String chinese;
    @DatabaseField(columnName = "cn_word")
    private String cnWord;
    @DatabaseField(columnName = "example")
    private String example;

    public Word() {

    }

    public Word(String word, String pron, String english, String chinese, String cnWord, String example) {
        this.word = word;
        this.pron = pron;
        this.english = english;
        this.chinese = chinese;
        this.cnWord = cnWord;
        this.example = example;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getCnWord() {
        return cnWord;
    }

    public void setCnWord(String cnWord) {
        this.cnWord = cnWord;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", pron='" + pron + '\'' +
                ", english='" + english + '\'' +
                ", chinese='" + chinese + '\'' +
                ", cnWord='" + cnWord + '\'' +
                ", example='" + example + '\'' +
                '}';
    }
}
