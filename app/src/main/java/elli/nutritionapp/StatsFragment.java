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

/**
 * Created by Ellina on 02-Oct-15.
 */
public class StatsFragment extends Fragment {

    private ProgressBar mProgressVeg;
    private ProgressBar mProgressGrains;
    private ProgressBar mProgressMilk;
    private ProgressBar mProgressMeat;

    // Mock values
    private int mockProgressStatusVeg = 50;
    private int mockProgressStatusGrains = 60;
    private int mockProgressStatusMilk = 40;
    private int mockProgressStatusMeat = 90;

    private NutritionDbHelper mDbStatsHelper;
    private static final String TAG = "Stats";

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
}
