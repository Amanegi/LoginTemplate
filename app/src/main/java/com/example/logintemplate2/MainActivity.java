package com.example.logintemplate2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.example.logintemplate2.databinding.ActivityMainBinding;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void load(View view) {
        disableEditText();

        animateButtonWidth();

        fadeOutTextAndShowProgressDialog();

        nextAction();
    }

    public void disableEditText() {
        mBinding.username.setFocusable(false);
        mBinding.password.setFocusable(false);
        mBinding.username.setKeyListener(null);
        mBinding.password.setKeyListener(null);
    }

    //fades out the text and shows the progress bar when text fades out completely
    private void fadeOutTextAndShowProgressDialog() {
        mBinding.text.animate().alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                })
                .start();
    }

    //animate the decrease of button width
    private void animateButtonWidth() {
        //ViewAnimator to decrease width from current size to required size
        ValueAnimator anim = ValueAnimator.ofInt(mBinding.button.getMeasuredWidth(), getFabWidth());
        //on each update the width of button will be changed
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                //get layout parameters
                ViewGroup.LayoutParams layoutParams = mBinding.button.getLayoutParams();
                //set the width in parameter and set it to button
                layoutParams.width = val;
                //to see changes request the layout
                mBinding.button.requestLayout();
            }
        });
        //set time duration of animation
        anim.setDuration(250);
        anim.start();
    }

    private void showProgressDialog() {
        mBinding.progressBar.setAlpha(1f);
        mBinding.progressBar
                .getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBar.setVisibility(VISIBLE);
    }

    //action performed after button shrinks and progress bar appears on it
    //here we only wait. You can add your code
    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();

                fadeOutProgressDialog();

                delayedStartNextActivity();
            }
        }, 2000);
    }

    //perform reveal animation
    private void revealButton() {
        //to remove shadow below the button
        mBinding.button.setElevation(0f);
        mBinding.reveal.setVisibility(VISIBLE);

        int cx = mBinding.reveal.getWidth();
        int cy = mBinding.reveal.getHeight();

        int x = (int) (getFabWidth() / 2 + mBinding.button.getX());
        int y = (int) (getFabWidth() / 2 + mBinding.button.getY());

        float finalRadius = Math.max(cx, cy) * 1.2f;

        Animator reveal = ViewAnimationUtils
                .createCircularReveal(mBinding.reveal, x, y, getFabWidth(), finalRadius);

        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                reset(animation);
                finish();
            }

            private void reset(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.reveal.setVisibility(INVISIBLE);
                mBinding.text.setVisibility(VISIBLE);
                mBinding.text.setAlpha(1f);
                mBinding.button.setElevation(4f);
                ViewGroup.LayoutParams layoutParams = mBinding.button.getLayoutParams();
                layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
                mBinding.button.requestLayout();
            }
        });

        reveal.start();
    }

    private void fadeOutProgressDialog() {
        mBinding.progressBar.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        }).start();
    }

    private void delayedStartNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra(Main2Activity.KEY_USERNAME, mBinding.username.getText().toString());
                intent.putExtra(Main2Activity.KEY_PASSWORD, mBinding.password.getText().toString());
                startActivity(intent);

            }
        }, 100);
    }

    //returns the width in pixels stored in dimens
    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }
}
