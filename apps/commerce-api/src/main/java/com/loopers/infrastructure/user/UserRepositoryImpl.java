package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<UserEntity> createUser(UserEntity userEntity) {
		try {
			UserEntity user = userJpaRepository.save(userEntity);
			return Optional.of(user);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
