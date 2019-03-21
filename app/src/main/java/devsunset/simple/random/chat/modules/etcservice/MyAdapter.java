package devsunset.simple.random.chat.modules.etcservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import devsunset.simple.random.chat.R;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvMessage;

        MyViewHolder(View view){
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tvMessage = view.findViewById(R.id.tv_message);
        }
    }

    private ArrayList<MessageItem> MessageItemArrayList;
    public MyAdapter(ArrayList<MessageItem> MessageItemArrayList){
        this.MessageItemArrayList = MessageItemArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.ivPicture.setImageResource(MessageItemArrayList.get(position).drawableId);
        myViewHolder.tvMessage.setText(MessageItemArrayList.get(position).getTALK_TEXT());
    }

    @Override
    public int getItemCount() {
        return MessageItemArrayList.size();
    }
}
