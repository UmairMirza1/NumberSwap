package com.example.numberswap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.numberswap.R;

import java.util.Timer;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView imageView = findViewById(R.id.logo);

        // SLIDE DOWN ANIMATION
        Animation slideDown = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.drop_down);
        imageView.startAnimation(slideDown);

        //FLIP ANIMATION
        AnimatorSet set=(AnimatorSet) AnimatorInflater.loadAnimator(SplashScreenActivity.this,R.animator.flip_anim);
        set.setTarget(imageView);
        AnimatorSet flip = (AnimatorSet) AnimatorInflater.loadAnimator(SplashScreenActivity.this,R.animator.flip_again);
        set.setTarget(imageView);

        //ZOOM OUT ANIMATION
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //set.start();
                //set.playSequentially(set,flip);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





    }
}