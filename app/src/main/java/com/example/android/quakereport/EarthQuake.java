package com.example.android.quakereport;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class EarthQuake {
    private final String LOG_TAG = EarthQuake.class.getSimpleName();
    private double mMagnitude;
    private String mLocation;
    private long mDate;
    private String mUrl;

    public EarthQuake(double mMagnitude, String mLocation, long mDate,String webSites) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mDate = mDate;
        this.mUrl = webSites;
    }

    public String getmMagnitude() {
        return new DecimalFormat("0.0").format(mMagnitude);
    }

    public String getmDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(mDate);
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        return dateFormat.format(mDate);
    }

    public String[] getLocation() {
        String mainLocation = mLocation;
        String nearLocation = null;
        String[] part = null;

        if (mainLocation.contains("of")) {
            part = mainLocation.split("of ");
            part[0] += " of";
        } else {
            part = new String[]{
                    nearLocation = "Near the",
                    mainLocation,
            };
        }

        return part;
    }

    public String getmUrl() {
        return mUrl;
    }

    public int getCircleColor() {
            int magnitudeColorResourceId;
            int magnitudeFloor = (int) Math.floor(mMagnitude);
            switch (magnitudeFloor) {
                case 0:
                case 1:
                    magnitudeColorResourceId = R.color.magnitude1;
                    break;
                case 2:
                    magnitudeColorResourceId = R.color.magnitude2;
                    break;
                case 3:
                    magnitudeColorResourceId = R.color.magnitude3;
                    break;
                case 4:
                    magnitudeColorResourceId = R.color.magnitude4;
                    break;
                case 5:
                    magnitudeColorResourceId = R.color.magnitude5;
                    break;
                case 6:
                    magnitudeColorResourceId = R.color.magnitude6;
                    break;
                case 7:
                    magnitudeColorResourceId = R.color.magnitude7;
                    break;
                case 8:
                    magnitudeColorResourceId = R.color.magnitude8;
                    break;
                case 9:
                    magnitudeColorResourceId = R.color.magnitude9;
                    break;
                default:
                    magnitudeColorResourceId = R.color.magnitude10plus;
                    break;
            }
            return magnitudeColorResourceId;
    }

    @Override
    public String toString() {
        return "EarthQuake{" +
                "mMagnitude=" + mMagnitude +
                ", mLocation='" + mLocation + '\'' +
                ", mDate='" + mDate + '\'' +
                '}';
    }

}
