package com.example.applicationcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    ImageView gotoLogin2;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isAdminBox, isOperatorBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
        gotoLogin2 = findViewById(R.id.gotoLogin2);

        isAdminBox = findViewById(R.id.isAdmin);
        isOperatorBox = findViewById(R.id.isOperator);

        // check only one

        isOperatorBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdminBox.setChecked(false);
                }
            }
        });

        isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isOperatorBox.setChecked(false);
                }
            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FancyGifDialog.Builder(Register.this)
                        .setTitle("QUICK CHECK") // You can also send title like R.string.from_resources
                        .setMessage("Have you entered all details correctly?") // or pass like R.string.description_from_resources
                        .setNegativeBtnText("NOT SURE") // or pass it like android.R.string.cancel
                        .setPositiveBtnBackground(R.color.blue) // or pass it like R.color.positiveButton
                        .setPositiveBtnText("YES, PROCEED") // or pass it like android.R.string.ok
                        .setNegativeBtnBackground(R.color.grey) // or pass it like R.color.negativeButton
                        .setGifResource(R.drawable.register)   //Pass your Gif here
                        .isCancellable(true)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                checkField(fullName);
                                checkField(email);
                                checkField(password);
                                checkField(phone);

                                //checkbox validation
                                if (!(isAdminBox.isChecked() || isOperatorBox.isChecked())){
                                    Toast.makeText(Register.this, "Select the Account Type", Toast.LENGTH_SHORT).show();
                                    return;

                                }

                                if(valid){
                                    //start registration
                                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            FirebaseUser user = fAuth.getCurrentUser();
                                            String userid = user.getUid().toString();
                                            Toast.makeText(Register.this,"Account created",Toast.LENGTH_SHORT).show();

                                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                                            Map<String,Object> userInfo = new HashMap<>();
                                            userInfo.put("FullName",fullName.getText().toString());
                                            userInfo.put("UserEmail",email.getText().toString());
                                            userInfo.put("PhoneNumber",phone.getText().toString());
                                            userInfo.put("uId",userid);


                                            //specify if user is admin
                                            if(isAdminBox.isChecked()){
                                                userInfo.put("isAdmin","1");
                                            }
                                            if(isOperatorBox.isChecked()){
                                                userInfo.put("isOperator","1");
                                            }

                                            df.set(userInfo);

                                            if(isAdminBox.isChecked()) {
                                                Intent newadmin = new Intent(getApplicationContext(), Admin.class);
                                                startActivity(newadmin);
                                                finish();
                                            }

                                            if(isOperatorBox.isChecked()) {
                                                Intent newop = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(newop);
                                                finish();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                            }
                        })
                        .build();
            }
        });


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        gotoLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}