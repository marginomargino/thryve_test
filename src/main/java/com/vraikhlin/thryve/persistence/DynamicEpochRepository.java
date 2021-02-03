package com.vraikhlin.thryve.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DynamicEpochRepository extends JpaRepository<DynamicEpochEntity, Long> {
}
