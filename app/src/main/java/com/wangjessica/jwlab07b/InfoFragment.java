package com.wangjessica.jwlab07b;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {
    View view;
    ImageView image;
    TextView description;
    OnFragmentClicked handler;
    String name;
    public static InfoFragment newInstance(String tag, String desc, String name){
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        args.putString("desc", desc);
        fragment.setArguments(args);
        fragment.name = name;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // On click listener for the overall fragment
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.showQuestion(getArguments().getString("tag", ""));
            }
        });
        // Layout components
        image = view.findViewById(R.id.image);
        description = view.findViewById(R.id.description);
        description.setText(getArguments().getString("desc", ""));
        ConstraintLayout layout = view.findViewById(R.id.layout);
        layout.setBackgroundColor(getResources().getColor(getResources().getIdentifier(name, "color", "com.wangjessica.jwlab07b"), null));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        handler = (OnFragmentClicked) context;
    }

    // Interfaces
    public interface OnFragmentClicked{
        void showQuestion(String tag);
    }
}
