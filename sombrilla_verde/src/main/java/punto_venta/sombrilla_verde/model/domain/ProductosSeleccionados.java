package punto_venta.sombrilla_verde.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosSeleccionados {

    private List<ProductosInter> productos = new ArrayList<>();

    // Agregar producto (si ya existe, suma 1 a la cantidad)
    public void add(ProductoEntity producto) {
        Optional<ProductosInter> existente = productos.stream()
                .filter(p -> p.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            // Incrementa la cantidad si el producto ya está en el carrito
            ProductosInter ps = existente.get();
            ps.setCantidad(ps.getCantidad().add(BigDecimal.ONE));
        } else {
            // Añade el producto con cantidad = 1 si no existe
            productos.add(new ProductosInter(producto, BigDecimal.ONE));
        }
    }

    // Eliminar producto (si cantidad > 1, resta 1; si es 1, lo elimina)
    public void remove(ProductoEntity producto) {
        Optional<ProductosInter> existente = productos.stream()
                .filter(p -> p.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            ProductosInter ps = existente.get();
            if (ps.getCantidad().compareTo(BigDecimal.ONE) > 0) {
                // Resta 1 si la cantidad es mayor que 1
                ps.setCantidad(ps.getCantidad().subtract(BigDecimal.ONE));
            } else {
                // Elimina el producto si la cantidad es 1
                productos.remove(ps);
            }
        }
    }

    public Integer getCantidadProductos() {
        BigDecimal cantidad = BigDecimal.ZERO;
        for (ProductosInter producto : productos) {
            cantidad = cantidad.add(producto.getCantidad());
        }
        return cantidad.intValue();
    }

    public BigDecimal getPrecioTotal() {
        if (productos == null || productos.isEmpty()) {
            return BigDecimal.ZERO; // Retorna 0 si no hay productos.
        }

        BigDecimal precioTotal = BigDecimal.ZERO;
        for (ProductosInter producto : productos) {
            if (producto != null
                    && producto.getProducto() != null
                    && producto.getProducto().getPrecioVenta() != null
                    && producto.getCantidad() != null) {
                // Calcula subtotal (precio * cantidad) y lo suma al total.
                BigDecimal subtotal = producto.getProducto().getPrecioVenta().multiply(producto.getCantidad());
                precioTotal = precioTotal.add(subtotal); // ¡Asigna el resultado!
            }
        }
        return precioTotal;
    }

    public BigDecimal getCostoTotal() {
        if (productos == null || productos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalCompra = BigDecimal.ZERO;
        for (ProductosInter pi : productos) {
            if (pi != null
                    && pi.getProducto() != null
                    && pi.getProducto().getPrecioCompra() != null
                    && pi.getCantidad() != null) {
                BigDecimal subtotal = pi.getProducto().getPrecioCompra().multiply(pi.getCantidad());
                totalCompra = totalCompra.add(subtotal);
            }
        }
        return totalCompra;
    }

    public BigDecimal getCantidad(ProductoEntity prod) {
        return productos.stream()
                .filter(p -> p.getProducto().getId().equals(prod.getId()))
                .map(ProductosInter::getCantidad)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    // Limpiar carrito
    public void clear() {
        productos.clear();
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    public class ProductosInter{
        private ProductoEntity producto;
        private BigDecimal cantidad;
    }
}
