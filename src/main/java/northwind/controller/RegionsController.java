package northwind.controller;

import northwind.model.Regions;

import northwind.service.api.IRegionsService;
import northwind.util.RegionIdsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

// import javax.swing.plaf.synth.Region;


@RestController
public class RegionsController {
    
    @Autowired
    private RegionIdsCache regionIdsCache;

    @Autowired
    private IRegionsService regionsService;

    @RequestMapping( value = "/region/{RegionID}",method = RequestMethod.GET)
    public Mono<Regions> getRegionID(@PathVariable("RegionID") int RegionID){
        return regionsService.getRegionID(RegionID);
    }

    @GetMapping(path = "/region-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Regions>> getRegionsStream() {
        Flux<ServerSentEvent<Regions>> sseRegionsFlux = Flux.fromIterable(regionIdsCache.getRegionIds())
                .delayElements(Duration.ofSeconds(5))
                .flatMap(RegionID -> regionsService.getRegionID(RegionID))
                .map(region -> {
                    ServerSentEvent<Regions> sseRegion = ServerSentEvent.<Regions>builder()
                        .id(String.valueOf(region.getRegionID()))
                        .event("region-stream-event")
                        .data(region)
                        .build();
                    return sseRegion;

                });
        return sseRegionsFlux;
    }
}
