package elli.nutritionapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        return rootView;
    }
}
