package com.example.adder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
    protected void remove_data(Query query){
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
    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("contacts");
        //CODE TO ADD DATA
        list=new ArrayList<String[]>();
        //list.add(new String[]{"Ramesh Kinwar","it.mgr@nhidcl.com","Manager","IT","NHIDCL HQrs (New Delhi)","+91 9711837966","011-23461706","user"});
        list.add(new String[]{"Prabhat Kumar Singh","pksinghnhai.2011@nhidcl.com","Executive Director","Project","RO-Port Blair","+91 99785 25079"," ","user"});
        list.add(new String[]{"Mummadi Koteswar Reddy","kr.mummadi@nhidcl.com","Deputy General Manager","Project","RO-Port Blair","+91 79938 32377"," ","user"});
        list.add(new String[]{"Hitesh Kumar Jangid","hitesh.jangid@nhidcl.com","Deputy General Manager","Project","PMU- Mayabunder","+91 9650912129"," ","user"});
        list.add(new String[]{"Ashok Kumar Rolaniya","ashok.rolaniya@gov.in","General Manager","Project","PMU-Baratang","+91 9718532717"," ","user"});
        list.add(new String[]{"Sunil Kumar","sunil.kumar64@nhidcl.com","Executive Director","Project","RO-Itanagar","+91 98788 71064"," ","user"});
        list.add(new String[]{"Pranjal Pratim Borgohain","pranjal.borgohain@nhidcl.com","General Manager","LA","RO-Itanagar","+91 89688 15831"," ","user"});
        list.add(new String[]{"Pradeep Kr. Agrawal","dgmf.itanagar@nhidcl.com","Deputy General Manager","Finance","RO-Itanagar","+91 98715 94433"," ","user"});
        list.add(new String[]{"Col. Madhusudan Sharma","ms.sharma63@nhidcl.com","General Manager","Project","PMU-Basar","+91 93247 02396"," ","user"});
        list.add(new String[]{"Ankit Umrao","ankit.umrao@gov.in","Deputy General Manager","Project","PMU-Basar","+91 97938 15727"," ","user"});
        list.add(new String[]{"Arindam Handique","arindam.handique@nhidcl.com","General Manager","Project","PMU-Roing & Namsai","+91 85279 61970"," ","user"});
        list.add(new String[]{"Prabhakar Kumar","prabhakar.k@nhidcl.com","General Manager","Project","PMU-Ziro","+91 98993 88092"," ","user"});
        list.add(new String[]{"Pankaj Singh","edpghy@nhidcl.com","Executive Director","Project","RO-Guwahati","+91 89488 82300"," ","user"});
        list.add(new String[]{"Bivekanand Jha","biveka.nand@nhidcl.com","General Manager","LA & Coordination","RO-Guwahati","+91 90313 93999"," ","user"});
        list.add(new String[]{"Sudarsan Sahoo","sahoo.sudarsan@nhidcl.com","Deputy General Manager","Finance","RO-Guwahati","+91 7008826196"," ","user"});
        list.add(new String[]{"Binayak Kumar","binayak.kumar@gov.in","General Manager","Project","SO-Boko","+91 96544 35903"," ","user"});
        list.add(new String[]{"Raj Kishore Singh","singh.rajkishor@nhidcl.com","Deputy General Manager","Project","PMU-Dibrugarh","+91 83769 58604"," ","user"});
        list.add(new String[]{"Gaurang Sanjay Deoghare","gaurang.deoghare@nic.in","General Manager","Project","PMU-Silchar","+91 77578 04901"," ","user"});
        list.add(new String[]{"Devender Kumar","devender.kumar91@gov.in","General Manager","Project","PMU-Jorhat","+91 99901 56519"," ","user"});
        list.add(new String[]{"Ajay Kumar Singh","gmp-pfutsero@nhidcl.com","General Manager","Project","PMU-Diphu","+91 74560 61717"," ","user"});
        list.add(new String[]{"Amiyanshu","ami.yanshu@gov.in","General Manager","Project","PMU-Karimganj","+91 8920980848"," ","user"});
        list.add(new String[]{"Nawab Singh","gm.pmu-dhubri@nhidcl.com","General Manager","Project","PMU-Dhubri","+91 7060153337"," ","user"});
        list.add(new String[]{"Ajay Kumar Chikhale","gm.pmu-bongaigaon@nhidcl.com","General Manager","Project","PMU-Bongaigaon","+91 8591180451"," ","user"});
        list.add(new String[]{"Bibek Joishi","bivek.joishi@gov.in","General Manager","Project","PMU-Tezpur","+91 9678390761"," ","user"});
        list.add(new String[]{"Manoj Kumar","manoj.kmr74@nic.in","Executive Director","Project","RO-Jammu","+91 8002483384"," ","user"});
        list.add(new String[]{"Ghulam Mohammad Dar","gmohammad.dar@nhidcl.com","General Manager","LA & Coordination","RO-Jammu","+91 7006429617"," ","user"});
        list.add(new String[]{"John Vali Shaik","dgmjammu@nhidcl.com","Deputy General Manager","Project","RO-Jammu","+91 7095197197"," ","user"});
        list.add(new String[]{"Surajpal Singh Sangwan ","gmjammu@nhidcl.com","General Manager","Project","PMU-Akhnoor","+91 9971985589"," ","user"});
        list.add(new String[]{"Ajat Shatroo Jamwal","ajatshatroo.jamwal@nhidcl.com","Deputy General Manager","Project","SO-Chenani","+91 8082748606"," ","user"});
        list.add(new String[]{"Naginder Singh Gill","naginder.gill@nhidcl.com","General Manager","Project","PMU-Doda","+91 9663539977"," ","user"});
        list.add(new String[]{"Gursewak Singh Sangha","gs.sangha@nhidcl.com","General Manager","Project","PMU-Kishtwar","+91 7973879817"," ","user"});
        list.add(new String[]{"Nirman Kishan Jambhulkar","nirman.kishan@nhidcl.com","Executive Director","Project","RO-Ladakh","+919818888419"," ","user"});
        list.add(new String[]{"Om Kailash","om.kailash@nhidcl.com","Deputy General Manager","LA & Coordination","RO-Ladakh","+91 9622996438"," ","user"});
        list.add(new String[]{"Chandra Bhushan Prasad","madhukar.bhushan@nhidcl.com","General Manager","Project","PMU-Infra Leh","+91 9818881845"," ","user"});
        list.add(new String[]{"Col. Porje Arun Bhikaji","porje.bhikaji@nhidcl.com","General Manager","Project","PMU-Kargil","+91 9411135751"," ","user"});
        list.add(new String[]{"Virender Kumar Jakhar","ed_aizawl@nhidcl.com","Executive Director","Project","RO-Imphal","+91 9996016545"," ","user"});
        list.add(new String[]{"Sadanand Pandey","gmp.imphal@nhidcl.com","General Manager","Project","RO-Imphal","+91 8825387542"," ","user"});
        list.add(new String[]{"Sarangthem Dholendro Singh","sarangthem.singh@nhidcl.com","Deputy General Manager","LA & Coordination","RO-Imphal","+91 7005846325"," ","user"});
        list.add(new String[]{"Rafique Ahmed Choudhury","rafique.choudhury@nhidcl.com","Deputy General Manager","Project","RO-Imphal","+91 9854058025"," ","user"});
        list.add(new String[]{"Prabal Das","prabal.das@gov.in","Deputy General Manager","Project","PMU-Tengnoupal","+91 9233103027"," ","user"});
        list.add(new String[]{"Sushil Kumar Rai","sushil.rai@nhidcl.com","General Manager","Project","PMU-Tamenlong","+91 8210059981"," ","user"});
        list.add(new String[]{"Lt. Col. Sonal Chandra","sonalchandra.533w@gov.in","General Manager","Project","PMU-Noney","+91 9582912234"," ","user"});
        list.add(new String[]{"Shankar Bhowmik","enbshankar19@nhidcl.com","General Manager","Project","PMU-Senapati","+91 9436463331"," ","user"});
        list.add(new String[]{"Ajay Kumar Rajak","dgmp.ccp@nhidcl.com","General Manager","Project","PMU-Churachandpur","+91 9402779317"," ","user"});
        list.add(new String[]{"Ajay Kumar Rajak","dgmp.ccp@nhidcl.com","General Manager","Project","PMU-Ukhrul","+91 9402779317"," ","user"});
        list.add(new String[]{"Pankaj Singh","edpghy@nhidcl.com","Executive Director","Project","RO-Shillong","+91 89488 82300"," ","user"});
        list.add(new String[]{"Mahavir Singh Rawat","mahavirrawat.356h@gov.in","General Manager","LA & Coordination","RO-Shillong","+91 9410511033"," ","user"});
        list.add(new String[]{"Kapil Singh","kapil.singh92@nic.in","Deputy General Manager","Project","RO-Shillong","+91 9993459225"," ","user"});
        list.add(new String[]{"Biswajit Dutta","biswajit.dutta67@nhidcl.com","General Manager","Project","PMU-Bagmara","+91 9862699016"," ","user"});
        list.add(new String[]{"Kunal Barua","kunal.barua@nhidcl.com","General Manager","Project","PMU-Tura","+91 8336961084"," ","user"});
        list.add(new String[]{"Ashish Shukla","shukla.ashish82@nhidcl.com","General Manager","Project","PMU-Mylliem","+91 8808879490"," ","user"});
        list.add(new String[]{"Virender Kumar Jakhar","ed_aizawl@nhidcl.com","Executive Director","Project","RO-Aizawl","+91 9996016545"," ","user"});
        list.add(new String[]{"Paleti Brahmanandam","p.Brahmanandam@nhidcl.com","General Manager","LA & Coordination","RO-Aizawl","+91 9889971777"," ","user"});
        list.add(new String[]{"Rahul Jajoria","rahul.jajoria@gov.in","Deputy General Manager","Project","RO-Aizawl","+91 8989896357"," ","user"});
        list.add(new String[]{"Debasis Roy","debasis.roy61@nhidcl.com","Deputy General Manager","Finance","RO-Aizawl","+91 7603056561"," ","user"});
        list.add(new String[]{"Binod Kumar Shrivastava","binodk.srivastava@nhidcl.com","General Manager","Project","PMU-Seling","+91 9810121754"," ","user"});
        list.add(new String[]{"Pranab Sinha","p.sinha59@nhidcl.com","General Manager","Project","PMU-Kolasib","+91 9435072039"," ","user"});
        list.add(new String[]{"Shiv Gulam Dwivedy","shivgulam.dwivedy@nhidcl.com","General Manager","Project","PMU-Lunglei","+91 9869456092"," ","user"});
        list.add(new String[]{"Parmesh Chandra Gujar","pc.gujar@nhidcl.com","General Manager","Project","PMU-Lawngtlai","+91 7880577443"," ","user"});
        list.add(new String[]{"Amrendra Narayan Singh","a.narayansingh@nhidcl.com","Executive Director","Project","RO-Kohima","+91 9162423600"," ","user"});
        list.add(new String[]{"Nchumbemo","nchumbemo.ncs@gov.in","General Manager","LA & Coordination","RO-Kohima","+91 8974632008"," ","user"});
        list.add(new String[]{"Vinakshi Dahat","VINAKSHI.dahat@nhidcl.com","Deputy General Manager","Project","RO-Kohima","+91 9366638808"," ","user"});
        list.add(new String[]{"PSVS Narayana Raju","psvs.raju@nhidcl.com","Deputy General Manager","Project","PMU-Pfutsero","+91 8247725743"," ","user"});
        list.add(new String[]{"Neeraj Khare","khare.neeraj@nhidcl.com","Deputy General Manager","Project","PMU-Mon","+91 9818140100"," ","user"});
        list.add(new String[]{"Gajendra Singh Rana","gagendra.singh@nhidcl.com","General Manager","Project","PMU-Jakhama","+91 8131873402"," ","user"});
        list.add(new String[]{"Ashok Kumar Saha, OSD","edt.tripura@nhidcl.com","Executive Director","Project","RO-Gangtok","+91 7980736548"," ","user"});
        list.add(new String[]{"Col. Rajesh Kumar","rajesh.kumar1162@nhidcl.com","General Manager","LA & Coordination","RO-Gangtok","+91 8607146292"," ","user"});
        list.add(new String[]{"Chhaya Rajput","chhaya.rajput@gov.in","Deputy General Manager","Project","RO-Gangtok","+91 7310676089"," ","user"});
        list.add(new String[]{"Apan Bhaumik","apan.bhoumik@nhidcl.com","General Manager","Project","PMU-Ranipool","+91 9436928388"," ","user"});
        list.add(new String[]{"Arbind Kumar Singh","arbindsingh60@nhidcl.com","General Manager","Project","PMU-Gyalshing","+91 9615539826"," ","user"});
        list.add(new String[]{"Udaya Singh","udaya.singh62@nhidcl.com","General Manager","Project","PMU-Kalimpong","+91 9304799149"," ","user"});
        list.add(new String[]{"Col. Kamal Ahmad Siddiqui","siddiqui.kamalahmad@nhidcl.com","General Manager","Project","PMU-Rhenock","+91 9452290525"," ","user"});
        list.add(new String[]{"Nirman Kishan Jambhulkar","nirman.kishan@nhidcl.com","Executive Director","Project","RO-Srinagar","+919818888419"," ","user"});
        list.add(new String[]{"Ghulam Mohammad Dar","gmohammad.dar@nhidcl.com","General Manager","LA & Coordination","RO-Srinagar","+91 7006429617"," ","user"});
        list.add(new String[]{"Vijay Kant Pandey","vijaykant.pandey@nhidcl.com","General Manager","Project","RO-Srinagar","+91 7781002577"," ","user"});
        list.add(new String[]{"Raghu Nath Sharma","raghu.nathsharma@nhidcl.com","General Manager","Project","PMU-Anantnag","+91 9419154648"," ","user"});
        list.add(new String[]{"Pradip Kumar","pradip.kr@nhidcl.com","Executive Director","Project","RO-Agartala","+91 9472823258"," ","user"});
        list.add(new String[]{"Vivekjit Singh","vivekjits.868p@gov.in","General Manager","LA & Coordination","RO-Agartala","+91 7087050514"," ","user"});
        list.add(new String[]{"Srinibas Mohapatra","Srinibas.Mohapatra@nhidcl.com","Deputy General Manager","Finance","RO-Agartala","+91 9437756839"," ","user"});
        list.add(new String[]{"Vidhyadhar Singh Meel ","vs.meel@nhidcl.com","General Manager","Project","PMU-Dharamnagar","+91 9799520779"," ","user"});
        list.add(new String[]{"Vijay Singh","vijay.s62@nhidcl.com","Deputy General Manager","Project","PMU-Kumarghat","+91 6378249056"," ","user"});
        list.add(new String[]{"Prabodh Kumar Sharma","gmtripura@nhidcl.com","General Manager","Project","PMU-Khowai","+91 9412054247"," ","user"});
        list.add(new String[]{"Mahesh Meena","meena.mahesh@gov.in","Deputy General Manager","Project","PMU-Udaipur","+91 7503486244"," ","user"});
        list.add(new String[]{"Bharat Asharam Patil","bharatkr.patil@nhidcl.com","Deputy General Manager","Project","SO-Teliamura","+91 9423230216"," ","user"});
        list.add(new String[]{"Sandeep Sudhera","ed-ddn@nhidcl.com","Executive Director","Project","RO-Dehradun","+91 9468520326"," ","user"});
        list.add(new String[]{"Sushil Verma","sushil.verma66@nhidcl.com","Deputy General Manager","Project","PMU-Chamoli","+91 9336385254"," ","user"});
        list.add(new String[]{"Lt. Col. Deepak Patil","gmputtarashi@nhidcl.com","General Manager","Project","PMU-Barkot","+91 9910992419"," ","user"});
        list.add(new String[]{"Md. Shadab Imam","shadab.imam@gov.in","Deputy General Manager","Project","PMU-Barkot","+91 9654948720"," ","user"});
        list.add(new String[]{"Madhusudan Sharma","ms.sharma63@nhidcl.com","General Manager","Project","PMU-Sonamarg","+91 9384702396"," ","user"});
        for(int i=0;i<list.size();i++){
            Log.e("list i",""+i);
            add_data(list.get(i));
        }
        //CODE TO ADD DATA END.....................................................

        //CODE TO REMOVE PARTICULAR DATA
        /*Query queryb = noteRef.whereIn("DESIGNATION", Arrays.asList("Deputy General Manager","General Manager")).whereNotEqualTo("ADDRESS","NHIDCL HQrs (New Delhi)");
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