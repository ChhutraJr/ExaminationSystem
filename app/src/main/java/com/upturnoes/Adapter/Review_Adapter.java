package com.upturnoes.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.upturnoes.Class.MyConfig;
import com.upturnoes.Model.Review_Model;
import com.upturnoes.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Review_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Review_Model> items = new ArrayList<>();
    private Context ctx;
    private Review_Adapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Review_Model obj, int position);
    }

    public void setOnItemClickListener(final Review_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Review_Adapter(Context context, List<Review_Model> items) {
        this.items = items;
        ctx = context;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_quetion,txt_selectanswer,txt_rightanswer;
        RadioGroup radiogrp;
        RadioButton radio1,radio2,radio3,radio4;
        public View lyt_parent;
        EditText edit_fill_answer;
        ImageView image_quetion,image_op1,image_op2,image_op3,image_op4;

        public OriginalViewHolder(View v) {
            super(v);

            txt_quetion = (TextView) v.findViewById(R.id.txt_quetion);
            radiogrp = (RadioGroup) v.findViewById(R.id.radiogrp);
            radio1 = (RadioButton) v.findViewById(R.id.radio1);
            radio2 = (RadioButton) v.findViewById(R.id.radio2);
            radio3 = (RadioButton) v.findViewById(R.id.radio3);
            radio4 = (RadioButton) v.findViewById(R.id.radio4);
            edit_fill_answer= (EditText) v.findViewById(R.id.edit_fill_answer);
            txt_selectanswer = (TextView) v.findViewById(R.id.txt_selectanswer);
            txt_rightanswer = (TextView) v.findViewById(R.id.txt_rightanswer);

            lyt_parent = (View) v.findViewById( R.id.lyt_parent);

            image_quetion = (ImageView) v.findViewById(R.id.image_quetion);
            image_op1 = (ImageView) v.findViewById(R.id.image_op1);
            image_op2 = (ImageView) v.findViewById(R.id.image_op2);
            image_op3 = (ImageView) v.findViewById(R.id.image_op3);
            image_op4 = (ImageView) v.findViewById(R.id.image_op4);

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        vh = new Review_Adapter.OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Review_Model p = items.get(position);
        if (holder instanceof Review_Adapter.OriginalViewHolder) {
            final Review_Adapter.OriginalViewHolder view = (Review_Adapter.OriginalViewHolder) holder;

            view.radio1.setClickable(false);
            view.radio2.setClickable(false);
            view.radio3.setClickable(false);
            view.radio4.setClickable(false);

            int srno = position+1;
            view.txt_quetion.setText(srno+".  "+p.getQuestion());

            if(p.getOp1().equals("-")  && p.getOp2().equals("-")){
                view.edit_fill_answer.setVisibility(View.VISIBLE);
                view.radiogrp.setVisibility(View.GONE);
            }else{
                view.edit_fill_answer.setVisibility(View.GONE);
                view.radiogrp.setVisibility(View.VISIBLE);
                view.radio1.setText(p.getOp1());
                view.radio2.setText(p.getOp2());
                view.radio3.setText(p.getOp3());
                view.radio4.setText(p.getOp4());

                if (p.getQuestion_image() != null && !p.getQuestion_image().isEmpty() && !p.getQuestion_image().equals("null")){

                    view.image_quetion.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getQuestion_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_quetion.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_quetion.setVisibility(View.GONE);
                }

                if (p.getOp1_image() != null && !p.getOp1_image().isEmpty() && !p.getOp1_image().equals("null")){

                    view.image_op1.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getOp1_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_op1.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_op1.setVisibility(View.GONE);
                }

                if (p.getOp2_image() != null && !p.getOp2_image().isEmpty() && !p.getOp2_image().equals("null")){

                    view.image_op2.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getOp2_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_op2.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_op2.setVisibility(View.GONE);
                }


                if (p.getOp3_image() != null && !p.getOp3_image().isEmpty() && !p.getOp3_image().equals("null")){

                    view.image_op3.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getOp3_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_op3.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_op3.setVisibility(View.GONE);
                }


                if (p.getOp4_image() != null && !p.getOp4_image().isEmpty() && !p.getOp4_image().equals("null")){

                    view.image_op4.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getOp4_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_op4.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_op4.setVisibility(View.GONE);
                }

            }
            view.radiogrp.setEnabled(false);
            String selct_ans = p.getSelected_answer();
            String right_ans = p.getAnswer();
           // Log.e("review ",selct_ans+"///"+right_ans);

            if(right_ans.equalsIgnoreCase(selct_ans)){

                view.txt_rightanswer.setText("Correct");
                view.txt_rightanswer.setTextColor(ctx.getResources().getColor(R.color.green_800));
            }
            else if(right_ans.equalsIgnoreCase("op1"))
            {
                view.txt_rightanswer.setText("Right answer is : "+ "option1");

            }else if(right_ans.equalsIgnoreCase("op2"))
            {

                view.txt_rightanswer.setText("Right answer is : "+ "option2");

            }else if(right_ans.equalsIgnoreCase("op3")){

                view.txt_rightanswer.setText("select answer is : "+ "option3");

            }else if(right_ans.equalsIgnoreCase("op4"))
            {

                view.txt_rightanswer.setText("Right answer is : "+ "option4");

            }else{
                view.txt_rightanswer.setText("Right answer is : "+ right_ans);
            }


            if(selct_ans.equalsIgnoreCase("op1"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option1");
                view.radio1.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("op2"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option2");
                view.radio2.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("op3"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option3");
                view.radio3.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("op4"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option4");
                view.radio4.setChecked(true);
            }else{
                view.edit_fill_answer.setText(selct_ans);
                view.txt_selectanswer.setText("Select answer is : "+ selct_ans);
            }

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


}



