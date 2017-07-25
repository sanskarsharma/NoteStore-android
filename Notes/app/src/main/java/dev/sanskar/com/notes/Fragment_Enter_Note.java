package dev.sanskar.com.notes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sanskar on 4/14/2017.
 */
public class Fragment_Enter_Note extends Fragment {

    Button addNote;
    EditText newNote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_enter_note, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add New Note");


        newNote= (EditText) getView().findViewById(R.id.New_Note);

        addNote = (Button)getView().findViewById(R.id.Button_Add_Note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data = newNote.getText().toString();

                if(data.length()!=0) {


                    DBHandler db = new DBHandler(getActivity());
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();

                    SessionManager sessy = new SessionManager(getActivity());

                    db.addNote(new NoteModel(ts, data, sessy.getUserMobileNumber()));

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(newNote.getWindowToken(), 0); // these two lines to hide keyboard

                    Fragment fragment = new Fragment_All_Notes();       // showing al notes frag after adding note
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Enter Something ",Toast.LENGTH_SHORT).show();

            }
        });

    }

}
