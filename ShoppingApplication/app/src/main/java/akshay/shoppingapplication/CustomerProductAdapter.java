package akshay.shoppingapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerProductAdapter extends BaseAdapter {
    Context ccon;
    ImageView cprdctimg;
    FirebaseFirestore ff;
    CollectionReference cref;
    DocumentReference dref;
    FirebaseUser user;
    int u;
    LayoutInflater cprdctinflater;
    View cprdctview;
    TextView cprdctname,cprdctdesc,cprdctprice,cprdctquant,cpquantity;
    ViewHolder mvh=null;
    ArrayList<Product> cproduct;
    Product currentproduct;
    Button cpincq,cpdecq,cpaddtocart;
    @Override
    public int getCount() {
        return cproduct.size();
    }

    @Override
    public Object getItem(int i) {
        return cproduct.get(i).getPname();
    }

    public CustomerProductAdapter(Context con,ArrayList<Product> product) {
        this.cproduct=product;
        this.ccon = con;
        cprdctinflater=LayoutInflater.from(con);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
            cprdctview = cprdctinflater.inflate(R.layout.cplist, null);
            cpincq = cprdctview.findViewById(R.id.cpincq);
            cpquantity = cprdctview.findViewById(R.id.cpquantity);
            cpdecq = cprdctview.findViewById(R.id.cpdecq);
            cprdctname = cprdctview.findViewById(R.id.cpname);
            cprdctdesc = cprdctview.findViewById(R.id.cpdesc);
            cprdctimg = cprdctview.findViewById(R.id.cppic);
            cprdctprice = cprdctview.findViewById(R.id.cpprice);
            cprdctquant = cprdctview.findViewById(R.id.cpquant);
            cpaddtocart=cprdctview.findViewById(R.id.cpaddtocart);

            ViewHolder vh = new ViewHolder();
            vh.vprdctname = cprdctname;
            vh.vprdctdesc = cprdctdesc;
            vh.vprdctprice = cprdctprice;
            vh.vprdctquant = cprdctquant;
            vh.vpincq = cpincq;
            vh.vpdecq = cpdecq;
            vh.vpquantity = cpquantity;
            vh.vaddtocart=cpaddtocart;

            vh.vpincq.setTag(R.id.key1,vh);
            vh.vpincq.setTag(R.id.key2,i);
            vh.vpdecq.setTag(R.id.key1,vh);
            vh.vpdecq.setTag(R.id.key2,i);
            vh.vaddtocart.setTag(R.id.key1,vh);
            vh.vaddtocart.setTag(R.id.key2,i);

            //currentproduct = cproduct.get(i);
            cprdctimg.setImageResource(cproduct.get(i).getPimage());
            vh.vprdctname.append(cproduct.get(i).getPname().toString());
            vh.vprdctdesc.append(cproduct.get(i).getPdesc().toString());
            vh.vprdctprice.append(cproduct.get(i).getPprice().toString());
            vh.vprdctquant.append(cproduct.get(i).getPquant());

            vh.vpincq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    u=(int)view.getTag(R.id.key2);
                    currentproduct=cproduct.get(u);
                    currentproduct.inccustomerquantity();
                    if (currentproduct.getcustomerquantity() > Integer.parseInt(currentproduct.getPquant())) {
                        Toast.makeText(viewGroup.getContext(), "You cannot order more than stock", Toast.LENGTH_SHORT).show();
                        currentproduct.deccustomerquantity();
                    } else {
                        mvh=(ViewHolder)view.getTag(R.id.key1);
                        //Toast.makeText(viewGroup.getContext(), "You selected item" + cpincq.getTag(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(viewGroup.getContext(),  mvh.vprdctname.getText().toString()+","+u+","+mvh.vpquantity.getText().toString()+","+currentproduct.getcustomerquantity(), Toast.LENGTH_SHORT).show();
                        mvh.vpquantity.setText("" + currentproduct.getcustomerquantity());
                    }
                }
            });
            vh.vpdecq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    u=(int)view.getTag(R.id.key2);
                    currentproduct=cproduct.get(u);
                    if (currentproduct.deccustomerquantity()) {
                        mvh=(ViewHolder)view.getTag(R.id.key1);
                        mvh.vpquantity.setText("" + currentproduct.getcustomerquantity());
                    }
                    else
                    {
                        currentproduct.inccustomerquantity();
                    }
                }
            });

            vh.vaddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    u = (int) view.getTag(R.id.key2);
                    currentproduct = cproduct.get(u);
                    ff = FirebaseFirestore.getInstance();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    cref = ff.collection("Cart");
                    dref = cref.document(user.getUid());
                    mvh = (ViewHolder) view.getTag(R.id.key1);
                    if (Integer.parseInt(mvh.vpquantity.getText().toString()) != 0) {
                        Map<String, ArrayList<Product>> m = new HashMap<>();
                        ArrayList<Product> pq=new ArrayList<>();
                        pq.add(currentproduct);
                        m.put(user.getEmail(), pq);

                        dref.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(viewGroup.getContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(viewGroup.getContext(), "Failed to add to cart!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(viewGroup.getContext(), "Atleast order one item", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        return cprdctview;
    }
}
