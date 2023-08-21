package com.example.reservation.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeSlotGenerator {
    String openingHour;
    String closingHour;
    int timeSlotInterval = 30;

    public TimeSlotGenerator() {
    }

    public TimeSlotGenerator(final String openingHour, final String closingHour) {
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(final String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(final String closingHour) {
        this.closingHour = closingHour;
    }

    public HashMap<String, Boolean> generateTimeSlot(String openHour, String closeHour, int intervalMinutes){
            HashMap<String, Boolean> timeSlots =  new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

            try {
                Date openingTime = sdf.parse(openHour);
                Date closingTime = sdf.parse(closeHour);
                Date currentTime =  openingTime;
                while(currentTime.before(closingTime)){
                    timeSlots.put(sdf.format(currentTime), true);
                    // timeSlots.add(sdf.format(currentTime));
                     currentTime  = new Date(currentTime.getTime() + intervalMinutes * 60 * 1000);
                }
            } catch (Exception e)  {
                   e.printStackTrace();
            }
          return timeSlots;
    }
}
