package com.example.BookApplication.mapper.user;

import com.example.BookApplication.dto.user.RegisterRequestDTO;
import com.example.BookApplication.dto.user.UserDTO;
import com.example.BookApplication.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    UserEntity mapToUserEntity(UserDTO userDTO);

    UserDTO mapToUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity mapToUserEntity(RegisterRequestDTO registerRequestDTO);

}
