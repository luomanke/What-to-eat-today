package com.example.luoma.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.example.luoma.myapplication.varRepo.FeedEntry.COLUMN_NAME_RESTAURANT;
import static com.example.luoma.myapplication.varRepo.FeedEntry.COLUMN_NAME_TITLE;
import static com.example.luoma.myapplication.varRepo.FeedEntry.TABLE_NAME;


public class MainActivity extends AppCompatActivity {
    public static final String appToken = "Bearer UXV4f0aKYr-gn8nAN7xjdU-hW7qZdcbVxfx0BmyrL9dm3HQU5rpTRMiihTH25HavVrBlWL8CjCBiYcp1lA2EFiT5LBeIo4gribwZT341Ga3KKE803w8JD8b__8PoWXYx";

    private Random randomGenerator = new Random();

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // =================== GET DATA FROM SQLITE ==========================
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // UPDATE YELP DATA
        double longitude = -79.399014;
        double latitude = 43.655820;

        String url = "https://api.yelp.com/v3/businesses/search?latitude=" +
                Double.toString(latitude) +
                "&longitude=" +Double.toString(longitude) +
                "&radius=" + "100";

        String jString = null;
        try {
            jString = new RetrieveFeedTask().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final ParseJson P = new ParseJson();
        P.setJsonData(jString);
        List<String> nameList = P.getNameList();

        // YELP list
        ContentValues values = new ContentValues();
        values.put("title", "YELP Recommendation");
        values.put("restaurant", nameList.toString());
        db.update("entry", values, "title='YELP Recommendation'", null);
        // END     -     UPDATE YELP DATA

        String[] projection = {
                COLUMN_NAME_TITLE,
                COLUMN_NAME_RESTAURANT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                COLUMN_NAME_RESTAURANT + " DESC";

        final Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        final List titles = new ArrayList<>();
        final List rests = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
            String rest = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_RESTAURANT));
            titles.add(title);
            rests.add(rest);
        }
        cursor.close();

        // =================== END ==== GET DATA FROM SQLITE ==========================



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(titles);

        mAdapter.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void onGoBtnSelect(int position) {
                String temp = rests.get(position).toString().replaceAll("[\\[\\]]","");
                String[] result = temp.split(",");
                int index = randomGenerator.nextInt(result.length);
                // YELP selected
                if (titles.get(position).toString().equals("YELP Recommendation")) {
                    String selected = P.getYelpSelected(result[index].trim());
                    Intent i = new Intent(MainActivity.this, ResultActivity.class);
                    i.putExtra("selected", selected);
                    startActivity(i);
                }

                Toast.makeText(MainActivity.this, "Let's go: " + result[index], Toast.LENGTH_SHORT).show();
//                a = position;
            }

            @Override
            public void OnAddBtnSelect() {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }

            @Override
            public void onDelBtnSelect(int position) {
                String selection = COLUMN_NAME_TITLE + " LIKE ?";
                String[] selectionArgs = {titles.get(position).toString()};
                db.delete(TABLE_NAME, selection, selectionArgs);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

//        final Button go = (Button) findViewById(R.id.button_id);
//        go.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String temp = rests.get(a).toString().replaceAll("[\\[\\]]","");
//                String[] result = temp.split(",");
//                int index = randomGenerator.nextInt(result.length);
//                // YELP selected
//                if (titles.get(a).toString().equals("YELP Recommendation")) {
//                    String selected = P.getYelpSelected(result[index].trim());
//                    Intent i = new Intent(MainActivity.this, ResultActivity.class);
//                    i.putExtra("selected", selected);
//                    startActivity(i);
//                    a = 0;
//                }
//
//                Toast.makeText(MainActivity.this, "Let's go: " + result[index], Toast.LENGTH_SHORT).show();
//            }
//        }
//        );
    }
}
