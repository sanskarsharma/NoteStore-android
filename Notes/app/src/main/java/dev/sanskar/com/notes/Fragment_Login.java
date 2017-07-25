package dev.sanskar.com.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Sanskar on 4/14/2017.
 */
public class Fragment_Login extends Fragment {

    Button login;
    EditText mobileEDT,passwordEDT;
    TextView signup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Login");

        SessionManager sessy = new SessionManager(getActivity());
        sessy.logoutUser();                                              // gurantees user logout if previous logout attempt had failed

        ((DrawerLockerInterface) getActivity()).drawerLocker(true); // navigation drawer locked so options are not available before login


        mobileEDT=(EditText)getView().findViewById(R.id.input_mobileNo);
        passwordEDT=(EditText)getView().findViewById(R.id.input_password);
        signup=(TextView)getView().findViewById(R.id.link_signup);

        DBHandlerUser dbhu = new DBHandlerUser(getActivity().getApplicationContext());


        login=(Button)getView().findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mob = mobileEDT.getText().toString();
                String pass = passwordEDT.getText().toString();


                if(mob.length()!=10){
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();

                }
                else if(pass.length()<4){
                    Toast.makeText(getActivity().getApplicationContext(),"Password too short",Toast.LENGTH_SHORT).show();

                }
                else{

                    DBHandlerUser userdb = new DBHandlerUser(getActivity().getApplicationContext());
                    UserModel userCredentials = userdb.getUser(mob);

                    if(userCredentials!=null){
                        if(userCredentials.getPassword().equals(pass.trim())){

                            // create session
                            SessionManager sessy = new SessionManager(getActivity().getApplicationContext());
                            sessy.createLoginSession(userCredentials.getMobileNumber(),userCredentials.getPassword(),userCredentials.getName());


                            // log in and redirect to notes page
                            Fragment fragment = new Fragment_All_Notes();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"This User is not Registered on Notes App",Toast.LENGTH_SHORT).show();

                    }


                }
                


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // dialog box to register new user

                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());

                Context context= getActivity();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText enterMobile = new EditText(context);
                enterMobile.setHint("Mobile Number");
                enterMobile.setInputType(InputType.TYPE_CLASS_NUMBER);
                enterMobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (enterMobile.getText().toString().length() != 10)        // check length of mobile no.
                            enterMobile.setError("Mobile number should be 10 digits long");
                        else if(new DBHandlerUser(getActivity()).getUser(enterMobile.getText().toString())!=null){ // check if no. has been alredy registered
                            enterMobile.setError("User with this Mobile number already exists");

                        }
                    }
                });

                layout.addView(enterMobile);


                final EditText enterName = new EditText(context);
                enterName.setHint("Name");
                enterName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (enterName.getText().toString().length() < 1)
                            enterName.setError("Enter Name");
                    }
                });
                layout.addView(enterName);

                final EditText enterPassword = new EditText(context);
                enterPassword.setHint("Set Password");
                enterPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                enterPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if(enterPassword.getText().toString().length()<4) // validate password
                            enterPassword.setError("Password should be atleast 4 Characters long");
                    }
                });
                layout.addView(enterPassword);

                alert.setTitle("New User ? Sign-up here");
                alert.setMessage("Enter details :");
                alert.setView(layout);

                alert.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String mob = enterMobile.getText().toString();
                                String pass = enterPassword.getText().toString();
                                String name = enterName.getText().toString();

                                if (mob.length() != 10 || new DBHandlerUser(getActivity()).getUser(enterMobile.getText().toString())!=null) {

                                    Toast.makeText(getActivity().getApplicationContext(), "Invalid Mobile Number"+"\n"+" Registration Failed", Toast.LENGTH_SHORT).show();
                                } else if (pass.length() < 4) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Password too short, Registration Failed", Toast.LENGTH_SHORT).show();

                                } else if (name.length() == 0) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Name Not Entered,Registration Failed", Toast.LENGTH_SHORT).show();

                                } else {
                                    UserModel newUser = new UserModel(mob,pass,name);

                                    DBHandlerUser dbuser = new DBHandlerUser(getActivity().getApplicationContext());
                                    dbuser.addUser(newUser);
                                    // adding new user after validation

                                    Toast.makeText(getActivity().getApplicationContext(),"Registration Successful "+"\n"+" You Can Log-in Now",Toast.LENGTH_SHORT).show();


                                }



                            }
                        }

                );

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

                        {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               // do nothingss
                            }
                        }

                );

                alert.show();
                }
            });



    }
}
