CREATE DATABASE IF NOT EXISTS ipos_pu;
USE ipos_pu;

DROP TABLE IF EXISTS CampaignItemMetrics;
DROP TABLE IF EXISTS CampaignMetrics;
DROP TABLE IF EXISTS OnlineOrderItem;
DROP TABLE IF EXISTS OnlineOrder;
DROP TABLE IF EXISTS PromotionCampaignItem;
DROP TABLE IF EXISTS PromotionCampaign;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS CommercialApplication;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Member;

CREATE TABLE IF NOT EXISTS Member (
    account_no VARCHAR(10) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    member_type ENUM('non_commercial', 'commercial', 'admin') NOT NULL DEFAULT 'non_commercial',
    is_first_login BOOLEAN NOT NULL DEFAULT TRUE,
    order_count INT NOT NULL DEFAULT 0
);

INSERT INTO Member (account_no, email, password, member_type, is_first_login, order_count) VALUES
('sysdba',  'curatecht33+sysdba@gmail.com',  'masterkey',     'admin',          FALSE, 0),
('manager', 'curatecht33+manager@gmail.com', 'GetPU it done', 'admin',          FALSE, 0),
('PU0001',  'curatecht33+pu0001@gmail.com',  '12ss_56_SS',    'non_commercial', FALSE, 0),
('PU0002',  'curatecht33+pu0002@gmail.com',  '34pp_78_LL',    'non_commercial', FALSE, 0),
('PU0003',  'curatecht33+pu0003@gmail.com',  'changeme',      'commercial',     FALSE, 0);

CREATE TABLE IF NOT EXISTS Product (
    item_id INT(10) PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    package_type VARCHAR(50),
    unit VARCHAR(50),
    units_in_pack INT NOT NULL DEFAULT 1,
    bulk_cost DECIMAL(10,2) NOT NULL,
    quantity_available INT NOT NULL DEFAULT 0,
    stock_limit INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS PromotionCampaign (
    campaign_id VARCHAR(10) PRIMARY KEY,
    campaign_name VARCHAR(255) NOT NULL,
    campaign_description TEXT,
    start_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    status ENUM('scheduled', 'active', 'cancelled', 'expired') NOT NULL DEFAULT 'scheduled',
    discount_mode ENUM('fixed', 'variable') NOT NULL,
    default_discount_pct DECIMAL(5,2) DEFAULT NULL,
    created_by VARCHAR(10) DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_campaign_created_by
    FOREIGN KEY (created_by) REFERENCES Member(account_no)
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS PromotionCampaignItem (
    campaign_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id VARCHAR(10) NOT NULL,
    item_id INT(10) NOT NULL,
    item_discount_pct DECIMAL(5,2) DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (campaign_id, item_id),
    CONSTRAINT fk_promo_item_campaign
    FOREIGN KEY (campaign_id) REFERENCES PromotionCampaign(campaign_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_promo_item_product
    FOREIGN KEY (item_id) REFERENCES Product(item_id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS OnlineOrder (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_account_no VARCHAR(10) NOT NULL,
    campaign_id VARCHAR(10) DEFAULT NULL,
    order_status ENUM('pending', 'paid', 'cancelled', 'dispatched', 'delivered') NOT NULL DEFAULT 'pending',
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discount_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_online_order_member
    FOREIGN KEY (member_account_no) REFERENCES Member(account_no)
    ON DELETE CASCADE,
    CONSTRAINT fk_online_order_campaign
    FOREIGN KEY (campaign_id) REFERENCES PromotionCampaign(campaign_id)
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS OnlineOrderItem (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_id INT(10) NOT NULL,
    campaign_id VARCHAR(10) DEFAULT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount_pct_applied DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    final_unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_item_order
    FOREIGN KEY (order_id) REFERENCES OnlineOrder(order_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product
    FOREIGN KEY (item_id) REFERENCES Product(item_id)
    ON DELETE RESTRICT,
    CONSTRAINT fk_order_item_campaign
    FOREIGN KEY (campaign_id) REFERENCES PromotionCampaign(campaign_id)
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS CampaignMetrics (
    campaign_id VARCHAR(10) PRIMARY KEY,
    campaign_hits INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_campaign_metrics_campaign
    FOREIGN KEY (campaign_id) REFERENCES PromotionCampaign(campaign_id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CampaignItemMetrics (
    campaign_id VARCHAR(10) NOT NULL,
    item_id INT(10) NOT NULL,
    included_in_order_count INT NOT NULL DEFAULT 0,
    purchased_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (campaign_id, item_id),
    CONSTRAINT fk_item_metrics_campaign
    FOREIGN KEY (campaign_id) REFERENCES PromotionCampaign(campaign_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_item_metrics_product
    FOREIGN KEY (item_id) REFERENCES Product(item_id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Orders (
    order_id VARCHAR(20) PRIMARY KEY,
    account_no VARCHAR(10) NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    FOREIGN KEY (account_no) REFERENCES Member(account_no)
);

CREATE TABLE payments (
    id            BIGINT UNSIGNED   NOT NULL AUTO_INCREMENT,
    card_first4   CHAR(4)           NOT NULL,
    card_last4    CHAR(4)           NOT NULL,
    card_expiry   CHAR(5)           NOT NULL,
    card_type     VARCHAR(20)       NOT NULL,
    amount        DECIMAL(10, 2)    NOT NULL,
    created_at    TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CommercialApplication (
    application_id VARCHAR(20) PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    companies_house_no VARCHAR(20) NOT NULL UNIQUE,
    director_name VARCHAR(100) NOT NULL,
    business_type VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending'
);

INSERT INTO CommercialApplication (application_id, company_name, companies_house_no, director_name, business_type, address, email, status)
VALUES ('APP0001', 'Pond Pharmacy', 'UK10003429', 'TBD', 'Pharmacy', 'Chislehurst, 25 High Street, BR7 5BN', 'curatecht33+pu0003@gmail.com', 'approved');