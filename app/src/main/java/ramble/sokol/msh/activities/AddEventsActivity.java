package ramble.sokol.msh.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringJoiner;

import ramble.sokol.msh.R;
import ramble.sokol.msh.intrerfaces.Api;
import ramble.sokol.msh.models.ApiUtils;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PostEvents;
import ramble.sokol.msh.models.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventsActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener{

    private EditText editDate, editArea, editDescription, editTasks, editParticipant, editEquipment, editTime, editDop;
    private  String date, area, description, tasks, participant, equipment, time, dop;
    private Calendar calendar = Calendar.getInstance();
    final int YEAR = calendar.get(Calendar.YEAR);
    final int MONTH = calendar.get(Calendar.MONTH);
    final int DAY = calendar.get(Calendar.DAY_OF_MONTH);
    final int HOURS = calendar.get(Calendar.HOUR_OF_DAY);
    final int MINUTE = calendar.get(Calendar.MINUTE);
    private ImageButton buttonBack;
    private TimePickerDialog timePickerDialog;
    private MaterialButton buttonEventsSubmit;
    private Api api;
    private ProgressBar progressBar;
    private ImageView imageAddBannerEvents;
    private String encodedImage;
    private TextView textAddImageEvents;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        init();
    }

    private void init(){
        textAddImageEvents = findViewById(R.id.textAddImageEvents);
        preferenceManager = new PreferenceManager(getApplicationContext());
        imageAddBannerEvents = findViewById(R.id.imageAddBannerEvents);
        imageAddBannerEvents.setOnClickListener(this);
        editDate = findViewById(R.id.editDateDescription);
        editDate.setOnFocusChangeListener(this);
        buttonBack = findViewById(R.id.imageButtonBackAppEvents);
        buttonBack.setOnClickListener(this);
        editTime = findViewById(R.id.editTimeDescription);
        editTime.setOnFocusChangeListener(this);
        editArea = findViewById(R.id.editEventsArea);
        editDescription = findViewById(R.id.editEventsDescription);
        editTasks = findViewById(R.id.editTasksDescription);
        editParticipant = findViewById(R.id.editParticipantsDescription);
        editEquipment = findViewById(R.id.editEquipmentDescription);
        editDop = findViewById(R.id.editDopinfoDescription);
        buttonEventsSubmit = findViewById(R.id.buttonEventsSubmit);
        buttonEventsSubmit.setOnClickListener(this);
        api = ApiUtils.getApi();
        editArea.setOnFocusChangeListener(this);
        progressBar = findViewById(R.id.progressEventsAdd);
    }

    private void clickSubmit(){
        area = editArea.getText().toString();
        description = editDescription.getText().toString();
        tasks = editTasks.getText().toString();
        participant = editParticipant.getText().toString();
        equipment = editEquipment.getText().toString();
        date = editDate.getText().toString();
        time = editTime.getText().toString();
        dop = editDop.getText().toString();
        if (encodedImage == null){
            imageAddBannerEvents.setBackgroundResource(R.drawable.image_background_error);
        }
        if (area.isEmpty()){
            editArea.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (description.isEmpty()){
            editDescription.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (tasks.isEmpty()){
            editTasks.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (participant.isEmpty()){
            editParticipant.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (equipment.isEmpty()){
            editEquipment.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (date.isEmpty()){
            editDate.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (time.isEmpty()){
            editTime.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (dop.isEmpty()){
            editDop.setBackgroundResource(R.drawable.edit_text_background_error);
        }
        if (!area.isEmpty() && !description.isEmpty() && !tasks.isEmpty() && !participant.isEmpty() &&
            !equipment.isEmpty() && !date.isEmpty() && !time.isEmpty() && !dop.isEmpty() && encodedImage != null){
            postRequestForEvents();
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            buttonEventsSubmit.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            buttonEventsSubmit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void postRequestForEvents(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> event = new HashMap<>();
        event.put(Constants.KEY_AREA, area);
        event.put(Constants.KEY_DESCRIPTION, description);
        event.put(Constants.KEY_TASKS, tasks);
        event.put(Constants.KEY_PARTICIPANT, participant);
        event.put(Constants.KEY_EQUIPMENT, equipment);
        event.put(Constants.KEY_DATE, date);
        event.put(Constants.KEY_TIME, time);
        event.put(Constants.KEY_DOP, dop);
        event.put(Constants.KEY_IMAGE_TITLE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_EVENTS)
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putString(Constants.KEY_EVENT_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_AREA, area);
                    preferenceManager.putString(Constants.KEY_DESCRIPTION, description);
                    preferenceManager.putString(Constants.KEY_TASKS, tasks);
                    preferenceManager.putString(Constants.KEY_PARTICIPANT, participant);
                    preferenceManager.putString(Constants.KEY_EQUIPMENT, equipment);
                    preferenceManager.putString(Constants.KEY_DATE, date);
                    preferenceManager.putString(Constants.KEY_TIME, time);
                    preferenceManager.putString(Constants.KEY_DOP, dop);
                    preferenceManager.putString(Constants.KEY_IMAGE_TITLE, encodedImage);
                    Toast.makeText(getApplicationContext(), "Регистрация мероприятия прошла успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });


//        api.postEvents(name, area, description, tasks, equipment, participant, date, time, dop)
//                .enqueue(new Callback<PostEvents>() {
//                    @Override
//                    public void onResponse(Call<PostEvents> call, Response<PostEvents> response) {
//                        Toast.makeText(AddEventsActivity.this, String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostEvents> call, Throwable t) {
//                        Toast.makeText(AddEventsActivity.this, String.valueOf(t), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.editDateDescription:
                editArea.setBackgroundResource(R.drawable.edit_text_background);
                editDate.setBackgroundResource(R.drawable.edit_text_background);
                editTime.setBackgroundResource(R.drawable.edit_text_background);
                editDescription.setBackgroundResource(R.drawable.edit_text_background);
                editTasks.setBackgroundResource(R.drawable.edit_text_background);
                editParticipant.setBackgroundResource(R.drawable.edit_text_background);
                editEquipment.setBackgroundResource(R.drawable.edit_text_background);
                editDop.setBackgroundResource(R.drawable.edit_text_background);
                imageAddBannerEvents.setBackgroundResource(R.drawable.image_background);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month+=1;
                        String date = day+"."+month+"."+year;
                        editDate.setText(date);
                    }
                }, YEAR, MONTH, DAY);
                datePickerDialog.show();
                break;
            case R.id.editTimeDescription:
                editArea.setBackgroundResource(R.drawable.edit_text_background);
                editDate.setBackgroundResource(R.drawable.edit_text_background);
                editTime.setBackgroundResource(R.drawable.edit_text_background);
                editDescription.setBackgroundResource(R.drawable.edit_text_background);
                editTasks.setBackgroundResource(R.drawable.edit_text_background);
                editParticipant.setBackgroundResource(R.drawable.edit_text_background);
                editEquipment.setBackgroundResource(R.drawable.edit_text_background);
                editDop.setBackgroundResource(R.drawable.edit_text_background);
                imageAddBannerEvents.setBackgroundResource(R.drawable.image_background);
                timePickerDialog = new TimePickerDialog(AddEventsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        StringBuilder sb = new StringBuilder("");
                        if (hour >= 0 && hour < 10){
                            sb.append("0"+hour);
                        }else{
                            sb.append(hour);
                        }
                        if (minutes >= 0 && minutes < 10){
                            sb.append(":0"+minutes);
                        }else {
                            sb.append(":"+minutes);
                        }
                        editTime.setText(sb);
                    }
                }, 0, 0, true);
                timePickerDialog.show();
                break;
            case R.id.editEventsArea:
            case R.id.editEventsDescription:
            case R.id.editEquipmentDescription:
            case R.id.editTasksDescription:
            case R.id.editParticipantsDescription:
            case R.id.editDopinfoDescription:
                editArea.setBackgroundResource(R.drawable.edit_text_background);
                editDate.setBackgroundResource(R.drawable.edit_text_background);
                editTime.setBackgroundResource(R.drawable.edit_text_background);
                editDescription.setBackgroundResource(R.drawable.edit_text_background);
                editTasks.setBackgroundResource(R.drawable.edit_text_background);
                editParticipant.setBackgroundResource(R.drawable.edit_text_background);
                editEquipment.setBackgroundResource(R.drawable.edit_text_background);
                editDop.setBackgroundResource(R.drawable.edit_text_background);
                imageAddBannerEvents.setBackgroundResource(R.drawable.image_background);
                break;
        }
    }

    //private EditText editDate, editArea, editDescription, editTasks, editParticipant, editEquipment, editTime, editDop;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageAddBannerEvents.setImageBitmap(bitmap);
                            textAddImageEvents.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonBackAppEvents:
                onBackPressed();
                break;
            case R.id.buttonEventsSubmit:
                clickSubmit();
                break;
            case R.id.imageAddBannerEvents:
                editArea.setBackgroundResource(R.drawable.edit_text_background);
                editDate.setBackgroundResource(R.drawable.edit_text_background);
                editTime.setBackgroundResource(R.drawable.edit_text_background);
                editDescription.setBackgroundResource(R.drawable.edit_text_background);
                editTasks.setBackgroundResource(R.drawable.edit_text_background);
                editParticipant.setBackgroundResource(R.drawable.edit_text_background);
                editEquipment.setBackgroundResource(R.drawable.edit_text_background);
                editDop.setBackgroundResource(R.drawable.edit_text_background);
                imageAddBannerEvents.setBackgroundResource(R.drawable.image_background);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
                break;
        }

    }
}