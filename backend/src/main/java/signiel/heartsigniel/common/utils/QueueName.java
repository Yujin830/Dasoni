package signiel.heartsigniel.common.utils;

import java.util.ArrayList;
import java.util.List;

public enum QueueName {
    FEMALE_0_200("queue_female_0_200"),
    FEMALE_100_300("queue_female_100_300"),
    FEMALE_200_400("queue_female_200_400"),
    FEMALE_300_500("queue_female_300_500"),
    FEMALE_400_600("queue_female_400_600"),
    FEMALE_500_700("queue_female_500_700"),
    FEMALE_600_800("queue_female_600_800"),
    FEMALE_700_900("queue_female_700_900"),
    FEMALE_800_1000("queue_female_800_1000"),
    FEMALE_900_1100("queue_female_900_1100"),
    FEMALE_1000_1200("queue_female_1000_1200"),
    FEMALE_1100_1300("queue_female_1100_1300"),
    FEMALE_1200_1400("queue_female_1200_1400"),
    FEMALE_1300_1500("queue_female_1300_1500"),
    FEMALE_1400_1600("queue_female_1400_1600"),
    FEMALE_1500_1700("queue_female_1500_1700"),
    FEMALE_1600_1800("queue_female_1600_1800"),
    FEMALE_1700_1900("queue_female_1700_1900"),
    FEMALE_1800_2000("queue_female_1800_2000"),
    FEMALE_1900_2100("queue_female_1900_2100"),

    MALE_0_200("queue_male_0_200"),
    MALE_100_300("queue_male_100_300"),
    MALE_200_400("queue_male_200_400"),
    MALE_300_500("queue_male_300_500"),
    MALE_400_600("queue_male_400_600"),
    MALE_500_700("queue_male_500_700"),
    MALE_600_800("queue_male_600_800"),
    MALE_700_900("queue_male_700_900"),
    MALE_800_1000("queue_male_800_1000"),
    MALE_900_1100("queue_male_900_1100"),
    MALE_1000_1200("queue_male_1000_1200"),
    MALE_1100_1300("queue_male_1100_1300"),
    MALE_1200_1400("queue_male_1200_1400"),
    MALE_1300_1500("queue_male_1300_1500"),
    MALE_1400_1600("queue_male_1400_1600"),
    MALE_1500_1700("queue_male_1500_1700"),
    MALE_1600_1800("queue_male_1600_1800"),
    MALE_1700_1900("queue_male_1700_1900"),
    MALE_1800_2000("queue_male_1800_2000"),
    MALE_1900_2100("queue_male_1900_2100");

    private String queueName;

    QueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public static List<QueueName> getQueueNameByRating(String gender, Long rating) {

        //점수 범위 계산
        int rangeLower = (rating.intValue() - 100) / 100;
        int rangeUpper = rating.intValue() / 100;

        // 범위가 0 미만이거나 20을 넘어가는 경우 조정
        rangeLower = Math.max(rangeLower, 0);
        rangeLower = Math.min(rangeLower, 20);
        rangeUpper = Math.min(rangeUpper, 20);

        // 큐 이름 생성 및 반환
        String queueNameLower = String.format("%s_%d00_%d00", gender.toLowerCase(), rangeLower, rangeLower + 2);
        String queueNameUpper = String.format("%s_%d00_%d00", gender.toLowerCase(), rangeUpper, rangeUpper + 2);

        List<QueueName> queueNames = new ArrayList<>();
        queueNames.add(valueOf(queueNameLower.toUpperCase()));
        queueNames.add(valueOf(queueNameUpper.toUpperCase()));

        return queueNames;
    }

    public long getMiddleValue() {
        String[] parts = queueName.split("_");
        String[] rangeParts = parts[2].split("-");

        int lowerBound = Integer.parseInt(rangeParts[0]);
        int upperBound = Integer.parseInt(rangeParts[1]);

        return (lowerBound + upperBound) / 2;
    }
}
