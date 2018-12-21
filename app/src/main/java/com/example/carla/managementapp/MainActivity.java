package com.example.carla.managementapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carla.managementapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "mybooksdatabase";
    EditText editTextName, editTextPrice, editTextAuthor;
    Spinner spinnerDepartment;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);
        editTextAuthor = (EditText) findViewById(R.id.editTextAuthor);



        findViewById(R.id.buttonAddBook).setOnClickListener(this);
        findViewById(R.id.buttonViewBook).setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        createBookTable();
    }


    private void createBookTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS books (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT book_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    genre varchar(200) NOT NULL,\n" +
                        "    addingdate datetime NOT NULL,\n" +
                        "    author varchar(200) NOT NULL, \n" +
                        "    price double NOT NULL\n" +
                        ");"
        );
    }

    private boolean inputsAreCorrect(String name, String price, String author) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (price.isEmpty() || Integer.parseInt(price) <= 0) {
            editTextPrice.setError("Please enter salary");
            editTextPrice.requestFocus();
            return false;
        }
        if(author.isEmpty()){
            editTextAuthor.setError("Please enter a name");
            editTextAuthor.requestFocus();
            return false;
        }

        return true;
    }

    private void addEmployee() {
        String name = editTextName.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String genre = spinnerDepartment.getSelectedItem().toString();
        String author = editTextAuthor.getText().toString().trim();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String addingdate = sdf.format(cal.getTime());

        if (inputsAreCorrect(name, price,author)) {

            String insertSQL = "INSERT INTO books \n" +
                    "(name, genre, addingdate, author, price)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?);";

            mDatabase.execSQL(insertSQL, new String[]{name, genre, addingdate, author, price});

            Toast.makeText(this, "Book Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddBook:
                addEmployee();
                break;
            case R.id.buttonViewBook:
                startActivity(new Intent(this, BookActivity.class));
                break;
        }
    }
}
