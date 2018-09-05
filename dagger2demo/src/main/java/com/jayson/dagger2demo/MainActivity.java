package com.jayson.dagger2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jayson.dagger2demo.modal.Book;
import com.jayson.dagger2demo.modal.BookModule;
import com.jayson.dagger2demo.modal.Books;
import com.jayson.dagger2demo.modal.DaggerBookComponent;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Book book;

    @Inject
    Books books;

    private TextView mainText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainText = findViewById(R.id.main_text);
        DaggerBookComponent.builder().bookModule(new BookModule()).build().inject(this);

        mainText.setText("Hi:" + book + books);

    }
}
