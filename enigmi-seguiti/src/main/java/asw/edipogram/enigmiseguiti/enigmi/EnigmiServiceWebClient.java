package asw.edipogram.enigmiseguiti.enigmi;

import asw.edipogram.enigmiseguiti.domain.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;

import java.util.*; 
import java.util.stream.*; 

@Service
@Primary
@Slf4j
public class EnigmiServiceWebClient implements EnigmiService {

	@Autowired 
	@Qualifier("loadBalancedWebClient")
    private WebClient loadBalancedWebClient;
	
	public Collection<Enigma> getEnigmiByTipi(Collection<String> tipi) {
		Collection<Enigma> enigmi = null; 
        Flux<Enigma> response = loadBalancedWebClient
                .get()
				.uri("http://enigmi/cercaenigmi/tipi/{tipi}", toString(tipi))
                .retrieve()
                .bodyToFlux(Enigma.class);
        try {
            enigmi = response.collectList().block();
        } catch (WebClientException e) {
            log.error("", e);
        }
		return enigmi; 
	}	

	private static String toString(Collection<String> c) {
		return c.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(",", "", ""));
	}
}
