
package signiel.heartsigniel.model.matching.queue;

import java.util.ArrayList;
import java.util.List;

public enum RatingQueue {
    MEGI_MALE_0_300("MALE_0_300", "male", 0L, 300L, "special"),
    MEGI_MALE_150_450("MALE_150_450", "male", 150L, 450L, "special"),
    MEGI_MALE_300_600("MALE_300_600", "male", 300L, 600L, "special"),
    MEGI_MALE_450_750("MALE_450_750", "male", 450L, 750L, "special"),
    MEGI_MALE_600_900("MALE_600_900", "male", 600L, 900L, "special"),
    MEGI_MALE_750_1050("MALE_750_1050", "male", 750L, 1050L, "special"),
    MEGI_MALE_900_1200("MALE_900_1200", "male", 900L, 1200L, "special"),
    MEGI_MALE_1050_1350("MALE_1050_1350", "male", 1050L, 1350L, "special"),
    MEGI_MALE_1200_1500("MALE_1200_1500", "male", 1200L, 1500L, "special"),
    MEGI_MALE_1350_1650("MALE_1350_1650", "male", 1350L, 1650L, "special"),
    MEGI_MALE_1500_1800("MALE_1500_1800", "male", 1500L, 1800L, "special"),
    MEGI_MALE_1650_1950("MALE_1650_1950", "male", 1650L, 1950L, "special"),
    MEGI_MALE_1800_2100("MALE_1800_2100", "male", 1800L, 2100L, "special"),
    MEGI_MALE_1950_2250("MALE_1950_2250", "male", 1950L, 2250L, "special"),
    MEGI_MALE_2100_2400("MALE_2100_2400", "male", 2100L, 2400L, "special"),
    MEGI_MALE_2250_2500("MALE_2250_2500", "male", 2250L, 2500L, "special"),
    MEGI_MALE_UNLIMITED("MALE_UNLIMITED", "male", 2500L, Long.MAX_VALUE, "special"),
    MEGI_FEMALE_0_300("FEMALE_0_300", "female", 0L, 300L, "special"),
    MEGI_FEMALE_150_450("FEMALE_150_450", "female", 150L, 450L, "special"),
    MEGI_FEMALE_300_600("FEMALE_300_600", "female", 300L, 600L, "special"),
    MEGI_FEMALE_450_750("FEMALE_450_750", "female", 450L, 750L, "special"),
    MEGI_FEMALE_600_900("FEMALE_600_900", "female", 600L, 900L, "special"),
    MEGI_FEMALE_750_1050("FEMALE_750_1050", "female", 750L, 1050L, "special"),
    MEGI_FEMALE_900_1200("FEMALE_900_1200", "female", 900L, 1200L, "special"),
    MEGI_FEMALE_1050_1350("FEMALE_1050_1350", "female", 1050L, 1350L, "special"),
    MEGI_FEMALE_1200_1500("FEMALE_1200_1500", "female", 1200L, 1500L, "special"),
    MEGI_FEMALE_1350_1650("FEMALE_1350_1650", "female", 1350L, 1650L, "special"),
    MEGI_FEMALE_1500_1800("FEMALE_1500_1800", "female", 1500L, 1800L, "special"),
    MEGI_FEMALE_1650_1950("FEMALE_1650_1950", "female", 1650L, 1950L, "special"),
    MEGI_FEMALE_1800_2100("FEMALE_1800_2100", "female", 1800L, 2100L, "special"),
    MEGI_FEMALE_1950_2250("FEMALE_1950_2250", "female", 1950L, 2250L, "special"),
    MEGI_FEMALE_2100_2400("FEMALE_2100_2400", "female", 2100L, 2400L, "special"),
    MEGI_FEMALE_2250_2500("FEMALE_2250_2500", "female", 2250L, 2500L, "special"),
    MEGI_FEMALE_UNLIMITED("FEMALE_UNLIMITED", "female", 2500L, Long.MAX_VALUE, "special"),
    MALE_0_300("MALE_0_300", "male", 0L, 300L, "normal"),
    MALE_150_450("MALE_150_450", "male", 150L, 450L, "normal"),
    MALE_300_600("MALE_300_600", "male", 300L, 600L, "normal"),
    MALE_450_750("MALE_450_750", "male", 450L, 750L, "normal"),
    MALE_600_900("MALE_600_900", "male", 600L, 900L, "normal"),
    MALE_750_1050("MALE_750_1050", "male", 750L, 1050L, "normal"),
    MALE_900_1200("MALE_900_1200", "male", 900L, 1200L, "normal"),
    MALE_1050_1350("MALE_1050_1350", "male", 1050L, 1350L, "normal"),
    MALE_1200_1500("MALE_1200_1500", "male", 1200L, 1500L, "normal"),
    MALE_1350_1650("MALE_1350_1650", "male", 1350L, 1650L, "normal"),
    MALE_1500_1800("MALE_1500_1800", "male", 1500L, 1800L, "normal"),
    MALE_1650_1950("MALE_1650_1950", "male", 1650L, 1950L, "normal"),
    MALE_1800_2100("MALE_1800_2100", "male", 1800L, 2100L, "normal"),
    MALE_1950_2250("MALE_1950_2250", "male", 1950L, 2250L, "normal"),
    MALE_2100_2400("MALE_2100_2400", "male", 2100L, 2400L, "normal"),
    MALE_2250_2500("MALE_2250_2500", "male", 2250L, 2500L, "normal"),
    MALE_UNLIMITED("MALE_UNLIMITED", "male", 2500L, Long.MAX_VALUE, "normal"),
    FEMALE_0_300("FEMALE_0_300", "female", 0L, 300L, "normal"),
    FEMALE_150_450("FEMALE_150_450", "female", 150L, 450L, "normal"),
    FEMALE_300_600("FEMALE_300_600", "female", 300L, 600L, "normal"),
    FEMALE_450_750("FEMALE_450_750", "female", 450L, 750L, "normal"),
    FEMALE_600_900("FEMALE_600_900", "female", 600L, 900L, "normal"),
    FEMALE_750_1050("FEMALE_750_1050", "female", 750L, 1050L, "normal"),
    FEMALE_900_1200("FEMALE_900_1200", "female", 900L, 1200L, "normal"),
    FEMALE_1050_1350("FEMALE_1050_1350", "female", 1050L, 1350L, "normal"),
    FEMALE_1200_1500("FEMALE_1200_1500", "female", 1200L, 1500L, "normal"),
    FEMALE_1350_1650("FEMALE_1350_1650", "female", 1350L, 1650L, "normal"),
    FEMALE_1500_1800("FEMALE_1500_1800", "female", 1500L, 1800L, "normal"),
    FEMALE_1650_1950("FEMALE_1650_1950", "female", 1650L, 1950L, "normal"),
    FEMALE_1800_2100("FEMALE_1800_2100", "female", 1800L, 2100L, "normal"),
    FEMALE_1950_2250("FEMALE_1950_2250", "female", 1950L, 2250L, "normal"),
    FEMALE_2100_2400("FEMALE_2100_2400", "female", 2100L, 2400L, "normal"),
    FEMALE_2250_2500("FEMALE_2250_2500", "female", 2250L, 2500L, "normal"),
    FEMALE_UNLIMITED("FEMALE_UNLIMITED", "female", 2500L, Long.MAX_VALUE, "normal");

    private String name;
    private String gender;
    private String type;
    private Long minRating;
    private Long maxRating;
    private Long medianRating;

    RatingQueue(String name, String gender, Long minRating, Long maxRating, String type) {
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.medianRating = (minRating + maxRating) / 2; // 중간값 계산
    }

    public String getName() {
        if (type.equals("special")) {
            return "MEGI_" + name;
        }
        return name;
    }

    public Long getMedianRating() {
        return medianRating;
    }

    public String getType() {
        return type;
    }


    public static RatingQueue getQueueByRatingAndGender(Long rating, String gender, String queueType) {
        for (RatingQueue queue : values()) {
            if (queue.gender.equals(gender) && rating >= queue.minRating && rating <= queue.maxRating && queue.type.equals(queueType)) {
                return queue;
            }
        }
        return null; // or throw an exception
    }

    public static RatingQueue getMegiQueueByMedianRating(Long medianRating, String gender){
        for (RatingQueue queue : values()) {
            if (queue.getMedianRating() == medianRating && queue.type.equals("match") && queue.gender.equals(gender)){
                return queue;
            }
        }
        return null;
    }

    public static RatingQueue getOppositeGenderQueue(RatingQueue queue) {
        String oppositeGender = queue.gender.equals("male") ? "female" : "male";
        for (RatingQueue q : values()) {
            if (q.gender.equals(oppositeGender) && q.minRating.equals(queue.minRating) && q.maxRating.equals(queue.maxRating) && q.type.equals(queue.type)) {
                return q;
            }
        }
        return null; // or throw an exception
    }

}
