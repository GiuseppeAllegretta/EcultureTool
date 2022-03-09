package com.example.eculturetool;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ObjectActivity extends AppCompatActivity implements View.OnClickListener{

    private BottomSheetBehavior bottomSheetBehavior;
    private View expandMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_object);

        LinearLayout linearLayout = findViewById(R.id.lowerMenu);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        expandMenu = findViewById(R.id.expandMenu);
        expandMenu.setOnClickListener(this);


    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.expandMenu:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

        }

    }

}
