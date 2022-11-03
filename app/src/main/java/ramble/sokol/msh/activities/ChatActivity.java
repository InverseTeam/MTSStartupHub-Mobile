package ramble.sokol.msh.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ramble.sokol.msh.R;
import ramble.sokol.msh.models.ChatAdapter;
import ramble.sokol.msh.models.ChatMessages;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PreferenceManager;
import ramble.sokol.msh.models.UserForChat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private UserForChat receiverUser;
    private TextView textNameChat;
    private ImageButton buttonBackChat;
    private List<ChatMessages> chatMessagesList;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private RecyclerView chatRecyclerView;
    private EditText editInputMessage;
    private FrameLayout layoutSend;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        listenMessage();
    }

    private void init(){
        textNameChat = findViewById(R.id.textNameChat);
        editInputMessage = findViewById(R.id.editInputMessage);
        buttonBackChat = findViewById(R.id.buttonBackChat);
        layoutSend = findViewById(R.id.layoutSend);
        layoutSend.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBarChat);
        buttonBackChat.setOnClickListener(this);
        loadReceiver();
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessagesList = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                getBitmapFromEncodedString(receiverUser.getImage()), chatMessagesList,
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();

    }

    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.getId());
        message.put(Constants.KEY_MESSAGE, editInputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        editInputMessage.setText(null);

    }

    private Bitmap getBitmapFromEncodedString(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count = chatMessagesList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessages chatMessages = new ChatMessages();
                    chatMessages.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessages.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessages.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessages.dataTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessages.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessagesList.add(chatMessages);
                }
              }
            Collections.sort(chatMessagesList, Comparator.comparing(obj -> obj.dateObject));
            if (count == 0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessagesList.size(), chatMessagesList.size());
                chatRecyclerView.smoothScrollToPosition(chatMessagesList.size() - 1);
            }
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    };

    private void listenMessage(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.getId())
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.getId())
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void loadReceiver(){
        receiverUser = (UserForChat) getIntent().getSerializableExtra(Constants.KEY_USER);
        textNameChat.setText(receiverUser.getFirstName() + " " + receiverUser.getLastName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackChat:
                onBackPressed();
                break;
            case R.id.layoutSend:
                layoutSend.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click_image));
                sendMessage();
                break;
        }
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);


    }
}