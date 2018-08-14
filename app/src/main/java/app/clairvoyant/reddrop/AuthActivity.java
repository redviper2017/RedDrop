package app.clairvoyant.reddrop;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner area, city, bloodGroup;
    private CircleImageView imageView;
    private static final int SELECT_PHOTO = 100;

    private static final String TAG = "Auth Activity";

    private String selectedCity, selectedArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        area = findViewById(R.id.spinner_area_name);
        city = findViewById(R.id.spinner_city_name);
        bloodGroup = findViewById(R.id.spinner_blood_group);

        imageView = findViewById(R.id.profile_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
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
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
