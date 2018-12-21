package com.example.carla.managementapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    Context mCtx;
    int listLayoutRes;
    List<Book> booksList;
    SQLiteDatabase mDatabase;

    public BookAdapter(Context mCtx, int listLayoutRes, List<Book> bookList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, bookList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.booksList = bookList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Book book = booksList.get(position);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDept = view.findViewById(R.id.textViewDepartment);
        TextView textViewSalary = view.findViewById(R.id.textViewSalary);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);
        TextView textViewAuthor = view.findViewById(R.id.textViewAuthor);


        textViewName.setText(book.getName());
        textViewDept.setText(book.getGenre());
        textViewSalary.setText(String.valueOf(book.getPrice()));
        textViewJoiningDate.setText(book.getAddingDate());
        textViewAuthor.setText(book.getAuthor());

        Button buttonDelete = view.findViewById(R.id.buttonDeleteEmployee);
        Button buttonEdit = view.findViewById(R.id.buttonEditEmployee);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(book);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM books WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{book.getId()});
                        reloadBooksFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateEmployee(final Book book) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_book, null);
        builder.setView(view);

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextSalary = view.findViewById(R.id.editTextSalary);
        final Spinner spinnerDepartment = view.findViewById(R.id.spinnerDepartment);
        final EditText editTextAuthor = view.findViewById(R.id.editTextAuthor);

        editTextAuthor.setText(book.getAuthor());
        editTextName.setText(book.getName());
        editTextSalary.setText(String.valueOf(book.getPrice()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String price = editTextSalary.getText().toString().trim();
                String genre = spinnerDepartment.getSelectedItem().toString();
                String author = editTextAuthor.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (price.isEmpty()) {
                    editTextSalary.setError("Price can't be blank");
                    editTextSalary.requestFocus();
                    return;
                }

                if(author.isEmpty()){
                    editTextAuthor.setError("Name can't be blank");
                    editTextAuthor.requestFocus();
                    return;
                }

                String sql = "UPDATE books \n" +
                        "SET name = ?, \n" +
                        "genre = ?, \n" +
                        "price = ?, \n" +
                        "author = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, genre, price, author, String.valueOf(book.getId())});
                Toast.makeText(mCtx, "Book Updated", Toast.LENGTH_SHORT).show();
                reloadBooksFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadBooksFromDatabase() {
        Cursor cursorBooks = mDatabase.rawQuery("SELECT * FROM books", null);
        if (cursorBooks.moveToFirst()) {
            booksList.clear();
            do {
                booksList.add(new Book(
                        cursorBooks.getInt(0),
                        cursorBooks.getString(1),
                        cursorBooks.getString(2),
                        cursorBooks.getString(3),
                        cursorBooks.getString(4),
                        cursorBooks.getDouble(5)
                ));
            } while (cursorBooks.moveToNext());
        }
        if(cursorBooks.getCount() == 0 ){
            booksList.clear();
        }

        cursorBooks.close();
        notifyDataSetChanged();

    }

}
