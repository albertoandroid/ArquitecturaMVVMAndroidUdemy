package com.androiddesdecero.arquitecturamvvm.ui.repo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.androiddesdecero.arquitecturamvvm.R;
import com.androiddesdecero.arquitecturamvvm.databinding.ContributorItemBinding;
import com.androiddesdecero.arquitecturamvvm.model.Contributor;
import com.androiddesdecero.arquitecturamvvm.ui.common.DataBoundListAdapter;

import java.util.Objects;

public class ContributorAdapter extends DataBoundListAdapter<Contributor, ContributorItemBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ContributorClickCallback callback;

    public ContributorAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              ContributorClickCallback callback){
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    protected ContributorItemBinding createBinding(ViewGroup parent) {
        ContributorItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.contributor_item, parent, false, dataBindingComponent);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contributor contributor = binding.getContributor();
                if(contributor != null && callback != null){
                    callback.onClick(contributor);
                }
            }
        });
        return binding;
    }

    @Override
    protected void bind(ContributorItemBinding binding, Contributor item) {
        binding.setContributor(item);
    }

    @Override
    protected boolean areItemsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getLogin(), newItem.getLogin());
    }

    @Override
    protected boolean areContentsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getAvatarUrl(), newItem.getAvatarUrl()) &&
                oldItem.getContributions() == newItem.getContributions();
    }


    public interface ContributorClickCallback{
        void onClick(Contributor contributor);
    }
}
