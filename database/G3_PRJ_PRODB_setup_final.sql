/* =========================================================
   RESET DATABASE
========================================================= */
USE master;
GO

IF DB_ID('Nhom3_PRJ301_Pro') IS NOT NULL
BEGIN
    ALTER DATABASE Nhom3_PRJ301_Pro 
    SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE Nhom3_PRJ301_Pro;
END
GO

CREATE DATABASE Nhom3_PRJ301_Pro;
GO

USE Nhom3_PRJ301_Pro;
GO


/* =========================================================
   1. USERS (UPDATED: thêm phone + avatar)
========================================================= */
CREATE TABLE users (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    userName NVARCHAR(100) NOT NULL,
    userEmail NVARCHAR(100) NOT NULL UNIQUE,
    userPassword NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20) NULL,                 -- SĐT người dùng
    avatar NVARCHAR(255) NULL,               -- Đường dẫn ảnh đại diện
    role NVARCHAR(20) DEFAULT 'user',        -- admin | staff | user
    balance DECIMAL(12,2) DEFAULT 0,
    status NVARCHAR(20) DEFAULT 'active'
);

INSERT INTO users (userName, userEmail, userPassword, phone, avatar, role, balance)
VALUES 
(N'Admin', 'admin@mail.com', '123', '0900000000', 'uploads/admin.png', 'admin', 0),
(N'User Test', 'user@mail.com', '123', '0912345678', 'uploads/avatar_6_1773080759588.jpg', 'user', 0);


/* =========================================================
   2. STUFF (ĐỒ PHƯỢT)
========================================================= */
CREATE TABLE stuff (
    stuffId INT IDENTITY(1,1) PRIMARY KEY,
    stuffName NVARCHAR(100) NOT NULL,
    base_price_per_day DECIMAL(12,2) DEFAULT 0,
    stuffIcon NVARCHAR(200) NULL
);
INSERT INTO stuff (stuffName, base_price_per_day, stuffIcon) VALUES
(N'Mũ bảo hiểm', 0, 'uploads/mubaohiem.jpg'),
(N'Kệ điện thoại', 5000, 'uploads/kededienthoai.webp'),
(N'Cổng sạc', 5000, 'uploads/congsac.jpg'),
(N'Lưới ràng đồ', 5000, 'uploads/Luoi_rang_do.webp'),
(N'Baga sau', 10000, 'uploads/bagasau.webp'),
(N'Bơm', 5000, 'uploads/bom.webp'),
(N'Bộ vá lốp xe', 10000, 'uploads/bovalopxe.webp'),
(N'Dây chằng', 5000, 'uploads/daychang.webp'),
(N'Balo chống nước', 15000, 'uploads/balochongnuoc.webp'),
(N'Túi treo bình xăng', 10000, 'uploads/tuitreobinhxang.webp'),
(N'Túi hông', 10000, 'uploads/tuihong.webp'),
(N'Camera hành trình', 30000, 'uploads/camera.webp'),
(N'Thùng đồ', 20000, 'uploads/thungcivi.png'),
(N'Găng tay phượt', 10000, 'uploads/Gang_tay_phuot.jpg'),
(N'Giáp bảo hộ', 30000, 'uploads/Giap_bao_ho.jpg'),
(N'Áo mưa phượt', 5000, 'uploads/Ao_mua_phuot.jpg'),
(N'Kính phượt', 10000, 'uploads/Kinh_Phuot.jpg');
/* =========================================================
   3. MOTORBIKES
========================================================= */
CREATE TABLE motorbikes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    brand NVARCHAR(100),
    current_price_per_day DECIMAL(12,2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'available',
    description NVARCHAR(MAX),
    engine_size NVARCHAR(50),
    transmission NVARCHAR(50),
    manufacture_year INT,
    bikeIcon NVARCHAR(200)
);

INSERT INTO motorbikes (name, brand, current_price_per_day, status, description, engine_size, transmission, manufacture_year, bikeIcon) VALUES 
('CRF150L', 'Honda', 350000.00, 'available', '', '150cc', 'Manual', 2019, 'uploads/bike_1773766917226_crf150.jpg'),
('CB500X', 'Honda', 1200000.00, 'available', '', '471cc', 'Manual', 2024, 'uploads/bike_1773766766841_CB500x-Black-Dynamic.png'),
('Wave Alpha', 'Honda', 150000.00, 'available', '', '110cc', 'Manual', 2017, 'uploads/bike_1773766358251_wave110.png'),
('Winner R', 'Honda', 450000.00, 'available', '', '155cc', 'Manual', 2026, 'uploads/bike_1773766164620_winner r.png'),
('Exciter 155', 'Yamaha', 500000.00, 'rented', '', '155cc', 'Manual', 2024, 'uploads/bike_1773767699834_05cyamaha-exciter-155-vva.jpg'),
('PG-1', 'Yamaha', 300000.00, 'available', 'Adventure underbone', '115cc', 'Manual', 2024, 'uploads/bike_1773765830210_pg1.jpg'),
('CT125', 'Honda', 600000.00, 'available', 'Hunter Cub', '125cc', 'Manual', 2023, 'uploads/bike_1773765636719_ct125.jpg'),
('Air Blade 160', 'Honda', 350000.00, 'available', 'Modern city scooter', '160cc', 'Automatic', 2024, 'uploads/bike_1773765609432_ab160.jpeg'),
('NVX 155', 'Yamaha', 400000.00, 'available', 'Sport scooter', '155cc', 'Automatic', 2023, 'uploads/bike_1773765575103_nvx155.jpg'),
('SH 350i', 'Honda', 800000.00, 'available', 'Premium scooter', '350cc', 'Automatic', 2025, 'uploads/bike_1773765412212_2025-honda-sh350i-silver-1000x667.jpg'),
('Vespa Sprint', 'Piaggio', 750000.00, 'rented', 'Classic Italian style', '150cc', 'Automatic', 2023, 'uploads/bike_1773765365716_vespa sprint.png'),
('W175', 'Kawasaki', 450000.00, 'available', 'Retro classic', '177cc', 'Manual', 2022, 'uploads/bike_1773765329781_w175.jpg'),
('XSR 155', 'Yamaha', 550000.00, 'available', 'Neo-retro sport', '155cc', 'Manual', 2023, 'uploads/bike_1773765301852_xsr155.png'),
('CB150X', 'Honda', 500000.00, 'available', 'Light adventure tourer', '150cc', 'Manual', 2023, 'uploads/bike_1773765270641_Honda-CB150X-1.png'),
('MT-15', 'Yamaha', 500000.00, 'available', 'Naked streetfighter', '155cc', 'Manual', 2023, 'uploads/bike_1773765034981_mt-15_2019_1.jpg'),
('R15M', 'Yamaha', 600000.00, 'rented', 'Sportbike', '155cc', 'Manual', 2024, 'uploads/bike_1773764860773_2025-yamaha-r15m-connected-abs-silver.jpg'),
('CBR150R', 'Honda', 600000.00, 'available', 'Sportbike', '150cc', 'Manual', 2023, 'uploads/bike_1773764798338_Honda-CBR150R-2025-Malaysia-BM-7-e1742456752676-1200x1045.jpg'),
('Duke 200', 'KTM', 700000.00, 'available', 'Aggressive naked bike', '200cc', 'Manual', 2022, 'uploads/bike_1773764703762_ktm_duke_200.jpg'),
('Himalayan', 'Royal Enfield', 900000.00, 'available', 'Adventure touring', '411cc', 'Manual', 2022, 'uploads/bike_1773764651690_himalayan.jpg'),
('V-Strom 250SX', 'Suzuki', 750000.00, 'available', 'Sport adventure tourer', '250cc', 'Manual', 2023, 'uploads/bike_1773764616567_vstrom.png'),
('Satria F150', 'Suzuki', 450000.00, 'available', 'Hyper-underbone', '150cc', 'Manual', 2023, 'uploads/bike_1773764557240_satria.png'),
('Raider R150', 'Suzuki', 450000.00, 'rented', 'Hyper-underbone', '150cc', 'Manual', 2023, 'uploads/bike_1773764467181_raider.png'),
('Vision', 'Honda', 200000.00, 'available', 'Compact scooter', '110cc', 'Automatic', 2024, 'uploads/bike_1773763520777_419291_23YM_HONDA_VISION_110.jpg'),
('XS155R', 'Yamaha', 550000.00, 'Deleted', 'Classic styling', '155cc', 'Manual', 2024, 'uploads/bike_1773763399465_xsr155.png'),
('ADV 160', 'Honda', 550000.00, 'available', 'Urban adventure scooter', '160cc', 'Automatic', 2024, 'uploads/bike_1773763053226_adv160.jpg');
--5xe tren 600cc
INSERT INTO motorbikes (name, brand, current_price_per_day, status, description, engine_size, transmission, manufacture_year, bikeIcon) VALUES 
('CB650R', 'Honda', 1200000.00, 'available', 'Neo sport cafe', '649cc', 'Manual', 2024, 'uploads/bike_cb650r.jpg'),
('MT-07', 'Yamaha', 1100000.00, 'available', 'Torque-rich naked bike', '689cc', 'Manual', 2023, 'uploads/bike_mt07.jpg'),
('Z900', 'Kawasaki', 1500000.00, 'available', 'Supernaked performance', '948cc', 'Manual', 2024, 'uploads/bike_z900.jpg'),
('Street Triple 765', 'Triumph', 1600000.00, 'available', 'Premium middleweight naked', '765cc', 'Manual', 2023, 'uploads/bike_street_triple_765.jpg'),
('Panigale V2', 'Ducati', 2500000.00, 'available', 'High-end superbike', '955cc', 'Manual', 2024, 'uploads/bike_panigale_v2.jpg');
/* =========================================================
   4. MOTORBIKE_STUFF_CONFIG
========================================================= */
CREATE TABLE motorbike_stuff_config (
    motorbike_id INT,
    stuff_id INT,
    included_quantity INT DEFAULT 0,
    price_override_per_day DECIMAL(12,2) NULL,

    PRIMARY KEY (motorbike_id, stuff_id),
    FOREIGN KEY (motorbike_id) REFERENCES motorbikes(id) ON DELETE CASCADE,
    FOREIGN KEY (stuff_id) REFERENCES stuff(stuffId) ON DELETE CASCADE
);



/* =========================================================
   5. RENT_ORDERS
========================================================= */
CREATE TABLE rent_orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    motorbike_id INT NOT NULL,

    price_per_day_at_rent DECIMAL(12,2) NOT NULL,

    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    total_days INT NOT NULL,

    total_bike_cost DECIMAL(12,2) NOT NULL,
    total_stuff_cost DECIMAL(12,2) DEFAULT 0,
    deposit_amount DECIMAL(12,2) DEFAULT 0,

    status NVARCHAR(20) DEFAULT 'pending',
    payment_status NVARCHAR(20) DEFAULT 'unpaid',

    FOREIGN KEY (user_id) REFERENCES users(userId),
    FOREIGN KEY (motorbike_id) REFERENCES motorbikes(id)
);


/* =========================================================
   6. ORDER_STUFF_DETAILS
========================================================= */
CREATE TABLE order_stuff_details (
    order_id INT,
    stuff_id INT,
    quantity INT NOT NULL,
    price_per_day_at_rent DECIMAL(12,2) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,

    PRIMARY KEY (order_id, stuff_id),
    FOREIGN KEY (order_id) REFERENCES rent_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (stuff_id) REFERENCES stuff(stuffId)
);


/* =========================================================
   7. TRANSACTIONS
========================================================= */
CREATE TABLE transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    order_id INT NULL,

    amount DECIMAL(12,2) NOT NULL,
    type NVARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (user_id) REFERENCES users(userId),
    FOREIGN KEY (order_id) REFERENCES rent_orders(id)
);

