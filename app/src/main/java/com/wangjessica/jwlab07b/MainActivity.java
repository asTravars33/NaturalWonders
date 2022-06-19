package com.wangjessica.jwlab07b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements InfoFragment.OnFragmentClicked, QuizFragment.OnAnswerSubmitted, QuizFragment.OnBackClicked{
    String[] descs;
    String[] excerpts;
    String[] questions;
    String[][] choices;
    HashMap<String, String> cols = new HashMap<String, String>();
    HashSet<String> correct = new HashSet<String>();
    String prefix = "aurora";
    int stage = 0;
    ArrayList<String> activeTags = new ArrayList<String>();
    ArrayList<String> previous = new ArrayList<String>();
    ImageView image;
    TextView finDesc;
    TextView aurora;
    TextView biobay;
    TextView sunhalo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Info arrays
        String[] correctArr = getResources().getStringArray(R.array.correct);
        for(String s: correctArr) correct.add(s);
        setDataValues(prefix);
        // Set all the fragments
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for(int i=0; i<descs.length; i++){
            activeTags.add(""+i);
            ft.add(getResources().getIdentifier("component"+(i+1), "id", "com.wangjessica.jwlab07b"), InfoFragment.newInstance(""+i, descs[i], prefix), ""+i);
        }
        ft.commit();
        // Layout components
        image = findViewById(R.id.scene);
        finDesc = findViewById(R.id.final_desc);
        aurora = findViewById(R.id.aurora);
        sunhalo = findViewById(R.id.sunhalo);
        biobay = findViewById(R.id.biobay);
    }

    private void setDataValues(String prefix) {
        descs = getResources().getStringArray(R.array.categories);
        excerpts = getResources().getStringArray(getResources().getIdentifier(prefix+"_excerpts", "array", "com.wangjessica.jwlab07b"));
        questions = getResources().getStringArray(getResources().getIdentifier(prefix+"_questions", "array", "com.wangjessica.jwlab07b"));
        choices = new String[][] {
                getResources().getStringArray(getResources().getIdentifier(prefix+"_opts_1", "array", "com.wangjessica.jwlab07b")),
                getResources().getStringArray(getResources().getIdentifier(prefix+"_opts_2", "array", "com.wangjessica.jwlab07b")),
                getResources().getStringArray(getResources().getIdentifier(prefix+"_opts_3", "array", "com.wangjessica.jwlab07b")),
                getResources().getStringArray(getResources().getIdentifier(prefix+"_opts_4", "array", "com.wangjessica.jwlab07b"))
        };
    }

    @Override
    public void showQuestion(String tag) {
        int id = Integer.parseInt(tag);
        System.out.println("Now showing question for: "+tag);
        // Start a new transaction
        Fragment targ = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Remove the clicked fragment
        if(targ!=null){
            ft.remove(targ);
            previous = new ArrayList<String>();
            for(String s: activeTags){
                previous.add(s);
            }
            activeTags.remove(tag);
        }
        // Show the detailed fragment for the clicked fragment
        Fragment toShow = QuizFragment.newInstance(tag+"big", excerpts[id], questions[id], choices[id], correct, prefix);
        ft.replace(R.id.big_container, toShow, tag+"big");
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.show(toShow);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void returnUpdate(String tag) {
        // Return to the main fragments
        Fragment toHide = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(toHide);
        if(activeTags.size()>0){
            ft.remove(getSupportFragmentManager().findFragmentByTag(activeTags.get(activeTags.size()-1)));
            // Shift the remaining fragments up
            ft.setCustomAnimations(R.animator.slide_up,R.animator.slide_down);
            for(int i=0; i<activeTags.size(); i++){
                ft.replace(getResources().getIdentifier("component"+(i+1), "id", "com.wangjessica.jwlab07b"), InfoFragment.newInstance(activeTags.get(i), descs[Integer.parseInt(activeTags.get(i))], prefix), activeTags.get(i));
            }
        }
        ft.commit();
        // Update the image
        System.out.println("Update the image");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stage++;
                image.setBackgroundResource(getResources().getIdentifier(prefix+stage, "drawable", "com.wangjessica.jwlab07b"));
                if(stage==4){
                    Handler nextHandler = new Handler();
                    nextHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            image.setBackgroundResource(getResources().getIdentifier(prefix+"5", "drawable", "com.wangjessica.jwlab07b"));
                            String overall="";
                            for(String s: excerpts) overall+=s;
                            finDesc.setText(overall);
                            finDesc.setVisibility(View.VISIBLE);
                        }
                    }, 800);
                }
            }
        }, 1000);
    }
    public void returnState(){
        getSupportFragmentManager().popBackStack();
        activeTags = previous;
        System.out.println(activeTags);
    }

    public void updateScene(View view) {
        System.out.println("Updating scene now..");
        finDesc.setVisibility(View.GONE);
        prefix = view.getTag().toString();
        switch(prefix){
            case "aurora":
                aurora.setTextColor(getResources().getColor(R.color.black, null));
                biobay.setTextColor(getResources().getColor(R.color.gray, null));
                sunhalo.setTextColor(getResources().getColor(R.color.gray, null));
                break;
            case "biobay":
                biobay.setTextColor(getResources().getColor(R.color.black, null));
                aurora.setTextColor(getResources().getColor(R.color.gray, null));
                sunhalo.setTextColor(getResources().getColor(R.color.gray, null));
                break;
            case "sunhalo":
                sunhalo.setTextColor(getResources().getColor(R.color.black, null));
                biobay.setTextColor(getResources().getColor(R.color.gray, null));
                aurora.setTextColor(getResources().getColor(R.color.gray, null));
                break;
        }
        stage = 0;
        setDataValues(prefix);
        image.setBackgroundResource(R.drawable.aurora0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        activeTags = new ArrayList<String>();
        for(int i=0; i<descs.length; i++){
            activeTags.add(""+i);
            ft.replace(getResources().getIdentifier("component"+(i+1), "id", "com.wangjessica.jwlab07b"), InfoFragment.newInstance(""+i, descs[i], prefix), ""+i);
        }
        ft.commit();
    }
}