USE sombrilla_verde;

DELIMITER //

-- Trigger para registrar entradas por COMPRAS (correcto)
CREATE TRIGGER after_detalle_compra_insert
AFTER INSERT ON detalle_compra
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_id_usuario INT;
    
    -- Obtener existencia actual del producto
    SELECT existencia INTO v_existencia_actual
    FROM productos
    WHERE id_producto = NEW.id_producto;
    
    -- Obtener usuario que registró la compra
    SELECT id_usuario_registro INTO v_id_usuario
    FROM compras_proveedor
    WHERE id_compra = NEW.id_compra;
    
    -- Registrar en historial
    INSERT INTO historial_productos (
        id_producto,
        tipo_movimiento,
        cantidad,
        existencia_actual,
        id_usuario,
        notas
    ) VALUES (
        NEW.id_producto,
        'COMPRA',
        NEW.cantidad,
        v_existencia_actual + NEW.cantidad,
        v_id_usuario,
        CONCAT('Compra a proveedor ID: ', NEW.id_compra)
    );
    
    -- Actualizar existencia del producto
    UPDATE productos
    SET existencia = existencia + NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END//

-- Trigger para registrar salidas por VENTAS (correcto)
CREATE TRIGGER after_detalle_venta_insert
AFTER INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_id_usuario INT;
    
    -- Obtener existencia actual del producto
    SELECT existencia INTO v_existencia_actual
    FROM productos
    WHERE id_producto = NEW.id_producto;
    
    -- Obtener vendedor (usuario) de la venta
    SELECT id_vendedor INTO v_id_usuario
    FROM ventas
    WHERE id_venta = NEW.id_venta;
    
    -- Registrar en historial
    INSERT INTO historial_productos (
        id_producto,
        tipo_movimiento,
        cantidad,
        existencia_actual,
        id_usuario,
        notas
    ) VALUES (
        NEW.id_producto,
        'VENTA',
        NEW.cantidad,
        v_existencia_actual - NEW.cantidad,
        v_id_usuario,
        CONCAT('Venta ID: ', NEW.id_venta)
    );
    
    -- Actualizar existencia del producto
    UPDATE productos
    SET existencia = existencia - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END//

-- Trigger para registrar salidas por RETIROS FAMILIARES (corregido)
CREATE TRIGGER after_detalle_retiro_familiar_insert
AFTER INSERT ON detalle_retiro_familiar
FOR EACH ROW
BEGIN
    DECLARE v_existencia_actual DECIMAL(10,3);
    DECLARE v_motivo VARCHAR(200);
    
    -- Obtener existencia actual del producto
    SELECT existencia INTO v_existencia_actual
    FROM productos
    WHERE id_producto = NEW.id_producto;
    
    -- Obtener motivo del retiro
    SELECT motivo INTO v_motivo
    FROM retiros_familiares
    WHERE id_retiro = NEW.id_retiro;
    
    -- Registrar en historial
    INSERT INTO historial_productos (
        id_producto,
        tipo_movimiento,
        cantidad,
        existencia_actual,
        id_usuario,
        notas
    ) VALUES (
        NEW.id_producto,
        'FAMILIAR',
        NEW.cantidad,
        v_existencia_actual - NEW.cantidad,
        (SELECT id_usuario FROM retiros_familiares WHERE id_retiro = NEW.id_retiro),
        CONCAT('Retiro familiar ID: ', NEW.id_retiro, ' - Motivo: ', v_motivo)
    );
    
    -- Actualizar existencia del producto
    UPDATE productos
    SET existencia = existencia - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END//

-- Trigger para AJUSTES MANUALES (corregido)
CREATE TRIGGER after_productos_update
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    DECLARE v_diferencia DECIMAL(10,3);
    
    IF OLD.existencia != NEW.existencia THEN
        SET v_diferencia = NEW.existencia - OLD.existencia;
        
        -- Usar el usuario de sistema (1) si no hay contexto de usuario
        SET @user_id = IFNULL(@current_user_id, 1);
        
        INSERT INTO historial_productos (
            id_producto,
            tipo_movimiento,
            cantidad,
            existencia_actual,
            id_usuario,
            notas
        ) VALUES (
            NEW.id_producto,
            'AJUSTE',
            v_diferencia,
            NEW.existencia,
            @user_id,
            'Ajuste manual de inventario'
        );
    END IF;
END//

DELIMITER ;