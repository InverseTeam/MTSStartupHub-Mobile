package ramble.sokol.msh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import ramble.sokol.msh.activities.ApplicationStartupActivity;
import ramble.sokol.msh.R;

public class StartupFragment extends Fragment implements View.OnClickListener{

    MaterialButton addStartup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_startup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
    
    private void init(){
        addStartup = getActivity().findViewById(R.id.buttonAddStartup);
        addStartup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonAddStartup:
                Intent intent = new Intent(getActivity(), ApplicationStartupActivity.class);
                startActivity(intent);
        }
    }
}