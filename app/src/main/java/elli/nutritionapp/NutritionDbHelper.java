package elli.nutritionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class. Creates a database and respective table(s)
 * for the Nutrition app. The Servings table contains information
 * on the amount of servings of vegetables and fruit, grains, milk,
 * and meat products and alternatives consumed per day.
 *
 * Note: Portions of this file are based on the Notepad sample app provided in the Notepad Tutorial.
 * Copyright (C) 2008 Google Inc.
 * Licensed under the Apache 2.0 License available at http://www.apache.org/licenses/LICENSE-2.0
 * The Notepad tutorial is available at http://developer.android.com/training/notepad/index.html
 */
public class NutritionDbHelper {

    private static final String DATABASE_NAME = "nutritiondata";
    private static final String DATABASE_TABLE = "servings";
    private static final int DATABASE_VERSION = 1;

    // Fields of the Servings table
    public static final String KEY_ROWID = "_id";
    public static final String KEY_VEG = "veg";
    public static final String KEY_GRAIN = "grain";
    public static final String KEY_MILK = "milk";
    public static final String KEY_MEAT = "meat";
    public static final String KEY_DATE = "date";

    private static final String DATABASE_CREATE =
            "create table servings (_id integer primary key autoincrement, "
                    + KEY_VEG + " integer, "
                    + KEY_GRAIN + " integer, "
                    + KEY_MILK + " integer, "
                    + KEY_MEAT + " integer, "
                    + KEY_DATE + " integer);";

    private final Context mContext;
    private AppDbHelper mAppDbHelper;
    private SQLiteDatabase mDatabase;

    /**
     * An implementation of the SQLiteOpenHelper class
     * for creating and upgrading a database.
     */
    private static class AppDbHelper extends SQLiteOpenHelper {

        AppDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    /**
     * Constructor
     * @param context
     */
    public NutritionDbHelper(Context context){
        this.mContext = context;
    }

    /**
     * Creates and opens a database.
     */
    public NutritionDbHelper open() throws SQLException {
        mAppDbHelper = new AppDbHelper(mContext);
        mDatabase = mAppDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database.
     */
    public void close() {
        mAppDbHelper.close();
    }

    /**
     * Creates a new record of daily servings of each of the food groups.
     * @param veg number of servings of vegetables and fruit
     * @param grain number of servings of grains
     * @param milk number of servings of milk and alternatives
     * @param meat number of servings of meat and alternatives
     * @param date date on which food servings are consumed
     * @return row Id or -1 if failed
     */
    public long createRecord(int veg, int grain, int milk, int meat, int date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_VEG, veg);
        initialValues.put(KEY_GRAIN, grain);
        initialValues.put(KEY_MILK, milk);
        initialValues.put(KEY_MEAT, meat );
        initialValues.put(KEY_DATE, date );

        return mDatabase.insert(DATABASE_TABLE, null, initialValues);
    }

}
