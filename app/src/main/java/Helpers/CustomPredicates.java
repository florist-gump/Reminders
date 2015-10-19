package Helpers;

import com.google.common.base.Predicate;

import Model.Occurrence;
import Model.Reminder;

/**
 * Created by Flo on 14/10/15.
 */
public class CustomPredicates {
    public static Predicate<Occurrence> filterPredicatesWithDayOfTheWeek(final DAYSOFTHEWEEK day) {
        return new Predicate<Occurrence>() {
            @Override
            public boolean apply(Occurrence input) {
                return input.getDay().equals(day);
            }
        };
    }
    public static Predicate<Reminder> filterReminders(final String filter) {
        return new Predicate<Reminder>() {
            @Override
            public boolean apply(Reminder input) {
                return input.getName().toLowerCase().startsWith(filter);
            }
        };
    }
}
