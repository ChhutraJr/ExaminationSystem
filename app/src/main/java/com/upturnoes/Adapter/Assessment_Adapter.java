package com.upturnoes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upturnoes.Model.Assessment_Model_List;
import com.upturnoes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Assessment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Assessment_Model_List> items = new ArrayList<>();
    private ArrayList<Assessment_Model_List> arraylist;
    private Context ctx;
    private Assessment_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Assessment_Model_List obj, int position);
    }

    public void setOnItemClickListener(final Assessment_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Assessment_Adapter(Context context, List<Assessment_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Assessment_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView text_exam,text_starttime,text_endtime,text_score,text_status;
        //public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            text_exam = (TextView) v.findViewById(R.id.text_exam);
            text_starttime = (TextView) v.findViewById(R.id.text_starttime);
            text_endtime = (TextView) v.findViewById(R.id.text_endtime);
            text_score = (TextView) v.findViewById(R.id.text_score);
            text_status = (TextView) v.findViewById(R.id.text_status);
        }
    }

  /*  public static class SectionViewHolder extends RecyclerView.ViewHolder {
       // public TextView title_section;

        public SectionViewHolder(View v) {
            super(v);
           // title_section = (TextView) v.findViewById(R.id.title_section);
        }
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment_record, parent, false);
        vh = new Assessment_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Assessment_Model_List p = items.get(position);
        if (holder instanceof Assessment_Adapter.OriginalViewHolder) {
            final Assessment_Adapter.OriginalViewHolder view = (Assessment_Adapter.OriginalViewHolder) holder;

            view.text_exam.setText(p.getExam());
            view.text_starttime.setText(p.getDate_taken());
            view.text_endtime.setText(p.getCompleted());
            view.text_score.setText(p.getScore()+"%");

            String status = p.getStatus();
            if(status.equalsIgnoreCase("You Passed!")) {
                view.text_status.setTextColor(ctx.getResources().getColor(R.color.green_900));
                view.text_status.setText(p.getStatus());
            }else{
                view.text_status.setTextColor(ctx.getResources().getColor(R.color.amber_800));
                view.text_status.setText(p.getStatus());
            }
            //view.description.setTextColor( ctx.getResources().getColor( R.color.grey_80 ) );

          /*  view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arraylist);
        } else {
            for (Assessment_Model_List wp : arraylist) {
                if (wp.getExam().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getDate_taken().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getScore().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getStatus().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}


