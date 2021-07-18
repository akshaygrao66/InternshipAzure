package akshay.shoppingapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminHome extends AppCompatActivity {

    String selmode="insert";
    CollectionReference cref;
    FirebaseFirestore ff;
    boolean ret=false,wait=true;
    LinearLayout ailayout,aulayout,adlayout,arlayout;
    Button aisubmit,ausubmit,adsubmit,arsubmitname,arsubmitall;
    EditText aipname,aupname,adpname,arpname,aipdesc,aupdesc,aipprice,aupprice,aipquant,aupquant;
    EditText pName;
    TextView aistatus,austatus,adstatus,arstatus;
    String pd,pp,pq;
    RadioGroup amode;
    DocumentReference dref;
    RadioButton sel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        aisubmit=findViewById(R.id.aisubmit);
        ausubmit=findViewById(R.id.ausubmit);
        adsubmit=findViewById(R.id.adsubmit);
        arsubmitname=findViewById(R.id.arsubmitpname);
        arsubmitall=findViewById(R.id.arsubmitall);
        aipname=findViewById(R.id.aipname);
        aupname=findViewById(R.id.aupname);
        adpname=findViewById(R.id.adpname);
        arpname=findViewById(R.id.arpname);
        aipdesc=findViewById(R.id.aipdesc);
        aupdesc=findViewById(R.id.aupdesc);
        aipprice=findViewById(R.id.aipprice);
        aupprice=findViewById(R.id.aupprice);
        aipquant=findViewById(R.id.aipquantity);
        aupquant=findViewById(R.id.aupquantity);
        aistatus=findViewById(R.id.aistatus);
        austatus=findViewById(R.id.austatus);
        adstatus=findViewById(R.id.adstatus);
        arstatus=findViewById(R.id.arstatus);
        amode=findViewById(R.id.amode);
        ailayout=findViewById(R.id.ilayout);
        aulayout=findViewById(R.id.aulayout);
        adlayout=findViewById(R.id.adlayout);
        arlayout=findViewById(R.id.arlayout);



        amode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sel=findViewById(i);
                if(sel==findViewById(R.id.ainsert))
                {
                    selmode="insert";
                    ailayout.setVisibility(View.VISIBLE);
                    aulayout.setVisibility(View.GONE);
                    adlayout.setVisibility(View.GONE);
                    arlayout.setVisibility(View.GONE);
                }
                else if(sel==findViewById(R.id.aupdate))
                {
                    selmode="update";
                    ailayout.setVisibility(View.GONE);
                    aulayout.setVisibility(View.VISIBLE);
                    adlayout.setVisibility(View.GONE);
                    arlayout.setVisibility(View.GONE);
                }
                else if(sel==findViewById(R.id.adelete))
                {
                    selmode="delete";
                    ailayout.setVisibility(View.GONE);
                    aulayout.setVisibility(View.GONE);
                    adlayout.setVisibility(View.VISIBLE);
                    arlayout.setVisibility(View.GONE);
                }
                else if(sel==findViewById(R.id.aretreive))
                {
                    selmode="retreive";
                    ailayout.setVisibility(View.GONE);
                    aulayout.setVisibility(View.GONE);
                    adlayout.setVisibility(View.GONE);
                    arlayout.setVisibility(View.VISIBLE);
                }
            }
        });

        aisubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetstatus();
                if(!(checkempty(aipname,true)||checkempty(aipdesc,true)||checkempty(aipprice,true)||checkempty(aipquant,true)))
                {
                    insertdb(aipname,aipdesc.getText().toString(),aipprice.getText().toString(),aipquant.getText().toString(),1);
                }
            }
        });

        ausubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetstatus();
                if(!(checkempty(aupname,true)))
                {
                    if((checkempty(aupdesc,false)&&checkempty(aupprice,false)&&checkempty(aupquant,false)))
                    {
                        austatus.setVisibility(View.VISIBLE);
                        austatus.setText("No changes specified");
                    }
                    else
                    {
                        insertdb(aupname,aupdesc.getText().toString(),aupprice.getText().toString(),aupquant.getText().toString(),2);
                    }
                }
            }
        });
        adsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetstatus();
                if(!checkempty(adpname,true))
                {
                    deletedb(adpname);
                }
            }
        });

        arsubmitall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetstatus();
                retreive();
            }
        });

    }
    public void retreive()
    {
        Intent ir=new Intent(AdminHome.this,ProductsAll.class);
        startActivity(ir);

    }
    public boolean checkempty(EditText name,boolean seterror)
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            if(seterror)
            {
                name.setError("Field cannot be empty!");
            }
            return true;
        }
        return  false;
    }

    public void deletedb(EditText pname)
    {
        this.pName=pname;
        ff=FirebaseFirestore.getInstance();
        cref=ff.collection("Products");
        dref=cref.document(pname.getText().toString());
            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists())
                        {
                            dref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            adstatus.setVisibility(View.VISIBLE);
                                            adstatus.setText("Product Deletion Successful");
                                            Toast.makeText(AdminHome.this, "Product deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            adstatus.setVisibility(View.VISIBLE);
                                            adstatus.setText("Product Deletion Failed");
                                            Toast.makeText(AdminHome.this, "Product deletion failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            pName.setError("Product Name doesn't exist");
                        }
                    } else {
                        pName.setError("Product Name doesn't exist");
                        Toast.makeText(AdminHome.this, "Internal Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    public void insertdb(EditText pname, String pdesc,String pprice,String pquant,int mode) //mode 1-insert 2-update 3-delete
    {
        this.pName=pname;
        pd=pdesc;
        pq=pquant;
        pp=pprice;
        ff=FirebaseFirestore.getInstance();
        cref=ff.collection("Products");
        dref=cref.document(pname.getText().toString());
        if(mode==1)
        {
            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            pName.setError("Product Name already exists");
                        } else {
                            Map<String,Object> m=new HashMap<>();
                            m.put("pname",pName.getText().toString());
                            m.put("pdesc",pd);
                            m.put("pprice",pp);
                            m.put("pstock",pq);


                            dref.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    aistatus.setVisibility(View.VISIBLE);
                                    aistatus.setText("Inserted Product Details");
                                    Toast.makeText(AdminHome.this, "Inserted Product Details", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    aistatus.setVisibility(View.VISIBLE);
                                    aistatus.setText("Couldn't insert Product details");
                                }
                            });
                        }
                    } else {
                        Map<String,Object> m=new HashMap<>();
                        m.put("pname",pName.getText().toString());
                        m.put("pdesc",pd);
                        m.put("pprice",pp);
                        m.put("pstock",pq);


                        dref.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                aistatus.setVisibility(View.VISIBLE);
                                aistatus.setText("Inserted Product Details");
                                Toast.makeText(AdminHome.this, "Inserted Product Details", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                aistatus.setVisibility(View.VISIBLE);
                                aistatus.setText("Couldn't insert Product details");
                            }
                        });
                        Toast.makeText(AdminHome.this, "Internal Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(mode==2)
        {
            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String,Object> m=new HashMap<>();
                            if(!pd.isEmpty())
                            {
                                m.put("pdesc",pd);
                            }
                            if(!pp.isEmpty())
                            {
                                m.put("pprice",pp);
                            }
                            if(!pq.isEmpty())
                            {
                                m.put("pstock",pq);
                            }
                            dref.update(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    austatus.setVisibility(View.VISIBLE);
                                    austatus.setText("Product Info Updated");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    austatus.setVisibility(View.VISIBLE);
                                    austatus.setText("Product Info not Updated");
                                }
                            });
                        } else {
                            pName.setError("Product Name doesn't exist");
                        }
                    } else {
                        pName.setError("Product Name doesn't exist");
                        Toast.makeText(AdminHome.this, "Internal Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void resetstatus()
    {
        aistatus.setVisibility(View.GONE);
        austatus.setVisibility(View.GONE);
        adstatus.setVisibility(View.GONE);
        arstatus.setVisibility(View.GONE);
    }
}

