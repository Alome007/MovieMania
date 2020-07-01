package com.codebosses.flicks.api;

import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsMainObject;
import com.codebosses.flicks.pojo.celebrity_detail.CelebrityDetailMainObject;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.moviespojo.ExternalId;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.latestmovies.LatestMoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.reviews.ReviewsMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.pojo.tvseasons.TvSeasonsMainObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    //        String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
//    String XWWWORMURLENCODED = "application/x-www-form-urlencoded";
//    String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
//
//    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Response response = chain.proceed(chain.request());
//            // Do anything with response here
//            response.header("Content-Type", APPLICATION_JSON_CHARSET_UTF_8);
//            response.header("Accept", APPLICATION_JSON_CHARSET_UTF_8);
//            return response;
//        }
//    }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).readTimeout(120, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS).retryOnConnectionFailure(true);
//    OkHttpClient client = httpClient.build();
//
//    Api WEB_SERVICE = new Retrofit.Builder()
//            .baseUrl(EndpointUrl.MOVIE_DB_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build().create(Api.class);

    @GET("movie/upcoming")
    Call<MoviesMainObject> getUpcomingMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.REGION) String region);

    @GET("movie/top_rated")
    Call<MoviesMainObject> getTopRatedMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.REGION) String region);

    @GET("movie/popular")
    Call<MoviesMainObject> getLatestMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.REGION) String region);

    @GET("movie/now_playing")
    Call<MoviesMainObject> getInTheaterMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.REGION) String region);

    @GET("person/popular")
    Call<CelebritiesMainObject> getTopRatedCelebrities(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("tv/top_rated")
    Call<TvMainObject> getTopRatedTvShows(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("tv/popular")
    Call<TvMainObject> getLatestTvShows(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("movie/{movie_id}/videos")
    Call<MoviesTrailerMainObject> getMovieTrailer(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("movie/{movie_id}")
    Call<MovieDetailMainObject> getMovieDetail(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("movie/{movie_id}/similar")
    Call<MoviesMainObject> getSimilarMovies(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("movie/{movie_id}/recommendations")
    Call<MoviesMainObject> getSuggestedMovies(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("tv/{tv_id}/videos")
    Call<MoviesTrailerMainObject> getTvTrailer(@Path(EndpointKeys.TV_ID) String tv_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("tv/{tv_id}")
    Call<TvShowsDetailMainObject> getTvDetail(@Path(EndpointKeys.TV_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("tv/{tv_id}/similar")
    Call<TvMainObject> getSimilarTvShows(@Path(EndpointKeys.TV_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("tv/{tv_id}/recommendations")
    Call<TvMainObject> getSuggestedTvShows(@Path(EndpointKeys.TV_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("search/movie")
    Call<MoviesMainObject> searchMovie(@Query(EndpointKeys.QUERY) String query, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.INCLUDE_ADULT) boolean include_adult);

    @GET("search/tv")
    Call<TvMainObject> searchTvShows(@Query(EndpointKeys.QUERY) String query, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("search/person")
    Call<CelebritiesMainObject> searchCelebrity(@Query(EndpointKeys.QUERY) String query, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.INCLUDE_ADULT) boolean include_adult);

    @GET("tv/on_the_air")
    Call<TvMainObject> getTvOnAir(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("tv/airing_today")
    Call<TvMainObject> getTvAiringToday(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/movie/{movie_id}/credits")
    Call<CastAndCrewMainObject> getMovieCredits(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key);

    @GET("/3/person/{person_id}/movie_credits")
    Call<CelebMoviesMainObject> getCelebMovies(@Path(EndpointKeys.PERSON_ID) String person_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/person/{person_id}/tv_credits")
    Call<CelebTvShowsMainObject> getCelebTvShows(@Path(EndpointKeys.PERSON_ID) String person_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/credits")
    Call<CastAndCrewMainObject> getTvCredits(@Path(EndpointKeys.TV_ID) String tv_id, @Query(EndpointKeys.API_KEY) String api_key);

    @GET("/3/tv/{tv_id}/season/{season_number}")
    Call<TvSeasonsMainObject> getTvSeasonDetail(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) String season_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/season/{season_number}/videos")
    Call<MoviesTrailerMainObject> getTvSeasonTrailer(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) String season_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/season/{season_number}/credits")
    Call<CastAndCrewMainObject> getTvSeasonCredits(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) String season_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/season/{season_number}/episode/{episode_number}/videos")
    Call<MoviesTrailerMainObject> getTvEpisodeTrailer(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) String season_number, @Path(EndpointKeys.EPISODE_NUMBER) String episode_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/season/{season_number}/episode/{episode_number}/images")
    Call<EpisodePhotosMainObject> getTvEpisodePhotos(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) String season_number, @Path(EndpointKeys.EPISODE_NUMBER) String episode_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewsMainObject> getMovieReviews(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/trending/{media_type}/{time_window}")
    Call<MoviesMainObject> getMoviesTrending(@Path(EndpointKeys.MEDIA_TYPE) String media_type, @Path(EndpointKeys.TIME_WINDOW) String time_window, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/trending/{media_type}/{time_window}")
    Call<TvMainObject> getTvShowsTrending(@Path(EndpointKeys.MEDIA_TYPE) String media_type, @Path(EndpointKeys.TIME_WINDOW) String time_window, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/trending/{media_type}/{time_window}")
    Call<CelebritiesMainObject> getCelebTrending(@Path(EndpointKeys.MEDIA_TYPE) String media_type, @Path(EndpointKeys.TIME_WINDOW) String time_window, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/movie/latest")
    Call<LatestMoviesMainObject> getLatestMovie(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/tv/{tv_id}/reviews")
    Call<ReviewsMainObject> getTvReviews(@Path(EndpointKeys.TV_ID) String tv_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page);

    @GET("/3/movie/{movie_id}/images")
    Call<EpisodePhotosMainObject> getMoviesImages(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query("include_image_language") String image_language);

    @GET("/3/discover/movie")
    Call<MoviesMainObject> getGenreMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query("sort_by") String sort_by, @Query(EndpointKeys.INCLUDE_ADULT) boolean include_adult, @Query("include_video") boolean include_video, @Query(EndpointKeys.PAGE) int page, @Query("with_genres") int with_genre);

    @GET("/3/discover/tv")
    Call<TvMainObject> getGenreTvShows(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query("sort_by") String sort_by, @Query(EndpointKeys.PAGE) int page, @Query("with_genres") int with_genre);

    @GET("/3/tv/{tv_id}/images")
    Call<EpisodePhotosMainObject> getTvImages(@Path(EndpointKeys.TV_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query("include_image_language") String image_language);

    @GET("/3/tv/{tv_id}/season/{season_number}/images")
    Call<EpisodePhotosMainObject> getTvSeasonImages(@Path(EndpointKeys.TV_ID) String tv_id, @Path(EndpointKeys.SEASON_NUMBER) int season_number, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/person/{person_id}/images")
    Call<EpisodePhotosMainObject> getCelebImages(@Path(EndpointKeys.PERSON_ID) String person_id, @Query(EndpointKeys.API_KEY) String api_key);

    @GET("/3/person/{person_id}")
    Call<CelebrityDetailMainObject> getCelebDetail(@Path(EndpointKeys.PERSON_ID) String person_id, @Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language);

    @GET("/3/movie/{movie_id}/external_ids")
    Call<ExternalId> getMovieExternalId(@Path(EndpointKeys.MOVIE_ID) String movie_id, @Query(EndpointKeys.API_KEY) String api_key);

    @GET("/3/tv/{tv_id}/external_ids")
    Call<ExternalId> getTvExternalId(@Path(EndpointKeys.TV_ID) String tv_id,@Query(EndpointKeys.API_KEY) String api_key);

}