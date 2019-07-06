package com.androiddesdecero.arquitecturamvvm.ui.search;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.androiddesdecero.arquitecturamvvm.R;
import com.androiddesdecero.arquitecturamvvm.binding.FragmentDataBindingComponent;
import com.androiddesdecero.arquitecturamvvm.databinding.FragmentSearchBinding;
import com.androiddesdecero.arquitecturamvvm.di.Injectable;
import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.ui.common.NavigationController;
import com.androiddesdecero.arquitecturamvvm.ui.common.RepoListAdapter;
import com.androiddesdecero.arquitecturamvvm.ui.common.RetryCall;
import com.androiddesdecero.arquitecturamvvm.utils.AutoClearedValue;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelProvider;

    @Inject
    NavigationController navigationController;

    androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentSearchBinding> binding;
    AutoClearedValue<RepoListAdapter> adapter;

    private SearchViewModel searchViewModel;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchViewModel = ViewModelProviders.of(this, viewModelProvider).get(SearchViewModel.class);
        initRecyclerView();

        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, true, new RepoListAdapter.RepoClickCallback() {
            @Override
            public void onClick(Repo repo) {
                navigationController.navigateToRepo(repo.owner.login, repo.name);
            }
        });

        binding.get().repoList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        initSearchInputListener();

        binding.get().setCallback(new RetryCall() {
            @Override
            public void retry() {
                searchViewModel.refresh();
            }
        });
    }

    private void initSearchInputListener(){
        binding.get().input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    SearchFragment.this.doSearch(v);
                    return true;
                }
                return false;
            }
        });

        binding.get().input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });
    }
}
