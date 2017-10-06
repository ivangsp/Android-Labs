package ee.ojiambout.ivan.homeassignment1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ivan on 10/2/2017.
 */

public class Contact implements Parcelable {
    private String name;
    private String email;
    private String number;
    private String id;

    public Contact(String id, String name, String email, String number) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        number = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(number);
        parcel.writeString(id);

    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int i) {
            return new Contact[i];
        }
    };
}
