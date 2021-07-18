package akshay.shoppingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CustomerHome extends AppCompatActivity {

    ListView cprdctlist;
    DocumentReference dref;
    int count=0;
    Integer[] cproductimage={R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7};
    FirebaseFirestore ff;
    CollectionReference cref;
    ArrayList<Product> cproduct;
    Product p;
    Button signoutbtn;
    ImageButton cartview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        cprdctlist=findViewById(R.id.clist);
        cartview=findViewById(R.id.vcart);
        signoutbtn=findViewById(R.id.psignout);
        cproduct=new ArrayList<>();

        ff=FirebaseFirestore.getInstance();
        cref=ff.collection("Products");

        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String pdesc = document.getString("pdesc");
                        String pname = document.getString("pname");
                        String pprice = document.getString("pprice");
                        String pstock = document.getString("pstock");
                        Product cp=new Product(cproductimage[count++],pname,pdesc,pprice,pstock);
                        cproduct.add(cp);
                        Log.d("MyTag", cp.getPdesc() + " / " + cp.getPname() + " / " + cp.getPprice() + " / " + cp.getPquant());
                    }
                    CustomerProductAdapter ma = new CustomerProductAdapter(CustomerHome.this,cproduct);
                    cprdctlist.setAdapter(ma);
                }
            }
        });

        cartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ie=new Intent(CustomerHome.this,Showcart.class);
                startActivity(ie);
            }
        });

        signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(CustomerHome.this);

                ad.setMessage("Do you want to sign out and  exit application?") .setTitle("Signout Confirmation").setPositiveButton("Stay with us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CustomerHome.this, "You aren't signed out", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Signout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CustomerHome.this, "You were signed out!", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(CustomerHome.this, "You have signed out!", Toast.LENGTH_SHORT).show();
                        Intent out=new Intent(CustomerHome.this,HomePage.class);
                        finish();
                    }
                });
                AlertDialog alert;
                alert=ad.create();
                alert.show();
            }
        });
    }
}
