package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {

	Optional<UserEntity> createUser(UserEntity userEntity);
}
