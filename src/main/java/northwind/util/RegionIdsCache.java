package northwind.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class RegionIdsCache {
    private List<Integer> regionIds;

    public List<Integer> getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(final List<Integer> regionIds) {
        this.regionIds = regionIds;
    }
}
