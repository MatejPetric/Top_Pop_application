
package matej.petric.toppop.album;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genres {

    @SerializedName("data")
    @Expose
    private List<AlbumData> data = null;

    public List<AlbumData> getData() {
        return data;
    }

    public void setData(List<AlbumData> data) {
        this.data = data;
    }

}
