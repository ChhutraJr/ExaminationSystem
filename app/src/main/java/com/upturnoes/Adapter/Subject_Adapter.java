package com.upturnoes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upturnoes.Model.Subject_Model_List;
import com.upturnoes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Subject_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subject_Model_List> items = new ArrayList<>();
    private ArrayList<Subject_Model_List> arraylist;
    private Context ctx;
    private Subject_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Subject_Model_List obj, int position);
    }

    public void setOnItemClickListener(final Subject_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Subject_Adapter(Context context, List<Subject_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Subject_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView text_name,text_id,text_dept,text_class,text_reg_date;
        //public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            text_name = (TextView) v.findViewById(R.id.text_name);
            text_id = (TextView) v.findViewById(R.id.text_id);
            text_dept = (TextView) v.findViewById(R.id.text_dept);
            text_class = (TextView) v.findViewById(R.id.text_class);
            text_reg_date = (TextView) v.findViewById(R.id.text_reg_date);
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subjects, parent, false);
        vh = new Subject_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Subject_Model_List p = items.get(position);
        if (holder instanceof Subject_Adapter.OriginalViewHolder) {
            final Subject_Adapter.OriginalViewHolder view = (Subject_Adapter.OriginalViewHolder) holder;

            view.text_name.setText(p.getSub_name());
            view.text_id.setText(p.getSub_id());
            view.text_dept.setText(p.getDept_name());
            view.text_class.setText(p.getClass_name());
            view.text_reg_date.setText(p.getReg_date());

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
            for (Subject_Model_List wp : arraylist) {
                if (wp.getSub_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getClass_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getDept_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }


            }
        }
        notifyDataSetChanged();
    }
}



