package com.example.android.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.OnInputListener{
    private MaterialDialog mMaterialDialog;
    private final String PRTAG = "PFTAG";
    private boolean twoPane=false;
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private MainActivityFragment mMainActivityFragment;
    private DetailActivityFragment mDetailActivityFramgment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load Fragment in main Activity
        if(savedInstanceState == null){
            mMainActivityFragment = new MainActivityFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.container, mMainActivityFragment, PRTAG).commit();
        }else{
            mMainActivityFragment =(MainActivityFragment) getSupportFragmentManager().findFragmentByTag(PRTAG);
        }

        //For Tablet Layout
        if(findViewById(R.id.detail_container)!=null){
            twoPane = true;
            if (savedInstanceState == null) {
                mDetailActivityFramgment = new DetailActivityFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detail_container, mDetailActivityFramgment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            twoPane = false;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            showDialogToAddCatagory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * showDualogToAddCatagory()- display Dialog to add new catagory
     *
     */
    private void showDialogToAddCatagory(){
//        mMaterialDialog = new MaterialDialog.Builder(this)
//                .title(R.string.title)
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .autoDismiss(true)
//                .negativeText(R.string.disagree)
//                .input(R.string.input_hint,R.string.input_pre_fill,false,
//                        new MaterialDialog.InputCallback(){
//                            @Override
//                            public void onInput(@NonNull MaterialDialog mDialog, CharSequence input){
//                                mMainActivityFragment.addCatagory(input.toString());
//                            }
//                        }).build();
//        mMaterialDialog.show();
         new MyDialogFragment().show(getSupportFragmentManager(), MainActivityFragment.EXTRA_TAG);
        //mMaterialDialog.show();
    }
    @Override
    public void onInput(@NonNull MaterialDialog mDialog, CharSequence input){
        mMainActivityFragment.addCatagory(input.toString());
    }

}
