
package akshay.shoppingapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class Showcart extends AppCompatActivity {

    ListView cartlist;
    TextView tview;
    FirebaseFirestore ff;
    CollectionReference cref;
    DocumentReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcart);
        cartlist=findViewById(R.id.showcartlist);
        tview=findViewById(R.id.showstatusofcart);

        ff=FirebaseFirestore.getInstance();
        cref=ff.collection("Cart");
        dref=cref.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()) {
                        ArrayList<Product> arr;
                        Map<String, Object> mp=doc.getData();
                        arr= (ArrayList<Product>) (mp.get(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                        //arr = (ArrayList<Product>) (doc.get(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                        Log.d("MyTag", (String) FirebaseAuth.getInstance().getCurrentUser().getEmail()+arr.get(0).getcustomerquantity());
                        MyProductAdapter pa = new MyProductAdapter(Showcart.this, arr);
                        cartlist.setAdapter(pa);
                    }
                    else
                    {
                        tview.setVisibility(View.VISIBLE);
                        tview.setText("Cart is empty");
                    }
                }
            }
        });

    }
}
