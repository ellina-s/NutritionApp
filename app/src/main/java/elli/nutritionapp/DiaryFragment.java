package elli.nutritionapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ellina on 02-Oct-15.
 */
public class DiaryFragment extends Fragment {

    private static final String TAG = "Diary";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        Button button = (Button) rootView.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "User clicked the Add button");
            }
        });

        return rootView;
    }
}
