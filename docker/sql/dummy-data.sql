-- ===== 파라미터 =====
SET
@NUM_BRANDS   = 100;
SET
@NUM_USERS    = 50000;
SET
@NUM_PRODUCTS = 1000000;
SET
@MAX_LIKES_PER_PRODUCT = 50000;

SET SESSION cte_max_recursion_depth = 2000000;

-- ===== brands =====
INSERT INTO brand (name, description, created_at, updated_at)
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
INSERT INTO member (user_id, name, gender, birth, email, created_at, updated_at)
WITH RECURSIVE cte(n) AS (SELECT 1
                          UNION ALL
                          SELECT n + 1
                          FROM cte
                          WHERE n < @NUM_USERS)
SELECT CONCAT('user', LPAD(n, 6, '0'))                                             AS user_id,
       CONCAT('User', LPAD(n, 6, '0'))                                             AS name,
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
INSERT INTO product (name, price, stock, ref_brand_id, like_count, created_at, updated_at)
WITH RECURSIVE cte(n) AS (SELECT 1
                          UNION ALL
                          SELECT n + 1
                          FROM cte
                          WHERE n < @NUM_PRODUCTS)
SELECT CONCAT('item', LPAD(n, 7, '0'))                    AS name,
       (FLOOR(RAND(n * 7) * 50) + 1) * 1000               AS price,      -- 1,000 ~ 50,000 (1천원 단위)
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