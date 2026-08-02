-- Seeds the roles that UserServiceImpl.saveUser() and SecurityConfig depend on.
-- Written as INSERT ... SELECT ... WHERE NOT EXISTS so it's safe to run on
-- every application startup (spring.sql.init.mode=always) without creating
-- duplicate rows.

INSERT INTO `RoleEntity` (`name`)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (
    SELECT 1 FROM `RoleEntity` WHERE `name` = 'ROLE_ADMIN'
);

INSERT INTO `RoleEntity` (`name`)
SELECT 'ROLE_GUEST' WHERE NOT EXISTS (
    SELECT 1 FROM `RoleEntity` WHERE `name` = 'ROLE_GUEST'
);
