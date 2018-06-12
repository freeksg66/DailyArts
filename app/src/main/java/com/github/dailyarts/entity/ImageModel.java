package com.github.dailyarts.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class ImageModel implements Parcelable{
    private String id;
    private String name;
    private String author;
    private String pic;
    private String image;
    private String bigImg;
    private String detail;
    private String publish;
    private String commentCount;
    private Integer commentcount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected ImageModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        author = in.readString();
        pic = in.readString();
        image = in.readString();
        bigImg = in.readString();
        detail = in.readString();
        publish = in.readString();
        commentCount = in.readString();
        if (in.readByte() == 0) {
            commentcount = null;
        } else {
            commentcount = in.readInt();
        }
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBigImg() {
        return bigImg;
    }

    public void setBigImg(String bigImg) {
        this.bigImg = bigImg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(pic);
        dest.writeString(image);
        dest.writeString(bigImg);
        dest.writeString(detail);
        dest.writeString(publish);
        dest.writeString(commentCount);
        if (commentcount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(commentcount);
        }
    }
}
