package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {
	@Column(name = "ref_user_id")
	private Long userId;
	@Column(name = "ref_product_id")
	private Long productId;
	private boolean liked;

	public static Like of(Long userId, Long productId) {
		Like like = new Like();
		like.userId = userId;
		like.productId = productId;
		return like;
	}

	public void like() {
		this.liked = true;
	}

	public void unLike(){
		this.liked = false;
	}

}
