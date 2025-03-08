/**
 * Initialize database with admin user.
 * Author:  Amarechal
 * Created: 2025-03-08
 */

/**
* Create admin user with password 123456.
*/
INSERT INTO users(id, username, firstname, email, password)
    VALUES(1, 'admin', 'Admin', 'admin@admin.com',
    '$2a$10$PzGWioWY1mquBLiseIiBSuzbSitH4T4v/cvDorq4au82wWGPELWVa');

/**
* Create roles.
*/
INSERT INTO authority(id, authority) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

/** 
* Add role admin to admin user.
*/
INSERT INTO user_authority_join_table(user_id, authority_id) VALUES
    (1, 1), (1, 2);