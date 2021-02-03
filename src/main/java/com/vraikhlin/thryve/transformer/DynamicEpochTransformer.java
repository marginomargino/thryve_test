package com.vraikhlin.thryve.transformer;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamicEpochTransformer {


    public static List<DynamicEpochEntity> toEntities(List<DynamicEpoch> dynamicEpochs) {

        return dynamicEpochs.stream()
                .map(dynamicEpoch -> toEntities(dynamicEpoch.getDataSources(), dynamicEpoch.getUserId()))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<DynamicEpochEntity> toEntities(List<DynamicEpoch.DataSource> dataSources, String userId) {
        return dataSources.stream()
                .map(dataSource -> toEntities(dataSource.getData(), dataSource.getDataSourceId(), userId))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<DynamicEpochEntity> toEntities(List<DynamicEpoch.Data> data, String dataSourceId, String userId) {
        return data.stream()
                .map(dataPoint -> toEntity(dataPoint, dataSourceId, userId))
                .collect(Collectors.toList());

    }

    private static DynamicEpochEntity toEntity(DynamicEpoch.Data dataPoint, String dataSourceId, String userId) {
        return DynamicEpochEntity.builder()
                .userId(userId)
                .dataSourceId(dataSourceId)
                .timestampStart(dataPoint.getStartTimestamp())
                .timestampEnd(dataPoint.getEndTimestamp())
                .createdAt(dataPoint.getCreatedAt())
                .dynamicValueType(dataPoint.getDynamicValueType())
                .value(dataPoint.getValue())
                .valueType(dataPoint.getValueType())
                .build();
    }


}
