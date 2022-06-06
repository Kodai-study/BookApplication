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

public class Debug extends AppCompatActivity {

    final public int a = 100;
    TextView allBooks;
    TextView allGenres;
    File file;          //本のデータなどが入っている、ファイルパス(output作成用)
    Output output;      //データファイルのやり取りを行うoutputクラス
    ListView Allbooks;  /**　TODO　レガシーなUIタイプのため、新しいものに置き換える　*/
    List<Book> bookList;
    Spinner AllGenres;
    String selectBook;
    TextView message;



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.debug_mode);
        ViewInit();
        Allbooks.setOnItemLongClickListener((a,b,c,d)->{return false;});
        registerForContextMenu(Allbooks);
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

    }

    public void Refresh(){
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,output.allBookName);
        Allbooks.setAdapter(SpinnerAdapter);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        switch(item.getItemId()){
            case R.id.bookInfomation:
                int j = output.delete("eww.dat");
                if (j != 1) {
                    message.setText("削除できません" + j);
                }
                break;
            case R.id.deleteBook:
                new DeleteDialog(selectBook,output,this).
                        show(getSupportFragmentManager(), "delete");
                Refresh();
                break;
            default:
                return false;
        }
        return true;
    }

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

    class BookListClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            message.setText((String)adapterView.getItemAtPosition(i));
        }
    }

}
