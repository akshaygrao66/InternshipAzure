package akshay.shoppingapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.FirebaseAuth.*;

public class HomePage extends AppCompatActivity{

    String pvercode,mode="customer";
    FirebaseFirestore fstore;
    RadioGroup rg;
    RadioButton sel;
    PhoneAuthCredential pauthcreddential;
    PhoneAuthProvider.ForceResendingToken mtoken;
    View v;
    boolean wrongnumber=false,pverified=false;
    FirebaseAuth mauth,lauth;
    Dialog myDialog,pDialog,fDialog;
    FirebaseUser u;
    ProgressBar pB;
    TextView sv,pstatus,hstatus,fgtstatus;
    Button regbtn,rbtn,pcode,fbtn,lbtn,fsend;
    EditText rfname,rmname,rlname,rdob,rpass,rcpass,rphone,rmail,vcode;
    EditText lname,lpass;
    EditText fmail;
    PasswordChecker pchecker;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        rbtn=this.findViewById(R.id.rbtn);
        fbtn=this.findViewById(R.id.fbtn);
        v=this.getCurrentFocus();
        rg=findViewById(R.id.mode);
        lbtn=findViewById(R.id.lbtn);
        lname=findViewById(R.id.lname);
        lpass=findViewById(R.id.lpass);
        hstatus=findViewById(R.id.hstatus);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sel=findViewById(i);
                if(sel.getId()==R.id.admin)
                {
                    mode="admin";
                    rbtn.setVisibility(View.GONE);
                    fbtn.setVisibility(View.GONE);
                }
                else if(sel.getId()==R.id.customer)
                {
                    mode="customer";
                    rbtn.setVisibility(View.VISIBLE);
                    fbtn.setVisibility(View.VISIBLE);
                }
            }
        });

        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("customer"))          //This is a customer login
                {
                    if(TextUtils.isEmpty(lname.getText().toString()))
                    {
                        lname.setError("Customer EmailId cannot be empty");
                    }
                    else if(TextUtils.isEmpty(lpass.getText().toString()))
                    {
                        lpass.setError("Customer Password cannot be empty");
                    }
                    else
                    {
                        mauth=FirebaseAuth.getInstance();
                            mauth.signInWithEmailAndPassword(lname.getText().toString(), lpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user=mauth.getCurrentUser();
                                        if(user.isEmailVerified()) {
                                            Intent i1 = new Intent(HomePage.this, CustomerHome.class);
                                            startActivity(i1);
                                            finish();
                                        }
                                        else
                                        {
                                            hstatus.setVisibility(View.VISIBLE);
                                            hstatus.setText("Verify the email account by clicking the verfication sent before logging in");
                                        }
                                    } else {
                                        hstatus.setVisibility(View.VISIBLE);
                                        hstatus.setText("Wrong Password or EmailID");
                                        hstatus.setTextColor(Color.RED);
                                        try
                                        {
                                            throw task.getException();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthInvalidUserException invalidEmail)
                                        {
                                            Toast.makeText(HomePage.this, "Invalid EmailID", Toast.LENGTH_SHORT).show();

                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                        {
                                            Toast.makeText(HomePage.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(HomePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    }
                }
                else if(mode.equals("admin"))        //This is a admin login
                {
                    if(TextUtils.isEmpty(lname.getText().toString()))
                    {
                        lname.setError("Admin EmailId cannot be empty");
                    }
                    else if(TextUtils.isEmpty(lpass.getText().toString()))
                    {
                        lpass.setError("Admin Password cannot be empty");
                    }
                    else
                    {
                        if(lname.getText().toString().equals("admin@gmail.com")&&lpass.getText().toString().equals("admin"))
                        {
                            Intent i2=new Intent(HomePage.this,AdminHome.class);
                            startActivity(i2);
                            finish();
                        }
                        else
                        {
                            hstatus.setVisibility(View.VISIBLE);
                            hstatus.setText("Wrong Password or EmailID");
                            Toast.makeText(HomePage.this, "Wrong EmailId or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth=FirebaseAuth.getInstance();
                callForgotDialog();

                //mauth.createUserWithEmailAndPassword()
                //mauth.sendPasswordResetEmail()
            }
        });
        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginDialog();
            }
        });

    }
    private void callForgotDialog()
    {
        fDialog=new Dialog(this);
        fDialog.setContentView(R.layout.forgotpasswordpage);
        fDialog.setCancelable(true);
        fmail=fDialog.findViewById(R.id.fmail);
        fsend=fDialog.findViewById(R.id.fsend);
        fgtstatus=fDialog.findViewById(R.id.fgtstatus);
        fDialog.show();

        fsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(fmail.getText().toString()))
                {
                    fmail.setError("Email cannot be empty");
                }
                else
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(fmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(HomePage.this, "Reset Link emailed to: "+fmail.getText().toString(), Toast.LENGTH_SHORT).show();
                                fDialog.dismiss();
                            }
                            else
                            {
                                fgtstatus.setText("EmailID doesn't exist");
                            }
                        }
                    });
                }
            }
        });

    }
    private void callLoginDialog()
    {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.register);
        myDialog.setCancelable(true);
        regbtn = (Button) myDialog.findViewById(R.id.rsubmit);

        rmail = (EditText) myDialog.findViewById(R.id.remail);
        rpass = (EditText) myDialog.findViewById(R.id.rpass);
        rcpass=myDialog.findViewById(R.id.rcpass);
        rfname=myDialog.findViewById(R.id.rfname);
        rphone=myDialog.findViewById(R.id.rphone);
        rmname=myDialog.findViewById(R.id.rmname);
        rlname=myDialog.findViewById(R.id.rlname);
        rdob=myDialog.findViewById(R.id.rdob);
        myDialog.show();
        pB = (ProgressBar) myDialog.findViewById(R.id.progressBar);
        sv = (TextView) myDialog.findViewById(R.id.password_strength);

        pchecker=new PasswordChecker(this,v,pB,sv);
        rpass.addTextChangedListener(pchecker);
        regbtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(rfname.getText().toString()))
                {
                  rfname.setError("USername cannot be empty!");
                }
                else if(TextUtils.isEmpty(rpass.getText().toString()))
                {
                    rpass.setError("Password cannot be empty");
                }
                else if(TextUtils.isEmpty(rcpass.getText().toString()))
                {
                    rcpass.setError("Confirm Password cannot be empty");
                }
                else if(TextUtils.isEmpty(rphone.getText().toString()))
                {
                    rphone.setError("Phone number cannot be empty");
                }
                else if(TextUtils.isEmpty(rmail.getText().toString()))
                {
                    rmail.setError("Email cannot be empty");
                }
                else if(TextUtils.isEmpty(rmname.getText().toString()))
                {
                    rmname.setError("Middle Name cannot be empty");
                }
                else if(TextUtils.isEmpty(rlname.getText().toString()))
                {
                    rlname.setError("Last Name cannot be empty");
                }
                else if(TextUtils.isEmpty(rdob.getText().toString()))
                {
                    rdob.setError("DOB cannot be empty");
                }
                else
                {
                    if(!(Patterns.EMAIL_ADDRESS.matcher(rmail.getText().toString()).matches()))
                    {
                        rmail.setError("Invalid EmailID");
                    }
                    else if(!(rpass.getText().toString().equals(rcpass.getText().toString())))
                    {
                        rcpass.setError("Passwords won't match");
                    }
                    else
                    {
                        mauth= FirebaseAuth.getInstance();
                        verifyphone(rphone.getText().toString());
                            /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference("Message");
                            myref.setValue("Hello world!");*/
                    }
                }
            }
        });
    }
    private void verifyphone(final String phone)
    {
        final String ph=phone;
        pDialog=new Dialog(this);
        pDialog.setContentView(R.layout.phoneverification);
        pDialog.setCancelable(true);
        pcode=pDialog.findViewById(R.id.pcodebtn);
        vcode=pDialog.findViewById(R.id.vcode);
        pstatus=pDialog.findViewById(R.id.pstatus);
        pDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 30, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(HomePage.this, "Verfied Phone", Toast.LENGTH_SHORT).show();
                signincredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(HomePage.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(HomePage.this, "SMS Quota exceeded.This is an internal error.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(HomePage.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(HomePage.this, "Verification Code sent to the phone number", Toast.LENGTH_SHORT).show();
                pstatus.setText("Verification Code has been sent to "+ph+".Please check phone and enter code to continue.");
                pvercode=s;
                mtoken=forceResendingToken;
            }
        });
        pcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(vcode.getText().toString())) {
                    vcode.setError("Verification Code cannot empty");
                } else if (vcode.getText().toString().length() != 6) {
                    vcode.setError("Verification code format is wrong");
                }
                else
                    {
                        pauthcreddential=PhoneAuthProvider.getCredential(pvercode,vcode.getText().toString());
                        signincredentials(pauthcreddential);
                    }
            }
        });

    }
    public void signincredentials(PhoneAuthCredential pcredential)
    {
        mauth.signInWithCredential(pcredential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pverified=true;
                    user = task.getResult().getUser();

                    Toast.makeText(HomePage.this, "Phone number verification sucessful", Toast.LENGTH_SHORT).show();
                    verifyemail();
                } else {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        pstatus.setText("Verification was unsuccessful.You entered wrong code!");
                        vcode.setError("Wrong Verification code was entered.");
                        Toast.makeText(HomePage.this, "Pverified="+vcode.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void verifyemail()
    {
        pDialog.dismiss();
        mauth.signOut();
        lauth=FirebaseAuth.getInstance();
        Toast.makeText(this, "Processing email verification", Toast.LENGTH_SHORT).show();
        lauth.createUserWithEmailAndPassword(rmail.getText().toString(), rpass.getText().toString()).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = lauth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("Jane Q. User")
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(HomePage.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                writedatabase(user);
                                Toast.makeText(HomePage.this, "We have sent you an email verification.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Toast.makeText(HomePage.this, "Registration sucessful", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    // if user enters wrong email.
                    catch (FirebaseAuthWeakPasswordException weakPassword)
                    {
                        rpass.setError("Password too weak");

                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                    {
                        rmail.setError("EmailId not valid");

                    }
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        rpass.setError("Already Exists");

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(HomePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //rmail.setError("Enter a correct mailID we will be sending a verification mail.");
                    //Toast.makeText(HomePage.this, "Wrong EmailID"+rmail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void writedatabase(FirebaseUser us)
    {
        u=us;
        fstore=FirebaseFirestore.getInstance();
        CollectionReference cref=fstore.collection("userauths");
        DocumentReference dref=cref.document("MailUidPairs");

        Map<String,Object> m=new HashMap<>();
        m.put(rmail.getText().toString(),user.getUid());

        dref.set(m, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomePage.this, "Auth Written to DB", Toast.LENGTH_SHORT).show();
                writeuserinfo(u);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this, "Cannot write userauth to database.Registration partially successful", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void writeuserinfo(FirebaseUser u)
    {
        fstore=FirebaseFirestore.getInstance();
        CollectionReference cref=fstore.collection("userinfo");
        DocumentReference dref=cref.document(u.getUid());

        Map<String,Object> m=new HashMap<>();
        m.put("EMailID",rmail.getText().toString());
        m.put("FirstName",rfname.getText().toString());
        m.put("MiddleName",rmname.getText().toString());
        m.put("LastName",rlname.getText().toString());
        m.put("PhoneNumber",rphone.getText().toString());
        m.put("DOB",rdob.getText().toString());

        dref.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomePage.this, "Registration successful", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this, "Cannot write userinfo.Registration partially successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
