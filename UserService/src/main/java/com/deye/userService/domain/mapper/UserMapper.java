package com.deye.userService.domain.mapper;

import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {


    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "userPassword", ignore = true)
    UserDto toDto(User user);
}
