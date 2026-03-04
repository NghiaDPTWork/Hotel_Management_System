-- ===================================================================
-- HOTEL MANAGEMENT DATABASE - PHIÊN BẢN TỐI ƯU
-- ===================================================================

-- Xóa Database cũ nếu tồn tại
IF DB_ID('HotelManagement') IS NOT NULL
BEGIN
    ALTER DATABASE HotelManagement SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE HotelManagement;
END
GO

-- Tạo Database
CREATE DATABASE HotelManagement;
GO

USE HotelManagement;
GO

-- ===================================================================
-- PHẦN 1: TẠO CẤU TRÚC CÁC BẢNG
-- ===================================================================

-- 1. Bảng Khách hàng (GUEST)
CREATE TABLE GUEST
(
    GuestID      INT IDENTITY(1,1) PRIMARY KEY,
    FullName     NVARCHAR(100) NOT NULL,
    Phone        NVARCHAR(20) UNIQUE,
    Email        NVARCHAR(100) UNIQUE,
    PasswordHash NVARCHAR(255) NOT NULL,
    Address      NVARCHAR(200),
    IDNumber     NVARCHAR(50),
    DateOfBirth  DATE
);

-- 2. Bảng Loại phòng (ROOM_TYPE)
CREATE TABLE ROOM_TYPE
(
    RoomTypeID    INT IDENTITY(1,1) PRIMARY KEY,
    TypeName      NVARCHAR(50) NOT NULL,
    Capacity      INT NOT NULL CHECK (Capacity > 0),
    PricePerNight DECIMAL(10, 2) NOT NULL CHECK (PricePerNight >= 0)
);

-- 3. Bảng Phòng (ROOM)
CREATE TABLE ROOM
(
    RoomID      INT IDENTITY(1,1) PRIMARY KEY,
    RoomNumber  NVARCHAR(10) UNIQUE NOT NULL,
    RoomTypeID  INT NOT NULL,
    Description NVARCHAR(500) NULL,
    Status      NVARCHAR(20) CHECK (Status IN ('Available', 'Occupied', 'Dirty', 'Maintenance')),
    FOREIGN KEY (RoomTypeID) REFERENCES ROOM_TYPE(RoomTypeID)
);

-- 4. Bảng Thiết bị (DEVICE)
CREATE TABLE DEVICE
(
    DeviceID    INT IDENTITY(1,1) PRIMARY KEY,
    DeviceName  NVARCHAR(100) NOT NULL,
    Description NVARCHAR(500) NULL
);

-- 5. Bảng Thiết bị trong phòng (ROOM_DEVICE)
CREATE TABLE ROOM_DEVICE
(
    RoomDeviceID INT IDENTITY(1,1) PRIMARY KEY,
    RoomID       INT NOT NULL,
    DeviceID     INT NOT NULL,
    Quantity     INT DEFAULT 1 CHECK (Quantity > 0),
    FOREIGN KEY (RoomID) REFERENCES ROOM(RoomID),
    FOREIGN KEY (DeviceID) REFERENCES DEVICE(DeviceID)
);

-- 6. Bảng Nhân viên (STAFF)
CREATE TABLE STAFF
(
    StaffID      INT IDENTITY(1,1) PRIMARY KEY,
    FullName     NVARCHAR(100) NOT NULL,
    Role         NVARCHAR(50) CHECK (Role IN ('Receptionist', 'Manager', 'Housekeeping', 'ServiceStaff', 'Admin', 'Repair')),
    Username     NVARCHAR(50) UNIQUE NOT NULL,
    PasswordHash NVARCHAR(255) NOT NULL,
    Phone        NVARCHAR(20),
    Email        NVARCHAR(100),
    Status       INT NOT NULL DEFAULT 1 CHECK (Status IN (1, 0))
);

-- 7. Bảng Sửa chữa (REPAIR)
CREATE TABLE REPAIR
(
    RepairID           INT IDENTITY(1,1) PRIMARY KEY,
    RoomDeviceID       INT NOT NULL,
    StaffID            INT NULL,
    ReportDate         DATETIME NOT NULL DEFAULT GETDATE(),
    CompletionDate     DATETIME NULL,
    NextdateMaintaince DATE NULL,
    Description        NVARCHAR(500) NOT NULL,
    Cost               DECIMAL(10, 2) DEFAULT 0,
    Status             NVARCHAR(50) CHECK (Status IN ('Pending', 'In Progress', 'Completed', 'Canceled')),
    FOREIGN KEY (RoomDeviceID) REFERENCES ROOM_DEVICE(RoomDeviceID),
    FOREIGN KEY (StaffID) REFERENCES STAFF(StaffID)
);

-- 8. Bảng Lịch sử dọn dẹp (ROOM_TASK)
CREATE TABLE ROOM_TASK
(
    RoomTaskID  INT IDENTITY(1,1) PRIMARY KEY,
    RoomID      INT NOT NULL,
    StaffID     INT NULL,
    StartTime   DATETIME NULL,
    EndTime     DATETIME NULL,
    StatusClean NVARCHAR(50) CHECK (StatusClean IN ('Cleaned', 'In Progress', 'Pending', 'Maintance')),
    Notes       NVARCHAR(500) NULL,
    FOREIGN KEY (RoomID) REFERENCES ROOM(RoomID),
    FOREIGN KEY (StaffID) REFERENCES STAFF(StaffID)
);

-- 9. Bảng Đặt phòng (BOOKING)
CREATE TABLE BOOKING
(
    BookingID    INT IDENTITY(1,1) PRIMARY KEY,
    GuestID      INT NOT NULL,
    RoomID       INT NOT NULL,
    CheckInDate  DATETIME NOT NULL,
    CheckOutDate DATETIME NOT NULL,
    BookingDate  DATE DEFAULT GETDATE(),
    Status       NVARCHAR(20) CHECK (Status IN ('Reserved', 'Checked-in', 'Checked-out', 'Canceled')),
    FOREIGN KEY (GuestID) REFERENCES GUEST(GuestID),
    FOREIGN KEY (RoomID) REFERENCES ROOM(RoomID),
    CHECK (CheckOutDate > CheckInDate)
);

-- 10. Bảng Dịch vụ (SERVICE)
CREATE TABLE SERVICE
(
    ServiceID   INT IDENTITY(1,1) PRIMARY KEY,
    ServiceName NVARCHAR(100) NOT NULL,
    ServiceType NVARCHAR(50),
    Price       DECIMAL(10, 2) NOT NULL CHECK (Price >= 0)
);

-- 11. Bảng Chi tiết Dịch vụ của Đặt phòng (BOOKING_SERVICE)
CREATE TABLE BOOKING_SERVICE
(
    Booking_Service_ID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID          INT NOT NULL,
    ServiceID          INT NOT NULL,
    StaffID            INT NULL,
    Quantity           INT DEFAULT 1 CHECK (Quantity > 0),
    ServiceDate        DATE DEFAULT GETDATE(),
    Status             INT DEFAULT 0, -- -1: huy ; 0: chua lam; 1: dang lam; 2:lam roi
    FOREIGN KEY (BookingID) REFERENCES BOOKING(BookingID),
    FOREIGN KEY (ServiceID) REFERENCES SERVICE(ServiceID),
    FOREIGN KEY (StaffID) REFERENCES STAFF(StaffID)
);

-- 12. Bảng Hóa đơn (INVOICE)
CREATE TABLE INVOICE
(
    InvoiceID   INT IDENTITY(1,1) PRIMARY KEY,
    BookingID   INT NOT NULL UNIQUE,
    IssueDate   DATE DEFAULT GETDATE(),
    Price       DECIMAL(12, 2) NOT NULL CHECK (Price >= 0),
    Discount    DECIMAL(12, 2) NOT NULL DEFAULT 0 CHECK (Discount >= 0),
    Tax         DECIMAL(12, 2) NOT NULL DEFAULT 0 CHECK (Tax >= 0),
    TotalAmount DECIMAL(12, 2) NOT NULL CHECK (TotalAmount >= 0),
    Status      NVARCHAR(20) CHECK (Status IN ('Unpaid', 'Paid', 'Canceled')),
    FOREIGN KEY (BookingID) REFERENCES BOOKING(BookingID)
);

-- 13. Bảng Thanh toán (PAYMENT)
CREATE TABLE PAYMENT
(
    PaymentID     INT IDENTITY(1,1) PRIMARY KEY,
    BookingID     INT NOT NULL,
    PaymentDate   DATE DEFAULT GETDATE(),
    Amount        DECIMAL(12, 2) NOT NULL CHECK (Amount >= 0),
    PaymentMethod NVARCHAR(50) CHECK (PaymentMethod IN ('Cash', 'Credit Card', 'Debit Card', 'Online')),
    Status        NVARCHAR(20) CHECK (Status IN ('Deposit', 'Pending', 'Completed', 'Failed')),
    FOREIGN KEY (BookingID) REFERENCES BOOKING(BookingID)
);

-- 14. Bảng Cấu hình hệ thống (SYSTEM_CONFIG)
CREATE TABLE SYSTEM_CONFIG
(
    ConfigID    INT IDENTITY(1,1) PRIMARY KEY,
    ConfigName  NVARCHAR(50) NOT NULL,
    ConfigValue NVARCHAR(50) NOT NULL,
    Status       INT NOT NULL DEFAULT 1 CHECK (Status IN (1, 0))
);
GO

-- ===================================================================
-- PHẦN 2: CHÈN DỮ LIỆU
-- ===================================================================

-- 1. GUEST - 10 khách hàng
INSERT INTO GUEST (FullName, Phone, Email, PasswordHash, Address, IDNumber, DateOfBirth)
VALUES 
(N'Dương Phạm Trọng Nghĩa', '0901793000', 'msitr2112279@gmail.com', '1', N'123 Đình Phong Phú, Q.9, TPHCM', '0123456000', '1999-02-01'),
(N'Trần Thị Bình', '0912345678', 'tranthibinh@email.com', '2', N'456 Hai Bà Trưng, Đà Nẵng', '087654321', '1988-11-22'),
(N'Lê Hoàng Cường', '0987654321', 'lehoangcuong@email.com', '3', N'789 Trần Hưng Đạo, Hà Nội', '055544433', '1995-02-10'),
(N'Phạm Thị Dung', '0911122333', 'phamthidung@email.com', '4', N'101 Nguyễn Trãi, Cần Thơ', '098712345', '2000-07-20'),
(N'Võ Minh Hải', '0933445566', 'vominhhai@email.com', '5', N'212 Lý Thường Kiệt, Huế', '045678912', '1985-01-30'),
(N'Đỗ Gia Hân', '0977889900', 'dogiahan@email.com', '6', N'333 Hùng Vương, Nha Trang', '078945612', '1992-09-05'),
(N'Hoàng Văn Kiên', '0905678123', 'hoangvankien@email.com', '7', N'555 Phan Châu Trinh, Đà Nẵng', '011223344', '1998-03-12'),
(N'Lý Thu Thảo', '0988776655', 'lythuthao@email.com', '8', N'444 Bạch Đằng, Hà Nội', '022334455', '1993-12-25'),
(N'Nguyễn Văn An', '0901234567', 'nguyenvanan@email.com', '9', N'123 Lê Lợi, Q1, TPHCM', '012345678', '1990-05-15'),
(N'Bùi Văn Tuấn', '0909111222', 'buivantuan@email.com', '10', N'567 Điện Biên Phủ, TPHCM', '034567891', '1991-06-18');

-- 2. ROOM_TYPE - 10 loại phòng
INSERT INTO ROOM_TYPE (TypeName, Capacity, PricePerNight)
VALUES 
('Standard Single', 1, 300000),
('Standard Double', 2, 500000),
('Deluxe King', 2, 750000),
('Family Suite', 4, 1200000),
('Executive VIP', 2, 1500000),
('Grand Suite', 3, 2000000),
('Deluxe Twin', 2, 550000),
('Triple Comfort', 3, 900000),
('The Penthouse', 6, 5000000),
('City View Studio', 2, 800000);

-- 3. ROOM - 50 phòng (phân bổ không đều giữa các loại)
INSERT INTO ROOM (RoomNumber, RoomTypeID, Description, Status)
VALUES
-- Standard Single (12 phòng) - Loại phổ biến nhất
('101', 1, N'Phòng đơn tầng 1', 'Available'),
('102', 1, N'Phòng đơn tầng 1', 'Available'),
('103', 1, N'Phòng đơn tầng 1', 'Occupied'),
('104', 1, N'Phòng đơn tầng 1', 'Available'),
('201', 1, N'Phòng đơn tầng 2', 'Available'),
('202', 1, N'Phòng đơn tầng 2', 'Dirty'),
('203', 1, N'Phòng đơn tầng 2', 'Available'),
('301', 1, N'Phòng đơn tầng 3', 'Available'),
('302', 1, N'Phòng đơn tầng 3', 'Occupied'),
('401', 1, N'Phòng đơn tầng 4', 'Available'),
('402', 1, N'Phòng đơn tầng 4', 'Available'),
('501', 1, N'Phòng đơn tầng 5', 'Available'),

-- Standard Double (10 phòng) - Loại phổ biến thứ 2
('105', 2, N'Phòng đôi tầng 1', 'Available'),
('106', 2, N'Phòng đôi tầng 1', 'Occupied'),
('204', 2, N'Phòng đôi tầng 2', 'Available'),
('205', 2, N'Phòng đôi tầng 2', 'Available'),
('303', 2, N'Phòng đôi tầng 3', 'Dirty'),
('304', 2, N'Phòng đôi tầng 3', 'Available'),
('403', 2, N'Phòng đôi tầng 4', 'Occupied'),
('404', 2, N'Phòng đôi tầng 4', 'Available'),
('502', 2, N'Phòng đôi tầng 5', 'Available'),
('503', 2, N'Phòng đôi tầng 5', 'Available'),

-- Deluxe King (8 phòng)
('107', 3, N'Phòng Deluxe view thành phố', 'Available'),
('108', 3, N'Phòng Deluxe view thành phố', 'Available'),
('206', 3, N'Phòng Deluxe tầng 2', 'Occupied'),
('207', 3, N'Phòng Deluxe tầng 2', 'Available'),
('305', 3, N'Phòng Deluxe tầng 3', 'Available'),
('405', 3, N'Phòng Deluxe tầng 4', 'Dirty'),
('504', 3, N'Phòng Deluxe tầng 5', 'Available'),
('505', 3, N'Phòng Deluxe tầng 5', 'Available'),

-- Family Suite (6 phòng)
('109', 4, N'Phòng gia đình 4 người', 'Available'),
('208', 4, N'Phòng gia đình view vườn', 'Available'),
('306', 4, N'Phòng gia đình tầng 3', 'Occupied'),
('406', 4, N'Phòng gia đình tầng 4', 'Available'),
('506', 4, N'Phòng gia đình tầng 5', 'Available'),
('507', 4, N'Phòng gia đình tầng 5', 'Available'),

-- Executive VIP (4 phòng)
('209', 5, N'Phòng VIP cao cấp', 'Available'),
('307', 5, N'Phòng VIP tầng 3', 'Occupied'),
('407', 5, N'Phòng VIP tầng 4', 'Available'),
('508', 5, N'Phòng VIP tầng 5', 'Available'),

-- Grand Suite (3 phòng)
('308', 6, N'Suite sang trọng', 'Available'),
('408', 6, N'Suite cao cấp', 'Available'),
('509', 6, N'Suite view đẹp', 'Occupied'),

-- Deluxe Twin (3 phòng)
('110', 7, N'Phòng Twin 2 giường', 'Available'),
('210', 7, N'Phòng Twin tầng 2', 'Available'),
('309', 7, N'Phòng Twin tầng 3', 'Dirty'),

-- Triple Comfort (2 phòng)
('310', 8, N'Phòng Triple 3 người', 'Available'),
('409', 8, N'Phòng Triple tầng 4', 'Available'),

-- The Penthouse (1 phòng) - Loại hiếm nhất
('511', 9, N'Penthouse tầng thượng', 'Available'),

-- City View Studio (1 phòng)
('510', 10, N'Studio có bếp nhỏ', 'Available');

-- 4. DEVICE - 13 thiết bị
INSERT INTO DEVICE (DeviceName, Description)
VALUES 
(N'Tivi LED 43 inch', N'Smart TV với kết nối wifi'),
(N'Điều hòa 2 chiều', N'Công suất 12000 BTU'),
(N'Tủ lạnh mini', N'Dung tích 50 lít'),
(N'Máy sấy tóc', N'Công suất 2000W'),
(N'Ấm đun nước siêu tốc', N'Dung tích 1.7 lít'),
(N'Két sắt điện tử', N'An toàn, kích thước nhỏ gọn'),
(N'Máy pha cà phê', N'Tự động, nhiều chế độ'),
(N'Bàn là hơi nước', N'Công suất 2400W'),
(N'Máy hút bụi cầm tay', N'Pin sạc, không dây'),
(N'Loa bluetooth', N'Âm thanh stereo chất lượng cao'),
(N'Đèn ngủ cảm ứng', N'3 chế độ ánh sáng'),
(N'Quạt trần', N'Điều khiển từ xa, 5 cánh'),
(N'Máy lọc không khí', N'HEPA filter, diện tích 30m²');

-- 5. STAFF - 8 nhân viên
INSERT INTO STAFF (FullName, Role, Username, PasswordHash, Phone, Email, Status)
VALUES 
(N'Dương Nghĩa', 'Admin', 'dn', '1', '0907654321', 'dn.admin@hotel.com', 1),
(N'Phạm Minh Quân', 'Manager', 'manager01', 'hash_1', '0331112222', 'quan.pm@hotel.com', 1),
(N'Hoàng Thị Lan', 'Receptionist', 'receptionist01', 'hash_2', '0333334444', 'lan.ht@hotel.com', 1),
(N'Trần Văn Bình', 'Receptionist', 'receptionist02', 'hash_3', '0333334445', 'binh.tv@hotel.com', 1),
(N'Nguyễn Thị Lan', 'Housekeeping', 'housekeeping01', 'hash_4', '0901234567', 'lan.nt@hotel.com', 1),
(N'Trần Văn An', 'Housekeeping', 'housekeeping02', 'hash_5', '0907654321', 'an.tv@hotel.com', 1),
(N'Bùi Thị Hoa', 'ServiceStaff', 'service01', 'hash_9', '0933445566', 'hoa.bt@hotel.com', 1),
(N'Hoàng Văn Nam', 'Repair', 'repair01', 'hash_10', '0944556677', 'nam.hv@hotel.com', 1);

-- 6. ROOM_DEVICE - Thiết bị cho 20 phòng đầu
INSERT INTO ROOM_DEVICE (RoomID, DeviceID, Quantity)
VALUES 
(1, 1, 1), (1, 2, 1), (2, 1, 1), (2, 2, 1), (3, 1, 1),
(3, 2, 1), (3, 3, 1), (4, 1, 1), (4, 2, 1), (5, 1, 1),
(6, 1, 1), (6, 2, 1), (6, 3, 1), (7, 1, 1), (7, 2, 1),
(8, 1, 1), (8, 2, 1), (9, 1, 2), (9, 2, 2), (10, 1, 1);

-- 7. BOOKING - 10 đặt phòng
INSERT INTO BOOKING (GuestID, RoomID, CheckInDate, CheckOutDate, BookingDate, Status)
VALUES
(1, 1, '2025-11-01 14:00:00', '2025-11-03 12:00:00', '2025-10-29', 'Checked-in'),
(2, 2, '2025-11-01 14:00:00', '2025-11-02 12:00:00', '2025-10-29', 'Checked-in'),
(3, 3, '2025-11-01 15:00:00', '2025-11-04 12:00:00', '2025-10-30', 'Checked-in'),
(4, 4, '2025-11-01 15:00:00', '2025-11-03 12:00:00', '2025-10-30', 'Checked-in'),
(5, 5, '2025-11-01 16:00:00', '2025-11-02 12:00:00', '2025-10-30', 'Checked-in'),
(6, 6, '2025-11-01 16:00:00', '2025-11-05 12:00:00', '2025-10-30', 'Checked-in'),
(7, 7, '2025-11-01 17:00:00', '2025-11-02 12:00:00', '2025-10-31', 'Checked-in'),
(8, 8, '2025-11-01 17:00:00', '2025-11-03 12:00:00', '2025-10-31', 'Checked-in'),
(9, 9, '2025-11-01 18:00:00', '2025-11-04 12:00:00', '2025-10-31', 'Checked-in'),
(10, 10, '2025-11-01 18:00:00', '2025-11-02 12:00:00', '2025-10-31', 'Checked-in');

-- 8. SERVICE - 20 dịch vụ
INSERT INTO SERVICE (ServiceName, ServiceType, Price)
VALUES 
('Buffet Breakfast', 'Food', 150000),
('Set Menu Lunch', 'Food', 250000),
('Laundry Service (1kg)', 'Laundry', 50000),
('Massage (60 min)', 'Spa', 400000),
('Room Cleaning', 'HouseKeeping', 100000),
('Airport Transfer', 'Transport', 300000),
('In-Room Dining', 'Food', 350000),
('Meeting Room Rental (1 hour)', 'Business', 500000),
('Bicycle Rental (1 day)', 'Recreation', 100000),
('Sauna & Steam Access', 'Spa', 200000),
('Premium Mini-bar', 'Amenity', 300000),
('Buffet Dinner', 'Food', 400000),
('Ironing Service', 'Laundry', 80000),
('Hair Salon Service', 'Beauty', 250000),
('Gym Access', 'Recreation', 100000),
('Car Rental (1 day)', 'Transport', 1500000),
('Babysitting (1 hour)', 'Service', 150000),
('City Tour', 'Recreation', 800000),
('Wine & Cheese Platter', 'Food', 500000),
('Full Spa Package', 'Spa', 1200000);

-- 9. BOOKING_SERVICE
INSERT INTO BOOKING_SERVICE (BookingID, ServiceID, StaffID, Quantity, ServiceDate, Status)
VALUES 
(1, 1, 7, 1, '2025-11-01', 1),
(1, 4, 7, 1, '2025-11-01', 0),
(2, 1, 7, 1, '2025-11-01', 1),
(3, 2, 7, 1, '2025-11-01', 0),
(4, 1, 7, 2, '2025-11-01', 1),
(5, 5, 7, 1, '2025-11-01', 1),
(6, 1, 7, 4, '2025-11-01', 0),
(7, 1, 7, 1, '2025-11-01', 1),
(8, 4, 7, 1, '2025-11-02', 0),
(9, 1, 7, 3, '2025-11-02', 1);

-- 10. INVOICE
INSERT INTO INVOICE (BookingID, IssueDate, Price, Discount, Tax, TotalAmount, Status)
VALUES 
(1, '2025-11-01', 600000, 30000, 57000, 627000, 'Unpaid'),
(2, '2025-11-01', 500000, 0, 50000, 550000, 'Unpaid');

-- 11. PAYMENT
INSERT INTO PAYMENT (BookingID, PaymentDate, Amount, PaymentMethod, Status)
VALUES
(1, '2025-11-01', 300000, 'Credit Card', 'Deposit'),
(2, '2025-11-01', 250000, 'Credit Card', 'Deposit'),
(3, '2025-11-01', 750000, 'Credit Card', 'Deposit'),
(4, '2025-11-01', 300000, 'Credit Card', 'Deposit'),
(5, '2025-11-01', 150000, 'Cash', 'Deposit'),
(10, '2025-11-01', 275000, 'Online', 'Deposit');

-- 12. SYSTEM_CONFIG
INSERT INTO SYSTEM_CONFIG (ConfigName, ConfigValue, Status)
VALUES 
('CheckInTime', '14:00', 1),
('CheckOutTime', '12:00', 1),
('CancellationPeriod', '24', 1),
('MaxGuestsPerRoom', '4', 1),
('TaxRate', '10', 1),
('DefaultCurrency', 'VND', 1),
('BookingDepositRate', '50', 1),
('LateCheckOutFee', '100000', 1),
('EarlyCheckInFee', '50000', 1),
('PetPolicy', 'NotAllowed', 1),
('SmokingPolicy', 'NoSmoking', 1),
('MinBookingDays', '1', 1),
('MaxBookingDays', '30', 1),
('CleaningFee', '50000', 1),
('ExtraBedFee', '100000', 1),
('ChildAge', '12', 1),
('ChildDiscountRate', '50', 1),
('LoyaltyPointsRate', '1', 1),
('RefundPolicy', '30', 1),
('SystemLanguage', 'Vietnamese', 1);

GO

-- ===================================================================
-- PHẦN 3: KIỂM TRA DỮ LIỆU
-- ===================================================================

PRINT N'============================================';
PRINT N'KIỂM TRA DỮ LIỆU TRONG CÁC BẢNG';
PRINT N'============================================';
PRINT N'';

PRINT N'1. GUEST: ' + CAST((SELECT COUNT(*) FROM GUEST) AS NVARCHAR(10)) + N' records';
PRINT N'2. ROOM_TYPE: ' + CAST((SELECT COUNT(*) FROM ROOM_TYPE) AS NVARCHAR(10)) + N' records';
PRINT N'3. ROOM: ' + CAST((SELECT COUNT(*) FROM ROOM) AS NVARCHAR(10)) + N' records';
PRINT N'4. DEVICE: ' + CAST((SELECT COUNT(*) FROM DEVICE) AS NVARCHAR(10)) + N' records';
PRINT N'5. STAFF: ' + CAST((SELECT COUNT(*) FROM STAFF) AS NVARCHAR(10)) + N' records';
PRINT N'6. ROOM_DEVICE: ' + CAST((SELECT COUNT(*) FROM ROOM_DEVICE) AS NVARCHAR(10)) + N' records';
PRINT N'7. REPAIR: ' + CAST((SELECT COUNT(*) FROM REPAIR) AS NVARCHAR(10)) + N' records';
PRINT N'8. ROOM_TASK: ' + CAST((SELECT COUNT(*) FROM ROOM_TASK) AS NVARCHAR(10)) + N' records';
PRINT N'9. BOOKING: ' + CAST((SELECT COUNT(*) FROM BOOKING) AS NVARCHAR(10)) + N' records';
PRINT N'10. SERVICE: ' + CAST((SELECT COUNT(*) FROM SERVICE) AS NVARCHAR(10)) + N' records';
PRINT N'11. BOOKING_SERVICE: ' + CAST((SELECT COUNT(*) FROM BOOKING_SERVICE) AS NVARCHAR(10)) + N' records';
PRINT N'12. INVOICE: ' + CAST((SELECT COUNT(*) FROM INVOICE) AS NVARCHAR(10)) + N' records';
PRINT N'13. PAYMENT: ' + CAST((SELECT COUNT(*) FROM PAYMENT) AS NVARCHAR(10)) + N' records';
PRINT N'14. SYSTEM_CONFIG: ' + CAST((SELECT COUNT(*) FROM SYSTEM_CONFIG) AS NVARCHAR(10)) + N' records';
PRINT N'';

-- Thống kê phân bổ phòng theo loại
PRINT N'============================================';
PRINT N'THỐNG KÊ PHÂN BỔ PHÒNG THEO LOẠI';
PRINT N'============================================';

SELECT 
    RT.TypeName AS N'Room Type',
    COUNT(R.RoomID) AS N'Quantity',
    RT.PricePerNight AS N'Price/Night',
    COUNT(R.RoomID) * RT.PricePerNight AS N'Total Value'
FROM ROOM_TYPE RT
LEFT JOIN ROOM R ON RT.RoomTypeID = R.RoomTypeID
GROUP BY RT.TypeName, RT.PricePerNight
ORDER BY COUNT(R.RoomID) DESC;

PRINT N'';
PRINT N'============================================';
PRINT N'HOÀN TẤT TẠO DATABASE VÀ CHÈN DỮ LIỆU';
PRINT N'============================================';
PRINT N'- 10 Khách hàng';
PRINT N'- 10 Loại phòng';
PRINT N'- 50 Phòng (phân bổ không đều)';
PRINT N'- 8 Nhân viên';
PRINT N'- 10 Bookings với đầy đủ dịch vụ';
PRINT N'============================================';

GO