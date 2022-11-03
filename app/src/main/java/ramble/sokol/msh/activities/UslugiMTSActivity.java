package ramble.sokol.msh.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

import ramble.sokol.msh.R;

public class UslugiMTSActivity extends AppCompatActivity implements View.OnClickListener{

    private MaterialButton one, two;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uslugi_mtsactivity);
        one = findViewById(R.id.buttonItemUslOne);
        one.setOnClickListener(this);
        two = findViewById(R.id.buttonItemUslTwo);
        two.setOnClickListener(this);
        back = findViewById(R.id.imageButtonBackUslug);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonItemUslOne:
                String uri = "https://marketolog.mts.ru/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
                break;
            case R.id.buttonItemUslTwo:
                String uri2 = "https://cloud.mts.ru/";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(uri2));
                startActivity(i2);
                break;
            case R.id.imageButtonBackUslug:
                Intent intent = new Intent(UslugiMTSActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UslugiMTSActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}