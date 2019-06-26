package com.androiddesdecero.arquitecturamvvm.ui.repo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddesdecero.arquitecturamvvm.R;
import com.androiddesdecero.arquitecturamvvm.di.Injectable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepoFragment extends Fragment implements Injectable {


    public RepoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo, container, false);
    }

}
