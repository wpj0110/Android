package com.example.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.squareup.picasso.Picasso;

public class ArticleFragment extends Fragment {

    private static final String TAG = "ArticleFragmentTAG";

    public ArticleFragment() {
        //Required empty public constructor
    }
    //currently working on this, look at 5_SecondGeographyDrawerLayout

    static ArticleFragment newInstance(NewsArticles article, int index, int max)
    {
        ArticleFragment a = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("ARTICLE_DATA", article);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        a.setArguments(bdl);
        return a;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_layout = inflater.inflate(R.layout.fragment_article, container, false);

        Bundle args = getArguments();
        if (args != null) {
            final NewsArticles currentArticle = (NewsArticles) args.getSerializable("ARTICLE_DATA");
            if (currentArticle == null) {
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");

            TextView headline = fragment_layout.findViewById(R.id.headline);
            headline.setText(currentArticle.getTitle());
            headline.setOnClickListener(v -> onClickArticle(currentArticle.getUrl()));

            TextView dateTime = fragment_layout.findViewById(R.id.dateTime);
            String date = currentArticle.getPublishedAt();
            //Log.d(TAG, "onCreateView: Date before = "+date);
            date = date.replaceAll("T", " ");
            date = date.replaceAll("Z", " ");
            //date length supposed to be 19
            String shortDate = date.substring(0,19);
            //Log.d(TAG, "onCreateView: short Date is "+shortDate);
            //Log.d(TAG, "onCreateView: new date is " + date);
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date tempDate = null;
            try {
                tempDate = simpleDateFormat.parse(shortDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "onCreateView: new date is "+tempDate.toString());

            if (TextUtils.isEmpty(currentArticle.getPublishedAt())){
                dateTime.setVisibility(View.GONE);
            } else {
                dateTime.setText(tempDate.toString()); //must fix the time
            }
            dateTime.setOnClickListener(v -> onClickArticle(currentArticle.getUrl()));

            String author = currentArticle.getAuthor();
            TextView authorSource = fragment_layout.findViewById(R.id.authorSource);
            if (TextUtils.isEmpty(author) || author.equals("null")){
                authorSource.setVisibility(View.GONE);
            } else {
                authorSource.setText(currentArticle.getAuthor()); //must fix the time
            }
            authorSource.setOnClickListener(v -> onClickArticle(currentArticle.getUrl()));

            TextView description = fragment_layout.findViewById(R.id.description);
            if (TextUtils.isEmpty(currentArticle.getAuthor())){
                description.setVisibility(View.GONE);
            } else {
                description.setText(currentArticle.getDescription());
            }
            description.setOnClickListener(v -> onClickArticle(currentArticle.getUrl()));

            TextView pageNum = fragment_layout.findViewById(R.id.articleCount); //current index, total articles
            pageNum.setText(String.format(Locale.US, "%d of %d", index, total));

            ImageView imageView = fragment_layout.findViewById(R.id.articleImage);
            if (!TextUtils.isEmpty(currentArticle.getUrlToImage())){
                Picasso.get().load(currentArticle.getUrlToImage())
                        .placeholder(R.drawable.doge)
                        .error(R.drawable.saddogesmol)
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
                //defaultImageView.setVisibility(View.INVISIBLE);
            } else{
                imageView.setVisibility(View.GONE);
            }
            imageView.setOnClickListener(v -> onClickArticle(currentArticle.getUrl()));

            return fragment_layout;
        } else {
            return null;
        }
    }

    public void onClickArticle(String url){
        Log.d(TAG, "onClickArticle: ");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

}
