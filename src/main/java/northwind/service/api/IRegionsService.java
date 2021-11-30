package northwind.service.api;



import northwind.model.Regions;
import reactor.core.publisher.Mono;
import java.util.List;

public interface IRegionsService {
    Mono<Regions> getRegionID(int RegionID);

    //List
    Mono<List<Integer>> getRegionsIds();
}
