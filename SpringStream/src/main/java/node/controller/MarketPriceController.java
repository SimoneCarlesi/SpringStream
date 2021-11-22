package node.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import node.model.MarketPriceDTO;
import node.service.MarketPriceServiceInterface;

@RestController
@Slf4j
@RequestMapping("/ruoli")
public class MarketPriceController {


	@Value("${spring.application.name}")
	private String appName;

	@Autowired
	MarketPriceServiceInterface marketPriceService;

	@GetMapping(value="/helloWorld")
	public ResponseEntity<String> gethelloWorld(){

		return ResponseEntity.ok().body("Hello World");
	}


	// JEE STYLE CON EJB
	// creare il metodo per ottenere TUTTI i gatti
	//@GET
	//@Produces(MediaType.APPLICATION_JSON) //formato di dato
	//@Path("allGatti") //variabile {}

	// SPRING
	/**
	 * Get list of roles
	 * @return list of Ruoli existing on DB
	 */
	@GetMapping("/getListMarketPrice")
	public ResponseEntity<List<MarketPriceDTO>> getAllMarketPrice() {
		//log.info("getListRuoli - START");
		List<MarketPriceDTO> response =  marketPriceService.getAllMarketPrice();
		//log.info("getListRuoli - response of count : {}", response.size());


		/*return Optional
				.ofNullable(response)
				.map( list -> ResponseEntity.ok().body(list))  
				.orElseGet( () -> ResponseEntity.notFound().build() );
		 */
		if(!response.isEmpty()) {
			return ResponseEntity.ok().body(response);
		} else {
			return ResponseEntity.notFound().build(); 
		}
	}
	
	@GetMapping("/getListByCurrency")
	public ResponseEntity<List<MarketPriceDTO>> getListMarketPriceByCurrency(String currency){
		List<MarketPriceDTO> lista = marketPriceService.findByCurrency(currency);
		if(!lista.isEmpty()) {
			return ResponseEntity.ok().body(lista);
		} else {
			return ResponseEntity.notFound().build(); 
		}
	}

	// fai una get di un servizio esterno
	@GetMapping("/scaricaDaServizioEsterno")
	public ResponseEntity<String> scaricaDaServizioEsterno() {
		//???
		// eseguire una get di un servizio esterno, scaricare i dati e salvarli nel mio DB
		try {
			String resultRequest = marketPriceService.chiamaServizioEsterno();
			return ResponseEntity.ok().body(resultRequest);
		} catch (Exception exc) {
			return ResponseEntity.internalServerError().body(exc.getMessage());
		}
	}

	// fai una get di un servizio esterno E trasformalo in java
	@GetMapping("/convertiLoScaricoDatiInJava")
	public ResponseEntity<List<MarketPriceDTO>> convertiLoScaricoDatiInJava() {
		//???
		// eseguire una get di un servizio esterno, scaricare i dati e salvarli nel mio DB
		try {
			List<MarketPriceDTO> resultRequest = marketPriceService.convertiLoScaricoDatiInJava();
			return ResponseEntity.ok().body(resultRequest);
		} catch (Exception exc) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	// scarica, converti e salva nel DB
	@GetMapping("/convertiLoScaricoDatiInJavaESalvaloNelDB")
	public ResponseEntity<List<MarketPriceDTO>> convertiLoScaricoDatiInJavaESalvaloNelDB() {
		//???
		// eseguire una get di un servizio esterno, scaricare i dati e salvarli nel mio DB
		try {
			List<MarketPriceDTO> resultRequest = marketPriceService.convertiLoScaricoDatiInJavaESalvaloNelDB();
			return ResponseEntity.ok().body(resultRequest);
		} catch (Exception exc) {
			System.out.println("e: " + exc.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	// seleziona i dati scaricati nel DB
		@GetMapping("/selectPairs")
		public ResponseEntity<List<MarketPriceDTO>> selectPairs() {
			//???
			// eseguire una get di un servizio esterno, scaricare i dati e salvarli nel mio DB
			try {
				List<MarketPriceDTO> resultRequest = marketPriceService.selectMarketPrice();
				return ResponseEntity.ok().body(resultRequest);
			} catch (Exception exc) {
				return ResponseEntity.internalServerError().build();
			}
		}
	
}
