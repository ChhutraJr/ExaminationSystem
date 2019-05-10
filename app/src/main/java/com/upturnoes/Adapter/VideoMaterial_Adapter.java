package com.upturnoes.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upturnoes.Class.MyConfig;
import com.upturnoes.Model.VideoMaterial_Model;
import com.upturnoes.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoMaterial_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoMaterial_Model> items = new ArrayList<>();
    private ArrayList<VideoMaterial_Model> arraylist;
    private Context ctx;
    private VideoMaterial_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, VideoMaterial_Model obj, int position);
    }

    public void setOnItemClickListener(final VideoMaterial_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public VideoMaterial_Adapter(Context context, List<VideoMaterial_Model> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<VideoMaterial_Model>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,description,txt_deadline;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.thum_img);
            name = (TextView) v.findViewById(R.id.txt_title);
            description = (TextView) v.findViewById(R.id.description);
            txt_deadline = (TextView) v.findViewById(R.id.txt_deadline);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        //if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_material, parent, false);
        vh = new VideoMaterial_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoMaterial_Model p = items.get(position);
        if (holder instanceof VideoMaterial_Adapter.OriginalViewHolder) {
            final VideoMaterial_Adapter.OriginalViewHolder view = (VideoMaterial_Adapter.OriginalViewHolder) holder;

            view.name.setText(p.getVideo_title());
            view.description.setText(p.getVideo_type());
            view.txt_deadline.setText("Deadline : "+p.getDeadline());
            view.name.setTextColor( ctx.getResources().getColor( R.color.colorAccentDark ) );
            view.description.setTextColor( ctx.getResources().getColor( R.color.grey_80 ) );

          //  view.image.setImageResource(R.drawable.pdfimg);
            String thumb_img = p.getThumbnail_img();
            String img_path = MyConfig.Parent_Url+"assets/thumbnail/"+thumb_img;
            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
            try {
                in = (InputStream) new URL(img_path).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
            view.image.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
            try {
                if(in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("Thumbnail", String.valueOf(img_path));

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
            for (VideoMaterial_Model wp : arraylist) {
                if (wp.getVideo_title().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getVideo_type().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }


            }
        }
        notifyDataSetChanged();
    }

}



