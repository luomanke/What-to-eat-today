package com.example.luoma.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        setContentView(R.layout.create);


        mRecyclerView = (RecyclerView) findViewById(R.id.create_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Button addNewRow = (Button) findViewById(R.id.addNewRow);
        addNewRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText newRow = new EditText(CreateActivity.this);
                mRecyclerView.addView(newRow);
            }
        });

        final Button create = (Button) findViewById(R.id.add_button);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText title = (EditText) findViewById(R.id.EditText1);
                EditText res1 = (EditText) findViewById(R.id.EditText2);
                EditText res2 = (EditText) findViewById(R.id.EditText3);

                List test = new ArrayList<>();
                test.add(res1.getText().toString());
                test.add(res2.getText().toString());


                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(v.getContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // **************** INSERT INTO DB *********************

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(varRepo.FeedEntry.COLUMN_NAME_TITLE, title.getText().toString());
                values.put(varRepo.FeedEntry.COLUMN_NAME_RESTAURANT, String.valueOf(test));

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(varRepo.FeedEntry.TABLE_NAME, null, values);

                // **************** READ FROM DB *********************
                String[] projection = {
                        varRepo.FeedEntry._ID,
                        varRepo.FeedEntry.COLUMN_NAME_TITLE,
                        varRepo.FeedEntry.COLUMN_NAME_RESTAURANT
                };

                // Filter results WHERE "title" = 'My Title'
//                String selection = varRepo.FeedEntry.COLUMN_NAME_TITLE + " = ?";
//                String[] selectionArgs = { "My Title" };

                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        varRepo.FeedEntry.COLUMN_NAME_RESTAURANT + " DESC";

                Cursor cursor = db.query(
                        varRepo.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        null,                                       // The columns for the WHERE clause
                        null,                                       // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );

                List itemIds = new ArrayList<>();
                while(cursor.moveToNext()) {
                    long itemId = cursor.getLong(
                            cursor.getColumnIndexOrThrow(varRepo.FeedEntry._ID));
                    String title_t = cursor.getString(cursor.getColumnIndexOrThrow(varRepo.FeedEntry.COLUMN_NAME_TITLE));
                    itemIds.add(itemId);
                }
                cursor.close();

                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        }
        );
    }
}

