package com.example.applicationcs;

import android.app.AlertDialog;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.MyAdapter> {
    Context c;
    List<ModelAdmin> list;
    int size;
    LottieAnimationView lottie;

    public AdapterAdmin(Context c, List<ModelAdmin> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public AdapterAdmin.MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_admin,parent,false);
        return new MyAdapter(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdmin.MyAdapter holder, int position) {
        ModelAdmin model=list.get(position);
        holder.profileImage.setImageResource(model.getProfileImage());
        holder.userName.setText(model.getUserName());
        holder.uId.setText(model.getuId());
        holder.msgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogview=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.cdialog_admin,null);
                de.hdodenhof.circleimageview.CircleImageView dialogbox_adminimg;
                TextView dialogbox_adminname;
                TextView dialogbox_aid;
                TextView dialogbox_admindesc;
                Button dialogbox_btn;
                dialogbox_adminimg=dialogview.findViewById(R.id.dialogbox_adminimg);
                dialogbox_adminname=dialogview.findViewById(R.id.dialogbox_adminname);
                dialogbox_aid=dialogview.findViewById(R.id.dialogbox_aid);
                dialogbox_admindesc=dialogview.findViewById(R.id.dialogbox_admindesc);
                dialogbox_btn=dialogview.findViewById(R.id.dialogbox_msgbtn2);
                lottie=dialogview.findViewById(R.id.leafadmin);

                dialogbox_adminimg.setImageResource(model.getProfileImage());
                dialogbox_adminname.setText(model.getUserName());
                dialogbox_aid.setText(model.getuId());
                dialogbox_admindesc.setText("This user is registered as an Administrator of ScanBuddy. Additonal Details:\n Name: "
                        +model.getUserName()+"\n Phone Number: "+model.getPhonenumber()
                        +"\n Email: "+model.getEmail());
                dialogbox_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vi) {
                        SmsManager mySmsManager = SmsManager.getDefault();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(vi.getRootView().getContext());
                        View smsview=LayoutInflater.from(vi.getRootView().getContext()).inflate(R.layout.popup_sms,null);
                        EditText smstext = smsview.findViewById(R.id.smstext);
                        TextView msgsent = smsview.findViewById(R.id.msgsent);
                        Button sendsms = smsview.findViewById(R.id.sendsms);
                        String phonenumber = model.getPhonenumber();

                        sendsms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (smstext.getText().toString().length()>0) {
                                    mySmsManager.sendTextMessage(phonenumber, null, smstext.getText().toString(), null, null);
                                    msgsent.setVisibility(View.VISIBLE);
                                    Toast.makeText(c, "Message sent", Toast.LENGTH_LONG).show();
                                    smstext.setText("");
                                    msgsent.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    Toast.makeText(c, "No message body", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        builder2.setView(smsview);
                        builder2.setCancelable(true);
                        builder2.show();
                    }
                });

                lottie.animate();
                lottie.setRepeatCount(Animation.INFINITE);
                builder.setView(dialogview);
                builder.setCancelable(true);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView profileImage;
        TextView userName;
        TextView uId;
        Button msgbtn;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.imageView_row2);
            userName=itemView.findViewById(R.id.adminname);
            uId=itemView.findViewById(R.id.admin_aid);
            msgbtn=itemView.findViewById(R.id.msgbtn2);
        }
    }
}