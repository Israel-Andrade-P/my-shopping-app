INSERT INTO roles(name, authority, reference_id, created_at, updated_at)
VALUES('USER', 'product:create,product:read,product:update,product:delete', '111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('MANAGER', 'product:create,product:read,product:update,product:delete', '222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('ADMIN', 'user:create,user:read,user:update,product:create,product:read,product:update,product:delete', '333', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('SUPER_ADMIN', 'user:create,user:read,user:update,user:delete,product:create,product:read,product:update,product:delete', '444', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);