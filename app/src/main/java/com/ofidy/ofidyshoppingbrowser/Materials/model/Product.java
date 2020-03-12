package com.ofidy.ofidyshoppingbrowser.Materials.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ari on 10/11/16.
 */
public class Product implements Parcelable {

    private String id;
    private String name;
    private String image;
    private String agentId;
    private int quantity;
    private String region;
    private String currencyCode;
    private String currency;
    private double price;
    private String description;
    private String spec;
    private String dimL;
    private String dimB;
    private String dimH;
    private String weight;
    private String colour;
    private String isize;
    private String battery;
    private String num;
    private String modelNum;
    private String[] images;
    private String agentSeller;

    private String isbn;
    private String publisherName;
    private String manNumber;
    private String NAFDACNumber;
    private String HCClassificationCode;
    private boolean rsize;
    private String processorType;
    private String processorSpeed;
    private String RAMSize;
    private String HDSize;
    private String HDType;
    private String OSType;
    private String WiFi;
    private String HDMI;
    private String bluetooth;
    private String USB;
    private String DVD;
    private String screenSize;
    private String keyboardLight;
    private String PSUPower;
    private String gender;
    private ArrayList<Seller> sellers;
    private ArrayList<Size> sizes;

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdatedAt() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        if(sellers != null && !sellers.isEmpty())
            return sellers.get(0).getRprice();
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDisplayPrice(){
        DecimalFormat precision = new DecimalFormat("0.00");
        return !TextUtils.isEmpty(currencyCode) ? currencyCode + precision.format(price) : currency + precision.format(price);
    }

    public String getDescription() {
        return description;
    }

    public String getSpec() {
        return spec;
    }

    public String getWeight() {
        return weight;
    }

    public String getColour() {
        return colour;
    }

    public String getIsize() {
        return colour;
    }

    public String getDimL() {
        if(TextUtils.isEmpty(dimL))
            return "Unknown or N/A";
        return dimL;
    }

    public String getDimB() {
        if(TextUtils.isEmpty(dimB))
            return "Unknown or N/A";
        return dimB;
    }

    public String getDimH() {
        if(TextUtils.isEmpty(dimH))
            return "Unknown or N/A";
        return dimH;
    }

    public String getBattery() {
        return battery;
    }

    public String getModelNum() {
        return modelNum;
    }

    public String getNum() {
        return num;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGender() {
        return gender;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getManNumber() {
        return manNumber;
    }

    public String getNAFDACNumber() {
        return NAFDACNumber;
    }

    public String getProcessorType() {
        return processorType;
    }

    public String getProcessorSpeed() {
        return processorSpeed;
    }

    public String getRAMSize() {
        return RAMSize;
    }

    public String getHDSize() {
        return HDSize;
    }

    public String getHDType() {
        return HDType;
    }

    public String getOSType() {
        return OSType;
    }

    public String getWiFi() {
        return WiFi;
    }

    public String getHDMI() {
        return HDMI;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public String getUSB() {
        return USB;
    }

    public String getDVD() {
        return DVD;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public String getKeyboardLight() {
        return keyboardLight;
    }

    public String getPSUPower() {
        return PSUPower;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(agentId);
        parcel.writeInt(quantity);
        parcel.writeString(region);
        parcel.writeString(currency);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(agentSeller);
    }

    public Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        agentId = in.readString();
        quantity = in.readInt();
        region = in.readString();
        currency = in.readString();
        price = in.readDouble();
        description = in.readString();
        agentSeller = in.readString();
    }

    public Product() {

    }

    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public ArrayList<Size> getSizes() {


        return sizes;
    }

    public void addSeller(Seller seller) {
        if(sellers == null)
            sellers = new ArrayList<>();
        sellers.add(seller);
    }

    public void addSize(Size size) {
        if(sizes == null)
            sizes = new ArrayList<>();
        sizes.add(size);
    }

    public String[] getImages() {
        return images;
    }

    public String getAgentSeller() {
        return agentSeller;
    }

    public void setAgentSeller(String s) {
        agentSeller = s;
    }

    public void setCurrency(String s) {
        currency = s;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean hasSize() {
        return rsize;
    }
}
