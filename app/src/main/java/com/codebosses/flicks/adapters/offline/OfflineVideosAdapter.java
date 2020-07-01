package com.codebosses.flicks.adapters.offline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.eventbus.EventBusOfflineClick;
import com.codebosses.flicks.pojo.offlinepojo.OfflineModel;
import com.codebosses.flicks.utils.DateUtils;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineVideosAdapter extends RecyclerView.Adapter<OfflineVideosAdapter.OfflineVideosHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<OfflineModel> offlineModelList = new ArrayList<>();

    public OfflineVideosAdapter(Context context, List<OfflineModel> offlineModelList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.offlineModelList = offlineModelList;
    }

    @NonNull
    @Override
    public OfflineVideosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfflineVideosHolder(layoutInflater.inflate(R.layout.row_offline_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineVideosHolder holder, int position) {
        OfflineModel offlineModel = offlineModelList.get(position);
        if (offlineModel != null) {
            Glide.with(context)
                    .load(offlineModel.getThumbnail())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .apply(new RequestOptions().error(R.drawable.placeholder))
                    .thumbnail(0.f)
                    .into(holder.imageViewOfflineThubnail);
            holder.textViewDuration.setText(DateUtils.convertSecondsToHMmSs(offlineModel.getDuration()));
            holder.textViewTitle.setText(offlineModel.getName());
            Date modifiedDate = null;
            try {
                modifiedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(offlineModel.getDate());
            } catch (Exception e) {

            }
            String lastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(modifiedDate);
            holder.textViewDate.setText(lastDate);
        }
    }

    @Override
    public int getItemCount() {
        return offlineModelList.size();
    }

    class OfflineVideosHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewThumbnailRowOffline)
        ImageView imageViewOfflineThubnail;
        @BindView(R.id.textViewDurationRowOffline)
        TextView textViewDuration;
        @BindView(R.id.textViewTitleRowOffline)
        TextView textViewTitle;
        @BindView(R.id.textViewDateRowOffline)
        TextView textViewDate;
        @BindView(R.id.imageViewMenuOfflineRow)
        ImageView imageViewDots;

        public OfflineVideosHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewTitle);
            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewDuration);
            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventBusOfflineClick(getAdapterPosition(),"offline",v));
                }
            });
            imageViewDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventBusOfflineClick(getAdapterPosition(),"menu",v));
                }
            });
        }
    }
}
