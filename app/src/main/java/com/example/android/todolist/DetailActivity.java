package com.example.android.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Bind;

public class DetailActivity extends AppCompatActivity implements MyDialogFragment.OnInputListener {
    public static final String ARGS_TASK = "ARGS_SYMBOL";

    private MaterialDialog mNewStockDialog;

    private DetailActivityFragment mDetailActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
        //    Bundle arguments = new Bundle();
        //    arguments.putParcelable(DetailActivityFragment.ARGS_SYMBOL,
         //           getArgu().getStringExtra(DetailActivity.ARGS_TASK));
            mDetailActivityFragment = new DetailActivityFragment();
            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, mDetailActivityFragment,ARGS_TASK)
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onInput(@NonNull MaterialDialog mDialog, CharSequence input){
        mDetailActivityFragment.addTask(input.toString());
    }
}
