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

public class MaterialSubject_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subject_Model_List> items = new ArrayList<>();
    private ArrayList<Subject_Model_List> arraylist;
    private Context ctx;
    private MaterialSubject_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Subject_Model_List obj, int position);
    }

    public void setOnItemClickListener(final MaterialSubject_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public MaterialSubject_Adapter(Context context, List<Subject_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Subject_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            txt_title = (TextView) v.findViewById(R.id.txt_title);
            lyt_parent= (View) v.findViewById(R.id.lyt_parent);
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_subjects, parent, false);
        vh = new MaterialSubject_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Subject_Model_List p = items.get(position);
        if (holder instanceof MaterialSubject_Adapter.OriginalViewHolder) {
            final MaterialSubject_Adapter.OriginalViewHolder view = (MaterialSubject_Adapter.OriginalViewHolder) holder;

            view.txt_title.setText(p.getSub_name());

            //view.description.setTextColor( ctx.getResources().getColor( R.color.grey_80 ) );

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
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


            }
        }
        notifyDataSetChanged();
    }
}