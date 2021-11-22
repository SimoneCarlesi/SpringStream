package node.service;

import java.util.List;

import node.model.MarketPriceDTO;

public interface MarketPriceServiceInterface {
	
	public List<MarketPriceDTO> getAllMarketPrice();

	public String chiamaServizioEsterno() throws Exception;

	public List<MarketPriceDTO> convertiLoScaricoDatiInJava() throws Exception;

	public List<MarketPriceDTO> convertiLoScaricoDatiInJavaESalvaloNelDB() throws Exception;

	public List<MarketPriceDTO> selectMarketPrice(); 
	
	public List<MarketPriceDTO> findByCurrency(String currency);
}
