package com.lcb.one.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableDemo implements Parcelable {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        //内容描述接口，基本不用管
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //写入接口函数，打包
        dest.writeString(name);
        dest.writeInt(age);
    }


    /*当出现：
    According to the Parcelable interface documentation, "Classes implementing the Parcelable interface must also have a" +
    " static field called CREATOR, which is an object implementing the Parcelable.Creator interface."
    （根据Parcelable接口文档，“实现Parcelable接口的类还必须有一个名为CREATOR的静态字段，它是实现Parcelable.Creator接口的对象。”）*/

    //你就要建立如下内容：
    public static final Creator<ParcelableDemo> CREATOR = new Creator<ParcelableDemo>() {
        @Override
        public ParcelableDemo createFromParcel(Parcel source) {
//读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板参数传入
            //为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数分别返回单个和多个继承类实例
            ParcelableDemo mPerson = new ParcelableDemo();
            mPerson.name = source.readString();
            mPerson.age = source.readInt();
            return mPerson;
        }

        @Override
        public ParcelableDemo[] newArray(int size) {
            return new ParcelableDemo[size];
        }
    };


}
