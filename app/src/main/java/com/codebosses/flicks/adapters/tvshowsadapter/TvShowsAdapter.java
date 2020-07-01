package com.codebosses.flicks.adapters.tvshowsadapter;

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
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private String tvShowType;

    public TvShowsAdapter(Context context, List<TvResult> tvResultArrayList, String tvShowType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.tvResultArrayList = tvResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.tvShowType = tvShowType;
    }

    @NonNull
    @Override
    public TvShowsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_tv_shows, parent, false);
        return new TvShowsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsHolder holder, int position) {
        TvResult tvResult = tvResultArrayList.get(position);
        if (tvResult != null) {
            String posterPath = tvResult.getPoster_path();
            String title = tvResult.getOriginal_name();
            String releaseDate = tvResult.getFirst_air_date();
            double voteAverage = tvResult.getVote_average();
            int voteCount = tvResult.getVote_count();
            if (posterPath != null && !posterPath.equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + tvResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            if (title != null && !title.isEmpty())
                holder.textViewMovieTitle.setText(title);
            else
                holder.textViewMovieTitle.setText("No Title Fount");
            if (releaseDate != null && !releaseDate.isEmpty()) {
                holder.textViewMovieYear.setText(releaseDate);
            } else {
                holder.textViewMovieYear.setText("No First Air Date");
            }
            holder.textViewRatingCount.setText(String.valueOf(voteAverage));
            holder.textViewVoteCount.setText(String.valueOf(voteCount));
        }
    }

    @Override
    public int getItemCount() {
        return tvResultArrayList.size();
    }

    class TvShowsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailTvShowsRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleTvShowsRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewYearTvShowsRow)
        TextView textViewMovieYear;
        @BindView(R.id.textViewAudienceTvShowsRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingTvShowsRow)
        TextView textViewRatingCount;
        @BindView(R.id.textViewVotesTvShowsRow)
        TextView textViewVoteCount;
        @BindView(R.id.textViewVotesCountTvShowsRow)
        TextView textViewVoteCountText;

        TvShowsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);
            fontUtils.setTextViewLightFont(textViewMovieYear);
            fontUtils.setTextViewLightFont(textViewRatingText);
            fontUtils.setTextViewLightFont(textViewVoteCountText);
            fontUtils.setTextViewRegularFont(textViewVoteCount);
            fontUtils.setTextViewRegularFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), tvShowType));
        }

    }

}
