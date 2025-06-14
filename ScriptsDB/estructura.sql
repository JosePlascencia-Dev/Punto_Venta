-- Eliminar y recrear la base de datos
DROP DATABASE IF EXISTS sombrilla_verde;
CREATE DATABASE sombrilla_verde CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sombrilla_verde;

-- 1. Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    contrasena_hash VARCHAR(255) NOT NULL,
    tipo ENUM('ADMINISTRADOR' , 'VENDEDOR') NOT NULL DEFAULT 'VENDEDOR',
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX (tipo, activo),
    INDEX (nombre_usuario)
) ENGINE=InnoDB;

-- 2. Tabla de provedores
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL UNIQUE,
    contacto_principal VARCHAR(100),
    telefono VARCHAR(15) UNIQUE,
    email VARCHAR(100) UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX (nombre_empresa)
) ENGINE=InnoDB;

-- 3. Tabla de categorías
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    detalles VARCHAR(300)
) ENGINE=InnoDB;

-- 4. Tabla de productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    codigo VARCHAR(32) NOT NULL UNIQUE,
    id_categoria INT,
    id_proveedor INT,
    precio_compra DECIMAL(12,2) UNSIGNED NOT NULL,
    precio_venta DECIMAL(12,2) UNSIGNED NOT NULL,
    existencia DECIMAL(10,3) UNSIGNED NOT NULL DEFAULT 0,
    unidad_medida ENUM('PIEZA', 'KILO', 'LITRO', 'PAQUETE', 'CAJA') NOT NULL DEFAULT 'PIEZA',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL,
    INDEX (nombre),
    INDEX (existencia)
) ENGINE=InnoDB;

-- 5. Tabla de compras a proveedores
CREATE TABLE compras_proveedor (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    fecha_compra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    id_usuario_registro INT NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    FOREIGN KEY (id_usuario_registro) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

-- 6. Tabla de ventas 
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_vendedor INT NOT NULL,
    total DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;


-- 7. Tabla de detalle de venta
CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    precio_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;

-- 8. Tabla de detalle de compras
CREATE TABLE detalle_compra (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_compra INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    costo_unitario DECIMAL(12,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(12,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_compra) REFERENCES compras_proveedor(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;

-- 9. Tabla de historial de productos 
CREATE TABLE historial_productos (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('COMPRA', 'VENTA', 'AJUSTE', 'FAMILIAR') NOT NULL,
    cantidad DECIMAL(10,3) NOT NULL,
    existencia_actual DECIMAL(10,3) NOT NULL,
    id_usuario INT NOT NULL,
    notas TEXT,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    INDEX (id_producto, fecha_movimiento),
    INDEX (tipo_movimiento)
) ENGINE=InnoDB; 


-- Tablas para el manejo de uso familiar

-- 10. Tabla para el retiro de efectivo
CREATE TABLE retiros_efectivo (
    id_retiro INT AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(10,2) NOT NULL,
    motivo VARCHAR(200) NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

-- 11. Tabla para la toma de producto
CREATE TABLE retiros_familiares (
    id_retiro INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL ,
    motivo VARCHAR(50),
    total_costo DECIMAL(10,2) UNSIGNED NOT NULL,
    notas TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    INDEX (fecha)
) ENGINE=InnoDB;

-- 12. Tabla de los detalles de productos tomados
CREATE TABLE detalle_retiro_familiar (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_retiro INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(10,3) UNSIGNED NOT NULL,
    costo_unitario DECIMAL(10,2) UNSIGNED NOT NULL,
    total_linea DECIMAL(10,2) UNSIGNED NOT NULL,
    FOREIGN KEY (id_retiro) REFERENCES retiros_familiares(id_retiro) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    INDEX (id_retiro)
) ENGINE=InnoDB;
