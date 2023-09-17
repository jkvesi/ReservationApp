package com.example.reservation.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.time.DayOfWeek;

public class IterationClass {

    public ArrayList<String> setEveryDay(String pickedDate){
        ArrayList<String> dates = new ArrayList<>();
        String formattedDate;

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        LocalDate local = LocalDate.parse(pickedDate, inputDateFormatter);
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String formattedPickedDate = local.format(outputDateFormatter);

        String[] dateParts = formattedPickedDate.split("\\.");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        while(calendar.get(Calendar.YEAR)==year){
            int currentMonth = calendar.get(Calendar.MONTH);


            //inkerementaciuja dana u mjesecu
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            while(calendar.get(Calendar.DAY_OF_MONTH) <= lastDay){
                int currentDay = calendar.get((Calendar.DAY_OF_MONTH));
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                //formatiranje datuma
                String date = currentDay + "." + currentMonth + "." + year + ".";
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d.M.yyyy.");
                LocalDate localDate = LocalDate.parse(date, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
                formattedDate = localDate.format(outputFormatter);
                dates.add(formattedDate);
            }

            //postavi na sljedeci mjesec
            calendar.add(Calendar.MONTH,1);
        }
        return dates;
    }

    public ArrayList<String> iterateWholeYear(String pickedDate){
        ArrayList<String> dates = new ArrayList<>();
        String formattedDate;

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        LocalDate local = LocalDate.parse(pickedDate, inputDateFormatter);
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String formattedPickedDate = local.format(outputDateFormatter);

        String[] dateParts = formattedPickedDate.split("\\.");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        LocalDate currentDate = LocalDate.of(year, month, day);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        while (!currentDate.isAfter(endDate)) {
           // String date = day + "." + month + "." + year + ".";
           // DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d.M.yyyy.");
           // LocalDate localDate = LocalDate.parse(date, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            formattedDate = currentDate.format(outputFormatter);
            dates.add(formattedDate);
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }

    public ArrayList<String> duringTheWeekIteration(String pickedDate){
        ArrayList<String> dates = new ArrayList<>();
        String formattedDate;

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        LocalDate local = LocalDate.parse(pickedDate, inputDateFormatter);
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String formattedPickedDate = local.format(outputDateFormatter);

        String[] dateParts = formattedPickedDate.split("\\.");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        LocalDate currentDate = LocalDate.of(year, month, day);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        while (!currentDate.isAfter(endDate)) {
            if(currentDate.getDayOfWeek() == DayOfWeek.MONDAY || currentDate.getDayOfWeek() == DayOfWeek.TUESDAY
                    || currentDate.getDayOfWeek() == DayOfWeek.WEDNESDAY || currentDate.getDayOfWeek() == DayOfWeek.THURSDAY
                    || currentDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
                formattedDate = currentDate.format(outputFormatter);
                dates.add(formattedDate);
                currentDate = currentDate.plusDays(1);
            } else {
                currentDate = currentDate.plusDays(1);
            }
        }
        return dates;
    }

    public ArrayList<String> weekendIteration(String pickedDate){
        ArrayList<String> dates = new ArrayList<>();
        String formattedDate;

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        LocalDate local = LocalDate.parse(pickedDate, inputDateFormatter);
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        String formattedPickedDate = local.format(outputDateFormatter);

        String[] dateParts = formattedPickedDate.split("\\.");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        LocalDate currentDate = LocalDate.of(year, month, day);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        while (!currentDate.isAfter(endDate)) {
            if(currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
                formattedDate = currentDate.format(outputFormatter);
                dates.add(formattedDate);
                currentDate = currentDate.plusDays(1);
            } else {
                currentDate = currentDate.plusDays(1);
            }
        }
        return dates;
    }
}
