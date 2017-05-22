package auribises.com.admin_appointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    @InjectView(R.id.editTextName)
    EditText eTxtName;

    @InjectView(R.id.editTextPhone)
    EditText eTxtPhone;

    @InjectView(R.id.editTextEmail)
    EditText eTxtEmail;

    @InjectView(R.id.editTextPurpose)
    EditText eTxtPurpose;

    @InjectView(R.id.editTextDate)
    EditText eTxtDate;

    @InjectView(R.id.editTextTime)
    EditText eTxtTime;

    @InjectView(R.id.editTextRoom)
    EditText eTxtRoom;

    @InjectView(R.id.radioButtonMale)
    RadioButton rbMale;

    @InjectView(R.id.radioButtonFemale)
    RadioButton rbFemale;

    ArrayAdapter<String> adapter;

    @InjectView(R.id.buttonAppointment)
    Button btnSubmit;

    auribises.com.admin_appointment.adminappointment adminappointment,rcvAdminappointment;

    ContentResolver resolver;

    boolean updateMode;

    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    DatePickerDialog datePickerDialog;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            eTxtDate.setText(i+"/"+(i1+1)+"/"+i2);

        }
    };

    TimePickerDialog timePickerDialog;
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            eTxtTime.setText(i+" : "+i1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        eTxtName = (EditText)findViewById(R.id.editTextName);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        adminappointment = new adminappointment();

        rbMale.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);

        resolver = getContentResolver();

        requestQueue = Volley.newRequestQueue(this);

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyadminappointment");


        if(updateMode){
            rcvAdminappointment = (auribises.com.admin_appointment.adminappointment) rcv.getSerializableExtra("keyadminappointment");
            Log.i("test",adminappointment.toString());
            eTxtName.setText(rcvAdminappointment.getName());
            eTxtPhone.setText(rcvAdminappointment.getPhone());
            eTxtEmail.setText(rcvAdminappointment.getEmail());
            eTxtPurpose.setText(rcvAdminappointment.getPurpose());
            eTxtDate.setText(rcvAdminappointment.getDate());
            eTxtTime.setText(rcvAdminappointment.getTime());
            eTxtRoom.setText(rcvAdminappointment.getRoom());

            if(rcvAdminappointment.getGender().equals("Male")){
                rbMale.setChecked(true);
            }else{
                rbFemale.setChecked(true);
            }

            btnSubmit.setText("Update");
        }
    }

    boolean isNetworkConected(){

        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i("insertIntoCloud",adminappointment.toString() );
        return (networkInfo!=null && networkInfo.isConnected());

    }

    public void clickHandler(View view){
        if(view.getId() == R.id.buttonAppointment){
            adminappointment.setName(eTxtName.getText().toString().trim());
            adminappointment.setPhone(eTxtPhone.getText().toString().trim());
            adminappointment.setEmail(eTxtEmail.getText().toString().trim());
            adminappointment.setPurpose(eTxtPurpose.getText().toString().trim());
            adminappointment.setDate(eTxtDate.getText().toString().trim());
            adminappointment.setTime(eTxtTime.getText().toString().trim());
            adminappointment.setRoom(eTxtRoom.getText().toString().trim());



            //insertIntoDB();

            if(validateFields()) {
                if (isNetworkConected())
                    insertIntoCloud();
                else
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Please correct Input", Toast.LENGTH_LONG).show();
            }
        }
    }

    void insertIntoCloud(){

        String url="";

        if(!updateMode){
            url = Util.INSERT_ADMIN_APPOINTMENT_TPHP;
        }else{
            url = Util.UPDATE_ADMIN_APPOINTMENT_PHP;
        }

        progressDialog.show();

        // Volley String Request
        /*StringRequest request = new StringRequest(Request.Method.GET, Util.INSERT_STUDENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Response: "+response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Some Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });*/

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("test",response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                        if(updateMode)
                            finish();

                    }else{
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("test",e.toString());
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("test",error.toString());
                Toast.makeText(MainActivity.this,"Some Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                Log.i("test",adminappointment.toString() );
                if(updateMode)
                    map.put("id",String.valueOf(rcvAdminappointment.getId()));

                map.put("name",adminappointment.getName());
                map.put("phone",adminappointment.getPhone());
                map.put("email",adminappointment.getEmail());
                map.put("gender",adminappointment.getGender());
                map.put("purpose",adminappointment.getPurpose());
                map.put("date",adminappointment.getDate());
                map.put("time",adminappointment.getTime());
                map.put("room",adminappointment.getRoom());
                return map;
            }
        };

        requestQueue.add(request); // execute the request, send it ti server

        clearFields();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        if(b) {
            if (id == R.id.radioButtonMale) {
                adminappointment.setGender("Male");
            } else {
                adminappointment.setGender("Female");
            }
        }
    }

    void insertIntoDB(){

        ContentValues values = new ContentValues();

        values.put(Util.COL_NAME,adminappointment.getName());
        values.put(Util.COL_PHONE,adminappointment.getPhone());
        values.put(Util.COL_EMAIL,adminappointment.getEmail());
        values.put(Util.COL_GENDER,adminappointment.getGender());
        values.put(Util.COL_PURPOSE,adminappointment.getPurpose());
        values.put(Util.COL_DATE,adminappointment.getDate());
        values.put(Util.COL_TIME,adminappointment.getTime());
        values.put(Util.COL_ROOM,adminappointment.getRoom());

        if(!updateMode){
            Uri dummy = resolver.insert(Util.ADMIN_APPOINTMENT_URI,values);
            Toast.makeText(this,adminappointment.getName()+ " Registered Successfully "+dummy.getLastPathSegment(),Toast.LENGTH_LONG).show();

            Log.i("Insert",adminappointment.toString());

            clearFields();
        }else{
            String where = Util.COL_ID + " = "+rcvAdminappointment.getId();
            int i = resolver.update(Util.ADMIN_APPOINTMENT_URI,values,where,null);
            if(i>0){
                Toast.makeText(this,"Updation Successful",Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }

    void clearFields(){
        eTxtName.setText("");
        eTxtPhone.setText("");
        eTxtEmail.setText("");
        eTxtPurpose.setText("");
        eTxtDate.setText("");
        eTxtTime.setText("");
        eTxtRoom.setText("");
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,101,0,"All Teacher");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 101){
            Intent i = new Intent(MainActivity.this, Alladmin_appointmentActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    boolean validateFields(){
        boolean flag = true;

        if(adminappointment.getName().isEmpty()){
            flag = false;
            eTxtName.setError("Please Enter Name");
        }

        if(adminappointment.getPhone().isEmpty()){
            flag = false;
            eTxtPhone.setError("Please Enter Phone");
        }else{
            if(adminappointment.getPhone().length()<10){
                flag = false;
                eTxtPhone.setError("Please Enter 10 digits Phone Number");
            }
        }

        if(adminappointment.getEmail().isEmpty()){
            flag = false;
            eTxtEmail.setError("Please Enter Email");
        }else{
            if(!(adminappointment.getEmail().contains("@") && adminappointment.getEmail().contains("."))){
                flag = false;
                eTxtEmail.setError("Please Enter correct Email");
            }
        }

        return flag;

    }


    void showDatePicker(View view){

        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH);
        int yy = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this,dateSetListener,yy,mm,dd);
        datePickerDialog.show();

    }

    void showTimePicker(View view){
        Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this,timeSetListener,hh,mm,true);
        timePickerDialog.show();
    }
}

