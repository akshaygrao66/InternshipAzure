package akshay.shoppingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread t=new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    sleep(3000);
                    Intent i1=new Intent(Welcome.this,HomePage.class);
                    startActivity(i1);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
