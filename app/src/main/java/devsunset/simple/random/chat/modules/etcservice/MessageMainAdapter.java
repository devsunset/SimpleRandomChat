/*
 * @(#)MessageMainAdapter.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.etcservice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import devsunset.simple.random.chat.ChatActivity;
import devsunset.simple.random.chat.R;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.utilservice.Consts;

/**
 * <PRE>
 * SimpleRandomChat MessageMainAdapter
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MessageMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPicture;
        final TextView tv_talk_text;
        final TextView tv_countryName_talk_target;
        final TextView tv_atx_local_time;
        final LinearLayout list_item_rows;
        final Button btnTranslation;
        final ImageView ivTalkTypeVoiceImage;

        MyViewHolder(View view){
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tv_talk_text = view.findViewById(R.id.tv_talk_text);
            tv_countryName_talk_target = view.findViewById(R.id.tv_countryName_talk_target);
            tv_atx_local_time = view.findViewById(R.id.tv_atx_local_time);
            list_item_rows = view.findViewById(R.id.list_item_rows);
            btnTranslation = view.findViewById(R.id.btnTranslation);
            ivTalkTypeVoiceImage= view.findViewById(R.id.ivTalkTypeVoiceImage);
        }
    }

    private final ArrayList<MessageItem> MessageItemArrayList;
    public MessageMainAdapter(ArrayList<MessageItem> MessageItemArrayList){
        this.MessageItemArrayList = MessageItemArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recycler_main_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.ivPicture.setImageResource(MessageItemArrayList.get(position).drawableId);
        myViewHolder.tv_talk_text.setText(MessageItemArrayList.get(position).getTALK_TEXT());

        String target = MessageItemArrayList.get(position).getTALK_TARGET();

        if(!"NO_DATA".equals(target)){
            if(target !=null  && !"".equals(target)){
                target = " - Me";
            }
            myViewHolder.tv_countryName_talk_target.setText(MessageItemArrayList.get(position).getCOUNTRY_NAME()+target);
            myViewHolder.tv_atx_local_time.setText(MessageItemArrayList.get(position).getATX_LOCAL_TIME());
        }

        if(!"NO_DATA".equals(MessageItemArrayList.get(position).getTALK_TARGET())){
            if(MessageItemArrayList.get(position).getFROM_LANG().equals(MessageItemArrayList.get(position).getTO_LANG())){
                myViewHolder.btnTranslation.setVisibility(View.GONE);
            }else{
                myViewHolder.btnTranslation.setVisibility(View.VISIBLE);
            }

            if(Consts.MESSAGE_TYPE_TEXT.equals(MessageItemArrayList.get(position).getTALK_TYPE())){
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.GONE);
            }else  if(Consts.MESSAGE_TYPE_IMAGE.equals(MessageItemArrayList.get(position).getTALK_TYPE())){
                myViewHolder.ivTalkTypeVoiceImage.setBackgroundResource(R.drawable.btn_image);
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.ivTalkTypeVoiceImage.setBackgroundResource(R.drawable.btn_microphone);
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.VISIBLE);
            }
        }

        if(MessageItemArrayList.get(position).drawableId == R.drawable.empty_message) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.empty_message_bg);
        } else if(MessageItemArrayList.get(position).drawableId == R.drawable.new_message){
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.newmessage_bg);
        } else if(MessageItemArrayList.get(position).drawableId == R.drawable.man){
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.man_bg);
        } else if(MessageItemArrayList.get(position).drawableId == R.drawable.woman){
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.woman_bg);
        } else if(MessageItemArrayList.get(position).drawableId == R.drawable.trash){
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.light_gray);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MessageItemArrayList.get(position).getATX_ID() !=null){
                    Context context = v.getContext();

                    // 삭제 상태인 경우 갱신 안함
                    if(!Consts.MESSAGE_STATUS_DELETE.equals(MessageItemArrayList.get(position).getATX_STATUS())){
                        readTalkMainStatus(context,MessageItemArrayList.get(position).getATX_ID());
                    }

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("ATX_ID",MessageItemArrayList.get(position).getATX_ID());
                    intent.putExtra("ATX_STATUS",MessageItemArrayList.get(position).getATX_STATUS());
                    intent.putExtra("REPLY_APP_KEY",MessageItemArrayList.get(position).getREPLY_APP_KEY());
                    context.startActivity(intent);
                }
            }
        });
    }

    private void readTalkMainStatus(Context ctx,String atxId) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(ctx).getAppDataBase()
                        .AppTalkMainDao().updateStatus(Consts.MESSAGE_STATUS_REPLY,atxId);

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    public int getItemCount() {
        return MessageItemArrayList.size();
    }
}
