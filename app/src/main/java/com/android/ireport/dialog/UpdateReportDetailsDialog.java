package com.android.ireport.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.ireport.R;
import com.android.ireport.fragment.EditReportFragment;
import com.android.ireport.utils.FireBaseHelper;

public class UpdateReportDetailsDialog extends AppCompatDialogFragment {
    private static final String TAG = "UpdateReportDetailsDial";

    private EditText details;
    private FireBaseHelper mFirebaseHelper;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: create Dialog");

        mFirebaseHelper = new FireBaseHelper(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        details = view.findViewById(R.id.et_details_dialog);

        builder.setView(view)
                .setTitle("Update report details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: save new description.");
                        String reportId = getArguments().getString("report_id_for_dialog");
                        updateReportDescription(details.getText().toString(), reportId);


                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        Fragment fragment = manager.findFragmentById(R.id.middle_layout);
                        if (fragment instanceof EditReportFragment) {
                            FragmentTransaction trans = manager.beginTransaction();
                            trans.remove(fragment);
                            trans.commit();
                            manager.popBackStack();
                        }
                    }
                });


        details.setText(getArguments().getString("description_for_dialog"));

        return builder.create();
    }

    private void updateReportDescription(String details, String reportId){
        mFirebaseHelper.updateReportDescription(details, reportId);
    }
}
