package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    List<UserResponse> toUserResponseList(List<User> entities);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);
}
