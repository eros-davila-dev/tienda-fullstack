package com.example.demo.service;

import com.example.demo.entity.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto>ListarProductos() {
        return productoRepository.findAll();
    }

    public Producto GuardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // 3. Buscar un solo producto (Útil para editar)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // 4. Eliminar un producto
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
