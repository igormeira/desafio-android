package com.igormeira.githubpop.pullrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.igormeira.githubpop.R;
import com.igormeira.githubpop.databinding.ActivityPullRequestsBinding;
import com.igormeira.githubpop.util.EventHandler;
import com.igormeira.githubpop.model.PullRequest;

import java.util.ArrayList;

import static com.igormeira.githubpop.util.Dialog.showLoadingDialog;

public class PullRequestsActivity extends AppCompatActivity {

    private PullRequestViewModel pullRequestViewModel;
    private PullRequestRecyclerAdapter pullRequestRecyclerAdapter;
    private String creator, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPullRequestsBinding activityPullRequestsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_pull_requests);

        setUpInformation(activityPullRequestsBinding);

        // Bind RecyclerView
        RecyclerView recyclerView = activityPullRequestsBinding.recyclerViewPullRequest;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        pullRequestViewModel = ViewModelProviders.of(this).get(PullRequestViewModel.class);
        pullRequestRecyclerAdapter = new PullRequestRecyclerAdapter();
        recyclerView.setAdapter(pullRequestRecyclerAdapter);
        getAllPullRequests();
        pullRequestRecyclerAdapter.setContext(this);
    }

    private void setUpInformation(ActivityPullRequestsBinding activityPullRequestsBinding){
        creator = getIntent().getStringExtra("creator");
        name = getIntent().getStringExtra("name");
        activityPullRequestsBinding.setName(name);
        activityPullRequestsBinding.setHandler(new EventHandler(this));
    }

    private void getAllPullRequests() {
        ProgressDialog dialog = showLoadingDialog(this);
        dialog.show();
        pullRequestViewModel.getAllPullRequests(creator, name).observe(this, pullRequests -> {
            pullRequestRecyclerAdapter.setPullRequestsList((ArrayList<PullRequest>) pullRequests);
            dialog.dismiss();
        });
    }
}
