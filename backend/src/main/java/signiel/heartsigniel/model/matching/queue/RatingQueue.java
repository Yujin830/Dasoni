
package signiel.heartsigniel.model.matching.queue;

public enum RatingQueue {
    MALE_0_300("MALE_0_300", "male", 0L, 300L),
    MALE_150_450("MALE_150_450", "male", 150L, 450L),
    MALE_300_600("MALE_300_600", "male", 300L, 600L),
    MALE_450_750("MALE_450_750", "male", 450L, 750L),
    MALE_600_900("MALE_600_900", "male", 600L, 900L),
    MALE_750_1050("MALE_750_1050", "male", 750L, 1050L),
    MALE_900_1200("MALE_900_1200", "male", 900L, 1200L),
    MALE_1050_1350("MALE_1050_1350", "male", 1050L, 1350L),
    MALE_1200_1500("MALE_1200_1500", "male", 1200L, 1500L),
    MALE_1350_1650("MALE_1350_1650", "male", 1350L, 1650L),
    MALE_1500_1800("MALE_1500_1800", "male", 1500L, 1800L),
    MALE_1650_1950("MALE_1650_1950", "male", 1650L, 1950L),
    MALE_1800_2100("MALE_1800_2100", "male", 1800L, 2100L),
    MALE_1950_2250("MALE_1950_2250", "male", 1950L, 2250L),
    MALE_2100_2400("MALE_2100_2400", "male", 2100L, 2400L),
    MALE_2250_2500("MALE_2250_2500", "male", 2250L, 2500L),
    MALE_UNLIMITED("MALE_UNLIMITED", "male", 2500L, Long.MAX_VALUE),
    FEMALE_0_300("FEMALE_0_300", "female", 0L, 300L),
    FEMALE_150_450("FEMALE_150_450", "female", 150L, 450L),
    FEMALE_300_600("FEMALE_300_600", "female", 300L, 600L),
    FEMALE_450_750("FEMALE_450_750", "female", 450L, 750L),
    FEMALE_600_900("FEMALE_600_900", "female", 600L, 900L),
    FEMALE_750_1050("FEMALE_750_1050", "female", 750L, 1050L),
    FEMALE_900_1200("FEMALE_900_1200", "female", 900L, 1200L),
    FEMALE_1050_1350("FEMALE_1050_1350", "female", 1050L, 1350L),
    FEMALE_1200_1500("FEMALE_1200_1500", "female", 1200L, 1500L),
    FEMALE_1350_1650("FEMALE_1350_1650", "female", 1350L, 1650L),
    FEMALE_1500_1800("FEMALE_1500_1800", "female", 1500L, 1800L),
    FEMALE_1650_1950("FEMALE_1650_1950", "female", 1650L, 1950L),
    FEMALE_1800_2100("FEMALE_1800_2100", "female", 1800L, 2100L),
    FEMALE_1950_2250("FEMALE_1950_2250", "female", 1950L, 2250L),
    FEMALE_2100_2400("FEMALE_2100_2400", "female", 2100L, 2400L),
    FEMALE_2250_2500("FEMALE_2250_2500", "female", 2250L, 2500L),
    FEMALE_UNLIMITED("FEMALE_UNLIMITED", "female", 2500L, Long.MAX_VALUE);

    private String name;
    private String gender;
    private Long minRating;
    private Long maxRating;
    private Long medianRating;

    RatingQueue(String name, String gender, Long minRating, Long maxRating) {
        this.name = name;
        this.gender = gender;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.medianRating = (minRating + maxRating) / 2; // 중간값 계산
    }

    public String getName() {
        return name;
    }

    public Long getMedianRating() {
        return medianRating;
    }

    public static RatingQueue getQueueByRatingAndGender(Long rating, String gender) {
        for (RatingQueue queue : values()) {
            if (queue.gender.equals(gender) && rating >= queue.minRating && rating <= queue.maxRating) {
                return queue;
            }
        }
        return null; // or throw an exception
    }

    public static RatingQueue getOppositeGenderQueue(RatingQueue queue) {
        String oppositeGender = queue.gender.equals("male") ? "female" : "male";
        for (RatingQueue q : values()) {
            if (q.gender.equals(oppositeGender) && q.minRating.equals(queue.minRating) && q.maxRating.equals(queue.maxRating)) {
                return q;
            }
        }
        return null; // or throw an exception
    }
}
