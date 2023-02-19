package com.example.hozon;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.sql.Ref;
import java.util.List;

/**
 * The type Debug.
 */
public class Debug extends AppCompatActivity {

    /**
     * 本のデータなどが入っている、ファイルパス(output作成用)
     */
    File file;
    /**
     * データファイルのやり取りを行うoutputクラス
     */
    Output output;
    /**
     * フォルダの中にあるファイル一覧
     */
    ListView Allbooks;
    /**
     * ドロップダウンメニューでジャンルを表示
     */
    Spinner AllGenres;
    /**
     * 選択、またはコンテキストメニューが作られたときに触られた要素(本の名前)
     */
    String selectBook;
    /**
     * デバッグ用のメッセージ表示
     */
    TextView message;
    /**
     * The Book information.
     */
    TextView bookInformation;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.debug_mode);
        ViewInit();
        Allbooks.setOnItemLongClickListener((a,b,c,d)->{return false;});
        registerForContextMenu(Allbooks);
        message.setOnClickListener(new AllDelete());
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nagaori_menu,contextMenu);
        contextMenu.setHeaderTitle("コンテキストメニュー");
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        this.selectBook = Allbooks.getItemAtPosition(position).toString();
        message.setText(selectBook);
    }


    /**
     * 画面に使われるViewたちの初期化。
     * レイアウトからの取得とイベントハンドラの割り当て
     */
    private void ViewInit(){
        file = getApplicationContext().getFilesDir();
        output = new Output(file.getAbsolutePath());
        Allbooks = findViewById(R.id.AllBooks);
        AllGenres = findViewById(R.id.Allgenres);

        String[] bookNames = output.allBookName();
        ArrayAdapter<String> List_adapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,bookNames);
        Allbooks.setAdapter(List_adapter);

        String[]  genreNames = output.genre.getGenreList();
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genreNames);
        AllGenres.setAdapter(SpinnerAdapter);
        message = findViewById(R.id.debug_pronpt);
        Allbooks.setOnItemSelectedListener(new ListSelect());

        Allbooks.setOnItemClickListener(new BookListClick());
        bookInformation = findViewById(R.id.bookInformtions);
    }

    /**
     * フォルダ内のファイル一覧を再読み込みする
     */
    public void Refresh(){
        output.Reflesh();
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,output.allBookName);
        Allbooks.setAdapter(SpinnerAdapter);
    }

    /* 長押しで出て来るコンテキストメニューを触ったときの処理 */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.bookInfomation:       //詳細情報を触ったとき、詳細情報のダイアログを表示
                new BookInformation(selectBook,output,this).
                        show(getSupportFragmentManager(), "delete");
                break;
            case R.id.deleteBook:           //削除を選択したとき、本当に削除しますか?のダイアログを表示
                new DeleteDialog(selectBook,output,this).
                        show(getSupportFragmentManager(), "delete");
                Refresh();
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * 本の名前を選択したときの処理
     * メッセージ欄に今選択したファイルの名前が書かれる
     */
    class ListSelect implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Object ob = adapterView.getSelectedItem();
            message.setText(ob.getClass().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Object ob = adapterView.getSelectedItem();
            message.setText(ob.getClass().toString());
        }
    }

    /**
     * The type Book list click.
     */
    class BookListClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            message.setText((String)adapterView.getItemAtPosition(i));
        }
    }

    /**
     * ジャンルオブジェクト以外の全てのオブジェクトを削除する
     */
    class AllDelete implements View.OnClickListener{
        int count = 0;
        @Override
        public void onClick(View view) {
            if(++count >= 10) {
                for(String e : output.allBookName()){
                    if(!e.contains("genre"))
                        output.delete(e);
                }
            }
        }
    }

    private void showBookInformation(String bookName){
        Book selectBook = output.<Book>Read(bookName);
        bookInformation.setText(selectBook.get_data(output.genre));
        bookInformation.setVisibility(View.VISIBLE);
        this.message.setText(selectBook.get_data(output.genre));
    }

}
