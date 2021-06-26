package io.cygnus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.cygnus.repository.entity.CygInstrument;

/**
 * 
 * @author yellow013
 * 
 *         CygInstrumentRepository
 *
 */
@Repository
public interface CygInstrumentRepository extends JpaRepository<CygInstrument, Long> {

}