package com.codebosses.flicks.fragments.celebritiesfragments;

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
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.CelebrityDetailActivity;
import com.codebosses.flicks.activities.CelebrityMoviesActivity;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebritiesAdapter;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentTopRatedCelebrities extends BaseFragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageTopRatedCelebrities)
    TextView textViewError;
    @BindView(R.id.circularProgressBarTopRatedCelebrities)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewTopRatedCelebrities)
    RecyclerView recyclerViewTopRatedCelebrities;
    @BindView(R.id.imageViewErrorTopRatedCelebrities)
    AppCompatImageView imageViewError;
    @BindView(R.id.textViewRetryMessageTopRatedCeleb)
    TextView textViewRetry;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.could_not_get_celebrities)
    String couldNotGetCelebrities;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<CelebritiesMainObject> celebritiesMainObjectCall;

    //    Adapter fields....
    private CelebritiesAdapter celebritiesAdapter;

    //    Instance fields....
    private List<CelebritiesResult> celebritiesResultList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    public FragmentTopRatedCelebrities() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_rated_celebrities, container, false);
        ButterKnife.bind(this, view);

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewRetry);

        if (getActivity() != null) {
            celebritiesAdapter = new CelebritiesAdapter(getActivity(), celebritiesResultList, EndpointKeys.TOP_RATED_CELEBRITIES);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewTopRatedCelebrities.setLayoutManager(linearLayoutManager);
            recyclerViewTopRatedCelebrities.setAdapter(celebritiesAdapter);
            recyclerViewTopRatedCelebrities.setItemAnimator(new FadeInDownAnimator());
            if (recyclerViewTopRatedCelebrities.getItemAnimator() != null)
                recyclerViewTopRatedCelebrities.getItemAnimator().setAddDuration(500);
            loadTopRatedCelebritiesFirstTime();
        }
        recyclerViewTopRatedCelebrities.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTopRatedCelebrities("en-US", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (celebritiesMainObjectCall != null && celebritiesMainObjectCall.isExecuted()) {
            celebritiesMainObjectCall.cancel();
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

    private void getTopRatedCelebrities(String language, int pageNumber) {
        celebritiesMainObjectCall = ApiClient.getClient().create(Api.class).getTopRatedCelebrities(EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        celebritiesMainObjectCall.enqueue(new Callback<CelebritiesMainObject>() {
            @Override
            public void onResponse(Call<CelebritiesMainObject> call, retrofit2.Response<CelebritiesMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewError.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    CelebritiesMainObject celebritiesMainObject = response.body();
                    if (celebritiesMainObject != null) {
                        totalPages = celebritiesMainObject.getTotal_pages();
                        if (celebritiesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < celebritiesMainObject.getResults().size(); i++) {
                                celebritiesResultList.add(celebritiesMainObject.getResults().get(i));
                                celebritiesAdapter.notifyItemInserted(celebritiesResultList.size() - 1);
                            }
                        }
                    }
                } else {
                    if (pageNumber == 1) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetCelebrities);
                        imageViewError.setVisibility(View.VISIBLE);
                        textViewRetry.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CelebritiesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                if (pageNumber == 1) {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewError.setVisibility(View.VISIBLE);
                    textViewRetry.setVisibility(View.VISIBLE);
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        if (pageNumber == 1)
                            textViewError.setText(internetProblem);
                    } else {
                        if (pageNumber == 1)
                            textViewError.setText(couldNotGetCelebrities);
                    }
                } else {
                    if (pageNumber == 1)
                        textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

    private void loadTopRatedCelebritiesFirstTime() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            circularProgressBar.setVisibility(View.VISIBLE);
            getTopRatedCelebrities("en-US", pageNumber);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryMessageTopRatedCeleb)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewError.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadTopRatedCelebritiesFirstTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTopRatedCelebrities(EventBusCelebrityClick eventBusCelebrityClick) {
        if (eventBusCelebrityClick.getCelebType().equals(EndpointKeys.TOP_RATED_CELEBRITIES)) {
            Intent intent = new Intent(getActivity(), CelebrityDetailActivity.class);
            intent.putExtra(EndpointKeys.CELEBRITY_ID, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.CELEB_NAME, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.CELEB_IMAGE, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getProfile_path());
            startActivity(intent);
        }
    }

}
