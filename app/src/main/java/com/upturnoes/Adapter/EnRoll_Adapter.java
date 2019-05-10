package com.upturnoes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upturnoes.Model.Enroll_Model_List;
import com.upturnoes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnRoll_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Enroll_Model_List> items = new ArrayList<>();
    private ArrayList<Enroll_Model_List> arraylist;
    private Context ctx;
    private EnRoll_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Enroll_Model_List obj, int position);
    }

    public void setOnItemClickListener(final EnRoll_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public EnRoll_Adapter(Context context, List<Enroll_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Enroll_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView text_student,text_exam,text_enroll_num,text_expiry;
        //public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            text_student = (TextView) v.findViewById(R.id.text_student);
            text_exam = (TextView) v.findViewById(R.id.text_exam);
            text_enroll_num = (TextView) v.findViewById(R.id.text_enroll_num);
            text_expiry = (TextView) v.findViewById(R.id.text_expiry);

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enroll_list, parent, false);
        vh = new EnRoll_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Enroll_Model_List p = items.get(position);
        if (holder instanceof EnRoll_Adapter.OriginalViewHolder) {
            final EnRoll_Adapter.OriginalViewHolder view = (EnRoll_Adapter.OriginalViewHolder) holder;

            view.text_student.setText(p.getFirst_name() +" "+p.getLast_name());

            view.text_enroll_num.setText(p.getEnroll_num());
            view.text_expiry.setText(p.getExpiry_date());
            String exam_id = p.getExam_id();
            String study_id = p.getStudy_mterial_id();
            String video_id = p.getVideo_id();

            if (exam_id != null && !exam_id.isEmpty() && !exam_id.equals("null")) {
                view.text_exam.setText(exam_id);
            }else  if (study_id != null && !study_id.isEmpty() && !study_id.equals("null")) {
                view.text_exam.setText(study_id);
            }else if (video_id != null && !video_id.isEmpty() && !video_id.equals("null")) {
                view.text_exam.setText(video_id);
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
            for (Enroll_Model_List wp : arraylist) {
                if (wp.getFirst_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getEnroll_num().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getLast_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getExam_id().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}



