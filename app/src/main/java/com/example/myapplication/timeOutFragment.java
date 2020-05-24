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

public class timeOutFragment extends Fragment {

    private static TextView boxOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timeout_fragment, container, false);
        TextView textOut = v.findViewById(R.id.resultOut);
        EventBus.getDefault().register(this);
        return v;

    }

    @Subscribe
    public void onMyEvent(MainActivity.SendTimeOut sendTimeOut){
        String text = sendTimeOut.getText();

        boxOut = getActivity().findViewById(R.id.resultOut);
        boxOut.setText(text);
    }

    @Subscribe
    public void onTimeOut(timeout.VerifyOut verifyOut){
        String text = verifyOut.getText();

        boxOut = getActivity().findViewById(R.id.resultOut);
        boxOut.setTextColor(Color.GREEN);
        boxOut.setText(text);
    }

    @Subscribe
    public void onTimeOutProblem(timeout.VerifyOutProblem verifyOutProblem){
        String text = verifyOutProblem.getText();

        boxOut = getActivity().findViewById(R.id.resultOut);
        boxOut.setTextColor(Color.RED);
        boxOut.setText(text);
    }

}
