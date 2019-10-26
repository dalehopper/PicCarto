package com.example.piccarto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignOnActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout Email_Section;
    private SignInButton Login_Button;
    private TextView Email;
    private GoogleApiClient googleApiClient;
    private static int REQ_Code = 9001;
    private static int WELCOME_DELAY = 2500;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signon);
        Email_Section = (LinearLayout)findViewById(R.id.email_section);
        Login_Button = (SignInButton)findViewById(R.id.login_button);
        Email = (TextView)findViewById(R.id.email);
        Login_Button.setOnClickListener(this);
        Email_Section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        signIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signIn() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        googleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(intent, REQ_Code);

    }
    private void handleResult(GoogleSignInResult result) {

        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            email = account.getEmail();
            Email.setText(email);
            updateUI(true);
        }
        else {
            updateUI(false);
        }
    }
    private void updateUI(boolean isLogIn) {
        if(isLogIn) {
            Email_Section.setVisibility(View.VISIBLE);
            Login_Button.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SignOnActivity.this, MainActivity.class);
                    intent.putExtra("email",email );
                    startActivity(intent);
                    finish();
                }
            }, WELCOME_DELAY);
        }
        else {
            Email_Section.setVisibility(View.GONE);
            Login_Button.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_Code){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
