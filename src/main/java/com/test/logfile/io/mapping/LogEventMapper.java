package com.test.logfile.io.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.test.logfile.io.db.entity.LogEventEntity;
import com.test.logfile.io.model.LogEventModel;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel="spring")
public interface LogEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "alert", ignore = true)
    @Mapping(target = "eventIdentifier", source = "id")
    LogEventEntity toEntity(LogEventModel model);

}
