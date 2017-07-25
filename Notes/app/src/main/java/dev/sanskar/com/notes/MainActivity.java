package dev.sanskar.com.notes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DrawerLockerInterface {

    DrawerLayout drawer;
    TextView NAVuser ;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displaySelectedScreen(R.id.fab);   // to go to add new note fragment


            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        NAVuser= (TextView) header.findViewById(R.id.NAVusername);



        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset != 0) {
                    SessionManager sessy =new SessionManager(getApplicationContext());
                    DBHandler db= new DBHandler(getApplicationContext());
                    NAVuser.setText("Welcome "+sessy.getNameOfUser());

                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        SessionManager sessy = new SessionManager(this);
        if(sessy.checkLogin())
            displaySelectedScreen(R.id.all_notes); // show all notes of logged in user
        else
            displaySelectedScreen(R.id.logout); // redirect to login fragment

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getTitle()=="Add New Note") {         // goes back to all notes fragment on back press
            displaySelectedScreen(R.id.all_notes);
        }
        else{
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
               finish();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if(item.getItemId()==R.id.deleteUser){          // alert dialog for user on pressing delete account

            AlertDialog.Builder deletealert = new AlertDialog.Builder(this);

            deletealert.setTitle("Are you sure you want to delete your account ?");
            deletealert.setMessage("This will permanently delete all your saved Notes");


            deletealert.setPositiveButton("Yes, Delete my Account", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                   DBHandlerUser db = new DBHandlerUser(getApplicationContext());
                    SessionManager sessy = new SessionManager(getApplicationContext());
                    db.deleteContact(sessy.getUserMobileNumber());
                    sessy.logoutUser();
                    Toast.makeText(getApplicationContext(),"Account Deleted Succesfully",Toast.LENGTH_SHORT).show();
                    displaySelectedScreen(R.id.logout); // redirect to login fragment

                }
            });

            deletealert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });

            deletealert.show();


        }
        else{
            displaySelectedScreen(item.getItemId()); //
        }


        return true;
    }

    private void displaySelectedScreen(int itemId) {

        decideFloatingActionButton(itemId);

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.all_notes:
                fragment = new Fragment_All_Notes();
                break;
            case R.id.Add_New_Note:

                fragment = new Fragment_Enter_Note();
                break;
            case R.id.logout:
                fragment = new Fragment_Login();
                break;
            case R.id.fab:
                fragment = new Fragment_Enter_Note(); // fab button also leads to enter note fragment
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void decideFloatingActionButton(int itemId) {

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();

        if(itemId== R.id.all_notes){

            params.setBehavior(null);
            fab.requestLayout();
            fab.setVisibility(View.VISIBLE);
        }
        else{
            params.setBehavior(null);
            fab.requestLayout();
            fab.setVisibility(View.GONE);

        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       // return super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final DBHandler db = new DBHandler(this);

        SessionManager sessy = new SessionManager(this);

        final List<NoteModel> notes = db.getAllNotes(sessy.getUserMobileNumber());

        switch(item.getItemId()) {




            case R.id.edit:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);

                final NoteModel note = notes.get(notes.size()-info.position-1); // because you reversed the list to be shown in Fragment_All_Notes
                alert.setTitle("Edit Note");
                alert.setMessage("Created on: " + getDateandtime(Long.parseLong(note.getCreatedAt())));



                edittext.setMaxLines(10);
                edittext.setText(note.getThenote());
                alert.setView(edittext);

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String editednote = edittext.getText().toString();
                        note.setThenote(editednote);
                        db.updateContact(note);
                        displaySelectedScreen(R.id.all_notes);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       // do nothing
                    }
                });

                alert.show();

                return true;
            case R.id.delete:

                db.deleteContact(notes.get(notes.size()-info.position-1));

                displaySelectedScreen(R.id.all_notes);

                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


    public  String getDateandtime(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("India/Kolkata");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }



    public void drawerLocker(Boolean signal){

        if(signal){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        else{
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.notesicon);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

        }


    }



}

 interface DrawerLockerInterface{
     public void drawerLocker(Boolean signal);

}
