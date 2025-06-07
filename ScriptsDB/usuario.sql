-- Elimina el usuario si existe (sintaxis compatible con MariaDB)
DROP USER IF EXISTS 'userpvsv'@'localhost';

-- Crea el nuevo usuario
CREATE USER 'userpvsv'@'localhost' IDENTIFIED BY 'Sombrilla123##';

-- Asigna privilegios
GRANT ALL PRIVILEGES ON sombrilla_verde.* TO 'userpvsv'@'localhost';
FLUSH PRIVILEGES;
