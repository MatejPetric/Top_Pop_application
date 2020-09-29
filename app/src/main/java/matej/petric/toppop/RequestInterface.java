package matej.petric.toppop;

import matej.petric.toppop.album.AlbumMainObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

interface RequestInteface {
    @GET("chart/0/tracks")
    Call<MovieObject> getSongJson();

    @GET("album/{album_id}")
    Call<AlbumMainObject> getAlbumJson(@Path("album_id") String album_id);
}

