package ru.itis.cal.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
public class ScheduleParser {
    private static HashMap<String, String> groupRange;

    public static HashMap<String, String> getGroupRange() {
        if (groupRange == null) {
            groupRange = new HashMap<>();
            groupRange.put("11-701", "AQ");
        }
        return groupRange;
    }
}
