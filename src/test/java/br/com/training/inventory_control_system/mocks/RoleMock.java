package br.com.training.inventory_control_system.mocks;

import br.com.training.inventory_control_system.domain.entities.Role;

import java.util.Set;

import static br.com.training.inventory_control_system.mocks.Constants.ROLE_ADMIN_MOCK;
import static br.com.training.inventory_control_system.mocks.Constants.ROLE_BASIC_MOCK;

public class RoleMock {

    public static Set<Role> getRoleListMock() {
        Role roleAdmin =  new Role(1, ROLE_ADMIN_MOCK);
        Role roleBasic = new Role(2, ROLE_BASIC_MOCK);

        return Set.of(roleAdmin, roleBasic);
    }

    public static Role getRoleAdmin() {
        return new Role(1, ROLE_ADMIN_MOCK);
    }

    public static Role getRoleBasic() {
        return  new Role(2, ROLE_ADMIN_MOCK);
    }
}
