package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //api key = c783f2a7e0644d5cb729e46355fb1366
    //the source is CNN
    //sample: https://newsapi.org/v2/top-headlines?sources=cnn&language=en&apiKey=c783f2a7e0644d5cb729e46355fb1366
    //format: https://newsapi.org/v2/sources?language=en&country=us&category=________&apiKey=________
    //the above sample is working.

    private static final String TAG = "MainActivityTAG";
    static final String ACTION_NEWS_STORY = "SERVICE_MESSAGE_1";
    static final String SOURCE_MESSAGE = "SOURCE_MESSAGE";

    private final ArrayList<News> newsList = new ArrayList<>();
    private final ArrayList<String> newsStringList = new ArrayList<>();
    private final HashMap<String, ArrayList<News>> newsData = new HashMap<>();
    private final ArrayList<String> newsSourceDisplayed = new ArrayList<>();

    private Menu opt_menu;

    private List<Fragment> fragments;

    private NewsReceiver newsReceiver;


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    //private ArrayAdapter<News> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;
    private String category;
    private String currentSource;

    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        //saving section
        myPrefs = getSharedPreferences("Sample Prefs", Context.MODE_PRIVATE);
        String saveCategory = myPrefs.getString("Category","");
        Log.d(TAG, "onCreate: category saved is "+saveCategory);

        //fragments and pager section
        fragments = new ArrayList<>();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);


        // Runs a thread to fill up the sources and categories
        if (newsData.isEmpty()){
            //Log.d(TAG, "onCreate: newsData is empty");
            NewsSourceLoaderRunnable nlr = new NewsSourceLoaderRunnable(this,category);
            new Thread(nlr).start();
        }

        /// Start the service (pass it the article list)
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        //intent.putExtra("ARTICLE_LIST", newsSourceDisplayed); //newsSourceDisplayed is the articleList
        startService(intent);


        newsReceiver = new NewsReceiver(this); //create a NewsReceiver object in onCreate

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);//is this right?
        registerReceiver(newsReceiver, filter1); //is this right?

        //saved article load attempt
        //int saveSource = myPrefs.getInt("Source",-1);
        //if (saveSource >= 0){
        //    selectItem(saveSource);
        //}
    }






    public void updateData(ArrayList<News> listIn) { //need to fix updateData
        
        for (News n : listIn) {
            if (!newsData.containsKey(n.getCategory())) {
                newsData.put(n.getCategory(), new ArrayList<>());
            }
            Log.d(TAG, "updateData: n.getName() "+n.getName());
            Log.d(TAG, "updateData: newsData "+newsData);
            ArrayList<News> nlist = newsData.get(n.getCategory());
            if (nlist != null) {
                nlist.add(n);
            }
            //Log.d(TAG, "updateData: nlist = "+nlist);
        }

        newsData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(newsData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);


        newsList.addAll(listIn);
        //Log.d(TAG, "updateData: get(0).getName() "+newsList.get(1).getName());


        for (int i = 0 ; i < newsList.size() ; i++)
            newsStringList.add(newsList.get(i).getName());
        Log.d(TAG, "updateData: newsList.contains('name') "+newsList);
        //arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, newsList);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, newsStringList);
        mDrawerList.setAdapter(arrayAdapter);

        //Log.d(TAG, "updateData: newsList = "+newsList.toArray());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        myPrefs = getSharedPreferences("Sample Prefs", Context.MODE_PRIVATE);
        String saveCategory = myPrefs.getString("Category","");

        //saving section
        if (saveCategory.length() != 0) {
            Log.d(TAG, "updateData: there's a saved category");
            setTitle(saveCategory);
            Log.d(TAG, "onOptionsItemSelected: title is " + saveCategory);

            newsList.clear();
            newsStringList.clear();
            newsSourceDisplayed.clear();

            ArrayList<News> nlst = newsData.get(saveCategory);
            ArrayList<String> nlst2 = new ArrayList<>();
            ArrayList<String> nlst3 = new ArrayList<>();

            for (int i = 0; i < nlst.size(); i++) {
                nlst2.add(nlst.get(i).getName());
                nlst3.add(nlst.get(i).getId());
            }
            if (nlst != null && nlst2 != null && nlst3 != null) {
                newsList.addAll(nlst);
                newsStringList.addAll(nlst2);
                newsSourceDisplayed.addAll(nlst3);
            }

            arrayAdapter.notifyDataSetChanged();
        }
        //saved article load attempt, not doing this, making too many requests... :(
        //int saveSource = myPrefs.getInt("Source",-1);
        //if (saveSource >= 0){
        //    selectItem(saveSource);
        //}
    }




    // You need the 2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { //nothing to change
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { //nothing to change
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //select a category, then the drawer is updated

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString("Category", item.getTitle().toString());
        prefsEditor.apply();

        setTitle(item.getTitle());
        Log.d(TAG, "onOptionsItemSelected: title is "+item.getTitle());

        newsList.clear();
        newsStringList.clear();
        newsSourceDisplayed.clear();

        ArrayList<News> nlst = newsData.get(item.getTitle().toString());
        ArrayList<String> nlst2 = new ArrayList<>();
        ArrayList<String> nlst3 = new ArrayList<>();

        for (int i = 0; i < nlst.size(); i++) {
            nlst2.add(nlst.get(i).getName());
            nlst3.add(nlst.get(i).getId());
        }
        if (nlst != null && nlst2 != null && nlst3 != null) {
            newsList.addAll(nlst);
            newsStringList.addAll(nlst2);
            newsSourceDisplayed.addAll(nlst3);
        }

        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

    }

    public void reDoFragments(ArrayList<NewsArticles> articleList) {

        setTitle(currentSource);

        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();

        for (int i = 0; i < articleList.size(); i++) {
            fragments.add(
                    ArticleFragment.newInstance(articleList.get(i), i+1, articleList.size()));
            //pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }



    private void selectItem(int position) {
        //selecting a news source here
        SharedPreferences.Editor prefsEditor = myPrefs.edit(); //saving
        prefsEditor.putInt("Source", position); //saving
        prefsEditor.apply(); //saving


        Log.d(TAG, "selectItem: position is " +position);
        pager.setBackground(null);
        currentSource = newsSourceDisplayed.get(position);

        sendBroadcastToService(currentSource);

        mDrawerLayout.closeDrawer(mDrawerList);

    }

    // You need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    //////////////////////////////////////

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        MyPageAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }

    private class NewsReceiver extends BroadcastReceiver {

        private final MainActivity mainActivity;

        public NewsReceiver(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Received!");

            String action = intent.getAction();
            if (action == null)
                return;

            switch (action) {
                case MainActivity.ACTION_NEWS_STORY:
                    if (intent.hasExtra("TEST")){
                        //Log.d(TAG, "onReceive: yes it has TEST");
                        reDoFragments((ArrayList<NewsArticles>) intent.getSerializableExtra("TEST"));
                    }
                    break;

                //case MainActivity.MESSAGE_FROM_SERVICE:
                //    break;

                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }

    private void sendBroadcastToService(String msg) {
        Log.d(TAG, "sendBroadcastToService: ");
        Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_NEWS_STORY);
        intent.putExtra(MainActivity.SOURCE_MESSAGE, msg);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);//is this right?
        registerReceiver(newsReceiver, filter1); //is this right?
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(newsReceiver);
        super.onStop();
    }
}