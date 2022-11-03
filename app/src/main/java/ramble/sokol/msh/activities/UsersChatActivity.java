package ramble.sokol.msh.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import ramble.sokol.msh.R;
import ramble.sokol.msh.intrerfaces.UserListener;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PreferenceManager;
import ramble.sokol.msh.models.UserChatAdapter;
import ramble.sokol.msh.models.UserForChat;

public class UsersChatActivity extends AppCompatActivity implements View.OnClickListener, UserListener {

    private ProgressBar progressBar;
    private PreferenceManager preferenceManager;
    private TextView textErrorMessageUsers;
    private RecyclerView recyclerView;
    private ImageButton imageButtonBackSelectUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_chat);
        init();
    }

    private void init(){
        progressBar = findViewById(R.id.progressSelectUsers);
        textErrorMessageUsers = findViewById(R.id.textErrorMessageUsers);
        recyclerView = findViewById(R.id.recyclerSelectUsers);
        imageButtonBackSelectUsers = findViewById(R.id.imageButtonBackSelectUsers);
        imageButtonBackSelectUsers.setOnClickListener(this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null){
                        List<UserForChat> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            UserForChat userForChat = new UserForChat();
                            userForChat.setFirstName(queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME));
                            userForChat.setLastName(queryDocumentSnapshot.getString(Constants.KEY_LAST_NAME));
                            userForChat.setImage(queryDocumentSnapshot.getString(Constants.KEY_IMAGE));
                            userForChat.setEmail(queryDocumentSnapshot.getString(Constants.KEY_EMAIL));
                            userForChat.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKENS));
                            userForChat.setId(queryDocumentSnapshot.getId());
                            users.add(userForChat);
                        }
                        if (users.size() > 0){
                            UserChatAdapter userChatAdapter = new UserChatAdapter(users, this);
                            recyclerView.setAdapter(userChatAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage(){
        textErrorMessageUsers.setText(String.format("%s", "Нет доступных пользователей"));
        textErrorMessageUsers.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonBackSelectUsers:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onUserClicked(UserForChat user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}