package com.izzedineeita.mihrab.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.izzedineeita.mihrab.R;
// import com.smarteist.autoimageslider.SliderViewAdapter; // Removed Image Slider dependency

import java.util.List;

// public class SliderAdapter extends
//         SliderViewAdapter<SliderAdapter.SliderAdapterVH> { // Removed Image Slider dependency
public class SliderAdapter { // Temporary placeholder

    private List<String> mSliderItems;

    public SliderAdapter(List<String> mSliderItems) {
        this.mSliderItems = mSliderItems;
    }

    // @Override
    // public SliderAdapterVH onCreateViewHolder(ViewGroup parent) { // Removed Image Slider dependency
    //     View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
    //     return new SliderAdapterVH(inflate);
    // }

    // @Override
    // public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) { // Removed Image Slider dependency

    //     String sliderItem = mSliderItems.get(position);
    //     Glide.with(viewHolder.itemView)
    //             .load(sliderItem)
    //             .fitCenter()
    //             .into(viewHolder.imageViewBackground);
    // }

    // @Override
    // public int getCount() { // Removed Image Slider dependency
    //     //slider view count could be dynamic size
    //     return mSliderItems.size();
    // }

    // class SliderAdapterVH extends SliderViewAdapter.ViewHolder { // Removed Image Slider dependency

    //     View itemView;
    //     ImageView imageViewBackground;

    //     public SliderAdapterVH(View itemView) {
    //         super(itemView);
    //         imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
    //         this.itemView = itemView;
    //     }
    // }

}