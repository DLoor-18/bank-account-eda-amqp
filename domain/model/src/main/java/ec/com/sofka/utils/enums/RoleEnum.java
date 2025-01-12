package ec.com.sofka.utils.enums;

public enum RoleEnum {
    ADMIN,
    EXECUTIVE,
    EMPLOYEE;

    public static RoleEnum fromString(String role) {
        return RoleEnum.valueOf(role);
    }
}
