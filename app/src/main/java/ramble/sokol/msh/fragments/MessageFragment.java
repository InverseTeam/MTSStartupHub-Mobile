package ramble.sokol.msh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ramble.sokol.msh.R;
import ramble.sokol.msh.activities.UsersChatActivity;

public class MessageFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton addUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        addUser = getActivity().findViewById(R.id.buttonAddUsers);
        addUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddUsers:
                startActivity(new Intent(getActivity(), UsersChatActivity.class));
                break;
        }
    }
}