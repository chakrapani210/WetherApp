package com.chakra.wuweather.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Error implements Parcelable {
    public enum ErrorCode {
        INTERNET_UNAVAILABLE(1, "Intenet not available. PLease check."),
        INVALID_ZIP_CODE(2, "Invalid Zip code was entered"),
        DATA_NOT_AVAILABLE(3, "Data not available at the Wether report Provider");
        private int errorCode;
        private String msg;
        private ErrorCode(int errorCode, String msg) {
            this.errorCode = errorCode;
            this.msg = msg;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    private int errorCode;
    private String msg;

    public Error(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.msg = errorCode.getMsg();
    }

    public Error(ErrorCode errorCode, String msg) {
        this.errorCode = errorCode.getErrorCode();
        this.msg = msg;
    }

    public Error(Parcel source) {
        errorCode = source.readInt();
        msg = source.readString();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.msg = errorCode.getMsg();
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Creator<Error> CREATOR = new Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel source) {
            return new Error(source);
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[0];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errorCode);
        dest.writeString(msg);
    }
}
