package elli.nutritionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
                    + KEY_VEG + " real, "
                    + KEY_GRAIN + " real, "
                    + KEY_MILK + " real, "
                    + KEY_MEAT + " real, "
                    + KEY_DATE + " DATE DEFAULT CURRENT_DATE);";

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
            db.execSQL("DROP TABLE IF EXISTS servings");
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
     * @return row Id or -1 if failed
     */
    public long createRecord(double veg, double grain, double milk, double meat) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_VEG, veg);
        initialValues.put(KEY_GRAIN, grain);
        initialValues.put(KEY_MILK, milk);
        initialValues.put(KEY_MEAT, meat);

        return mDatabase.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Fetches a record containing the information on food servings
     * given a particular row ID.
     * @param rowId ID of a record to be fetched
     * @return a Cursor that points to the fetched item
     * @throws SQLException
     */
    public Cursor fetchRecord(long rowId) throws SQLException {

        Cursor mCursor =
                mDatabase.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_VEG, KEY_GRAIN, KEY_MILK, KEY_MEAT, KEY_DATE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update a particular record of food servings given a row ID
     * @param rowId a row ID of the record to be updated
     * @param veg a new number of servings of vegetables and fruit
     * @param grain a new number of servings of grains
     * @param milk a new number of servings of milk and alternatives
     * @param meat a new number of servings of meat and alternatives
     * @return True if the note was successfully updated. Otherwise, False
     */
    public boolean updateRecord(long rowId, int veg, int grain, int milk, int meat) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_VEG, veg);
        newValues.put(KEY_GRAIN, grain);
        newValues.put(KEY_MILK, milk);
        newValues.put(KEY_MEAT, meat );

        return mDatabase.update(DATABASE_TABLE, newValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Fetch all records from the Servings table.
     * @return A cursor over all records in the Servings table.
     */
    public Cursor fetchAllServingsRecords(){
        return mDatabase.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_VEG, KEY_GRAIN, KEY_MILK, KEY_MEAT, KEY_DATE}, null, null, null, null, null);
    }

    /**
     * A mock method to fetch the most recent mock record by time in the Servings table.
     * Assumes that the most recent record is given by id.
     * Retrieves timestamp in seconds since epoch (see the reference).
     * Reference: https://androidcookbook.com/Recipe.seam?recipeId=413
     * @param id row ID of a mock record to be retrieved
     * @return A cursor to the retrieved record
     */
    public Cursor mockFetchMostRecentRecordByTimeAtId(int id){
        Cursor cursor = mDatabase.rawQuery(
                "SELECT " + KEY_ROWID +
                        ", (strftime('%s', date) * 1000) AS " + KEY_DATE + ", " +
                        KEY_VEG + ", " + KEY_GRAIN + ", " + KEY_MILK + ", " +KEY_MEAT +
                        " FROM " + DATABASE_TABLE + " where _id = " + id, new String[0]);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Fetches a record with the maximum row id, i.e. the most recently created record.
     * Reference: http://stackoverflow.com/questions/390534/aggregate-functions-in-where-clause-in-sqlite
     *
     * @return A cursor on the fetched record.
     */
    public Cursor fetchRecordWithMaxRowId(){

        Cursor cursor = mDatabase.rawQuery(
                "Select " + KEY_ROWID + ", " + KEY_VEG + ", " + KEY_GRAIN + ", " + KEY_MILK +
                        ", " + KEY_MEAT + ", (strftime('%s', date) * 1000) AS " + KEY_DATE + " FROM " + DATABASE_TABLE +
                        " where _id = (select max(_id) from " + DATABASE_TABLE + ")", new String[0]);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
