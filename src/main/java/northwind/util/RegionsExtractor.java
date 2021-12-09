package northwind.util;

import java.io.IOException;
import java.util.stream.StreamSupport;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.stream.Collectors;
import northwind.model.Regions;

public class RegionsExtractor {
	public static Regions extractRegions(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode regions = (ArrayNode) node;
				Regions region = StreamSupport.stream(regions.spliterator(), false).map((regionsNode) -> {
					Regions c = new Regions();
					c.setRegionID(regionsNode.get("RegionID").asInt());
					c.setRegionDescription(regionsNode.get("RegionDescription").asText());
					return c;
				}).findFirst().orElseThrow(() -> new IOException());
				return region;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Integer> extractRegionsIds(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode regions = (ArrayNode) node;
				List<Integer> regionsIds = StreamSupport.stream(regions.spliterator(), false).map((regionsNode) -> {
					return regionsNode.get("RegionID").asInt();
				}).collect(Collectors.toList());
				return regionsIds;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
