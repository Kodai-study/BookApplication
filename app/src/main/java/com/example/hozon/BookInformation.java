package com.example.hozon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class BookInformation extends DialogFragment {

    String deleteBookName;
    Output output;
    Debug debug;

    public BookInformation(String deleteBookName,Output output,Debug debug){
        this.deleteBookName = deleteBookName;
        this.output = output;
        this.debug = debug;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle){
        Book selectBook = output.<Book>Read(deleteBookName);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(selectBook.get_data(output.genre));
        dialog.setTitle(deleteBookName + " の詳細");
        dialog.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeleteDialog(deleteBookName,output,debug).
                        show(debug.getSupportFragmentManager(), "delete");
            }
        });
        dialog.setNegativeButton("閉じる", (a,b) -> {onDestroyView();});

        return dialog.create();
    }

}
