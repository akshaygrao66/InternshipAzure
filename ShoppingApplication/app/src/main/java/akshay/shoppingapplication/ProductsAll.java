package akshay.shoppingapplication;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductsAll extends AppCompatActivity {

    ListView prdctlist;
    int count=0;
    Integer[] productimage={R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7};
    FirebaseFirestore ff;
    CollectionReference cref;
    DocumentReference dref;
    ArrayList<Product> product;
    Product p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_all);
        prdctlist=findViewById(R.id.productlist);
        product=new ArrayList<>();

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
                        Product p=new Product(productimage[count++],pname,pdesc,pprice,pstock);
                        product.add(p);
                        Log.d("MyTag", p.getPdesc() + " / " + p.getPname() + " / " + p.getPprice() + " / " + p.getPquant());
                    }
                    MyProductAdapter ma = new MyProductAdapter(ProductsAll.this,product);
                    prdctlist.setAdapter(ma);
                }
            }
        });
    }
}
