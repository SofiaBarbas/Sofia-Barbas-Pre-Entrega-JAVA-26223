package com.techlab.articulo;
import com.techlab.categoria.Categoria;

/*
 * CLASE ABSTRACTA ARTICULO
 * --------------------------------------------------
 * Esta clase contiene:
 * - lo común a todos los artículos
 * - atributos compartidos
 * - getters y setters compartidos
 * - lógica común como toString()
 */

public abstract class Articulo extends Object {

    private int codigo;
    private String nombre;
    private double precio;
    private Categoria categoria;

    //Constructor de la clase padre
    public Articulo(int codigo, String nombre, double precio, Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /* Método abstracto
     * Obliga a cada clase hija o subclase a decir qué tipo de artículo es
     */
    public abstract String getTipoArticulo();

    /* Método abstracto.
     * Obliga a cada subclase a devolver su detalle específico
     * Esto sirve para no meter toda la lógica en la clase padre
     */
    public abstract String getDetalleEspecifico();

    /* toString()
     * Este método es para mostrar un objeto de manera legible
     */
    @Override
    public String toString() {
        return "Artículo {" +
                "código=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoría='" + categoria.getNombre() + '\'' +
                ", tipo='" + this.getTipoArticulo() + '\'' +
                ", detalle='" + this.getDetalleEspecifico() + '\'' +
                '}';
    }
}
