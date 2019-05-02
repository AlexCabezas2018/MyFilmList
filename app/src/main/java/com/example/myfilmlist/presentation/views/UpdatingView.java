package com.example.myfilmlist.presentation.views;

import android.support.v7.app.AppCompatActivity;

import com.example.myfilmlist.presentation.context.Context;

/**
 * With this interface, the classes that implements it must include this method,
 * which is useful for the presenter
 */
public abstract class UpdatingView extends AppCompatActivity {
    public abstract void update(Context resultData);
}
