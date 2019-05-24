package id.renaldirey.instagramstories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.renaldirey.instagramstories.widget.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoriesActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    private static final String TAG = StoriesActivity.class.getSimpleName();

    private int counter = 0;
    private long pressTime = 0L,
            limit = 5000L;

    StoriesProgressView storiesProgressView;
    ImageView ivStories;
    CircleImageView civUser;
    TextView tvUsername;
    View reverse, hold, skip;

    List<Integer> images;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                pressTime = System.currentTimeMillis();
                storiesProgressView.pause();
                Log.e(TAG, "press");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                long now = System.currentTimeMillis();
                storiesProgressView.resume();
                Log.e(TAG, "release");
                return limit < now - pressTime;
            }

            return true;
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    pressTime = System.currentTimeMillis();
//                    storiesProgressView.pause();
//                    Log.e(TAG, "press");
//                    return false;
//
//                case MotionEvent.ACTION_UP:
//                    long now = System.currentTimeMillis();
//                    storiesProgressView.resume();
//                    Log.e(TAG, "release");
//                    return limit < now - pressTime;
//            }
//
//            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        images = new ArrayList<>();

        initView();
    }

    private void initView() {
        // TODO: Initial Views
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);
        hold = findViewById(R.id.hold);
        ivStories = findViewById(R.id.iv_content);
        civUser = findViewById(R.id.civ_user);
        tvUsername = findViewById(R.id.tv_username);
        storiesProgressView = findViewById(R.id.spv_stories);

        Picasso.with(this)
                .load(R.drawable.user)
                .into(civUser);

        tvUsername.setText("Renaldi");

//        for(int i=1;i<10;i++) {
//            images.add(R.drawable.content);
//        }

        images.add(R.drawable.content2);
        images.add(R.drawable.content3);
        images.add(R.drawable.content4);
        images.add(R.drawable.content5);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        hold.setOnTouchListener(onTouchListener);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

//        ivStories.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                storiesProgressView.pause();
//                return false;
//            }
//        });

        setupStories();
    }

    private void setupStories() {
        Picasso.with(this)
                .load(images.get(0))
                .into(ivStories);

        storiesProgressView.setStoriesCount(images.size());
        storiesProgressView.setStoryDuration(limit);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();
    }

    @Override
    public void onNext() {
        Picasso.with(this)
                .load(images.get(++counter))
                .into(ivStories);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

        Picasso.with(this)
                .load(images.get(--counter))
                .into(ivStories);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storiesProgressView.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        storiesProgressView.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesProgressView.resume();
    }

    @Override
    public void onComplete() {
        finish();
    }
}
