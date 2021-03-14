package com.example.paramedic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class dashboard extends AppCompatActivity {

    @BindView(R.id.dashboard_hospitalList_Layout)
    LinearLayout hospitalListLayout;
    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        unbinder = ButterKnife.bind(this);


        hospitalListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this,HospitalListActivity.class));
            }
        });

    }
}