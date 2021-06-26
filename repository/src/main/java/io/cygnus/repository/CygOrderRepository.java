package io.cygnus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.cygnus.repository.entity.CygOrder;

/**
 * 
 * @author yellow013
 * 
 *         CygOrderRepository
 *
 */
@Repository
public interface CygOrderRepository extends JpaRepository<CygOrder, Long> {

}