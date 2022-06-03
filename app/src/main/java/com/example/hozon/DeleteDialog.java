package com.example.hozon;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("削除しますか?");
        dialog.setPositiveButton("削除します",null);

        return dialog.create();
    }

    class A implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }

}
