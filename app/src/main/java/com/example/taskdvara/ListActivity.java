package com.example.taskdvara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DetailsAdapter.UserAdapterListener {
    private EditText search;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private List<User> list;
    private DBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        db = new DBClass(ListActivity.this);

        list = db.getUserDetails();

        detailsAdapter = new DetailsAdapter(this, list, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(detailsAdapter);


        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detailsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onUserSelected(User user) {
        Toast.makeText(getApplicationContext(), "Selected: " + user.getPhoneNumber() + ", " + user.getNetSpeed(), Toast.LENGTH_LONG).show();
    }
}