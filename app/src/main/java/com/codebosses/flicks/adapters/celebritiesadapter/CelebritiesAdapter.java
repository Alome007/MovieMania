package com.codebosses.flicks.adapters.celebritiesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.CelebritiesHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<CelebritiesResult> celebritiesResultArrayList = new ArrayList<>();
    private String celebType;

    public CelebritiesAdapter(Context context, List<CelebritiesResult> celebritiesResultArrayList, String celebType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.celebritiesResultArrayList = celebritiesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.celebType = celebType;
    }

    @NonNull
    @Override
    public CelebritiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_celebrities, parent, false);
        return new CelebritiesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebritiesHolder holder, int position) {
        CelebritiesResult celebritiesResult = celebritiesResultArrayList.get(position);
        if (celebritiesResult != null) {
            String profilePath = celebritiesResult.getProfile_path();
            String name = celebritiesResult.getName();
            double popularity = 0.0;
            if (celebritiesResult.getPopularity() != null) {
                popularity = celebritiesResult.getPopularity();
            }
            if (profilePath != null && !profilePath.equals(""))
                Glide.with(context)
                        .load(EndpointUrl.PROFILE_BASE_URL + "/" + profilePath)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            if (name != null && !name.isEmpty())
                holder.textViewMovieTitle.setText(name);
            else
                holder.textViewMovieTitle.setText("No Name Found");
            holder.textViewRatingCount.setText(String.valueOf(popularity));
        }
    }

    @Override
    public int getItemCount() {
        return celebritiesResultArrayList.size();
    }

    class CelebritiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailCelebritiesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleCelebritiesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewAudienceCelebritiesRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingCelebritiesRow)
        TextView textViewRatingCount;

        CelebritiesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);
            fontUtils.setTextViewLightFont(textViewRatingText);
            fontUtils.setTextViewRegularFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusCelebrityClick(getAdapterPosition(), celebType));
        }

    }

}
