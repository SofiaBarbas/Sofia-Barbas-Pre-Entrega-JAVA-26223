package com.techlab.repositorio;

import java.util.ArrayList;
import com.techlab.articulo.Articulo;

public class RepositorioArticulos implements RepositorioInterfaz {
    
    // ahora el arraylist está acá
    private ArrayList<Articulo> articulos;

    public RepositorioArticulos() {
        this.articulos = new ArrayList<>();
    }

    @Override
    public void guardar(Articulo articulo) {
        articulos.add(articulo);
    }

    @Override
    public ArrayList<Articulo> listarTodos() {
        return articulos;
    }

    @Override
    public Articulo buscarPorCodigo(int codigo) {
        for (Articulo articulo : articulos) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }

    @Override
    public void eliminar(Articulo articulo) {
        articulos.remove(articulo);
    }
}