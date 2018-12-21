package com.example.carla.managementapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    List<Book> bookList;
    SQLiteDatabase mDatabase;
    ListView listViewBooks;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        listViewBooks = (ListView) findViewById(R.id.listViewBooks);
        bookList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        showBooksFromDatabase();
    }

    private void showBooksFromDatabase() {
        Cursor cursorBooks = mDatabase.rawQuery("SELECT * FROM books", null);

        if (cursorBooks.moveToFirst()) {
            do {
                bookList.add(new Book(
                        cursorBooks.getInt(0),
                        cursorBooks.getString(1),
                        cursorBooks.getString(2),
                        cursorBooks.getString(3),
                        cursorBooks.getString(4),
                        cursorBooks.getDouble(5)
                ));
            } while (cursorBooks.moveToNext());
        }
        cursorBooks.close();

        adapter = new BookAdapter(this, R.layout.list_layout_book, bookList, mDatabase);
        adapter.notifyDataSetChanged();
        listViewBooks.setAdapter(adapter);

    }

}
