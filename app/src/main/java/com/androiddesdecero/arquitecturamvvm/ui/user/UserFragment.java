package com.androiddesdecero.arquitecturamvvm.ui.user;


import android.os.Bundle;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddesdecero.arquitecturamvvm.R;
import com.androiddesdecero.arquitecturamvvm.binding.FragmentDataBindingComponent;
import com.androiddesdecero.arquitecturamvvm.databinding.FragmentUserBinding;
import com.androiddesdecero.arquitecturamvvm.di.Injectable;
import com.androiddesdecero.arquitecturamvvm.ui.common.NavigationController;
import com.androiddesdecero.arquitecturamvvm.ui.common.RepoListAdapter;
import com.androiddesdecero.arquitecturamvvm.ui.common.RetryCall;
import com.androiddesdecero.arquitecturamvvm.utils.AutoClearedValue;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements Injectable {

    private static final String LOGIN_KEY = "login";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;

    AutoClearedValue<FragmentUserBinding> binding;
    private AutoClearedValue<RepoListAdapter> adapter;

    public static UserFragment create(String login){
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_KEY, login);
        userFragment.setArguments(bundle);
        return userFragment;
    }


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentUserBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false, dataBindingComponent);
        dataBinding.setRetryCallback(new RetryCall() {
            @Override
            public void retry() {
                userViewModel.retry();
            }
        });
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

}
