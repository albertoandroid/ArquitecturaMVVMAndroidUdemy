package com.androiddesdecero.arquitecturamvvm.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AutoClearedValue<T> {
    private T value;
    public AutoClearedValue(Fragment fragment, T value){
        FragmentManager fragmentManager = fragment.getFragmentManager();

        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                        if(f == fragment){
                            AutoClearedValue.this.value = null;
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                }, false);
        this.value = value;
    }

    public T get(){
        return value;
    }
}
