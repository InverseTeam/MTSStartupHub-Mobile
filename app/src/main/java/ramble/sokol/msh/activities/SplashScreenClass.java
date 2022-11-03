package ramble.sokol.msh.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ramble.sokol.msh.R;

public class SplashScreenClass extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;
    private Animation topAnim, botAnim;
    private ImageView imageSplash;
    private TextView textSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    private void init(){
        topAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation_top);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation_bottom);
        imageSplash = findViewById(R.id.image_splash_screen);
        textSplash = findViewById(R.id.text_splash_screen);
        imageSplash.setAnimation(botAnim);
        textSplash.setAnimation(topAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firstEntry();
            }
        }, SPLASH_SCREEN);
    }

    private void firstEntry(){
        SharedPreferences sPref1 = getSharedPreferences("saveEntry", MODE_PRIVATE);
        boolean entryFirst = sPref1.getBoolean("entrySave", false);
        Intent intent;
        if (entryFirst){
            intent = new Intent(SplashScreenClass.this, MainMenuActivity.class);
        }else{
            intent = new Intent(SplashScreenClass.this, ZeroActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
