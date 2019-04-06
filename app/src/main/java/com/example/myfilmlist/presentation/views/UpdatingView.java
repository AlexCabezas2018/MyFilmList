package com.example.myfilmlist.presentation.views;

import com.example.myfilmlist.presentation.context.Context;

/**
 * With this interface, the classes that implements it must include this method,
 * which is useful for the presenter
 */
public interface UpdatingView {
    void update(Context resultData);
}
