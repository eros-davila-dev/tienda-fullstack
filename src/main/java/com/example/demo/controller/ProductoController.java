package com.example.demo.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173") // "*" permite acceso desde cualquier lado (útil para pruebas)
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ESTO ES LO QUE TE FALTABA:

    // 1. Método para leer (GET)
    @GetMapping
    public List<Producto> listar() {
        return productoService.ListarProductos();
    }

    // 2. Método para guardar (POST)
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.GuardarProducto(producto);
    }

    // 3. Actualizar un producto existente (PUT)
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        // Primero buscamos si el producto existe
        Producto productoExistente = productoService.obtenerPorId(id);

        if (productoExistente != null) {
            // Si existe, actualizamos sus datos con los nuevos
            productoExistente.setNombre(productoDetalles.getNombre());
            productoExistente.setPrecio(productoDetalles.getPrecio());
            productoExistente.setDescripcion(productoDetalles.getDescripcion());

            // Guardamos los cambios
            return productoService.GuardarProducto(productoExistente);
        }

        return null; // O podrías devolver un error 404, pero por ahora null está bien
    }

    // 4. Eliminar un producto (DELETE)
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "Producto eliminado correctamente";
    }
}