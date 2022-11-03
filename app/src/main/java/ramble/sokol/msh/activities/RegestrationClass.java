package ramble.sokol.msh.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import ramble.sokol.msh.R;
import ramble.sokol.msh.databinding.ActivityRegestrationBinding;
import ramble.sokol.msh.intrerfaces.Api;
import ramble.sokol.msh.models.ApiUtils;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PostRegistration;
import ramble.sokol.msh.models.PostToken;
import ramble.sokol.msh.models.PreferenceManager;
import ramble.sokol.msh.models.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegestrationClass extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    private TextView entryTextClick, textErrorPassword, textErrorEmail, textAddImage;
    private long backPressedTime;
    private Toast backToast;
    private EditText editName, editSurname, editMail, editPassword;
    private MaterialButton buttonCreate;
    private String name, surname, mail, password;
    private Api api;
    private RoundedImageView roundedImageView;
    private ActivityRegestrationBinding binding;
    private ProgressBar progressBar;
    private String encodedImage;
    private FrameLayout linearImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegestrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    private void init(){
        progressBar = findViewById(R.id.progressRegistration);
        preferenceManager = new PreferenceManager(getApplicationContext());
        linearImage = findViewById(R.id.linearImage);
        roundedImageView = findViewById(R.id.imageProfile);
        roundedImageView.setOnClickListener(this);
        entryTextClick =  findViewById(R.id.textClickEntry);
        entryTextClick.setOnClickListener(this);
        buttonCreate = findViewById(R.id.buttonCreateAccount);
        buttonCreate.setOnClickListener(this);
        editName = findViewById(R.id.editRegName);
        editSurname = findViewById(R.id.editRegSurname);
        editMail = findViewById(R.id.editRegMail);
        editPassword = findViewById(R.id.editRegPas);
        editName.setOnFocusChangeListener(this);
        editSurname.setOnFocusChangeListener(this);
        editMail.setOnFocusChangeListener(this);
        editPassword.setOnFocusChangeListener(this);
        textErrorPassword = findViewById(R.id.textErrorPassword);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textAddImage = findViewById(R.id.textAddImage);
        api = ApiUtils.getApi();
    }

    private void saveEntry(){
        SharedPreferences sPref = getSharedPreferences("saveEntry", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("entrySave", true);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textClickEntry:
                Intent intent1 = new Intent(RegestrationClass.this, EntryActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.buttonCreateAccount:
                postRequestForRegistration();
                break;
            case R.id.imageProfile:
                editName.setBackgroundResource(R.drawable.edit_text_background);
                editSurname.setBackgroundResource(R.drawable.edit_text_background);
                editMail.setBackgroundResource(R.drawable.edit_text_background);
                editPassword.setBackgroundResource(R.drawable.edit_text_background);
                roundedImageView.setBackgroundResource(R.drawable.background_image_add);
                textErrorEmail.setVisibility(View.GONE);
                textErrorPassword.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
                }
        }

    private void postRequestForRegistration() {
        name = editName.getText().toString();
        surname = editSurname.getText().toString();
        mail = editMail.getText().toString();
        password = editPassword.getText().toString();
        if (encodedImage == null){
            roundedImageView.setBackgroundResource(R.drawable.background_image_add_error);
        }
        if (name.isEmpty()){
            editName.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (surname.isEmpty()){
            editSurname.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (mail.isEmpty()){
            editMail.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (password.isEmpty()){
            editPassword.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (password.length()<8){
            editPassword.setBackgroundResource(R.drawable.edit_text_background_error);
            textErrorPassword.setVisibility(View.VISIBLE);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            editMail.setBackgroundResource(R.drawable.edit_text_background_error);
            textErrorEmail.setVisibility(View.VISIBLE);
        }
        if (!(name.isEmpty()) && !(surname.isEmpty()) && !(mail.isEmpty()) && !(password.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(mail).matches() && password.length()>=8 && encodedImage != null){
            loading(true);
            signUp();
//            api.savePost(mail, name, surname, password).enqueue(new Callback<PostRegistration>() {
//
//                @Override
//                public void onResponse(Call<PostRegistration> call, Response<PostRegistration> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(RegestrationClass.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegestrationClass.this, MainMenuActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        if (response.code() == 400){
//                            textErrorEmail.setVisibility(View.VISIBLE);
//                            textErrorEmail.setText(R.string.text_error_email_double);
//                        }else{
//                            Toast.makeText(RegestrationClass.this, "Ошибка: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//                @Override
//                public void onFailure(Call<PostRegistration> call, Throwable t) {
//                    Toast.makeText(RegestrationClass.this, "Возникла ошибка, возможны проблемы с серовером", Toast.LENGTH_SHORT).show();
//
//                }
//            });
        }
    }

    private void signUp(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, name);
        user.put(Constants.KEY_LAST_NAME, surname);
        user.put(Constants.KEY_EMAIL, mail);
        user.put(Constants.KEY_PASSWORD, password);
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, name);
                    preferenceManager.putString(Constants.KEY_LAST_NAME, surname);
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    saveEntry();
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            roundedImageView.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void loading(Boolean isLoading){
        if (isLoading){
            buttonCreate.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            buttonCreate.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch(view.getId()){
            case R.id.editRegName:
                editName.setBackgroundResource(R.drawable.edit_text_background);
                editSurname.setBackgroundResource(R.drawable.edit_text_background);
                editMail.setBackgroundResource(R.drawable.edit_text_background);
                editPassword.setBackgroundResource(R.drawable.edit_text_background);
                roundedImageView.setBackgroundResource(R.drawable.background_image_add);
                textErrorEmail.setVisibility(View.GONE);
                textErrorPassword.setVisibility(View.GONE);
            case R.id.editRegSurname:
                editName.setBackgroundResource(R.drawable.edit_text_background);
                editSurname.setBackgroundResource(R.drawable.edit_text_background);
                editMail.setBackgroundResource(R.drawable.edit_text_background);
                editPassword.setBackgroundResource(R.drawable.edit_text_background);
                roundedImageView.setBackgroundResource(R.drawable.background_image_add);
                textErrorEmail.setVisibility(View.GONE);
                textErrorPassword.setVisibility(View.GONE);
            case R.id.editRegMail:
                editName.setBackgroundResource(R.drawable.edit_text_background);
                editSurname.setBackgroundResource(R.drawable.edit_text_background);
                editMail.setBackgroundResource(R.drawable.edit_text_background);
                editPassword.setBackgroundResource(R.drawable.edit_text_background);
                roundedImageView.setBackgroundResource(R.drawable.background_image_add);
                textErrorEmail.setVisibility(View.GONE);
                textErrorPassword.setVisibility(View.GONE);
            case R.id.editRegPas:
                editName.setBackgroundResource(R.drawable.edit_text_background);
                editSurname.setBackgroundResource(R.drawable.edit_text_background);
                editMail.setBackgroundResource(R.drawable.edit_text_background);
                editPassword.setBackgroundResource(R.drawable.edit_text_background);
                roundedImageView.setBackgroundResource(R.drawable.background_image_add);
                textErrorEmail.setVisibility(View.GONE);
                textErrorPassword.setVisibility(View.GONE);
        }
    }
}