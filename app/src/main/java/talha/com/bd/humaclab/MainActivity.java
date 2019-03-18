package talha.com.bd.humaclab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText userName, password;
    private ApiInterface apiInterface;
    private ConstraintLayout contextView;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private boolean isLacationEnabeled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);

        contextView  = findViewById(R.id.context_view);

        getLocationPermission();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://test.selliscope.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public void logIn(View view) {

        String name = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pass)){
            showSnackBar("Username and Password both are required!");
        }
        else {

            String AUTH = "Basic " + Base64.encodeToString((name+":"+pass).getBytes(), Base64.NO_WRAP);

            Call<LoginResponse> call = apiInterface.userLogin(AUTH,name,pass);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (!response.isSuccessful()){
                        showSnackBar(response.message());
                        return;

                    }
                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.isError()){
                        if (isLacationEnabeled){
                            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                            showSnackBar("Login Successful!");
                            startActivity(intent);
                            finish();
                        }
                        else {
                            getLocationPermission();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void showSnackBar(String msg) {

        Snackbar snackbar = Snackbar.make(contextView, msg, Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }



    private void getLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            isLacationEnabeled = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    isLacationEnabeled = true;
                }
            }

        }
    }
}
