package me.siketyan.webimager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import me.siketyan.webimager.R;
import me.siketyan.webimager.object.Image;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends ArrayAdapter<Image> {
    private LayoutInflater inflater;

    public ImageListAdapter(Context context, List<Image> images) {
        super(context, 0, images);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.image_list_row, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text_view);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Image image = getItem(position);
        CheckBox checkBox = holder.checkBox;
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> image.setChecked(isChecked));

        holder.textView.setText(FilenameUtils.getName(image.getUri()));
        holder.checkBox.setChecked(image.isChecked());

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }
}