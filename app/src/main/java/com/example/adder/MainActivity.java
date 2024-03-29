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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference noteRef;

    public static String selected;
    public static final String Name="NAME";
    public static final String Email="EMAIL";
    public static final String Designation="DESIGNATION";
    public static final String Department="DEPARTMENT";
    public static final String Address="ADDRESS";
    public static final String Mobile="MOBILE";
    public static final String Landline="LANDLINE";
    public static final String Password="PASSWORD";
    public static final String Type="TYPE";
    public static final String Pin="PIN";
    ArrayList<String[]> list;
    List<String> d;

    Query querya;
    Map<String, List<String>> m;
    protected void add_data(String s[]) {
        try{
            querya = noteRef.whereEqualTo(MainActivity.Email, s[1]);
        }
        catch(Exception e){
            Log.e("add_data: ", e.toString());
        }

        Map<String, String> m = new HashMap<String, String>();
        querya.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    m.put(Name, s[0]);
                    m.put(Email, s[1]);
                    m.put(Designation, s[2]);
                    m.put(Department, s[3]);
                    m.put(Address, s[4]);
                    m.put(Mobile, s[5]);
                    m.put(Landline, s[6]);
                    Log.e("name", s[0]);
                    m.put(Type, s[7]);
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
   protected void editDesignationArray(List<String> s){
        try{
            m=new HashMap<String, java.util.List<String>>();
            m.put("desig",s);
        }
        catch(Exception e){
           Log.e("m.put",e.toString());
       }
        try {
            db.collection("desig").document("desi").set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.e("onSuccess: ","Designation array successfully updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("onFailure: ", e.toString());
                }
            });
        }
        catch (Exception e){
            Log.e("setm",e.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("contacts");
        list=new ArrayList<String[]>();
        list.add(new String[]{"Anil Kumar Gautam","gm.fin@nhidcl.com","General Manager","Finance","Corporate Office (New Delhi)","9650990140","","user"});

        for(int i=0;i<list.size();i++){
            Log.e("list i",""+i);
            add_data(list.get(i));
        }
        /*String[] designations = {"Managing Director","Director","Executive Director","General Manager","Deputy General Manager","Manager","Consultant","Deputy Manager","Assistant Manager","Junior Manager","Assistant Director - Rajbhasha"};
        try{
            d = Arrays.asList(designations);
        }
        catch(Exception e){
            Log.e("list assignment",e.toString());
        }
        editDesignationArray(d);*/
    }
}