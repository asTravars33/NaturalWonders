package com.wangjessica.jwlab07b;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.HashSet;

public class QuizFragment extends Fragment {
    View view;
    String excerpt;
    String question;
    String[] choices;
    TextView message;
    HashSet<String> correct;
    OnAnswerSubmitted submitHandler;
    OnBackClicked backHandler;
    String name;
    public static QuizFragment newInstance(String tag, String excerpt, String question, String[] choices, HashSet<String> correct, String name){
        QuizFragment fragment = new QuizFragment();
        fragment.name = name;
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        fragment.question = question;
        fragment.excerpt = excerpt;
        fragment.choices = choices;
        fragment.correct = correct;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Access layout components
        ConstraintLayout layout = view.findViewById(R.id.layout);
        layout.setBackgroundColor(getResources().getColor(getResources().getIdentifier(name, "color", "com.wangjessica.jwlab07b"), null));
        TextView excerptView = view.findViewById(R.id.excerpt_view);
        TextView questionView = view.findViewById(R.id.question_view);
        message = view.findViewById(R.id.message);
        RadioGroup group = view.findViewById(R.id.choices_group);
        // Set the corresponding content
        excerptView.setText(excerpt);
        questionView.setText(question);
        for(int i=0; i<group.getChildCount(); i++){
            RadioButton cur = (RadioButton) group.getChildAt(i);
            cur.setText(choices[i]);
        }
        // On click listener for Submit and Back
        Button submit = view.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submitted = "";
                for(int i=0; i<group.getChildCount(); i++){
                    RadioButton cur = (RadioButton) group.getChildAt(i);
                    if(cur.isChecked()){
                        submitted = cur.getText().toString();
                        break;
                    }
                }
                if(correct.contains(submitted)){
                    submitHandler.returnUpdate(getArguments().getString("tag", ""));
                }
                else{
                    message.setText("Incorrect, try again?");
                    message.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            message.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
            }
        });
        Button back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHandler.returnState();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        submitHandler = (OnAnswerSubmitted) context;
        backHandler = (OnBackClicked) context;
    }

    // Interfaces
    public interface OnAnswerSubmitted{
        void returnUpdate(String tag);
    }
    public interface OnBackClicked{
        void returnState();
    }
}
