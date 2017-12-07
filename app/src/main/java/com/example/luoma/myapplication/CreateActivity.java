package com.example.luoma.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuoMa on 09/25/2017.
 */

public class CreateActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final List<String> rest_ls = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.create_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final RecyclerView.Adapter mAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            class ViewHolder extends RecyclerView.ViewHolder {
                // each data item is just a string in this case
                LinearLayout mLinearLayout;
                ViewHolder(View v) {
                    super(v);
                    mLinearLayout = (LinearLayout) v;
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_rv_item, parent, false);
                ViewHolder vh= new ViewHolder(v);
                return vh;
            }

            @Override
            public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
                ViewHolder vh = (ViewHolder) holder;

                TextView text_item = (TextView) vh.mLinearLayout.findViewById(R.id.text_item);
                text_item.setText(rest_ls.get(position));

                ImageButton delBtn = (ImageButton) vh.mLinearLayout.findViewById(R.id.delete_btn);

                delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        rest_ls.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return rest_ls.size();
            }

        };
        mRecyclerView.setAdapter(mAdapter);

        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText rest = (EditText) findViewById(R.id.rest_input);
                rest_ls.add(rest.getText().toString());
                mAdapter.notifyItemInserted(rest_ls.size()-1);
                rest.setText("");
            }
        });

        final Button create = (Button) findViewById(R.id.create_list);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText title = (EditText) findViewById(R.id.title_input);

                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(v.getContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // **************** INSERT INTO DB *********************

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(varRepo.FeedEntry.COLUMN_NAME_TITLE, title.getText().toString());
                values.put(varRepo.FeedEntry.COLUMN_NAME_RESTAURANT, String.valueOf(rest_ls));

                // Insert the new row, returning the primary key value of the new row
                db.insert(varRepo.FeedEntry.TABLE_NAME, null, values);

                // **************** END *** INSERT INTO DB *********************

                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        }
        );
    }
}

