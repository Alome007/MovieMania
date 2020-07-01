package com.codebosses.flicks.adapters.favorite;

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
import com.codebosses.flicks.database.entities.TvShowEntity;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteTvShowsAdapter extends RecyclerView.Adapter<FavoriteTvShowsAdapter.MoviesHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<TvShowEntity> moviesResultArrayList = new ArrayList<>();
    private String movieType;

    public FavoriteTvShowsAdapter(Context context, List<TvShowEntity> moviesResultArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_tv_shows, parent, false);
        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder holder, int position) {
        TvShowEntity moviesResult = moviesResultArrayList.get(position);
        if (moviesResult != null) {
            String posterPath = moviesResult.getPosterPath();
            String title = moviesResult.getName();
            String releaseDate = moviesResult.getFirstAirDate();
            double voteAverage = moviesResult.getVoteAverage();
            int voteCount = 0;
            if (posterPath != null && !posterPath.equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + posterPath)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            if (title != null && !title.isEmpty()) {
                holder.textViewMovieTitle.setText(title);
            } else {
                holder.textViewMovieTitle.setText("No Title Found");
            }
            if (releaseDate != null && !releaseDate.isEmpty())
                holder.textViewMovieYear.setText(releaseDate);
            else {
                holder.textViewMovieYear.setText("No Release Date");
            }
            holder.textViewRatingCount.setText(String.valueOf(voteAverage));
            holder.textViewVoteCount.setText(String.valueOf(voteCount));
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        MoviesHolder(@NonNull View itemView) {
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
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), movieType));
        }

    }

}
