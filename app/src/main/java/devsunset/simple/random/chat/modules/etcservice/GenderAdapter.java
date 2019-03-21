/*
 * @(#)GenderAdapter.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.etcservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import devsunset.simple.random.chat.R;

/**
 * <PRE>
 * SimpleRandomChat GenderAdapter
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class GenderAdapter extends ArrayAdapter<GenderOption> {

    public GenderAdapter(Context context, List<GenderOption> GenderOptions) {
        super(context, 0, GenderOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gender_option, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        GenderOption option = getItem(position);
        vh.gender_description.setText(option.gender_description);
        vh.gender_value.setText(option.gender_value);
        return convertView;
    }

    private static final class ViewHolder {
        TextView gender_description;
        TextView gender_value;

        public ViewHolder(View v) {
            gender_description = (TextView) v.findViewById(R.id.gender_description);
            gender_value = (TextView) v.findViewById(R.id.gender_value);
        }
    }
}
