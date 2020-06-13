package com.example.quanlysach.view;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.quanlysach.R;
import com.example.quanlysach.database.helper.BookDatabaseHelper;
import com.example.quanlysach.database.model.Book;
import com.example.quanlysach.utils.MyDividerItemDecoration;
import com.example.quanlysach.utils.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private BooksAdapter bookListAdapterAdapter;
    private List<Book> booksList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noBooksView;
    public int currentBookId = 0;

    private BookDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noBooksView = findViewById(R.id.empty_books_view);

        db = new BookDatabaseHelper(this);

        booksList.addAll(db.getAllBooks());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookDialog(false, null, -1);
            }
        });

        bookListAdapterAdapter = new BooksAdapter(this, booksList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(bookListAdapterAdapter);

        toggleEmptyBooks();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void createBookEntry(String title, String author, String category) {
        Book book = new Book(currentBookId, title, author, category);
        Log.d("AAAA", book.toString());
        int id = db.addBook(book);

        // get the newly inserted title from db
        Book newBook = db.getBook(id);
        Log.d("AAA", newBook.toString());
        if (newBook != null) {
            // adding new title to array list at 0 position
            booksList.add(0, newBook);

            // refreshing the list
            bookListAdapterAdapter.notifyDataSetChanged();

            toggleEmptyBooks();
        }

        currentBookId ++;
    }

    private void updateBook(String bookTitle, String bookAuthor, String bookCategory, int position) {
        Book book = booksList.get(position);
        book.setTitle(bookTitle);
        book.setAuthor(bookAuthor);
        book.setCategory(bookCategory);
        db.updateBook(book);

        // refreshing the list
        booksList.set(position, book);
        bookListAdapterAdapter.notifyItemChanged(position);

        toggleEmptyBooks();
    }

    private void deleteBook(int position) {
        db.deleteBook(booksList.get(position));

        booksList.remove(position);
        bookListAdapterAdapter.notifyItemRemoved(position);

        toggleEmptyBooks();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Chỉnh sửa", "Xóa"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tùy chọn");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showBookDialog(true, booksList.get(position), position);
                } else {
                    deleteBook(position);
                }
            }
        });
        builder.show();
    }

    private void showBookDialog(final boolean shouldUpdate, final Book book, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.book_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.book_title_edit);
        final EditText inputAuthor = view.findViewById(R.id.book_author_edit);
        final EditText inputCategory = view.findViewById(R.id.book_category_edit);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);

        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_add_book) : getString(R.string.lbl_edit_book_title));

        if (shouldUpdate && book != null) {
            inputTitle.setText(book.getTitle());
            inputAuthor.setText(book.getAuthor());
            inputCategory.setText(book.getCategory());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Chỉnh sửa" : "Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Hủy",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputTitle.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating book
                if (shouldUpdate && book != null) {
                    // update book by it's id
                    updateBook(inputTitle.getText().toString(), inputAuthor.getText().toString(), inputCategory.getText().toString(), position);
                } else {
                    // create new book
                    createBookEntry(inputTitle.getText().toString(), inputAuthor.getText().toString(), inputCategory.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyBooks() {

        if (db.getBooksCount() > 0) {
            noBooksView.setVisibility(View.GONE);
        } else {
            noBooksView.setVisibility(View.VISIBLE);
        }
    }
}
