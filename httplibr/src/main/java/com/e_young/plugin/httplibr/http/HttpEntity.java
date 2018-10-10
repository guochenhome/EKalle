package com.e_young.plugin.httplibr.http;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 数据沙盒漏斗
 * 配合过滤器拦截的bean类
 * @author guochen
 * @see 2018.9.21
 */
public class HttpEntity implements Parcelable {

    @JSONField(name = "succeed")
    private boolean mSucceed;

    @JSONField(name="message")
    private String mMessage;

    @JSONField(name="data")
    private String mData;


    public HttpEntity() {

    }

    protected HttpEntity(Parcel in) {
        mSucceed = in.readByte() != 0;
        mMessage = in.readString();
        mData = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mSucceed ? 1 : 0));
        dest.writeString(mMessage);
        dest.writeString(mData);
    }

    public boolean ismSucceed() {
        return mSucceed;
    }

    public void setmSucceed(boolean mSucceed) {
        this.mSucceed = mSucceed;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public static final Creator<HttpEntity> CREATOR = new Creator<HttpEntity>() {
        @Override
        public HttpEntity createFromParcel(Parcel in) {
            return new HttpEntity(in);
        }

        @Override
        public HttpEntity[] newArray(int size) {
            return new HttpEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
