package com.example.hozon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

    String deleteBookName;
    Output output;
    Debug debug;

    public DeleteDialog(String deleteBookName,Output output,Debug debug){
        this.deleteBookName = deleteBookName;
        this.output = output;
        this.debug = debug;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(deleteBookName + " を削除しますか?");
        dialog.setPositiveButton("削除します", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                output.delete(deleteBookName);
                debug.Refresh();
            }
        });

        return dialog.create();
    }

}
