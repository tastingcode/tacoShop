-- ===== 파라미터 =====
SET
@NUM_BRANDS   = 100;
SET
# @NUM_USERS    = 50000;
@NUM_USERS    = 500;
SET
# @NUM_PRODUCTS = 1000000;
@NUM_PRODUCTS = 10000;
SET
# @MAX_LIKES_PER_PRODUCT = 50000;
@MAX_LIKES_PER_PRODUCT = 500;

SET SESSION cte_max_recursion_depth = 2000000;

-- ===== brands =====
INSERT INTO loopers.brand (name, description, created_at, updated_at)
WITH RECURSIVE cte(n) AS (SELECT 1
                          UNION ALL
                          SELECT n + 1
                          FROM cte
                          WHERE n < @NUM_BRANDS)
SELECT CONCAT('brand', LPAD(n, 3, '0')),
       CONCAT('brand desc', n),
       -- created_at : 최근 10년 내 임의의 시각
       @created := TIMESTAMP(
        DATE_SUB(NOW(), INTERVAL FLOOR(RAND(n*7) * 3650) DAY)
        + INTERVAL FLOOR(RAND(n*13) * 86400) SECOND
        ),
       -- updated_at : created_at 이후 ~ 현재까지 랜덤
        TIMESTAMP(
        @created + INTERVAL FLOOR(RAND(n*19) *
        TIMESTAMPDIFF(SECOND, @created, NOW())) SECOND
        )
FROM cte;

-- ===== users (member) =====
INSERT INTO loopers.member (user_id, name, gender, birth, email, created_at, updated_at)
WITH RECURSIVE cte(n) AS (SELECT 1
                          UNION ALL
                          SELECT n + 1
                          FROM cte
                          WHERE n < @NUM_USERS)
SELECT CONCAT('user', LPAD(n, 6, '0'))                                             AS user_id,
       CONCAT('user', LPAD(n, 6, '0'))                                             AS name,
       CASE WHEN n % 2 = 0 THEN 'M' ELSE 'F' END                                   AS gender,
       DATE_FORMAT(DATE_ADD('1980-01-01', INTERVAL (n MOD 15000) DAY), '%Y-%m-%d') AS birth,
       CONCAT('user', LPAD(n, 6, '0'), '@example.com')                             AS email,
       -- created_at : 최근 10년 내 임의의 시각
       @created := TIMESTAMP(
        DATE_SUB(NOW(), INTERVAL FLOOR(RAND(n*7) * 3650) DAY)
        + INTERVAL FLOOR(RAND(n*13) * 86400) SECOND
        ),
       -- updated_at : created_at 이후 ~ 현재까지 랜덤
        TIMESTAMP(
        @created + INTERVAL FLOOR(RAND(n*19) *
        TIMESTAMPDIFF(SECOND, @created, NOW())) SECOND
        )
FROM cte;

-- ===== products =====
INSERT INTO loopers.product (name, price, stock, ref_brand_id, like_count, created_at, updated_at)
WITH RECURSIVE cte(n) AS (SELECT 1
                          UNION ALL
                          SELECT n + 1
                          FROM cte
                          WHERE n < @NUM_PRODUCTS)
SELECT CONCAT('item', LPAD(n, 7, '0'))                    AS name,
       10000                                              AS price,
       (FLOOR(RAND(n * 11) * 96) + 5)                     AS stock,      -- 5 ~ 100
       1 + FLOOR(RAND(n) * @NUM_BRANDS)                   AS brand_id,   -- 1..100
       FLOOR(RAND(n * 13) * (@MAX_LIKES_PER_PRODUCT + 1)) AS like_count, -- 0..50,000
       -- created_at : 최근 10년 내 임의의 시각
       @created := TIMESTAMP(
        DATE_SUB(NOW(), INTERVAL FLOOR(RAND(n*7) * 3650) DAY)
        + INTERVAL FLOOR(RAND(n*13) * 86400) SECOND
        ),
       -- updated_at : created_at 이후 ~ 현재까지 랜덤
        TIMESTAMP(
        @created + INTERVAL FLOOR(RAND(n*19) *
        TIMESTAMPDIFF(SECOND, @created, NOW())) SECOND
        )
FROM cte;

-- 1. 정액 할인 쿠폰 (1000원 할인)
INSERT INTO loopers.coupon (
    discount_fixed_amount,
    discount_percentage,
    minimum_order_price,
    created_at,
    updated_at,
    coupon_code,
    name,
    status,
    type
) VALUES (
             1000,                     -- 정액 할인 금액
             0,                        -- 정률 할인은 0
             1000,                     -- 최소 주문 금액
             NOW(), NOW(),
             'FIX1000',
             '1000원 정액 할인 쿠폰',
             'ACTIVE',
             'FIXED_AMOUNT'
         );

-- 2. 정률 할인 쿠폰 (10% 할인)
INSERT INTO loopers.coupon (
    discount_fixed_amount,
    discount_percentage,
    minimum_order_price,
    created_at,
    updated_at,
    coupon_code,
    name,
    status,
    type
) VALUES (
             0,                        -- 정액 할인 금액
             10,                       -- 10% 할인
             1000,                     -- 최소 주문 금액
             NOW(), NOW(),
             'PERC10',
             '10% 정률 할인 쿠폰',
             'ACTIVE',
             'PERCENTAGE'
         );


-- 유저 쿠폰 등록
-- userId = 1 (user000001의 PK라고 가정)

INSERT INTO loopers.user_coupon (
    used,
    created_at,
    updated_at,
    ref_coupon_id,
    ref_user_id,
    version
) VALUES (
             b'0', NOW(), NOW(), 1, 1, 0
         );

INSERT INTO loopers.user_coupon (
    used,
    created_at,
    updated_at,
    ref_coupon_id,
    ref_user_id,
    version
) VALUES (
             b'0', NOW(), NOW(), 2, 1, 0
         );
