package com.example.android.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by veeral on 29/08/2016.
 */
public class MyDialogFragment extends DialogFragment {


    public interface OnInputListener {
        void onInput(@NonNull MaterialDialog mDialog, CharSequence input);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnInputListener)) {
            throw new ClassCastException(activity.toString() + " must implement YesNoListener");
        }
    }

    @Override
    public MaterialDialog onCreateDialog(Bundle savedInstanceState) {

        return new MaterialDialog.Builder(getActivity())
                .title(R.string.title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .autoDismiss(true)
                .negativeText(R.string.disagree)
                .input(R.string.input_hint,R.string.input_pre_fill,false,
                        new MaterialDialog.InputCallback(){
                            @Override
                            public void onInput(@NonNull MaterialDialog mDialog, CharSequence input){
                                //smMainActivityFragment.addCatagory(input.toString());
                                ((OnInputListener) getActivity()).onInput(mDialog,input);
                            }
                        }).build();


        /*return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_my_title)
                .setMessage(R.string.dialog_my_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((YesNoListener) getActivity()).onYes();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((YesNoListener) getActivity()).onNo();
                    }
                })
                .create();*/
    }
}
