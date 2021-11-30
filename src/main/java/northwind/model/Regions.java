package northwind.model;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Regions {
    private int RegionID; 

    private String RegionDescription;

    public int getRegionID() {
		return RegionID;
	}

	public void setRegionID(int RegionID) {
		this.RegionID = RegionID;
	}

    public String getRegionDescription() {
		return RegionDescription;
	}

	public void setRegionDescription(String RegionDescription) {
		this.RegionDescription = RegionDescription;
	}

}
