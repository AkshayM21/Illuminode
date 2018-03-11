package akshaym.stortree;

/**
 *
 * taken from cardview tutorial - https://www.androidtutorialpoint.com/material-design/android-cardview-tutorial/
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


public class CardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView MyRecyclerView;
    private String[] numbers;
    private String[] content;
    private boolean isArchived;
    private String prompt;

    public CardFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CardFragment newInstance(int position, String[] nums, String[] cont) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putStringArray("nums", nums);
        args.putStringArray("content", cont);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        numbers = this.getArguments().getStringArray("nums");
        content = this.getArguments().getStringArray("content");
        int position = this.getArguments().getInt("position");
        if(position==0) isArchived= true;
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (numbers.length > 0 && MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(numbers));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private String[] list;

        public MyAdapter(String[] Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.titleTextView.setText(list[position]);
            if(isArchived){
                holder.mainTextView.setText(content[position]);
            }

        }

        @Override
        public int getItemCount() {
            return list.length;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView mainTextView;
        Button button;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            mainTextView = (TextView) v.findViewById(R.id.mainTextView);
                button = (Button) v.findViewById(R.id.moreInfoButton);
            if(!isArchived) {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent("holywhyisitsocoldinthisroomwilhacks2017");
                        intent.putExtra("Prompt", prompt);
                        startActivity(intent);
                    }
                });

            }else{
                button.setVisibility(View.INVISIBLE);
            }



        }
    }






}






