package auribises.com.admin_appointment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class admin_appointmentAdapter extends ArrayAdapter<adminappointment> {

    Context context;
    int resource;
    ArrayList<adminappointment> adminappointmentsList,tempList;

    public admin_appointmentAdapter(Context context, int resource, ArrayList<adminappointment> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        adminappointmentsList = objects;
        tempList = new ArrayList<>();
        tempList.addAll(adminappointmentsList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.textViewName);
        TextView txtGender = (TextView)view.findViewById(R.id.textViewGender);

        adminappointment adminappointment = adminappointmentsList.get(position);
        txtName.setText(adminappointment.getName());
        //txtGender.setText(student.getGender());
        txtGender.setText(String.valueOf(adminappointment.getId()));

        Log.i("Test",adminappointment.toString());

        return view;
    }

    public void filter(String str){

        adminappointmentsList.clear();

        if(str.length()==0){
            adminappointmentsList.addAll(tempList);
        }else{
            for(adminappointment a : tempList){
                if(a.getName().toLowerCase().contains(str.toLowerCase())){
                    adminappointmentsList.add(a);
                }
            }
        }

        notifyDataSetChanged();
    }
}
