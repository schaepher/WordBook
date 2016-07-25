package com.ftd.schaepher.wordbook.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ftd.schaepher.wordbook.pojo.Word;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schaepher on 2016/6/30.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "wordbook.db";

    private static final int DATABASE_VERSION = 1;

    private Map<String, Dao> daoMap = new HashMap<>();

    private Dao<Word, Integer> wordDao;

    private static DataBaseHelper instance;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DataBaseHelper.class) {
                if (instance == null) {
                    instance = new DataBaseHelper(context);
                }
            }
        }
        return instance;
    }

    public synchronized Dao getDao(Class cls) throws SQLException {
        Dao dao;
        String clsName = cls.getSimpleName();
        if (daoMap.containsKey(clsName)) {
            dao = daoMap.get(clsName);
        } else {
            dao = super.getDao(cls);
            daoMap.put(clsName, dao);
        }
        return dao;
    }

    public Dao<Word, Integer> getWordDao() {
        if (wordDao == null) {
            try {
                wordDao = getDao(Word.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wordDao;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Word.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Word.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
