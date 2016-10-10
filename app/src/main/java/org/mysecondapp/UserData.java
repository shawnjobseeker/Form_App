package org.mysecondapp;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import io.realm.RealmObject;


/**
 * Created by Shawn Li on 10/5/2016.
 */
public class UserData implements Parcelable {

    private int ID;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String dob;
    private byte[] picBlob;
    private String country;
    private String gender = "";
    private Bitmap pic;

    public UserData() {

    }
    public UserData(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        dob = in.readString();
        picBlob = in.createByteArray();
        country = in.readString();
        gender = in.readString();
        pic = in.readParcelable(Bitmap.class.getClassLoader());
    }
    @Override
    public String toString(){
        return name + " " + (gender.startsWith("M") ? "♂" : "♀");
    }
    public String detailedToString() {
        return "Name: " + name + "\nDate of Birth: " + dob + "\nAddress: " + address + "\nE-Mail: " + email + "\nPhone Number: " + phone + "\nCountry of Birth: " + country + "\n" + gender;
    }
    public int getID(){
        return this.ID;
    }
    public String getName(){
    return this.name;
    }
    public String getDob(){
        return this.dob;
    }
    public String getAddress(){
        return this.address;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPhone(){
return this.phone;
    }
    public String getCountry() {
        return this.country;
    }
    public String getGender() {
        return this.gender;
    }
    public Bitmap getImage() {
        if (pic == null && picBlob != null)
            pic = BitmapFactory.decodeByteArray(picBlob, 0, picBlob.length);
        return pic;
    }
    public byte[] getPicBlob()
    {
        return this.picBlob;
    }
    @TargetApi(12)
    private byte[] getImageBlob(Bitmap pic)
    {
        if (pic == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }
    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            throw new UnsupportedOperationException("newArray(int size)");
        }
    };
    void setAll(int id, String name, String phone, String email, String address, String dob, byte[] picBlob, String country, String gender ){
        this.ID = id;
        this.name = name;
        this.dob = dob;
        this.picBlob = picBlob;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.country = country;
    }
    public void setAll(String name, String phone, String email, String address, String dob, Bitmap pic, String country, String gender ){
        this.name = name;
        this.dob = dob;
        this.picBlob = getImageBlob(pic);
        this.pic = pic;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.country = country;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(phone);
        out.writeString(email);
        out.writeString(address);
        out.writeString(dob);
        out.writeByteArray(picBlob);
        out.writeString(country);
        out.writeString(gender);
        out.writeParcelable(pic, flags);

    }
    public int describeContents() {
        return 8;
    }
}
