package com.example.paramedic.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paramedic.Model.HospitalModel;
import com.example.paramedic.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.MyViewHolder> {
  Context context;
   List<HospitalModel> hospitalModelList ;

    public HospitalsAdapter(Context context, List<HospitalModel> hospitalModelList) {
        this.context = context;
        this.hospitalModelList = hospitalModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HospitalsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_hospitals,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.hospitalNameText.setText(hospitalModelList.get(position).getHospitalName());
        holder.addressText.setText(hospitalModelList.get(position).getAddress());
        holder.openText.setText(hospitalModelList.get(position).getHours());

        holder.callImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(context).withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_CALL);

                                intent.setData(Uri.parse("tel:" + hospitalModelList.get(position).getPhone()));
                                context.startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(context,"Permission Denied , Please Enable it to call from app.",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

                holder.navImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringTokenizer stringTokenizer = new StringTokenizer(hospitalModelList.get(position).getDirections());
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + stringTokenizer.nextToken() + "," + stringTokenizer.nextToken() + "&mode=l");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);

                    }
                });

    }

    @Override
    public int getItemCount() {
        return hospitalModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Unbinder unbinder;

        @BindView(R.id.LHOS_address_txt)
        TextView addressText;

        @BindView(R.id.LHOS_call_img)
        ImageView callImg;

        @BindView(R.id.LHOS_name_txt)
        TextView hospitalNameText;

        @BindView(R.id.LHOS_nav_img)
        ImageView navImg;

        @BindView(R.id.LHOS_openTime_txt)
        TextView openText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
