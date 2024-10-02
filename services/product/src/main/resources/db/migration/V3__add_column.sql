ALTER TABLE products ADD COLUMN created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE products ADD COLUMN  updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE products ADD COLUMN  updated_by VARCHAR(100);

CREATE INDEX index_products_product_id ON products (product_id);