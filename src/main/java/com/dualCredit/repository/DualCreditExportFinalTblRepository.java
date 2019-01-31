package com.dualCredit.repository;

import com.dualCredit.entities.DualCreditExportFinalTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DualCreditExportFinalTblRepository extends JpaRepository<DualCreditExportFinalTbl,Long> {

}
