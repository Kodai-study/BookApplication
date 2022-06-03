package com.example.hozon;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.Calendar;
import java.util.List;


public class Regestar_Activity extends AppCompatActivity {

    InputProcess inputProcess = new InputProcess();
    ImageView photo;
    EditText input_name;
    EditText input_genre;
    Spinner dropdown;
    Button button;
    File file;
    Output output;
    TextView textView;
    TextView error;
    Calendar calendar;
    EditText date;
    EditText befor_days;
    ImageButton calendar_button;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        init();

        dropdown.setOnItemSelectedListener(inputProcess.new Doropdown_proces());
        input_genre.setOnFocusChangeListener( inputProcess.new EditText_cursol(inputProcess.DEFOLT_GENRE));
        input_name.setOnFocusChangeListener(inputProcess.new EditText_cursol(inputProcess.DEFOLT_BOOKNAME));
        button.setOnClickListener(inputProcess.new RegestarButton_click());
        calendar_button.setOnClickListener(inputProcess.new calendarButton_push());
        photo.setOnClickListener(inputProcess.new DebugButtonPush());

        try {
            String[] genres = output.genre.getGenreList();
            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genres);

            output.main(true,"Genre_names");
            List<Book> BookList = output.all_books();
            dropdown.setAdapter(spinner_adapter);
        }
        catch (Exception e){
            Log.w("MAIN","output.mainのエラー" + e.getMessage());
            view_error(e);
        }
        finally {
            output.finish();
        }

    }




    private void init(){
        setContentView(R.layout.register_scene);
        photo = findViewById(R.id.imageView);
        input_name = findViewById(R.id.BookName);
        input_genre = findViewById(R.id.BookGenre);
        dropdown = findViewById(R.id.Genres);
        button = findViewById(R.id.Resgester_button);
        file = getApplicationContext().getFilesDir();
        output = new Output(file.getAbsolutePath());
        textView = findViewById(R.id.textView2);
        error = findViewById(R.id.error_message);
        error.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        date = findViewById(R.id.input_date);
        befor_days = findViewById(R.id.days_ago);
        calendar_button = findViewById(R.id.calendar_button);

        input_name.setText(inputProcess.DEFOLT_BOOKNAME, TextView.BufferType.NORMAL);
        input_genre.setText(inputProcess.DEFOLT_GENRE, TextView.BufferType.NORMAL);


    }

    public void view_error(Exception e){
        error.setVisibility(View.VISIBLE);
        error.setText(e.getMessage(), TextView.BufferType.NORMAL);
    }
    public void view_error(String errorMessage){

        if(errorMessage == null){
            error.setVisibility(View.GONE);
            return;
        }


        error.setVisibility(View.VISIBLE);
        error.setText(errorMessage, TextView.BufferType.NORMAL);

    }

    protected void refresh(){
        String[] genres = output.genre.getGenreList();
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genres);
        dropdown.setAdapter(spinner_adapter);
    }

    private class InputProcess {

        private int dropdown_position = 0;
        public final String DEFOLT_GENRE = "新しく追加するジャンルを入力";
        public final String DEFOLT_BOOKNAME = "本の名前を入力";


        class Doropdown_proces implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //text.setText((String)spinner.getSelectedItem());
                if (position == getSelectSize() - 1) {
                    input_genre.setVisibility(View.VISIBLE);
                } else {
                    input_genre.setVisibility(View.GONE);
                }
                textView.setText((String) dropdown.getSelectedItem());
                dropdown_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dropdown.setSelection(dropdown.getAdapter().getCount() - 1);
            }

            public int getSelectSize() {
                return dropdown.getAdapter().getCount();
            }
        }


        class EditText_cursol implements View.OnFocusChangeListener {
            private String default_text;

            public EditText_cursol(String default_text) {
                this.default_text = default_text;
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText text = (EditText) v;

                if (hasFocus && text.getText().toString().contains(default_text))
                    text.setText("", TextView.BufferType.NORMAL);
                else if (!hasFocus && text.getText().toString().length() <= 2)
                    text.setText(default_text, TextView.BufferType.NORMAL);
            }// クラス ForcusChange
        }


        class RegestarButton_click implements View.OnClickListener {

            @Override
            public void onClick(View v) {

                Book regest_book;
                String bookName = input_name.getText().toString();
                if(bookName.contains(inputProcess.DEFOLT_BOOKNAME) || bookName.length() <= 2){
                    view_error("本の名前を入力してください");
                    return;
                }
                if(input_genre.getVisibility() != View.GONE) {
                    String genre = input_genre.getText().toString();
                    if (genre.contains(inputProcess.DEFOLT_GENRE) || genre.length() <= 2) {
                        view_error("ジャンル名を入力してください");
                        return;
                    }
                    regest_book = new Book(bookName,genre,output.genre);
                }
                else {
                    int genre_id = dropdown.getSelectedItemPosition();
                    regest_book = new Book(bookName, genre_id);

                }
                if(!output.Regestar_book(regest_book))
                    view_error("Bookを登録できませんでした");
                else {
                    output.finish();
                    refresh();

                }
            }
        }// クラス 登録ボタンクリック


        class calendarButton_push implements View.OnClickListener{
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(Regestar_Activity.this,new calendar_Select(),calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        }

        class calendar_Select implements DatePickerDialog.OnDateSetListener{
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectDay = Calendar.getInstance();
                selectDay.clear();
                selectDay.set(year,month,dayOfMonth);

                if(selectDay.after(calendar)) {
                    view_error("未来を選んでいます");
                    befor_days.setText("0");

                    return;
                }
                int def_cnt = -1;
                while(calendar.after(selectDay)){
                    selectDay.add(Calendar.DATE,1);
                    def_cnt++;
                }
                befor_days.setText(String.valueOf(def_cnt));
                date.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth));
                view_error((String)null);
            }

            private void day_set(Calendar c){
                //date.setText();
            }
        }

        class DebugButtonPush implements View.OnClickListener{
            private int debugButtonCount = 0;
            @Override
            public void onClick(View v) {
                debugButtonCount++;
                if(debugButtonCount > 7){
                    output.finish();
                    Intent intent = new Intent(getApplication(),Debug.class);
                    startActivity(intent);
                }
            }
        }
    }
}


