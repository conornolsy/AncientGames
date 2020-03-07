package conor.nolan.ancientgames.quiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private String question= "";
    private String option_A="";
    private String option_B="";
    private String option_C="";
    private String option_D="";
    private String correct_Answer="";

    public Question(String question,String option_A,String option_B,String option_C,String option_D, String correct_Answer){
        this.question = question;
        this.option_A = option_A;
        this.option_B = option_B;
        this.option_C = option_C;
        this.option_D = option_D;
        this.correct_Answer = correct_Answer;
    }

    public Question(Parcel source) {
        question = source.readString();
        option_A = source.readString();
        option_B = source.readString();
        option_C = source.readString();
        option_D = source.readString();
        correct_Answer = source.readString();
    }


    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion()
    {
        return question;
    }

    public String getOption_A()
    {
        return option_A;
    }

    public String getOption_B()
    {
        return option_B;
    }

    public String getOption_C()
    {
        return option_C;
    }

    public String getOption_D()
    {
        return option_D;
    }

    public String getCorrect_Answer()
    {
        return correct_Answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(option_A);
        dest.writeString(option_B);
        dest.writeString(option_C);
        dest.writeString(option_D);
        dest.writeString(correct_Answer);


    }
}
