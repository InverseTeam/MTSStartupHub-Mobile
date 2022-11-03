package ramble.sokol.msh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ramble.sokol.msh.activities.AddEventsActivity;
import ramble.sokol.msh.R;
import ramble.sokol.msh.models.Constants;
import ramble.sokol.msh.models.Events;
import ramble.sokol.msh.models.EventsAdapter;
import ramble.sokol.msh.models.EventsGetAdapter;
import ramble.sokol.msh.models.PreferenceManager;
import ramble.sokol.msh.models.RecylerGetEvents;
import ramble.sokol.msh.models.UserChatAdapter;
import ramble.sokol.msh.models.UserForChat;

public class EventsFragment extends Fragment implements View.OnClickListener{

    private ImageView imageButtonAdd;
    private ProgressBar progressBar;
    private PreferenceManager preferenceManager;
    private TextView textErrorMessageUsers;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

        private void init(){
        imageButtonAdd = getActivity().findViewById(R.id.imageButtonAddEvents);
        imageButtonAdd.setOnClickListener(this);
        progressBar = getActivity().findViewById(R.id.progressEvents);
        textErrorMessageUsers = getActivity().findViewById(R.id.textErrorMessageEvents);
        recyclerView = getActivity().findViewById(R.id.recyclerEvents);
        preferenceManager = new PreferenceManager(getActivity());
        getEvents();
    }

    private void getEvents(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_EVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentEventsId = preferenceManager.getString(Constants.KEY_EVENT_ID);
                    if (task.isSuccessful() && task.getResult() != null){
                        List<Events> events = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            Events event = new Events();
                            event.setArea(queryDocumentSnapshot.getString(Constants.KEY_AREA));
                            event.setDescription(queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION));
                            event.setImage(queryDocumentSnapshot.getString(Constants.KEY_IMAGE_TITLE));
                            event.setTheme(queryDocumentSnapshot.getString(Constants.KEY_PARTICIPANT));
                            event.setDate(queryDocumentSnapshot.getString(Constants.KEY_DATE));
                            event.setTime(queryDocumentSnapshot.getString(Constants.KEY_TIME));
                            event.setTime(queryDocumentSnapshot.getString(Constants.KEY_TIME));
                            events.add(event);
                        }
                        if (events.size() > 0){
                            EventsAdapter eventsAdapter = new EventsAdapter(events);
                            recyclerView.setAdapter(eventsAdapter);
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
        textErrorMessageUsers.setText(String.format("%s", "Нет доступных мероприятий"));
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
            case R.id.imageButtonAddEvents:
                imageButtonAdd.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_click_image));
                Intent intent1 = new Intent(getActivity(), AddEventsActivity.class);
                startActivity(intent1);
                break;
        }
    }
}