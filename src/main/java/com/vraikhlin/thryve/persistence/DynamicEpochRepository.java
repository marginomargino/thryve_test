package com.vraikhlin.thryve.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DynamicEpochRepository extends JpaRepository<DynamicEpochEntity, Long> {

    @Query("SELECT d FROM DynamicEpochEntity d " +
            "WHERE (:timestampStart IS NULL OR d.timestampStart >= :timestampStart)" +
            "AND (:timestampEnd IS NULL OR d.timestampEnd <= :timestampEnd)" +
            "AND (:userId IS NULL OR d.userId = :userId)" +
            "AND (:dynamicValueType IS NULL OR d.dynamicValueType = :dynamicValueType)")
    List<DynamicEpochEntity> getEpochData(@Param("timestampStart") Long timestampStart,
                                          @Param("timestampEnd") Long timestampEnd,
                                          @Param("userId") String userId,
                                          @Param("dynamicValueType") Integer dynamicValueType);

}
