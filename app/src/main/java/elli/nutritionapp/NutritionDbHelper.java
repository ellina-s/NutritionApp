package elli.nutritionapp;

import android.content.Context;

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

    private final Context mContext;

    /**
     * Constructor
     * @param context
     */
    public NutritionDbHelper(Context context){
        this.mContext = context;
    }

}
