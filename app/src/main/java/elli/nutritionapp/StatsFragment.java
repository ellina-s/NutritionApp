package elli.nutritionapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Date;

/**
 * Created by Ellina on 02-Oct-15.
 */
public class StatsFragment extends Fragment {

    private ProgressBar mProgressVeg;
    private ProgressBar mProgressGrains;
    private ProgressBar mProgressMilk;
    private ProgressBar mProgressMeat;

    private int mProgressStatusVeg = 0;
    private int mProgressStatusGrains = 0;
    private int mProgressStatusMilk = 0;
    private int mProgressStatusMeat = 0;

    // Mock values
    private int mockProgressStatusVeg = 50;
    private int mockProgressStatusGrains = 60;
    private int mockProgressStatusMilk = 40;
    private int mockProgressStatusMeat = 90;
    private static final int MOCK_TIMESTAMP_ROWID = 1;

    private NutritionDbHelper mDbStatsHelper;
    private static final String TAG = "Stats";
    private static final int MAX_VEG_SERVINGS = 6;
    private static final int MAX_GRAINS_SERVINGS = 6;
    private static final int MAX_MILK_SERVINGS = 2;
    private static final int MAX_MEAT_SERVINGS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        mProgressVeg = (ProgressBar) rootView.findViewById(R.id.vegProgressBar);
        mProgressGrains = (ProgressBar) rootView.findViewById(R.id.grainsProgressBar);
        mProgressMilk = (ProgressBar) rootView.findViewById(R.id.milkProgressBar);
        mProgressMeat = (ProgressBar) rootView.findViewById(R.id.meatProgressBar);

        mProgressVeg.setProgress(mockProgressStatusVeg);
        mProgressGrains.setProgress(mockProgressStatusGrains);
        mProgressMilk.setProgress(mockProgressStatusMilk);
        mProgressMeat.setProgress(mockProgressStatusMeat);

        mockStatsDisplayInLog();
        mockRetrieveProgressValues();
        mockSetProgressBars();
        mockDisplayMostRecentRecordByID();
        mockDisplayMostRecentRecordByTime();
        retrieveProgressValues();
        setProgressBars();

        return rootView;
    }

    /**
     * Overrides onAttach(Context context) to access the Activity
     * which is needed to create and open a database.
     * Reference #1:  http://stackoverflow.com/questions/32083053/android-fragment-onattach-deprecated
     * Reference #2: https://code.google.com/p/android/issues/detail?id=183358#c11
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "called onAttach (Context  context)");

        // Check if context is an instance of Activity.
        // If true, then cast it to Activity.
        Activity activity;
        if(context instanceof Activity){
            activity = (Activity) context;
            mDbStatsHelper = new NutritionDbHelper(activity);
            mDbStatsHelper.open();
        }
    }

    /**
     * A mock method to display all records of food servings in the Log.
     * Reference: http://stackoverflow.com/questions/2810615/how-to-retrieve-data-from-cursor-class
     */
    public void mockStatsDisplayInLog() {
        Log.d(TAG, "called mockStatsDisplayInLog()");

        Cursor cursor = mDbStatsHelper.fetchAllServingsRecords();
        cursor.moveToFirst();
        if(cursor != null){
            while(!cursor.isAfterLast()){
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    Log.i(TAG, "" + cursor.getString(i));
                }
                cursor.moveToNext();
            }
        }
    }

    /**
     * A mock method to retrieve progress bar values from the database
     * for the first record in the database.
     */
    public void mockRetrieveProgressValues(){
        Log.d(TAG, "called mockRetrieveProgressValues()");

        int recordsCount = 1;
        int id, veg, grains, milk, meat;

        Cursor cursor = mDbStatsHelper.fetchAllServingsRecords();
        cursor.moveToFirst();
        if(cursor != null){
            while(!cursor.isAfterLast()){
                if(recordsCount == 1){
                    id = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_ROWID));
                    Log.d(TAG, "Record's id: " + id);
                    veg = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_VEG));
                    grains = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_GRAIN));
                    milk = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_MILK));
                    meat = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_MEAT));
                    mockSetProgressValues(veg, grains, milk, meat);
                }
                cursor.moveToNext();
                recordsCount++;
            }
        }
    }

    /**
     * Set mock progress bars values by computing the percentage of
     * recommended daily number of serving for each food group
     * (defined by the MAX_..._SERVINGS constants)
     *
     * Reference: http://stackoverflow.com/questions/343584/how-do-i-get-whole-and-fractional-parts-from-double-in-jsp-java
     *
     * @param veg number of consumed servings of vegetables and fruit
     * @param grains number of consumed servings of grains
     * @param milk number of consumed servings of milk
     * @param meat number of consumed servings of meat
     */
    private void mockSetProgressValues(int veg, int grains, int milk, int meat){
        Log.d(TAG, "Called mockSetProgressValues()");

        double vegPercent = (double) veg / MAX_VEG_SERVINGS * 100;
        double grainsPercent = (double) grains / MAX_GRAINS_SERVINGS * 100;
        double milkPercent = (double) milk / MAX_MILK_SERVINGS * 100;
        double meatPercent = (double) meat / MAX_MEAT_SERVINGS * 100;

        Log.d(TAG, "Double percentages: veg " + vegPercent + " grains " + grainsPercent + " milk " + milkPercent + " meat "+ meatPercent);

        this.mockProgressStatusVeg = (int) vegPercent; // take the integer part of the percentage
        this.mockProgressStatusGrains = (int) grainsPercent;
        this.mockProgressStatusMilk = (int) milkPercent;
        this.mockProgressStatusMeat = (int) meatPercent;
    }

    /**
     * Set mock values of all progress bars.
     */
    public void mockSetProgressBars(){
        Log.d(TAG, "Called mockSetProgressBars()");

        mProgressVeg.setProgress(mockProgressStatusVeg);
        mProgressGrains.setProgress(mockProgressStatusGrains);
        mProgressMilk.setProgress(mockProgressStatusMilk);
        mProgressMeat.setProgress(mockProgressStatusMeat);
    }

    /**
     * Display the most recent record by ID in the Log.
     */
    public void mockDisplayMostRecentRecordByID(){
        Log.d(TAG, "called mockDisplayMaxIdRecord()");

        Cursor cursor = mDbStatsHelper.fetchRecordWithMaxRowId();
        int id = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_ROWID));
        Log.d(TAG, "Max ID is " + id);

        long millis = cursor.getLong(cursor.getColumnIndexOrThrow(NutritionDbHelper.KEY_DATE));
        Log.d(TAG, "Max ID Record's date as TIMESTAMP in LONG is " + millis);
        Date dateCreated = new Date(millis);
        Log.d(TAG, "Max ID Record's date as TIMESTAMP is " + dateCreated.toString());
    }

    /**
     * A mock method to display the most recent record by timestamp.
     * Reference for use of strftime() SQLite's function: https://androidcookbook.com/Recipe.seam?recipeId=413
     */
    public void mockDisplayMostRecentRecordByTime() {
        Log.d(TAG, "called mockDisplayMostRecentRecordByTime()");

        Cursor cursor = mDbStatsHelper.mockFetchMostRecentRecordByTimeAtId(MOCK_TIMESTAMP_ROWID);
        int id = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_ROWID));
        Log.d(TAG, "Record's id: " + id);

        int veg = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_VEG));
        int grains = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_GRAIN));
        int milk = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_MILK));
        int meat = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_MEAT));

        String record = "Veggies: " + veg + " Grains: " + grains + " Milk: " + milk + " Meat: " + meat;
        Log.d(TAG, record);

        long millis = cursor.getLong(cursor.getColumnIndexOrThrow(NutritionDbHelper.KEY_DATE));
        Log.d(TAG, "Record's date as time since epoch: " + millis);
        Date dateCreated = new Date(millis);
        Log.d(TAG, "Record's date: " + dateCreated.toString());
    }

    /**
     * Retrieves progress values from the most recent entry in the database
     * and passes those values to setProgressValues().
     */
    public void retrieveProgressValues(){
        Log.i(TAG, "Called retrieveProgressValues()");

        int id;
        double veg, grains, milk, meat;

        Cursor cursor = mDbStatsHelper.fetchRecordWithMaxRowId();
        id = cursor.getInt(cursor.getColumnIndex(NutritionDbHelper.KEY_ROWID));
        veg = cursor.getDouble(cursor.getColumnIndex(NutritionDbHelper.KEY_VEG));
        grains = cursor.getDouble(cursor.getColumnIndex(NutritionDbHelper.KEY_GRAIN));
        milk = cursor.getDouble(cursor.getColumnIndex(NutritionDbHelper.KEY_MILK));
        meat = cursor.getDouble(cursor.getColumnIndex(NutritionDbHelper.KEY_MEAT));
        setProgressValues(veg, grains, milk, meat);

        Log.d(TAG, "Retrieved progress values from the record with ID: " + id);
    }


    /**
     * Sets progress bars' values by computing corresponding percentages
     * of consumed food servings with respect to the recommended daily amount
     * of food serving for each food group (as defined by the MAX_..._SERVINGS constants).
     * @param veg number of vegetables servings
     * @param grains number of grains servings
     * @param milk number of milk servings
     * @param meat number of meat servings
     */
    private void setProgressValues(double veg, double grains, double milk, double meat){
        Log.d(TAG, "Called setProgressValues()");

        double vegPercent = veg / MAX_VEG_SERVINGS * 100;
        double grainsPercent = grains / MAX_GRAINS_SERVINGS * 100;
        double milkPercent = milk / MAX_MILK_SERVINGS * 100;
        double meatPercent = meat / MAX_MEAT_SERVINGS * 100;

        Log.d(TAG, "Percentages: veg " + vegPercent + " grains " + grainsPercent + " milk " + milkPercent + " meat "+ meatPercent);

        this.mProgressStatusVeg = (int) vegPercent; // take the integer part of the percentage
        this.mProgressStatusGrains = (int) grainsPercent;
        this.mProgressStatusMilk = (int) milkPercent;
        this.mProgressStatusMeat = (int) meatPercent;
    }

    /**
     * Set values of all progress bars.
     */
    public void setProgressBars(){
        Log.d(TAG, "Called setProgressBars()");

        mProgressVeg.setProgress(mProgressStatusVeg);
        mProgressGrains.setProgress(mProgressStatusGrains);
        mProgressMilk.setProgress(mProgressStatusMilk);
        mProgressMeat.setProgress(mProgressStatusMeat);
    }
}
