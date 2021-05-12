package com.example.newsapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class NewsService extends Service {

    private static final String TAG = "NewsServiceTAG";
    public static final String DATA_EXTRA = "DATA_EXTRA";
    private boolean running = true;

    private ArrayList<NewsArticles> articleList = new ArrayList<>();
    private ServiceReceiver serviceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        //Creating new thread for my service
        //ALWAYS write your long running tasks
        // in a separate thread, to avoid an ANR issue


        //I must create a ServiceReceiver object here, which means I also have to
        //make a ServiceReceiver class within the NewsService class?
        //Create IntentFilters
        //Register ServiceReceiver

        ServiceReceiver serviceReceiverObj = new ServiceReceiver(this);
        IntentFilter filter1 = new IntentFilter(MainActivity.ACTION_NEWS_STORY);
        registerReceiver(serviceReceiverObj, filter1);


        //start the thread below
        new Thread(() -> {

            //int counter = 1;
            while (running) {
                Log.d(TAG, "onStartCommand: running!!!");

                try {
                    while (articleList.size() == 0) {
                        Log.d(TAG, "onStartCommand: zeroArticles... sleeping...");
                        Thread.sleep(250); //sleep must be 250ms
                    }
                    Log.d(TAG, "onStartCommand: Inside try/catch");
                    Intent intent1 = new Intent();
                    intent1.setAction(MainActivity.ACTION_NEWS_STORY);
                    intent1.putExtra("TEST", articleList);
                    sendBroadcast(intent1);
                    articleList.removeAll(articleList);
                    //articleList.clear();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //sendBroadcast("Count Service Broadcast Message " + counter);
                //counter++;
            }

            // Send a final message
            sendBroadcast("Service Thread Stopped");

            Log.d(TAG, "run: Ending loop");
        }).start();

        return Service.START_STICKY;
    }

    private void sendBroadcast(String msg) {
        Intent intent = new Intent();
        //intent.setAction(MainActivity.MESSAGE_FROM_SERVICE);
        intent.putExtra(DATA_EXTRA, msg);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        // Send a message when destroyed
        running = false;
        unregisterReceiver(serviceReceiver);
        Intent intent = new Intent(NewsService.this, MainActivity.class);
        stopService(intent);
        super.onDestroy();
    }



    public void setArticles(ArrayList<NewsArticles> thisArticleList) {
        Log.d(TAG, "setArticles: hello there "+thisArticleList);
        articleList.clear();
        articleList.addAll(thisArticleList);
    }

    private class ServiceReceiver extends BroadcastReceiver { //The Receiver of NewsService

        private final NewsService newsService;
        public ServiceReceiver(NewsService newsService) { this.newsService = newsService;}

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");

            String action = intent.getAction();
            if (action == null)
                return;

            switch (action) {
                case MainActivity.ACTION_NEWS_STORY:
                    Log.d(TAG, "onReceive: Service message 1 received!");
                    if (intent.hasExtra(MainActivity.SOURCE_MESSAGE)){
                        String choiceSource = intent.getSerializableExtra(MainActivity.SOURCE_MESSAGE).toString();
                        Log.d(TAG, "onReceive: Source chosen is " + intent.getSerializableExtra(MainActivity.SOURCE_MESSAGE));
                        NewsArticleLoaderRunnable narc = new NewsArticleLoaderRunnable(NewsService.this, choiceSource);
                        new Thread(narc).start();
                    }

                    break;

                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }

    }
}