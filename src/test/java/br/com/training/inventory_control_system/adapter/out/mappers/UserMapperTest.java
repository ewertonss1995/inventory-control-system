package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.controllers.user.request.UserRequest;
import br.com.training.inventory_control_system.adapter.out.responses.UserResponse;
import br.com.training.inventory_control_system.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static br.com.training.inventory_control_system.mocks.Constants.UUID_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.USER_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.PASSWORD_MOCK;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserListMock;
import static br.com.training.inventory_control_system.mocks.UserMock.getUserRequestMock;
import static br.com.training.inventory_control_system.mocks.UserMock.FIRST_USER;
import static br.com.training.inventory_control_system.mocks.UserMock.SECOND_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private UserMapper userMapper;

    private List<User> userListMock;
    private UserRequest useRequestMock;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        userListMock = getUserListMock();
        useRequestMock = getUserRequestMock();
    }

    @Test
    void testToUserResponseList() {
        List<UserResponse> userResponses = userMapper.toUserResponseList(userListMock);

        assertNotNull(userResponses);
        assertEquals(2, userResponses.size());

        assertEquals(FIRST_USER, userResponses.get(0).userName());
        assertEquals(UUID_MOCK, userResponses.get(0).userId());
        assertNotNull(userResponses.get(0).roles());
        assertEquals(SECOND_USER, userResponses.get(1).userName());
        assertEquals(UUID_MOCK, userResponses.get(1).userId());
        assertNotNull(userResponses.get(1).roles());
    }

    @Test
    void testToEntity() {
        User user = userMapper.toEntity(useRequestMock);

        assertNotNull(user);

        assertEquals(USER_MOCK, user.getUserName());
        assertEquals(PASSWORD_MOCK, user.getPassword());
        assertNull(user.getUserId());
        assertNull(user.getRoles());
    }
}
