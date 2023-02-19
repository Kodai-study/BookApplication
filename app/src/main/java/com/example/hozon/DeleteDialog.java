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

/**
 * デバッグ画面で、「削除する」を選択した時に、削除の再確認を行うダイアログ
 */
public class DeleteDialog extends DialogFragment {

    /**
     * 削除するファイルの名前
     */
    String deleteBookName;

    Output output;
    /**
     * 元のデバッグ画面を変更する。
     */
    Debug debug;


    public DeleteDialog(String deleteBookName,Output output,Debug debug){
        this.deleteBookName = deleteBookName;
        this.output = output;
        this.debug = debug;
    }

    /* "本当に削除しますか?"を表示して削除が押されたらフォルダから削除する */
    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(deleteBookName + " を削除しますか?");
        dialog.setNegativeButton("キャンセル",null);
        dialog.setPositiveButton("削除します", new DialogInterface.OnClickListener() {
            @Override
            /* 削除を試して、削除できなかったらポップアップを表示 */
            public void onClick(DialogInterface dialogInterface, int i) {
                if(output.delete(deleteBookName) == output.FILE_DELETE_SUCCESS) {
                    debug.Refresh();
                }
                else{
                    Toast noDeletePopup = Toast.makeText(
                            debug.getApplicationContext(),deleteBookName + "が削除できません",Toast.LENGTH_LONG
                    );
                    noDeletePopup.show();
                }
            }
        });

        return dialog.create();
    }

}
