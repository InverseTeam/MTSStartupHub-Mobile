package ramble.sokol.msh.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ramble.sokol.msh.R;
import ramble.sokol.msh.databinding.ActivityEntryBinding;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PreferenceManager;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    private ImageButton buttonBack;
    private MaterialButton buttonEntry;
    private ActivityEntryBinding binding;
    private EditText editPasswordEntry, editEmailEntry;
    private String email, password;
    private TextView textErrorEmail;
    private ProgressBar progressEntry;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    private void saveEntry(){
        SharedPreferences sPref = getSharedPreferences("saveEntry", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("entrySave", true);
        editor.commit();
    }

    private void init(){
        buttonBack = findViewById(R.id.imageButtonBackEntry);
        buttonBack.setOnClickListener(this);
        buttonEntry = findViewById(R.id.buttonEntrySign);
        buttonEntry.setOnClickListener(this);
        editPasswordEntry = findViewById(R.id.editPasswordEntry);
        editEmailEntry = findViewById(R.id.editEmailEntry);
        editPasswordEntry.setOnFocusChangeListener(this);
        editEmailEntry.setOnFocusChangeListener(this);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        progressEntry = findViewById(R.id.progressEntry);
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .whereEqualTo(Constants.KEY_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        saveEntry();
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        loading(false);
                        Toast.makeText(getApplicationContext(), "Не удается войти в систему", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            buttonEntry.setVisibility(View.INVISIBLE);
            progressEntry.setVisibility(View.VISIBLE);
        }else{
            buttonEntry.setVisibility(View.VISIBLE);
            progressEntry.setVisibility(View.INVISIBLE);
        }
    }

    private void signInRequest(){
        email = editEmailEntry.getText().toString();
        password = editPasswordEntry.getText().toString();
        if (email.isEmpty()){
            editEmailEntry.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (password.isEmpty()){
            editPasswordEntry.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmailEntry.setBackgroundResource(R.drawable.edit_text_background_error);
            textErrorEmail.setVisibility(View.VISIBLE);
        }
        if (!(email.isEmpty()) && !(password.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signIn();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonBackEntry:
                Intent intent = new Intent(EntryActivity.this, RegestrationClass.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonEntrySign:
                signInRequest();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EntryActivity.this, RegestrationClass.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
}