package com.namclu.android.justbooks.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namclu.android.justbooks.R;
import com.namclu.android.justbooks.api.models.Book;

import java.util.List;

/**
 * Created by namlu on 04-Apr-17.
 */

public class BookItemsAdapter extends RecyclerView.Adapter<BookItemsAdapter.BookItemsAdapterViewHolder>{

    /* private fields */
    private final List<Book> mBooks;

    public BookItemsAdapter(List<Book> books) {
        mBooks = books;
    }

    @Override
    public BookItemsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new BookItemsAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(BookItemsAdapterViewHolder holder, int position) {
        Book currentBook = mBooks.get(position);

        holder.bookTitle.setText(R.string.heading_title);
        holder.bookTitleText.setText(String.format("%s", currentBook.getTitle()));
        holder.bookAuthor.setText(R.string.heading_author);
        holder.bookAuthorText.setText(String.format("%s", currentBook.getAuthor()));
        holder.bookDescription.setText(R.string.heading_description);
        holder.bookDescriptionText.setText(String.format("%s", currentBook.getDescription()));
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class BookItemsAdapterViewHolder extends RecyclerView.ViewHolder {

        /* Private fields */
        final TextView bookTitle;
        final TextView bookTitleText;
        final TextView bookAuthor;
        final TextView bookAuthorText;
        final TextView bookDescription;
        final TextView bookDescriptionText;

        public BookItemsAdapterViewHolder(View itemView) {
            super(itemView);

            bookTitle = (TextView) itemView.findViewById(R.id.text_book_title);
            bookTitleText = (TextView) itemView.findViewById(R.id.text_book_title_text);
            bookAuthor = (TextView) itemView.findViewById(R.id.text_book_author);
            bookAuthorText = (TextView) itemView.findViewById(R.id.text_book_author_text);
            bookDescription = (TextView) itemView.findViewById(R.id.text_book_description);
            bookDescriptionText = (TextView) itemView.findViewById(R.id.text_book_description_text);
        }
    }

    public void clear() {

        int size = mBooks.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mBooks.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }
}
