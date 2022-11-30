package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.dto.SummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.ReportProjection;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query("SELECT new com.devsuperior.dsmeta.dto.SummaryDTO(obj.seller.name, SUM(obj.amount)) "
	+ "FROM Sale obj "
	+ "WHERE obj.date BETWEEN :min AND :max "
	+ "GROUP BY obj.seller.name" )
	Page<SummaryDTO> getSummary(LocalDate min, LocalDate max, Pageable pageable);

	@Query(nativeQuery = true, value ="SELECT SEL.ID, SAL.DATE, SAL.AMOUNT, SEL.NAME "
			+ "FROM TB_SALES SAL "
			+ "JOIN TB_SELLER SEL ON SAL.SELLER_ID=SEL.ID "
			+ "WHERE SAL.DATE BETWEEN :min AND :max AND UPPER(SEL.NAME) LIKE UPPER(CONCAT('%', :name, '%'))")
	List<ReportProjection> getReport(LocalDate min, LocalDate max, String name);
}
