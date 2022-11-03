package ramble.sokol.msh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import ramble.sokol.msh.activities.UslugiMTSActivity;
import ramble.sokol.msh.R;

public class BonusFragment extends Fragment implements View.OnClickListener{

    CardView cardBonus, cardMyMero, cardSoob, cardFinance, cardUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bonus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        cardBonus = getActivity().findViewById(R.id.cardViewBonus);
        cardMyMero = getActivity().findViewById(R.id.cardViewMyMero);
        cardSoob = getActivity().findViewById(R.id.cardViewSoob);
        cardFinance = getActivity().findViewById(R.id.cardViewFinance);
        cardUp = getActivity().findViewById(R.id.cardViewUp);
        cardBonus.setOnClickListener(this);
        cardMyMero.setOnClickListener(this);
        cardSoob.setOnClickListener(this);
        cardFinance.setOnClickListener(this);
        cardUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardViewSoob:
                break;
            case R.id.cardViewBonus:
                break;
            case R.id.cardViewFinance:
                break;
            case R.id.cardViewMyMero:
                cardMyMero.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_click_image));
                Intent intent = new Intent(getActivity(), UslugiMTSActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.cardViewUp:
                break;
        }
    }
}