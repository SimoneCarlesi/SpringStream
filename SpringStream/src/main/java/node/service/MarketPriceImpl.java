package node.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import node.entity.MarketPriceEntity;
import node.model.MarketPriceDTO;
import node.repository.MarketPriceRepository;

@Service
public class MarketPriceImpl implements MarketPriceServiceInterface{
	
	@Autowired
	public MarketPriceRepository marketPriceRepository;
	
	@Override
	public List<MarketPriceDTO> getAllMarketPrice() {
		
		// JEE STYLE CON HIBERNATE E NAMEDQUERY
		//em.createNamedQuery(Padrone.NAMED_QUERY_ALL, Padrone.class)
		//.getResultList();
		
		// SPRING REPOSITORY
		List<MarketPriceEntity> listMarketPriceEntity = marketPriceRepository.findAll();
		
		return listMarketPriceEntity.stream()
				.map(entity -> {
					MarketPriceDTO dto= new MarketPriceDTO();
					dto.setTimestamp(entity.getTimestamp());
					dto.setCurrency1(entity.getCurrency1());
					dto.setCurrency2(entity.getCurrency2());
					dto.setLast(entity.getLast());
					return dto;
				})
				.collect(Collectors.toList());
	}
	
	public List<MarketPriceDTO> findByCurrency(String currency){
		
		List<MarketPriceEntity> listaMarketPrice=marketPriceRepository.findByCurrency1(currency);
		
		return listaMarketPrice.stream()
				.map(entity -> {
					MarketPriceDTO dto=new MarketPriceDTO();
					dto.setTimestamp(entity.getTimestamp());
					dto.setCurrency1(entity.getCurrency1());
					dto.setCurrency2(entity.getCurrency2());
					dto.setLast(entity.getLast());
					return dto;
				})
				.collect(Collectors.toList());
	}
	
	public void saveMarketPrice(MarketPriceEntity market) {
		marketPriceRepository.save(market);
	}

		
	public String chiamaServizioEsterno() throws Exception {
		// come faccio a fare una chiamata Rest dall'interno della mia applicazione
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target("https://cex.io/api/tickers/EUR");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		String responseString = response.readEntity(String.class);
		System.out.println(responseString);
		return responseString;
		
	}

	@Override
	public List<MarketPriceDTO> convertiLoScaricoDatiInJava() throws Exception {
		String response = chiamaServizioEsterno();
		
		// main thread
		// si ma come facciamo a prendere solo l'elemento DATA
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response);
		
		// scorri lista e fammi vedere cosa hai dentro!!
		/* dataContent = jsonNode.get("data");
		 * for(String dataRow : dataContent) {
			System.out.println(dataRow);
		}*/
		
		final List<MarketPriceDTO> listMarketPriceDTO = new ArrayList<MarketPriceDTO>();
		
		// ogni riga del json
		jsonNode.get("data").forEach(dataRow -> {
			
			// secondo thread
			String[] pair = dataRow.get("pair").asText().split(":");
			String marketCurrency1Estratto = pair[0];
			String marketCurrency2Estratto= pair[1];
			String marketTimestamp=dataRow.get("timestamp").asText();
			SimpleDateFormat simple=new SimpleDateFormat();
			long epoch = Long.parseLong(marketTimestamp);
			Date expiry = new Date( epoch * 1000 );
			String marketLast=dataRow.get("last").asText();
			Float last=Float.parseFloat(marketLast);
			
			// crea classe java
			MarketPriceDTO marketDTO = new MarketPriceDTO();
			marketDTO.setCurrency1(marketCurrency1Estratto);
			marketDTO.setCurrency2(marketCurrency2Estratto);
			marketDTO.setTimestamp(expiry);
			marketDTO.setLast(last);
			
			
			// aggiungendo elemento
			listMarketPriceDTO.add(marketDTO);
		});
		
		return listMarketPriceDTO;
	}

	@Override
	public List<MarketPriceDTO> convertiLoScaricoDatiInJavaESalvaloNelDB() throws Exception {
		List<MarketPriceDTO> result = convertiLoScaricoDatiInJava();
		
		// si ma come si salva sul db? 
		// convertire in entita
		List<MarketPriceEntity> entityResult = result.stream().map(
			// per ogni elemento del mio result, tu devi fare una funzione!
			marketPriceCheStoScorrendo -> {
				// questa è la funzione!!
				MarketPriceEntity marketPriceEntity = new MarketPriceEntity();
				marketPriceEntity.setCurrency1(marketPriceCheStoScorrendo.getCurrency1());
				marketPriceEntity.setCurrency2(marketPriceCheStoScorrendo.getCurrency2());
				marketPriceEntity.setTimestamp(marketPriceCheStoScorrendo.getTimestamp());
				marketPriceEntity.setLast(marketPriceCheStoScorrendo.getLast());
				return marketPriceEntity;
			}
		).collect(Collectors.toList());
		
		// java7
		/*
		 * -- inizializza la variable entity
		 * -- foreach di resultDTO (map)
		 * creami un nuovo entity
		 * riempimi il nuovo entity
		 * -- assegna l'entita tramite listEntity.add() (map)
		 * 
		 */
		
		// salvare le entita!
		marketPriceRepository.saveAll(entityResult);
		return result;
	}

	@Override
	public List<MarketPriceDTO> selectMarketPrice() {
		// scarica dati
		List<MarketPriceEntity> listEntities = marketPriceRepository.findAll();
		
		// trasforma da entity a DTO
		List<MarketPriceDTO> listMarketPriceDTO = listEntities.stream().map(
			// scorro ogni elemento (pairEntity)
				MarketPriceEntity -> {
				// convert il pairEntity in DTO
					MarketPriceDTO marketPriceDTO = new MarketPriceDTO();
					marketPriceDTO.setTimestamp(MarketPriceEntity.getTimestamp());
					marketPriceDTO.setCurrency1(MarketPriceEntity.getCurrency1());
					marketPriceDTO.setCurrency2(MarketPriceEntity.getCurrency2());
					marketPriceDTO.setLast(MarketPriceEntity.getLast());
				return marketPriceDTO;
			}
			// lo trasforma da stream a collection
		).collect(Collectors.toList());
		
		return listMarketPriceDTO;
	}
	
}
