//package com.example.eight.scannews.utils;
//
//import android.content.Context;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.eight.scannews.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ChannelHolder> {
//    private Context context;
//    private List<String> list = new ArrayList<>();
//    private ItemClickListener itemClickListener;
//
//    public ChannelsAdapter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
//        final ChannelHolder holder = new ChannelHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(v, (Integer) v.getTag());
//                }
//            }
//        });
//        return holder;
//        //return new ChannelHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ChannelHolder holder, int position) {
//        holder.channelText.setText(list.get(position));
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public void getData(List<String> list){
//        this.list = list;
//    }
//
//    public class ChannelHolder extends RecyclerView.ViewHolder {
//        private TextView channelText;
//        public ChannelHolder(View itemView) {
//            super(itemView);
//            channelText =  itemView.findViewById(R.id.channel_text);
//        }
//    }
//
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
//
//
//}
