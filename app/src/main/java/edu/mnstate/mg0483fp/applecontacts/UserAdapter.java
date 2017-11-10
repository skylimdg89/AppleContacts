package edu.mnstate.mg0483fp.applecontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by dongkyulim on 11/3/17.
 */

public class UserAdapter extends ArrayAdapter<User>{
    private static final String TAG = "UserAdapter";
    UserDB myDB;
    ArrayList<User> userList;
    Context context;
    private TextView name_row;
    //int index=0;

    public UserAdapter(Context context, ArrayList<User> userList){
        super(context, -1, userList);
        this.context = context;
        this.userList = userList;
    }

    public View getView(final int position, View view, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.listview_row, parent, false);
        name_row = (TextView)v.findViewById(R.id.name_row);
        myDB= new UserDB(context);

        name_row.setText(userList.get(position).getFirstName() + " "+userList.get(position).getLastName());

        return v;
    }
}
