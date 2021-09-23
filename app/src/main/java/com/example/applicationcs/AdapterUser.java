package com.example.applicationcs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyAdapter> {
    Context c;
    List<ModelUser>list;
    int size;
    LottieAnimationView lottie;

    public AdapterUser(Context c, List<ModelUser> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public AdapterUser.MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new MyAdapter(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.MyAdapter holder, int position) {
        ModelUser model=list.get(position);
        holder.profileImage.setImageResource(model.getProfileImage());
        holder.userName.setText(model.getUserName());
        holder.uId.setText(model.getuId());
        holder.msgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogview=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.cdialog_user,null);
                de.hdodenhof.circleimageview.CircleImageView dialogbox_userimg;
                TextView dialogbox_username;
                TextView dialogbox_uid;
                TextView dialogbox_userdesc;
                Button dialogbox_btn;
                dialogbox_userimg=dialogview.findViewById(R.id.dialogbox_userimg);
                dialogbox_username=dialogview.findViewById(R.id.dialogbox_username);
                dialogbox_uid=dialogview.findViewById(R.id.dialogbox_uid);
                dialogbox_userdesc=dialogview.findViewById(R.id.dialogbox_userdesc);
                dialogbox_btn=dialogview.findViewById(R.id.dialogbox_msgbtn);
                lottie=dialogview.findViewById(R.id.leafblow);

                dialogbox_userimg.setImageResource(model.getProfileImage());
                dialogbox_username.setText(model.getUserName());
                dialogbox_uid.setText(model.getuId());
                dialogbox_userdesc.setText("This user is registered as an Operator of ScanBuddy. Additonal Details:\n Name: "
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
            profileImage=itemView.findViewById(R.id.imageView_row);
            userName=itemView.findViewById(R.id.username);
            uId=itemView.findViewById(R.id.user_uid);
            msgbtn=itemView.findViewById(R.id.msgbtn);
        }
    }
}
