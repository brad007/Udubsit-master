package com.ionicframework.udubsit252887.Utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CoLab on 2016/05/11.
 */
public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static String getPushId() {
        String url = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY).push().toString();
        String[] arr = url.split("/");
        return arr[arr.length - 1];
    }

    public static String getDay(int n) {

        switch (n) {
            case 1:
                return "Mon";
            case 2:
                return "Tues";
            case 3:
                return "Wed";
            case 4:
                return "Thurs";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            default:
                return "Sun";
        }
    }

    public static String getMonth(int n) {
        switch (n) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "Aug";
            case 8:
                return "Sept";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            default:
                return "Dec";
        }
    }


    public static String getUserEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getEmail().replace(".", ",");
        }
        return null;
    }

    public static String getDate(String date) {
        if (date != null) {
            String[] ADate = date.split("/");
            String temp = ADate[0] + " ";

            switch (Integer.parseInt(ADate[1])) {
                case 1:
                    temp += "Jan ";
                    break;
                case 2:
                    temp += "Feb ";
                    break;
                case 3:
                    temp += "Mar ";
                    break;
                case 4:
                    temp += "Apr ";
                    break;
                case 5:
                    temp += "May  ";
                    break;
                case 6:
                    temp += "Jun ";
                    break;
                case 7:
                    temp += "Jul ";
                    break;
                case 8:
                    temp += "Aug ";
                    break;
                case 9:
                    temp += "Sep ";
                    break;
                case 10:
                    temp += "Oct ";
                    break;
                case 11:
                    temp += "Nov ";
                    break;
                case 12:
                    temp += "Dec ";
                    break;
            }

            return temp += ADate[2];
        }
        return "No Date Set";
    }

    public static String getCaseSensitiveText(String name) {
        String[] A = name.split(" ");
        String temp = "";
        for (int i = 0; i < A.length; i++) {
            A[i] = A[i].charAt(0) + A[i].toLowerCase().substring(1);
            if (i < A.length - 1) {
                temp += A[i] + " ";
            } else {
                temp += A[i];
            }
        }
        return temp;
    }

    public static String getDate(long date) {
        Date date1 = new Date();
        date1.setTime(date);
        String dateStr = "";
        switch (date1.getDay()) {
            case 1:
                dateStr = "Monday,";
                break;
            case 2:
                dateStr = "Tuesday,";
                break;
            case 3:
                dateStr = "Wednesday,";
                break;
            case 4:
                dateStr = "Thursday,";
                break;
            case 5:
                dateStr = "Friday,";
                break;
            case 6:
                dateStr = "Saturday,";
                break;
            case 7:
                dateStr = "Sunday,";
                break;
        }

        dateStr += (" " + date1.getDate());

        switch (date1.getMonth()) {
            case 1:
                dateStr += (" January");
                break;
            case 2:
                dateStr += (" February");
                break;
            case 3:
                dateStr += (" March");
                break;
            case 4:
                dateStr += (" April");
                break;
            case 5:
                dateStr += (" May");
                break;
            case 6:
                dateStr += (" June");
                break;
            case 7:
                dateStr += (" July");
                break;
            case 8:
                dateStr += (" August");
                break;
            case 9:
                dateStr += (" September");
                break;
            case 10:
                dateStr += (" October");
                break;
            case 11:
                dateStr += (" Novermber");
                break;
            case 12:
                dateStr += (" January");
                break;
        }

        return dateStr;
    }


    public static String getEventDate(long n) {
        Date d = new Date(n);
        return getDate(n) + " " + d.getHours() + ":" + d.getMinutes();
    }

    public static void removeFromList(DatabaseReference databaseRef, final Context context, final DatabaseReference groupList) {
        databaseRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ArrayList arrayList = (ArrayList) dataSnapshot.getValue();
                    arrayList.remove(Utils.getUserEmail().replace(".", ","));
                    groupList.setValue(arrayList);
                    Toast.makeText(context, "Left", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void addToList(final DatabaseReference databaseRef, Context context) {
        databaseRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                ArrayList arrayList = (ArrayList) dataSnapshot.getValue();
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(Utils.getUserEmail().replace(".", ","));
                databaseRef.setValue(arrayList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
