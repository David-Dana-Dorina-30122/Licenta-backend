package project.dto;

import project.user.User;

public record UserDto(
        int id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
    }
}
