package node.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import node.entity.MarketPriceEntity;
import node.model.MarketPriceDTO;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPriceEntity,Long> { 
	
	
	public List<MarketPriceEntity> findByCurrency1(String currency1);
	
}