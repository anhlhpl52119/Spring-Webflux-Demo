package northwind.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import northwind.util.NorthwindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import northwind.client.ReactiveHttpClient;
import northwind.exception.CoreException;
import northwind.model.Regions;
import northwind.service.api.IRegionsService;
import northwind.util.RegionsExtractor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


@Service
public class RegionService implements IRegionsService {
	
	@Autowired
	Scheduler schedular;
	
	@Autowired
	ReactiveHttpClient httpClient;
	
	public Mono<Regions> getRegionID(int RegionID) {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$filter", "RegionID eq "+RegionID);
		return Mono.create((emitter)->{
		try {
			Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL,"Regions", HttpMethod.GET, headers, queryParams,null);
			System.out.println("Non blocking");
			response.subscribeOn(schedular);
			response.subscribe((ClientResponse clientResponse)->{
				clientResponse.bodyToMono(String.class).subscribe((json) -> {
					Regions regions = RegionsExtractor.extractRegions(json);
					emitter.success(regions);
				});

			});
		} catch (CoreException e) {
			e.printStackTrace();
			emitter.error(e);
		}
		});
	}

	@Override
	public Mono<List<Integer>> getRegionsIds() {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$select", "RegionID");
		return Mono.create((emitter)->{
			try {
				Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL,"Regions", HttpMethod.GET, headers, queryParams, null);
				response.subscribeOn(schedular);
				response.subscribe((ClientResponse clientResponse) -> {
					clientResponse.bodyToMono(String.class).subscribe((json) -> {
						List<Integer> regionsIds = RegionsExtractor.extractRegionsIds(json);
						emitter.success(regionsIds);
					});

				});
			}catch (CoreException e) {
				e.printStackTrace();
				emitter.error(e);
			}
		});
	}


}

