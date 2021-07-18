package akshay.shoppingapplication;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ybs.passwordstrengthmeter.PasswordStrength;

public class PasswordChecker  implements TextWatcher {

    Activity a;
    View v;
    String txt;
    ProgressBar progressBar;
    TextView strengthView;
    public PasswordChecker(Activity a,View v,ProgressBar p,TextView tv) {
        progressBar=p;
        strengthView=tv;
        this.a = a;
        this.v=v;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    private void updatePasswordStrengthView(String password) {


        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }
        PasswordStrength str = PasswordStrength.calculateStrength(password);
        txt=str.getText(a).toString();
        strengthView.setText(txt);
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (txt.equals("Weak")) {
            progressBar.setProgress(25);
        } else if (txt.equals("Medium")) {
            progressBar.setProgress(50);
        } else if (txt.equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }
}
