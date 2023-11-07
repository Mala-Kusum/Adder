package com.example.adder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference noteRef;

    public static String selected;
    public static final String Name="NAME";
    public static final String Email="EMAIL";
    public static final String Designation="DESIGNATION";
    public static final String Department="DEPARTMENT";
    public static final String State="STATE";
    public static final String Address="ADDRESS";
    public static final String Mobile="MOBILE";
    public static final String Landline="LANDLINE";
    public static final String Password="PASSWORD";
    public static final String Type="TYPE";
    public static final String Pin="PIN";
    ArrayList<String[]> list;

    Query querya;
    protected void add_data(String s[]) {
        querya = noteRef.whereEqualTo(MainActivity.Email, s[1]);
        Map<String, String> m = new HashMap<String, String>();
        querya.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    m.put(Name, s[0]);
                    m.put(Email, s[1]);
                    m.put(Designation, s[2]);
                    m.put(Department, s[3]);
                    m.put(State, s[4]);
                    m.put(Address, s[5]);
                    m.put(Mobile, s[6]);
                    m.put(Landline, s[7]);
                    Log.e("name", s[0]);
                    m.put(Type, s[8]);
                    noteRef.document(s[1]).set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("add", "success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error adding", e.toString());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d("mainActivity ", "inquery");
                Log.e("tage", e.toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("contacts");
        list=new ArrayList<String[]>();
        list.add(new String[]{"Anita Malik","anita.malik@nhidcl.com","Legal Advisor","Finance","Delhi","Corporate Office (New Delhi)","","","user"});

        for(int i=0;i<list.size();i++){
            Log.e("list i",""+i);
            add_data(list.get(i));
        }
    }
}