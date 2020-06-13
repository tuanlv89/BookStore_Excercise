package com.example.quanlysach.view;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.quanlysach.R;
import com.example.quanlysach.database.model.Book;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context context;
    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView dot;
        public TextView author;
        public TextView category;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            dot = view.findViewById(R.id.dot);
            author = view.findViewById(R.id.author);
            category = view.findViewById(R.id.category);
        }
    }


    public BooksAdapter(Context context, List<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);

        holder.title.setText(book.getTitle());
        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.author.setText(book.getAuthor());
        holder.category.setText(book.getCategory());

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

}

