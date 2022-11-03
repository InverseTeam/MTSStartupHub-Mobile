package ramble.sokol.msh.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

import ramble.sokol.msh.R;
import ramble.sokol.msh.activities.RegestrationClass;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.PreferenceManager;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private RoundedImageView imageProfileAvatar;
    private TextView textNameProfile, textOutProfile;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        imageProfileAvatar = getActivity().findViewById(R.id.imageProfileAvatar);
        textNameProfile = getActivity().findViewById(R.id.textNameProfile);
        preferenceManager = new PreferenceManager(getActivity());
        textOutProfile = getActivity().findViewById(R.id.textOutProfile);
        textOutProfile.setOnClickListener(this);
        loadUserDetails();
        getToken();
    }

    private void loadUserDetails(){
        textNameProfile.setText(preferenceManager.getString(Constants.KEY_FIRST_NAME) + " " + preferenceManager.getString(Constants.KEY_LAST_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageProfileAvatar.setImageBitmap(bitmap);
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void signOut(){
        Toast.makeText(getActivity(), "Выход...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKENS, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    notSaveEntry();
                    startActivity(new Intent(getActivity(), RegestrationClass.class));
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Не удалось выйти из системы", Toast.LENGTH_SHORT).show());
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
            );
        documentReference.update(Constants.KEY_FCM_TOKENS, token);
    }

    private void notSaveEntry(){
        SharedPreferences sPref = getActivity().getSharedPreferences("saveEntry", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("entrySave", false);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textOutProfile:
                signOut();
        }
    }
}