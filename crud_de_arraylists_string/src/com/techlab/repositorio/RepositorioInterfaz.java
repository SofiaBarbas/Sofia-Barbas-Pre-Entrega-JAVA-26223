package com.techlab.repositorio;

import java.util.ArrayList;
import com.techlab.articulo.Articulo;

public interface RepositorioInterfaz {
    void guardar(Articulo articulo);
    ArrayList<Articulo> listarTodos();
    Articulo buscarPorCodigo(int codigo);
    void eliminar(Articulo articulo);
}