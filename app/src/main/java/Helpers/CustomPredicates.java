package Helpers;

import com.google.common.base.Predicate;

import Model.Occurrence;

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
}
