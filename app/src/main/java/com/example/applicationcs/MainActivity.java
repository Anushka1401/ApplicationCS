package com.example.applicationcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText datetime;
    EditText empid, empname, temperature, dateofentry;
    Button done;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextView opname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();

        Button logout = findViewById(R.id.logoutBtn);
        done = findViewById(R.id.done);

        datetime = findViewById(R.id.time);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        datetime.setText(date);

        empid = findViewById(R.id.empid);
        empname = findViewById(R.id.empname);
        temperature = findViewById(R.id.temp);
        dateofentry = findViewById(R.id.time);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        new FancyGifDialog.Builder(MainActivity.this)
                .setTitle("QUICK CHECK") // You can also send title like R.string.from_resources
                .setMessage("Have you entered all details correctly?") // or pass like R.string.description_from_resources
                .setNegativeBtnText("NOT SURE") // or pass it like android.R.string.cancel
                .setPositiveBtnBackground(R.color.blue) // or pass it like R.color.positiveButton
                .setPositiveBtnText("YES, PROCEED") // or pass it like android.R.string.ok
                .setNegativeBtnBackground(R.color.grey) // or pass it like R.color.negativeButton
                .setGifResource(R.drawable.image)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        reference = rootNode.getReference(datetime.getText().toString());

                        String eid = empid.getText().toString();
                        String ename = empname.getText().toString();
                        String tem = temperature.getText().toString();
                        String dat = dateofentry.getText().toString();

                        HelperClass helperClass = new HelperClass(eid, ename, tem);
                        reference.child(eid).setValue(helperClass);

                        tableviewClass tableviewClass = new tableviewClass(dat,eid,ename,tem);
                        reference.child(eid).setValue(tableviewClass);

                        Toast.makeText(MainActivity.this,"Added to records",Toast.LENGTH_SHORT).show();
                        empid.setText("");
                        empname.setText("");
                        temperature.setText("");
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



        logout.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view) {
               FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(getApplicationContext(),Login.class));
               finish();
           }
        });
    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String current = user.getUid();//getting unique user id

        opname = findViewById(R.id.opname);
        db.collection("Users")
                .whereEqualTo("uId", current)//looks for the corresponding value with the field
                // in the database
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                opname.setText("Operator " + (CharSequence) document.get("FullName") + " is currently logged in");

                                // These values must exactly match the fields in the db

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}