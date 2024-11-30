package com.swe599.ramp.mapper;

import com.swe599.ramp.dto.user.UserAuthenticationDto;
import com.swe599.ramp.dto.user.UserCreateRequestDto;
import com.swe599.ramp.dto.user.UserDto;
import com.swe599.ramp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userCreateRequestDto.getPassword()))")
    User toEntity(UserCreateRequestDto userCreateRequestDto, PasswordEncoder passwordEncoder);

    UserAuthenticationDto toAuthenticationDto(String token);
}
