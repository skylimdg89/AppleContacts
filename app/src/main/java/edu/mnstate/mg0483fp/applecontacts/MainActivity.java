package edu.mnstate.mg0483fp.applecontacts;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by dongkyulim on 11/2/17.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView first_name_text;
    private TextView last_name_text;
    private TextView address_text;
    private TextView phone_text;
    private TextView email_text;
    private TextView result_text;
    private TextView name_row;

    private TextView first_name_row;
    private TextView last_name_row;
    private TextView address_row;
    private TextView phone_row;
    private TextView email_row;

    private ListView list_view;

    private EditText find_text;
    private EditText first_name_dialog;
    private EditText last_name_dialog;
    private EditText address_dialog;
    private EditText phone_dialog;
    private EditText email_dialog;

    private Button cancel_button;
    private ImageButton add_button;
    private Button find_button;
    private Button delete_button;

    private ImageView imageView;
    private ImageView imageView2;

    int index = 0;

    int mDelay=100;
    UserAdapter adapter;
    Context context = this;
    ArrayList<User> myList =new ArrayList<>();
    ArrayList<User> findList = new ArrayList<>();
    UserDB myDB = new UserDB(this);

    private android.widget.LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to widgets (controls)
        list_view = (ListView) findViewById(R.id.list_view);

        name_row = (TextView) findViewById(R.id.name_row);
        first_name_row = (TextView)findViewById(R.id.first_name_row);
        last_name_row = (TextView)findViewById(R.id.last_name_row);
        address_row = (TextView)findViewById(R.id.address_row);
        phone_row = (TextView)findViewById(R.id.phone_row);
        email_row = (TextView)findViewById(R.id.email_row);

        add_button = (ImageButton)findViewById(R.id.add_button);

        find_text = (EditText)findViewById(R.id.find_text);
        delete_button = (Button)findViewById(R.id.delete_button);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView2 = (ImageView)findViewById(R.id.imageView2);

        //get the database object
        final UserDB myDB = new UserDB(this);
        myList = myDB.getUserList();

        adapter = new UserAdapter(context,myDB.getUserList());
        myList = myDB.getUserList();

        startThread();

        //list_view listener (short click)
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                final String f = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, position +1 + "th Clicked", Toast.LENGTH_SHORT).show();

                String value_fisrt_name = myList.get(position).getFirstName();
                String value_last_name = myList.get(position).getLastName();
                String value_address = myList.get(position).getAddress();
                String value_phone = myList.get(position).getPhone();
                String value_email = myList.get(position).getEmail();

                //set data to widgets
                first_name_row.setText(value_fisrt_name);
                last_name_row.setText(value_last_name);
                address_row.setText(value_address);
                phone_row.setText(value_phone);
                email_row.setText(value_email);
            }

        });

        //list_view listener (long click)
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                Log.i("long clicked","pos: " + (pos+1));

                return true;
            }
        });

        /*


        //drag listener
        imageView2.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                //ClipData dragData = ClipData.newPlainText("", "");
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView2);
                v.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });

        imageView2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(TAG, "ACTION_DROP event");

                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView2);

                    imageView2.startDrag(data, shadowBuilder, imageView2, 0);
                    imageView2.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        */

        //edit text listener
        find_text.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){

                //get data from the widget
                String myUser = find_text.getText().toString();

                //if edit text has no input (null)
                if(myUser.equals("")){
                    //set all database data into adapter
                    adapter = new UserAdapter(context,myDB.getUserList());
                    myList = myDB.getUserList();
                    startThread();
                }

                else{
                    //find the first name on the edit text
                    findList.clear();
                    for(User tmp: myList){
                        if(tmp.getFirstName().equals(myUser)){
                            findList.add(tmp);
                        }
                    }
                    // update the list with the data found
                    if(findList.size()>0){
                        myList = myDB.findUser(findList.get(0));
                        adapter = new UserAdapter(context,myList);
                        startThread();
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}

        });

        //set the soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    //create option menu on the menu bar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu,menu);
        return true;
    }

    //case when option items selected
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.about:
                return true;
            case R.id.help:
                return true;
            case R.id.preferences:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //modify user data when button clicked
    public void modifyButtonClicked(View view){

        /*
        String fn, ln, ad, ph, em;
        fn = first_name_row.getText().toString();
        ln = last_name_row.getText().toString();
        ad = address_row.getText().toString();
        ph = phone_row.getText().toString();
        em = email_row.getText().toString();
        User user = new User(fn, ln, ad, ph, em);
        myDB.modifyUser(user);
        adapter = new UserAdapter(context,myDB.getUserList());
        myList = myDB.getUserList();
        startThread();
        */
    }

    //delete user data
    public void deleteButtonClicked(View view){
        String fn, ln, ad, ph, em;
        fn = first_name_row.getText().toString();
        ln = last_name_row.getText().toString();
        ad = address_row.getText().toString();
        ph = phone_row.getText().toString();
        em = email_row.getText().toString();
        User user = new User(fn, ln, ad, ph, em);
        myDB.deleteUser(user);

        //set the adapter for list view
        adapter = new UserAdapter(context,myDB.getUserList());
        myList = myDB.getUserList();
        startThread();

        //set data null
        first_name_row.setText(null);
        last_name_row.setText(null);
        address_row.setText(null);
        phone_row.setText(null);
        email_row.setText(null);

    }

    //add button listener
    public void addButtonClicked(View view){
        LayoutInflater li = LayoutInflater.from(context);
        View getInfoView = li.inflate(R.layout.user_dialog,null);
        AlertDialog.Builder alertDlgBld = new AlertDialog.Builder(context);
        alertDlgBld.setView(getInfoView);

        final AlertDialog alertDialog = alertDlgBld.create();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //get references to dialog
                EditText first_name_dialog = (EditText) alertDialog.findViewById(R.id.first_name_dialog);
                EditText last_name_dialog = (EditText) alertDialog.findViewById(R.id.last_name_dialog);
                EditText address_dialog = (EditText) alertDialog.findViewById(R.id.address_dialog);
                EditText phone_dialog = (EditText) alertDialog.findViewById(R.id.phone_dialog);
                EditText email_dialog = (EditText) alertDialog.findViewById(R.id.email_dialog);



                //get data from dialog
                String fName = first_name_dialog.getText().toString();
                String lName = last_name_dialog.getText().toString();
                String addr = address_dialog.getText().toString();
                String ph = phone_dialog.getText().toString();
                String em = email_dialog.getText().toString();


                /*
                if (TextUtils.isEmpty(fName)) {
                    first_name_dialog.setError("First Name CANNOT be empty.");
                    return;
                }
                if (TextUtils.isEmpty(lName)) {
                    last_name_dialog.setError("Last Name CANNOT be empty.");
                    return;
                }
                if (TextUtils.isEmpty(addr)) {
                    address_dialog.setError("Address CANNOT be empty.");
                    return;
                }
                if (TextUtils.isEmpty(ph)) {
                    phone_dialog.setError("Phone number CANNOT be empty");
                    return;
                }
                if (TextUtils.isEmpty(em)) {
                    email_dialog.setError("Email address CANNOT be empty");
                    return;
                }
                */

                //store data from dialog into user object
                User user = new User(fName, lName, addr, ph, em);
                //insert user into the database
                myDB.insertUser(user);
                adapter = new UserAdapter(context, myDB.getUserList());
                myList = myDB.getUserList();
                startThread();


            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){alertDialog.dismiss();}
        });
        alertDialog.show();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //hide the soft keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    //start thread of database
    public void startThread(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(mDelay);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                list_view.post(new Runnable(){
                    @Override
                    public void run(){
                        list_view.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    /*
    public void findButtonClicked(View view){
        //Log.i(TAG, "################### findList size b4 clear = "+Integer.toString(findList.size()));
        findList.clear();
        String myUser = find_text.getText().toString();
        Log.i(TAG, "################### find_text when findbutton clicked = "+myUser);
        for(User tmp: myList){
            if(tmp.getFirstName().equals(myUser)){
                findList.add(tmp);
            }
        }

        if(findList.size()>0){
            //findList = myDB.findUser(findList.get(0));
            myList = myDB.findUser(findList.get(0));
            adapter = new UserAdapter(context,myList);
            startThread();

            Log.i(TAG, "################### (find button clicked) findList size = "+Integer.toString(findList.size()));
        }

    }
    */
}
