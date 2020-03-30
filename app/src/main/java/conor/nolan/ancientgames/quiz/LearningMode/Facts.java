package conor.nolan.ancientgames.quiz.LearningMode;

import android.os.Parcel;
import android.os.Parcelable;

public class Facts implements Parcelable {

    private String fact= "";

    public Facts(String fact){
        this.fact = fact;
    }

    public Facts(Parcel source) {
        fact = source.readString();
    }

    public static final Creator<Facts> CREATOR = new Creator<Facts>() {
        @Override
        public Facts createFromParcel(Parcel in) {
            return new Facts(in);
        }

        @Override
        public Facts[] newArray(int size) {
            return new Facts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fact);

    }

    public String getFact()
    {
        return fact;
    }
}
