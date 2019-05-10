package com.upturnoes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upturnoes.Model.StudyMaterial_Model;
import com.upturnoes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudyMaterial_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudyMaterial_Model> items = new ArrayList<>();
    private ArrayList<StudyMaterial_Model> arraylist;
    private Context ctx;
    private StudyMaterial_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, StudyMaterial_Model obj, int position);
    }

    public void setOnItemClickListener(final StudyMaterial_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public StudyMaterial_Adapter(Context context, List<StudyMaterial_Model> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<StudyMaterial_Model>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,description;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.list_avatar);
            name = (TextView) v.findViewById(R.id.name);
            description = (TextView) v.findViewById(R.id.description);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        //if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_material, parent, false);
        vh = new StudyMaterial_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        StudyMaterial_Model p = items.get(position);
        if (holder instanceof StudyMaterial_Adapter.OriginalViewHolder) {
            final StudyMaterial_Adapter.OriginalViewHolder view = (StudyMaterial_Adapter.OriginalViewHolder) holder;

            view.name.setText(p.getStudy_title());
            view.description.setText("Class : "+p.getClass_name());
            view.name.setTextColor( ctx.getResources().getColor( R.color.colorAccentDark ) );
            view.description.setTextColor( ctx.getResources().getColor( R.color.grey_80 ) );

            view.image.setImageResource(R.drawable.pdfimg);

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
            for (StudyMaterial_Model wp : arraylist) {
                if (wp.getStudy_title().toLowerCase(Locale.getDefault())
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
                if (wp.getSubject().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getStudy_type().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }


            }
        }
        notifyDataSetChanged();
    }

}


