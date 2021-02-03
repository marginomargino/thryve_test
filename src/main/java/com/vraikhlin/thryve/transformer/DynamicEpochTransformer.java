package com.vraikhlin.thryve.transformer;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.DynamicEpochData;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamicEpochTransformer {


    public static List<DynamicEpochData> toDto(List<DynamicEpochEntity> entities){
        return entities.stream()
                .filter(Objects::nonNull)
                .map(DynamicEpochTransformer::toDto)
                .collect(Collectors.toList());
    }

    private static DynamicEpochData toDto (DynamicEpochEntity entity){
        return DynamicEpochData.builder()
                .userId(entity.getUserId())
                .dataSourceId(entity.getDataSourceId())
                .createdAt(entity.getCreatedAt())
                .startTimestamp(entity.getTimestampStart())
                .endTimestamp(entity.getTimestampEnd())
                .dynamicValueType(entity.getDynamicValueType())
                .value(entity.getValue())
                .valueType(entity.getValueType())
                .build();
    }

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

    private static List<DynamicEpochEntity> toEntities(List<DynamicEpoch.DataPoint> data, String dataSourceId, String userId) {
        return data.stream()
                .filter(Objects::nonNull)
                .map(dataPointPoint -> toEntity(dataPointPoint, dataSourceId, userId))
                .collect(Collectors.toList());

    }

    private static DynamicEpochEntity toEntity(DynamicEpoch.DataPoint dataPoint, String dataSourceId, String userId) {
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
