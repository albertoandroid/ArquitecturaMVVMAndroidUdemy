package com.androiddesdecero.arquitecturamvvm.ui.common;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.androiddesdecero.arquitecturamvvm.MainActivity;
import com.androiddesdecero.arquitecturamvvm.R;

import javax.inject.Inject;

public class NavigationController {

    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity){
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }
}
