-- login with root and execute
CREATE USER 'root'@'%' IDENTIFIED BY 'vnc12345';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
create user springstudent identified by 'springstudent';
GRANT ALL PRIVILEGES ON * . * TO 'springstudent'@'%';

-- Use below queries to verify if required
-- use mysql;
-- select * from mysql.user;

-- drop user 'root'@'%';
-- drop user 'springstudent';