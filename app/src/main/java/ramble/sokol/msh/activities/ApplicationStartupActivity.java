package ramble.sokol.msh.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

import ramble.sokol.msh.R;

public class ApplicationStartupActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editStartName, editStartSurname, editStartSecondName, editStartNumber, editStartMail, editStartCountry, editStartPartProgram,
            editStartProjectName, editStartWebsite, editStartStage, editStartDescrip, editStartPresent, editStartDopMaterials;
    MaterialButton buttonStart;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_startup);
        init();
    }

    private void init(){
        editStartName = findViewById(R.id.editStartName);
        editStartSurname = findViewById(R.id.editStartSurname);
        editStartSecondName = findViewById(R.id.editStartSecondName);
        editStartNumber = findViewById(R.id.editStartNumber);
        editStartMail = findViewById(R.id.editStartMail);
        editStartCountry = findViewById(R.id.editStartCountry);
        editStartPartProgram = findViewById(R.id.editStartPartProgram);
        editStartProjectName = findViewById(R.id.editStartProjectName);
        editStartWebsite = findViewById(R.id.editStartWebsite);
        editStartStage = findViewById(R.id.editStartStage);
        editStartDescrip = findViewById(R.id.editStartDescrip);
        editStartPresent = findViewById(R.id.editStartPresent);
        editStartDopMaterials = findViewById(R.id.editStartDopMaterials);
        buttonStart = findViewById(R.id.buttonStartSubmit);
        buttonStart.setOnClickListener(this);
        buttonBack = findViewById(R.id.imageButtonBackAppStartup);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonStartSubmit:
                break;
            case R.id.imageButtonBackAppStartup:
                Intent intent = new Intent(ApplicationStartupActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}