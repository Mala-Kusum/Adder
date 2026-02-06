package com.example.adder;

import  androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
    ArrayList<String[]> removedList;

    Query querya;
    Map<String, List<String>> m;
    /*protected void remove_data(Query query){
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("document found to be deleted", document.getId());
                                noteRef.document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("document deleted", document.getId() + " => " + document.getData());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Error deleting document",  document.getId() + e.toString(), e);
                                                Log.e("Error deleting document",  document.getId() + e.toString());
                                            }
                                        });
                            }
                        } else {
                            Log.d("Error getting documents: ", String.valueOf(task.getException()));
                        }
                    }
                });
    }*/
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
                    try{
                        m.put(Name, s[0]);
                        m.put(Email, s[1]);
                        m.put(Designation, s[2]);
                        m.put(Department, s[3]);
                        m.put(Address, s[4]);
                        m.put(Mobile, s[5]);
                        m.put(Landline, s[6]);
                        Log.e("name", s[0]);
                        m.put(Type, s[7]);
                    }
                    catch(Exception e){
                        Log.e("Error before adding ", e.toString());
                    }
                    noteRef.document(s[1]).set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("added", s[1]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error adding", e.toString());
                            Log.d("error adding", s[1] +" "+e.toString());
                        }
                    });
                }
                /*else{
                    m.put(Name, s[0]);
                    m.put(Email, s[1]);
                    m.put(Designation, s[2]);
                    m.put(Department, s[3]);
                    m.put(Address, s[4]);
                    m.put(Mobile, s[5]);
                    m.put(Landline, s[6]);
                    Log.e("name", s[0]);
                    m.put(Type, s[7]);
                    noteRef.document(s[1]).set(m, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                }*/
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
    protected void createExcel(){
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Deleted Data");
        HSSFRow hssfRow0 = hssfSheet.createRow(0);
        int j=0;
        HSSFCell cell1 = hssfRow0.createCell(j++);
        cell1.setCellValue("Document ID");
        HSSFCell cell2 = hssfRow0.createCell(j++);
        cell2.setCellValue("ADDRESS");
        HSSFCell cell3 = hssfRow0.createCell(j++);
        cell3.setCellValue("DEPARTMENT");
        HSSFCell cell4 = hssfRow0.createCell(j++);
        cell4.setCellValue("DESIGNATION");
        HSSFCell cell5 = hssfRow0.createCell(j++);
        cell5.setCellValue("EMAIL");
        HSSFCell cell6 = hssfRow0.createCell(j++);
        cell6.setCellValue("LANDLINE");
        HSSFCell cell7 = hssfRow0.createCell(j++);
        cell7.setCellValue("MOBILE");
        HSSFCell cell8 = hssfRow0.createCell(j++);
        cell8.setCellValue("NAME");
        HSSFCell cell9 = hssfRow0.createCell(j++);
        cell9.setCellValue("TYPE");
        saveWorkBook(hssfWorkbook);
    }
    private void saveWorkBook(HSSFWorkbook hssfWorkbook){
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0); // internal storage

        File fileOutput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            //Objects.requireNonNull(storageVolume.getDirectory()).ge
            fileOutput = new File(Objects.requireNonNull(storageVolume.getDirectory()).getPath() +"/Download","DeletedDataExceptDirectorsAndMD"+String.valueOf(System.currentTimeMillis())+".xls");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            hssfWorkbook.close();
            Toast.makeText(this, "File Created Successfully", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "File Creation Failed", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }
    protected void deleteAllexceptDirectorsAndMD(){
        createExcel();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("contacts");
        //export excel
        //createExcel();
        //CODE TO ADD DATA
        //list=new ArrayList<String[]>();
        list.add(new String[]{"Ramesh Kinwar","it.mgr@nhidcl.com","Manager","IT","NHIDCL HQrs (New Delhi)","+91 9711837966","011-23461706","user"});
        list.add(new String[]{	"Dr. Krishan Kumar,"md@nhidcl.com","Managing Director","Office of MD","NHIDCL HQrs (New Delhi)"," ","011-26768901","user"});
        list.add(new String[]{	"Anshu Manish Khalkho,"dir@nhidcl.com","Director Admin & Finance","Admin & Finance","NHIDCL HQrs (New Delhi)"," ","011-26768913","user"	});
        list.add(new String[]{	"Amarendra Kumar,"technicaldirector.2@nhidcl.com","Director Technical", "Technical","NHIDCL HQrs (New Delhi)"," ","011-26768924","user"	});
        list.add(new String[]{	"U.J. Chamargore,"technicaldirector@nhidcl.com","Director Technical-II","Technical","NHIDCL HQrs (New Delhi)"," ","011-26798979","user"	});
        list.add(new String[]{	"Narendra Sharma,"technicaldirector.3@nhidcl.com","Director Technical-III","Technical","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Samar Bahadur Singh,"ed1@nhidcl.com","Executive Director","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768912","user"	});
        list.add(new String[]{	"Nitin Kumar Sharma,"nitin.sharma66@nhidcl.com","Executive Director","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768910","user"	});
        list.add(new String[]{	"Trivendra Kumar,"ed2@nhidcl.com","Executive Director","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768954","user"	});
        list.add(new String[]{	"Manoj Kumar,"manoj.kmr74@gov.in","Executive Director","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768911","user"	});
        list.add(new String[]{	"Sh Jagdeep Singh Tung,"ed6.hq@nhidcl.com","Executive Director","Technical","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Prakash Nevatia,"gmhr.hq@nhidcl.com","General Manager","HR","NHIDCL HQrs (New Delhi)"," ","011-26768926","user"	});
        list.add(new String[]{	"Ved Prakash Arya,"gmlegal.hq@nhidcl.com","General Manager","Legal","NHIDCL HQrs (New Delhi)"," ","011-26768933","user"	});
        list.add(new String[]{	"Anil Kumar Gautam,"gm.fin@nhidcl.com","General Manager","Finance","NHIDCL HQrs (New Delhi)"," ","011-26768925","user"	});
        list.add(new String[]{	"Ashok Kumar Dogra,"dogra.ak@nic.in","General Manager ","Land Acquisition & Coordination","NHIDCL HQrs (New Delhi)"," ","011-26768932","user"	});
        list.add(new String[]{	"Tajinder Singh Behal,"coord.hq@nhidcl.com","General Manager","Coordination","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Ashok Kumar Jha,"ak.jha196@nic.in","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768963","user"	});
        list.add(new String[]{	"Surendra Yadav,"surendra.yadav1962@nhidcl.com","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768958","user"	});
        list.add(new String[]{	"Mohammad Khalid,"md.khalid@nhidcl.com","General Manager","Technical","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Subodh Kumar,"gmtmeghalaya@nhidcl.com","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768959","user"	});
        list.add(new String[]{	"Mohammed Tajuddin,"gm.infra@nhidcl.com","General Manager ","Infrastructure","NHIDCL HQrs (New Delhi)"," ","011-26768907","user"	});
        list.add(new String[]{	"Ankush Mehta,"ankush.mehta@gov.in","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768956","user"	});
        list.add(new String[]{	"Ralceme S.,"ralceme.s@gov.in","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768909","user"	});
        list.add(new String[]{	"Lt Col Ashutosh Mishra,"mishra.ashutosh@nhidcl.com","General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768965","user"	});
        list.add(new String[]{	"Yogendra Mohan,"gmla.hq1@nhidcl.com","General Manager","Recruitment","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Mahesh Gupta,"dgm.fin@nhidcl.com","Deputy General Manager (Finance)","Finance","NHIDCL HQrs (New Delhi)"," ","011-26768927","user"	});
        list.add(new String[]{	"Vivek Jain,"vivekjain.367n@gov.in","Deputy General Manager ","HR & Admin.","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Jitendra Kumar Mishra,"jitendrakr.mishra@nhidcl.com","Deputy General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768964","user"	});
        list.add(new String[]{	"Ashish Gupta,"ashish.g19@gov.in","Deputy General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768908","user"	});
        list.add(new String[]{	"Naveen Kumar Jain,"dgmt.hq@nhidcl.com","Deputy General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768929","user"	});
        list.add(new String[]{	"Saurav Deo,"saurav.deo@gov.in","Deputy General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768957","user"	});
        list.add(new String[]{	"Ankit Loyal,"loyalak.rth@gov.in","Deputy General Manager","Technical","NHIDCL HQrs (New Delhi)"," ","011-26768967","user"	});
        list.add(new String[]{	"Bhaskar Mallick,"mallick.bhaskar65@nhidcl.com","Manager","Finance","NHIDCL HQrs (New Delhi)"," ","011-23461635","user"	});
        list.add(new String[]{	"Rakesh Kumar Jain,,"Manager","Technical","NHIDCL HQrs (New Delhi)"," "," ","user"	});" + list.add(new String[]{	"Kalyan Singh Verma,"dymgrrajbhasha@nhidcl.com","Assistant Director","Official Language","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Udayan Varoon,,"Dy. Manager","Technical","NHIDCL HQrs (New Delhi)"," "," ","user"	});" + "list.add(new String[]{	"Devendra Pratap Singh,"devendra.ps@nhidcl.com","Assistant Manager","HR","NHIDCL HQrs (New Delhi)"," ","011-23461690","user"	});
        list.add(new String[]{	"Sunil Kumar Singh,"jr.mgradmin@nhidcl.com","Junior Manager","HR","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Digvijay Singh,"digvijay.singh05@nhidcl.com","Junior Manager","HR","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Manoj Kumar Singh,"manoj.singh10@nhidcl.com","Junior Manager","HR","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Rajesh Kumar,"rajeshkumar.15@nhidcl.com","Junior Manager","Finance","NHIDCL HQrs (New Delhi)"," "," ","user"	});
        list.add(new String[]{	"Prasant Kumar Sahoo,"company.secy@nhidcl.com","Company Secretary"," ","NHIDCL HQrs (New Delhi)"," "," ","user"	});

        for(int i=0;i<list.size();i++){
            Log.e("list i",""+i);
            add_data(list.get(i));
        }
        //CODE TO ADD DATA END.....................................................

        //CODE TO REMOVE PARTICULAR DATA
        /*Query queryb = noteRef.whereIn("DESIGNATION", Arrays.asList("Deputy General Manager","General Manager")).whereNotEqualTo("ADDRESS","NHIDCL HQrs (New Delhi)");
        queryb.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }
                //Log.e("Taaag", String.valueOf(flag));
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        removedList.add(dc.getDocument().toObject(ColorSpace.Model.class));
                    }
                }
            }
        });
        remove_data(queryb);*/

        //CODE TO REMOVE PARTICULAR DATA END..................................................

        //CODE TO EDIT DESIGNATION ARRAY
        /*String[] designations = {"Managing Director","Director","Executive Director","General Manager","Deputy General Manager","Manager","Consultant","Deputy Manager","Assistant Manager","Junior Manager","Assistant Director - Rajbhasha"};
        try{
            d = Arrays.asList(designations);
        }
        catch(Exception e){
            Log.e("list assignment",e.toString());
        }
        editDesignationArray(d);*/
        //CODE TO EDIT DESIGNATION ARRAY END..............................................
    }
}