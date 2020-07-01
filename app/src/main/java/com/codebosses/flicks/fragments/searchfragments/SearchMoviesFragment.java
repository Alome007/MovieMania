package com.codebosses.flicks.fragments.searchfragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusSearchText;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchMoviesFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSearchMovies)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSearchMovies)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSearchMovies)
    RecyclerView recyclerViewSearchMovies;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.imageViewNotFoundSearchMovies)
    AppCompatImageView imageViewNotFound;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.could_not_search_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<MoviesMainObject> searchMoviesCall;

    //    Adapter fields....
    private List<MoviesResult> searchMoviesResultList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String searchText = "";

    public SearchMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_movies, container, false);
        ButterKnife.bind(this, view);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

                moviesAdapter = new MoviesAdapter(getActivity(), searchMoviesResultList, EndpointKeys.SEARCH_MOVIE);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewSearchMovies.setLayoutManager(linearLayoutManager);
                recyclerViewSearchMovies.setItemAnimator(new DefaultItemAnimator());
                recyclerViewSearchMovies.setAdapter(moviesAdapter);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewSearchMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        searchMovies(searchText, "en-US", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchMoviesCall != null && searchMoviesCall.isExecuted()) {
            searchMoviesCall.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchMovie(EventBusSearchText eventBusSearchText) {
        if (!eventBusSearchText.getSearchText().isEmpty()) {
            pageNumber = 1;
            searchText = eventBusSearchText.getSearchText();
            circularProgressBar.setVisibility(View.VISIBLE);
            moviesAdapter.notifyItemRangeRemoved(0, searchMoviesResultList.size());
            searchMoviesResultList.clear();
            searchMovies(searchText, "en-US", pageNumber);
        }
    }

    private void searchMovies(String query, String language, int pageNumber) {
        textViewError.setVisibility(View.GONE);
        imageViewNotFound.setVisibility(View.GONE);
        searchMoviesCall = ApiClient.getClient().create(Api.class).searchMovie(query, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber, true);
        searchMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        totalPages = moviesMainObject.getTotal_pages();
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewError.setVisibility(View.GONE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                try {
                                    if (!moviesMainObject.getResults().get(i).getTitle().equalsIgnoreCase("venom") &&
                                            !moviesMainObject.getResults().get(i).getRelease_date().equalsIgnoreCase("2018-09-28")) {
                                        searchMoviesResultList.add(moviesMainObject.getResults().get(i));
                                        moviesAdapter.notifyItemInserted(searchMoviesResultList.size() - 1);
                                    }
                                }catch (Exception e){

                                }
                            }
                        } else {
                            textViewError.setVisibility(View.VISIBLE);
                            textViewError.setText(getResources().getString(R.string.no_movie_found));
                            imageViewNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetMovies);
                    imageViewNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.VISIBLE);
                imageViewNotFound.setVisibility(View.VISIBLE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        textViewError.setText(internetProblem);
                    } else {
                        textViewError.setText(error.getMessage());
                    }
                } else {
                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchMovieClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SEARCH_MOVIE)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, searchMoviesResultList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, searchMoviesResultList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, searchMoviesResultList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
