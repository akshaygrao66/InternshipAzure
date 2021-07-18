package akshay.shoppingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyProductAdapter extends BaseAdapter {

    Context con;
    ImageView prdctimg;

    LayoutInflater prdctinflater;
    View prdctview;
    TextView prdctname,prdctdesc,prdctprice,prdctquant;
    ArrayList<Product> product;
    Integer[] productimage={R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7};

    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int i) {
        return product.get(i).getPname();
    }

    public MyProductAdapter(Context con,ArrayList<Product> product) {
        this.product=product;
        this.con = con;
        prdctinflater=LayoutInflater.from(con);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        prdctview=prdctinflater.inflate(R.layout.plist,null);
        prdctname=prdctview.findViewById(R.id.pname);
        prdctdesc=prdctview.findViewById(R.id.pdesc);
        prdctimg=prdctview.findViewById(R.id.ppic);
        prdctprice=prdctview.findViewById(R.id.pprice);
        prdctquant=prdctview.findViewById(R.id.pquant);

        //prdctimg.setImageResource(product.get(i).getPimage());
        prdctimg.setImageResource(productimage[i]);
        prdctname.append(product.get(i).getPname().toString());
        prdctdesc.append(product.get(i).getPdesc().toString());
        prdctprice.append(product.get(i).getPprice().toString());
        prdctquant.append(product.get(i).getPquant());
        return prdctview;
    }
}
