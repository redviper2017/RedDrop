package app.clairvoyant.reddrop;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner area, city, bloodGroup;

    private EditText name, email, password, mobileNumber;

    private CircleImageView imageView;

    private Button makeADonorButton;

    private static final int SELECT_PHOTO = 100;

    private static final String TAG = "Auth Activity";

    private String selectedCity, selectedArea, selectedBloodGroup;

    private StorageReference storageReference;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private Uri selectedImage = null;

    private TextView alreadyHaveAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        area = findViewById(R.id.spinner_area_name);
        city = findViewById(R.id.spinner_city_name);
        bloodGroup = findViewById(R.id.spinner_blood_group);

        imageView = findViewById(R.id.profile_image);

        name = findViewById(R.id.donor_name);
        email = findViewById(R.id.donor_email);
        password = findViewById(R.id.donor_password);
        mobileNumber = findViewById(R.id.donor_number);

        makeADonorButton = findViewById(R.id.btn_make_donor);

        alreadyHaveAccountText = findViewById(R.id.btn_takeToLoginScreen);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        firebaseAuth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        alreadyHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthActivity.this,LoginActivity.class));
                finish();
            }
        });

        makeADonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText())){
                    Toast.makeText(getApplicationContext(),"please enter your name to proceed.",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(email.getText())){
                    Toast.makeText(getApplicationContext(),"email field cannot be kept empty!.",Toast.LENGTH_SHORT).show();
                }
                if (!isEmailValid(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),"enter a valid email address!",Toast.LENGTH_SHORT).show();
                }
                if (!isPasswordValid(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"enter a valid password!",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(getApplicationContext(),"password field cannot be kept empty!",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(mobileNumber.getText())){
                    Toast.makeText(getApplicationContext(),"please provide your mobile number in order to proceed.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG,"selected image: "+selectedImage);
                    storageReference = FirebaseStorage.getInstance().getReference("User Images").child(name.getText().toString());
                    storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Upload done!!",Toast.LENGTH_SHORT).show();
                            String n = name.getText().toString();
                            final String e = email.getText().toString().trim();
                            final String p = password.getText().toString().trim();
                            String m = mobileNumber.getText().toString().trim();
                            final User user = new User(n,e,p,m,selectedArea,selectedCity,selectedBloodGroup,null);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int children = (int) dataSnapshot.getChildrenCount();
                                    final int currentChildSerial = children+1;
                                    String imgUrl = taskSnapshot.getMetadata().getPath();
                                    Log.d(TAG,"firebase image url: "+imgUrl);

                                    createAccount(e,p,currentChildSerial);

                                    databaseReference.child(String.valueOf(currentChildSerial)).setValue(user);
                                    databaseReference.child(String.valueOf(currentChildSerial)).child("image").setValue(imgUrl);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            startActivity(new Intent(AuthActivity.this,DonorHomeActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Registration failed!!",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        // area Spinner Drop down elements
        List<String> areas = new ArrayList<String>();
        areas.add("Area");
        areas.add("Banani");
        areas.add("Baridhara");
        areas.add("Bashundhara");
        areas.add("Dhanmondi");
        areas.add("Gulshan");
        areas.add("Mirpur");
        areas.add("Moghbazar");
        areas.add("Mohammadpur");
        areas.add("Motijheel");
        areas.add("Tejgaon");
        areas.add("Uttara");

        // city spinner Drop down elements
        List<String> cities = new ArrayList<String>();
        cities.add("City");
        cities.add("Dhaka");
        cities.add("Chittagong");
        cities.add("Khulna");
        cities.add("Sylhet");
        cities.add("Rajshahi");
        cities.add("Barisal");
        cities.add("Rangpur");
        cities.add("Mymensingh");

        // bloodGroup Spinner Drop down elements
        List<String> bloodGroups = new ArrayList<String>();
        bloodGroups.add("Blood Group");
        bloodGroups.add("A+");
        bloodGroups.add("B+");
        bloodGroups.add("O+");
        bloodGroups.add("AB+");
        bloodGroups.add("A-");
        bloodGroups.add("B-");
        bloodGroups.add("O-");
        bloodGroups.add("AB-");

        // Creating adapter for area spinner
        ArrayAdapter<String> areasDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas);

        // Creating adapter for city spinner
        ArrayAdapter<String> citiesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);

        // Creating adapter for bloodGroup spinner
        ArrayAdapter<String> bloodGroupsDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodGroups);

        areasDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citiesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bloodGroupsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        area.setAdapter(areasDataAdapter);

        city.setAdapter(citiesDataAdapter);

        bloodGroup.setAdapter(bloodGroupsDataAdapter);

        city.setOnItemSelectedListener(this);

        area.setOnItemSelectedListener(this);

        bloodGroup.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged in then go to Home Activity bypassing the Auth Activity
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(AuthActivity.this,DonorHomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    if (data != null) {
                        selectedImage = data.getData();
                    }
                    Log.d(TAG, "image is: "+selectedImage);
                    if(selectedImage !=null){
                        imageView.setImageURI(selectedImage);
                    }
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.getItemAtPosition(i);
        switch (adapterView.getId()){
            case R.id.spinner_city_name:
                if (adapterView.getItemAtPosition(i).equals("Dhaka") || adapterView.getItemAtPosition(i).equals("City")){
                    selectedCity = (String) adapterView.getItemAtPosition(i);
                    Log.d(TAG,"selected city is: "+selectedCity);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Sorry, we haven't started our service in this city yet.");
                    builder.setCancelable(true);
                    builder.setNeutralButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    builder.show();
                }
                break;
            case R.id.spinner_area_name:
                selectedArea = (String) adapterView.getItemAtPosition(i);
                Log.d(TAG,"selected area is: "+selectedArea);
                break;
            case R.id.spinner_blood_group:
                selectedBloodGroup = (String) adapterView.getItemAtPosition(i);
                Log.d(TAG,"selected blood group is: "+selectedBloodGroup);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Check password with minimum requirement here(it should be minimum 6 characters)
    public static boolean isPasswordValid(String password){
        return password.length() >= 6;
    }

    public void createAccount(String email, String password, final int currentChildSerial){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Congratulations! You are now a registered blood donor of Red Drop",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(AuthActivity.this,HomeActivity.class);
                            intent.putExtra("CURRENT_USER_SERIAL",currentChildSerial);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"Authentication failed!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
