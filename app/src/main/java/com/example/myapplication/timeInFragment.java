package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class timeInFragment extends Fragment {

    private static TextView boxIn;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timein_fragment, container, false);
        TextView textIn = v.findViewById(R.id.resultIn);
        EventBus.getDefault().register(this);

        return v;
    }

    @Subscribe
    public void onMyEvent(MainActivity.SendTimeIn sendTimeIn){
        String text = sendTimeIn.getText();

        boxIn = getActivity().findViewById(R.id.resultIn);
        boxIn.setText(text);
    }

    @Subscribe
    public void onVerifyIn(timein.VerifyIn verifyIn){
        String text = verifyIn.getText();

        boxIn = getActivity().findViewById(R.id.resultIn);
        boxIn.setTextColor(Color.GREEN);
        boxIn.setText(text);
    }

    @Subscribe
    public void onVerifyInHacker(timein.VerifyInHacker verifyInHacker){
        String text = verifyInHacker.getText();

        boxIn = getActivity().findViewById(R.id.resultIn);
        boxIn.setTextColor(Color.RED);
        boxIn.setText(text);
    }

}
