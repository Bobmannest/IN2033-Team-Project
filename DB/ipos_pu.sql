CREATE DATABASE IF NOT EXISTS ipos_pu;
USE ipos_pu;

CREATE TABLE IF NOT EXISTS Member (
    account_no VARCHAR(10) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    member_type ENUM('non_commercial', 'commercial') NOT NULL DEFAULT 'non_commercial',
    is_first_login BOOLEAN NOT NULL DEFAULT TRUE,
    order_count INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Product (
    item_id VARCHAR(10) PRIMARY KEY,
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
    item_id VARCHAR(10) NOT NULL,
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
    item_id VARCHAR(10) NOT NULL,
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
    item_id VARCHAR(10) NOT NULL,
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