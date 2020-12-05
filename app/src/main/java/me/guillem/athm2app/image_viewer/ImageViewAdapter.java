package me.guillem.athm2app.image_viewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import me.guillem.athm2app.R;
import me.guillem.athm2app.image_viewer.MediaFile;

import java.util.List;

/**
 * * Created by Guillem on 04/12/20.
 */
public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {

    private Context context;
    private List<MediaFile> imagesFiles;

    public ImageViewAdapter(Context context, List<MediaFile> imagesFiles) {
        this.context = context;
        this.imagesFiles = imagesFiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.image_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get()
                .load(imagesFiles.get(position).getFile())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesFiles.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }


    }
}
