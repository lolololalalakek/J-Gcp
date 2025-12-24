INSERT INTO users (
    full_name,
    address,
    phone_number,
    email,
    photo_url,
    pinfl,
    age,
    gender,
    document_type,
    issue_date,
    expiry_date,
    citizenship,
    death_date
)
SELECT
    'User ' || gs AS full_name,
    'Tashkent, street ' || (gs % 5000) || ', house ' || (gs % 300) AS address,
    '+998' || LPAD(((900000000 + (gs % 99999999))::text), 9, '0') AS phone_number,
    'user' || gs || '@mail.com' AS email,
    CASE WHEN (random() < 0.7) THEN NULL ELSE 'https://pics.example/' || gs || '.jpg' END AS photo_url,
    LPAD(gs::text, 14, '0') AS pinfl,
    (18 + (gs % 63))::int AS age, -- 18..80
    CASE WHEN (gs % 2 = 0) THEN 'MALE' ELSE 'FEMALE' END AS gender,
    CASE (gs % 3)
        WHEN 0 THEN 'PASSPORT'
        WHEN 1 THEN 'ID_CARD'
        ELSE 'DRIVER_LICENSE'
        END AS document_type,
    (DATE '2010-01-01' + ((gs % 5000))::int) AS issue_date,
    (DATE '2010-01-01' + ((gs % 5000))::int + 3650) AS expiry_date, -- +10 лет
    CASE (gs % 5)
        WHEN 0 THEN 'Uzbekistan'
        WHEN 1 THEN 'Russia'
        WHEN 2 THEN 'Kazakhstan'
        WHEN 3 THEN 'Kyrgyzstan'
        ELSE 'Tajikistan'
        END AS citizenship,
    CASE WHEN random() < 0.97 THEN NULL ELSE (DATE '2015-01-01' + ((gs % 3000))::int) END AS death_date
FROM generate_series(1, 3000000) AS gs;