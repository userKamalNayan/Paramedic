package com.example.paramedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.paramedic.Adapter.HospitalsAdapter;
import com.example.paramedic.Common.Common;
import com.example.paramedic.Model.HospitalModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalListActivity extends AppCompatActivity {

    @BindView(R.id.HL_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.HL_recyclerView)
    RecyclerView recyclerView;

    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        unbinder = ButterKnife.bind(this);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //getSupportActionBar().setTitle("Hospitals")

        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
        List<HospitalModel> hospitalList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference(Common.HOSPITALS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        HospitalModel hospitalModel = dataSnapshot.getValue(HospitalModel.class);
                        hospitalModel.setKey(dataSnapshot.getKey());
                        hospitalList.add(hospitalModel);

                        System.out.println(hospitalModel.getHospitalName());
                    }
                    setDataToFields(hospitalList);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(HospitalListActivity.this, "No Hospitals Found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HospitalListActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataToFields(List<HospitalModel> hospitalList) {
        progressBar.setVisibility(View.GONE);
        System.out.println(hospitalList.size() + " is the number of total entries");
        if (hospitalList.size() == 0) {
            return;
        }


        HospitalsAdapter hospitalsAdapter = new HospitalsAdapter(HospitalListActivity.this, hospitalList);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(HospitalListActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(HospitalListActivity.this,new LinearLayoutManager(HospitalListActivity.this).getOrientation()));
        recyclerView.setAdapter(hospitalsAdapter);
    }
}