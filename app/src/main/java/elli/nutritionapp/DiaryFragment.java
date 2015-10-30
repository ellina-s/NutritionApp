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
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

/**
 * Created by Ellina on 02-Oct-15.
 */
public class DiaryFragment extends Fragment {

    private static final String TAG = "Diary";
    private NutritionDbHelper mDbHelper;
    private EditText mVegEditText;
    private EditText mGrainEditText;
    private EditText mMilkEditText;
    private EditText mMeatEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        mVegEditText = (EditText) rootView.findViewById(R.id.vegEditText);
        mGrainEditText = (EditText) rootView.findViewById(R.id.grainsEditText);
        mMilkEditText = (EditText) rootView.findViewById(R.id.milkEditText);
        mMeatEditText = (EditText) rootView.findViewById(R.id.meatEditText);

        Button button = (Button) rootView.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "User clicked the Add button");
                addNewRecord();
            }
        });

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
            mDbHelper = new NutritionDbHelper(activity);
            mDbHelper.open();
        }
    }
    /**
     * A mock method to add a mock record to the database.
     */
    private void mockAddNewRecord(){

        Log.d(TAG, "called mockAddNewRecord()");
        mDbHelper.createRecord(5, 6, 1, 2);
    }

    /**
     * A mock method to display the records of food servings in the Log.
     * Displays records that were created by pressing the Add button.
     *
     * Reference: http://stackoverflow.com/questions/2810615/how-to-retrieve-data-from-cursor-class
     */
    public void mockDisplayRecord(){
        Log.d(TAG, "called mockDisplayRecord()");

        Cursor cursor = mDbHelper.fetchAllServingsRecords();
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
     * Records user input in the database.
     */
    public void addNewRecord(){
        Log.d(TAG, "called addNewRecord()");

        double veg = Double.parseDouble(mVegEditText.getText().toString());
        double grain = Double.parseDouble(mGrainEditText.getText().toString());
        double milk = Double.parseDouble(mMilkEditText.getText().toString());
        double meat = Double.parseDouble(mMeatEditText.getText().toString());

        Log.d(TAG, "Veggies: " + veg + " grains: " + grain + " milk: " + milk + " meat: " + meat);
        mDbHelper.createRecord(veg, grain, milk, meat);
    }

}
