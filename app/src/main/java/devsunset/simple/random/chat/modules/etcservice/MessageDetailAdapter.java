/*
 * @(#)MessageDetailAdapter.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.etcservice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import devsunset.simple.random.chat.ChatDownloadActivity;
import devsunset.simple.random.chat.R;
import devsunset.simple.random.chat.WebViewActivity;
import devsunset.simple.random.chat.modules.utilservice.Consts;

/**
 * <PRE>
 * SimpleRandomChat MessageDetailAdapter
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MessageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String MY_LANG = "";
    private final ArrayList<MessageItem> MessageItemArrayList;

    public MessageDetailAdapter(ArrayList<MessageItem> MessageItemArrayList, String lang) {
        this.MessageItemArrayList = MessageItemArrayList;
        MY_LANG = lang;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recycler_detail_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.ivPicture.setImageResource(MessageItemArrayList.get(position).drawableId);
        myViewHolder.tv_talk_text.setText(MessageItemArrayList.get(position).getTALK_TEXT());

        String target = MessageItemArrayList.get(position).getTALK_TARGET();

        if (!"NO_DATA".equals(target)) {
            if (target != null && !"".equals(target)) {
                target = " - Me";
            }
            myViewHolder.tv_countryName_talk_target.setText(MessageItemArrayList.get(position).getCOUNTRY_NAME());
            myViewHolder.tv_atx_local_time.setText(MessageItemArrayList.get(position).getATX_LOCAL_TIME());

            if (MY_LANG.equals(MessageItemArrayList.get(position).getTALK_LANG())) {
                myViewHolder.btnTranslation.setVisibility(View.GONE);
            } else {
                myViewHolder.btnTranslation.setVisibility(View.VISIBLE);
            }

            if (Consts.MESSAGE_TYPE_TEXT.equals(MessageItemArrayList.get(position).getTALK_TYPE())) {
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.GONE);
            } else if (Consts.MESSAGE_TYPE_IMAGE.equals(MessageItemArrayList.get(position).getTALK_TYPE())) {
                myViewHolder.ivTalkTypeVoiceImage.setBackgroundResource(R.drawable.ic_camera);
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.ivTalkTypeVoiceImage.setBackgroundResource(R.drawable.ic_audio);
                myViewHolder.ivTalkTypeVoiceImage.setVisibility(View.VISIBLE);
            }
        }

        if (MessageItemArrayList.get(position).drawableId == R.drawable.empty_message) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.empty_message_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.new_message) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.newmessage_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.man) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.man_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.man_me) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.man_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.woman) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.woman_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.woman_me) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.woman_bg);
        } else if (MessageItemArrayList.get(position).drawableId == R.drawable.trash) {
            myViewHolder.list_item_rows.setBackgroundResource(R.drawable.delete_bg);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MessageItemArrayList.get(position).getATX_ID() != null) {
                    Context context = v.getContext();
                    if (Consts.MESSAGE_TYPE_TEXT.equals(MessageItemArrayList.get(position).getTALK_TYPE())) {
                        if (!MY_LANG.equals(MessageItemArrayList.get(position).getTALK_LANG())) {
                            String value = MessageItemArrayList.get(position).getTALK_TEXT();
                            /*
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.google.com/#view=home&op=translate&sl="+MessageItemArrayList.get(position).getTALK_LANG()+"&tl="+MY_LANG+"&text="+Uri.encode(value)));
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent);
                            */
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("URL_ADDRESS", "https://translate.google.com/#view=home&op=translate&sl=" + MessageItemArrayList.get(position).getTALK_LANG() + "&tl=" + MY_LANG + "&text=" + Uri.encode(value));
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent);
                        }
                    } else {
                        if (Consts.MESSAGE_TYPE_TEXT.equals(MessageItemArrayList.get(position).getTALK_TYPE())) {
                            Intent intent = new Intent(context, ChatDownloadActivity.class);
                            intent.putExtra("TALK_TYPE", MessageItemArrayList.get(position).getTALK_TYPE());
                            intent.putExtra("TALK_TEXT_VOICE", MessageItemArrayList.get(position).getTALK_TEXT_VOICE());
                            intent.putExtra("TALK_TEXT_IMAGE", MessageItemArrayList.get(position).getTALK_TEXT_IMAGE());
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent);
                        } else {
                            String fileName;

                            if (Consts.MESSAGE_TYPE_IMAGE.equals(MessageItemArrayList.get(position).getTALK_TYPE())) {
                                fileName = MessageItemArrayList.get(position).getTALK_TEXT_IMAGE();
                            } else {
                                fileName = MessageItemArrayList.get(position).getTALK_TEXT_VOICE();
                            }

                            long lastTime = Long.parseLong(fileName.substring(0, fileName.indexOf('_')));
                            long curTime = System.currentTimeMillis();
                            long diffTime = (curTime - lastTime) / (1000 * 60 * 60 * 24);

                            if (diffTime > Consts.ATTACH_FILE_MAX_DAY_PERIOD) {
                                Toast.makeText(context, context.getApplicationContext().getResources().getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(context, ChatDownloadActivity.class);
                                intent.putExtra("TALK_TYPE", MessageItemArrayList.get(position).getTALK_TYPE());
                                intent.putExtra("TALK_TEXT_VOICE", MessageItemArrayList.get(position).getTALK_TEXT_VOICE());
                                intent.putExtra("TALK_TEXT_IMAGE", MessageItemArrayList.get(position).getTALK_TEXT_IMAGE());
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MessageItemArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPicture;
        final TextView tv_talk_text;
        final TextView tv_countryName_talk_target;
        final TextView tv_atx_local_time;
        final LinearLayout list_item_rows;
        final Button btnTranslation;
        final ImageView ivTalkTypeVoiceImage;

        MyViewHolder(View view) {
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tv_talk_text = view.findViewById(R.id.tv_talk_text);
            tv_countryName_talk_target = view.findViewById(R.id.tv_countryName_talk_target);
            tv_atx_local_time = view.findViewById(R.id.tv_atx_local_time);
            list_item_rows = view.findViewById(R.id.list_item_rows);
            btnTranslation = view.findViewById(R.id.btnTranslation);
            ivTalkTypeVoiceImage = view.findViewById(R.id.ivTalkTypeVoiceImage);
        }
    }
}
