package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Thought;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by anirudh on 27/12/17.
 */

public class ThoughtAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    DatabaseReference databaseReference;
    ArrayList<Thought> thoughtList;
    public static final int BLACK = android.R.color.black;
    public static final int WHITE = android.R.color.white;
    int[] backgroundColor = {
            Color.parseColor("#b6e1ab"),
            Color.parseColor("#e1ddab"),
            Color.parseColor("#e1bfab")
    };

    int[] bannerColor = {
            Color.parseColor("#4d7942"),
            Color.parseColor("#6b642a"),
            Color.parseColor("#8e6044")
    };

    int[] textColor = {
            WHITE,
            WHITE,
            WHITE
    };

    public ThoughtAdapter(Context context) {
        this.context = context;
        thoughtList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("thoughts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Log.d("THOUGHT", "onDataChange: " + dataSnap.getValue(Thought.class));
                    thoughtList.add(dataSnap.getValue(Thought.class));
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getCount() {
        return thoughtList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_thought, container, false);
        TextView tvThoughtText = view.findViewById(R.id.tv_thought_text);
        tvThoughtText.setText(thoughtList.get(position).getText());
        tvThoughtText.setTextColor(context.getResources().getColor(textColor[position % textColor.length]));
        AppCompatImageView imageView = view.findViewById(R.id.ivBackground);
        final RelativeLayout bottomBarRL = view.findViewById(R.id.bottom_bar);
        imageView.setColorFilter(backgroundColor[position % backgroundColor.length], PorterDuff.Mode.MULTIPLY);
        FrameLayout frameLayout = view.findViewById(R.id.rootLayoutThought);
        FrameLayout frameLayoutForClick = view.findViewById(R.id.emptyClickable);
        LinearLayout thoughtContainerLL = view.findViewById(R.id.ll_thought_container);

        ImageView header = view.findViewById(R.id.iv_header);
        ImageView footer = view.findViewById(R.id.iv_footer);

        header.setColorFilter(bannerColor[position % backgroundColor.length], PorterDuff.Mode.SRC_IN);
        footer.setColorFilter(bannerColor[position % backgroundColor.length], PorterDuff.Mode.SRC_IN);
        View.OnClickListener switcher = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomBarRL.getVisibility()){
                    case View.GONE:
                        bottomBarRL.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        bottomBarRL.setVisibility(View.GONE);
                        break;
                }
            }
        };
        frameLayoutForClick.setOnClickListener(switcher);
        tvThoughtText.setOnClickListener(switcher);

        ImageView ivComments = view.findViewById(R.id.ivComments);
        ImageView ivLike = view.findViewById(R.id.ivLike);

        ivComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view);


        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
