package uz.javacourse.jgcp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toResponseDto(User entity);

    MarkDeceasedResponseDto toMarkDeceasedResponseDto(User entity);
}
