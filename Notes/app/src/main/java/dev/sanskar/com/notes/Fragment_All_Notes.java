package dev.sanskar.com.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sanskar on 4/14/2017.
 */
public class Fragment_All_Notes extends Fragment {

    ListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_notes, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("All Notes");

        ((DrawerLockerInterface) getActivity()).drawerLocker(false);         // unlocking drawer which was locked in login fragment

        lv= (ListView) getView().findViewById(R.id.listView);

        List<NoteModel> items = null;

        DBHandler db = new DBHandler(getActivity());

        SessionManager sessy = new SessionManager(getActivity());

        items = db.getAllNotes(sessy.getUserMobileNumber());

        if(items!=null){
            Collections.reverse(items);             // reversing list so that new entries show on top

            final ArrayList<String> list = new ArrayList<String>();          // arraylist to show notes

            for (NoteModel cn : items) {                         // adding notes to arraylist from the model class
                String log =  cn.getThenote() ;
                list.add(log);

            }

            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),   // using array adapter
                    R.layout.mytextview,list
            );

            lv.setAdapter(adapter);
            registerForContextMenu(lv); /// registering for context menu which gives edit and delete options
        }



    }



}