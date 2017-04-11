package kr.ds.handler;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Administrator on 2016-08-05.
 */
public class ListHandler  implements Parcelable{

    public String title;
    public String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ListHandler(){

    }

    public ListHandler (Parcel src){
        this.title = src.readString();
        this.url = src.readString();


    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(this.title);
        dest.writeString(this.url);



    }
    public static final Creator CREATOR = new Creator() { //데이터 가져오기

        @Override
        public Object createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new ListHandler(in);
        }
        @Override
        public Object[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ListHandler[size];
        }
    };





}
