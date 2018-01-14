package com.agarwal.vinod.govindkigali.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.PopListAdapter;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by darsh on 15/12/17.
 */

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    private PopListAdapter adapter;
    private RecyclerView rvPopPlayList;
    LinearLayout llList;
    private Context context;
    private EditText etListName;
    private Button btCreate, btCancel;
    private Song song;
    private FragmentActivity activity;
    private DatabaseReference popReference = FirebaseDatabase.getInstance().getReference("pop");
    private ArrayList<String> popupList = new ArrayList<>();
    public static final String TAG = "POP";
    public CustomDialogClass(@NonNull Context context, Song song, FragmentActivity activity) {
        super(context);
        this.context = context;
        this.song = song;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);

        rvPopPlayList = findViewById(R.id.rv_popPlaylist);
        llList = findViewById(R.id.ll_createPlaylist);
        etListName = findViewById(R.id.et_list_name);
        btCreate = findViewById(R.id.bt_create);
        btCancel = findViewById(R.id.bt_cancel);

        etListName.setVisibility(View.GONE);
        btCreate.setVisibility(View.GONE);
        btCancel.setVisibility(View.GONE);

        llList.setOnClickListener(this);
        btCreate.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        popReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                popupList.clear();
                for (DataSnapshot popSnapshot : dataSnapshot.getChildren()) {
                    String name = popSnapshot.getKey();
                    popupList.add(name);
                    Log.d(TAG, "onDataChange: " + popSnapshot.getKey());
                }
                adapter.updateList(popupList);
                Log.d(TAG, "onDataChange: :)   :)   :)" + dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvPopPlayList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PopListAdapter(context, song, true, activity);
        rvPopPlayList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.ll_createPlaylist:
                etListName.setVisibility(View.VISIBLE);
                btCreate.setVisibility(View.VISIBLE);
                btCancel.setVisibility(View.VISIBLE);
                rvPopPlayList.setVisibility(View.GONE);
                break;
            case R.id.bt_create:
                final String id = etListName.getText().toString();
                popReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(id).exists()) {
                            Toast.makeText(context, "PLay List Already Exits", Toast.LENGTH_SHORT).show();
                        } else {
                            popReference.child(etListName.getText().toString()).child(song.getId()).setValue(song);
                            Toast.makeText(context, "Song Added to " + etListName.getText().toString(), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.bt_cancel:
                etListName.setVisibility(View.GONE);
                btCreate.setVisibility(View.GONE);
                btCancel.setVisibility(View.GONE);
                rvPopPlayList.setVisibility(View.VISIBLE);
                break;
        }
    }
}
