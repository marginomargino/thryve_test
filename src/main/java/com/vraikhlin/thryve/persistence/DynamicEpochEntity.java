package com.vraikhlin.thryve.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.vraikhlin.thryve.persistence.DynamicEpochEntity.TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TABLE_NAME)
public class DynamicEpochEntity {

    public static final String TABLE_NAME = "dynamic_epoch";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "de_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "de_timestamp_start")
    private Long timestampStart;

    @Column(name = "de_timestamp_end")
    private Long timestampEnd;

    @Column(name = "de_created_at")
    private Long createdAt;

    @Column(name = "de_user_id")
    private String userId;

    @Column(name = "de_data_source_id")
    private String dataSourceId;

    @Column(name = "de_dynamic_value_type")
    private Integer dynamicValueType;

    @Column(name = "de_value")
    private String value;

    @Column(name = "de_value_type")
    private String valueType;


}
