package com.loopers.domain.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BrandService {
	private final BrandRepository brandRepository;

	@Transactional(readOnly = true)
	public Optional<Brand> getBrand(Long brandId) {
		return brandRepository.findById(brandId);
	}
}
