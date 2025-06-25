package project.user;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleEnumTest {

    @Test
    public void testRoleEnumValues() {
        assertEquals(Role.ROLE_USER, Role.valueOf("ROLE_USER"));
        assertEquals(Role.ROLE_ADMIN, Role.valueOf("ROLE_ADMIN"));
        assertEquals(Role.ROLE_STAFF, Role.valueOf("ROLE_STAFF"));
    }

    @Test
    public void testInvalidRoleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("STAFF"));
    }
}
