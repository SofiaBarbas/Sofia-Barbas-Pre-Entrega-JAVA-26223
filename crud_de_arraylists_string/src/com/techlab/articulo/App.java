package com.techlab.articulo;

import java.util.ArrayList;
import java.util.Scanner;

import com.techlab.articulo.*;
import com.techlab.categoria.*;
import com.techlab.repositorio.*;


//puse una interfaz

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RepositorioInterfaz repositorio = new RepositorioArticulos();

        ArrayList<Categoria> categorias = new ArrayList<>();
        precargarCategorias(categorias);

        int opcion;

        do {
            System.out.println("\n======================================================");
            System.out.println(" SISTEMA DE ARTÍCULOS");
            System.out.println("======================================================");
            System.out.println("1 - Ingresar artículo");
            System.out.println("2 - Listar artículos");
            System.out.println("3 - Consultar un artículo");
            System.out.println("4 - Modificar un artículo");
            System.out.println("5 - Eliminar un artículo");
            System.out.println("6 - Listar categorías");
            System.out.println("0 - Salir");
            System.out.println("======================================================");

            opcion = leerEntero(scanner, "Ingrese una opción: ");

            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, repositorio, categorias);
                    break;
                case 2:
                    listarArticulos(repositorio);
                    break;
                case 3:
                    consultarArticulo(scanner, repositorio);
                    break;
                case 4:
                    modificarArticulo(scanner, repositorio, categorias);
                    break;
                case 5:
                    eliminarArticulo(scanner, repositorio);
                    break;
                case 6:
                    listarCategorias(categorias);
                    break;
                case 0:
                    System.out.println("\nSaliendo del sistema...\n¡Hasta luego!");
                    break;
                default:
                    System.out.println("\nError: la opción ingresada no es válida");
            }

        } while (opcion != 0);

        scanner.close();
    }

    //categorías precargadas para no tener 1 solo CRUD
    public static void precargarCategorias(ArrayList<Categoria> categorias) {
        categorias.add(new Categoria(1, "Electrónica", "Productos tecnológicos y electrónicos"));
        categorias.add(new Categoria(2, "Periféricos", "Accesorios para computadora"));
        categorias.add(new Categoria(3, "Alimentos", "Productos alimenticios"));
        categorias.add(new Categoria(4, "Limpieza", "Artículos de limpieza del hogar"));
    }


    /*
     * MÉTODO: ingresarArticulo
     */
    public static void ingresarArticulo(Scanner scanner, RepositorioInterfaz repositorio, ArrayList<Categoria> categorias) {
        System.out.println("\n--- INGRESAR ARTÍCULO ---");
        System.out.println("1 - Artículo electrónico");
        System.out.println("2 - Artículo alimenticio");

        int tipo;
        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de artículo: ");

            if (tipo != 1 && tipo != 2) {
                System.out.println("Error: debe elegir 1 o 2");
            }

        } while (tipo != 1 && tipo != 2);

        int codigo = leerEntero(scanner, "Ingrese el código del artículo: ");

        if (repositorio.buscarPorCodigo(codigo) != null) {
            System.out.println("Error: ya existe un artículo con ese código");
            return;
        }

        String nombre;
        boolean nombreDuplicado;
        do {
            nombreDuplicado = false;
            nombre = leerTextoNoVacio(scanner, "Ingrese el nombre del artículo: ");

            for (Articulo otroArticulo : repositorio.listarTodos()) {
                if (otroArticulo.getNombre().equalsIgnoreCase(nombre)) {
                    System.out.println("Error: Ya existe un artículo registrado con el nombre " + otroArticulo.getNombre());
                    nombreDuplicado = true;
                    break;
                }
            }
        } while (nombreDuplicado);        
        
        double precio = leerDoubleNoNegativo(scanner, "Ingrese el precio del artículo: ");

        listarCategorias(categorias);
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);


        Articulo articulo;

        if (tipo == 1) {
            int garantiaMeses = leerEnteroNoNegativo(scanner, "Ingrese la garantía en meses: ");

            // objeto de la clase hija ArticuloElectronico.
            articulo = new ArticuloElectronico(codigo, nombre, precio, categoria, garantiaMeses);
        } else {
            int diasParaVencimiento = leerEnteroNoNegativo(scanner, "Ingrese los días para vencimiento: ");

            // objeto de la clase hija ArticuloAlimenticio.
            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoria, diasParaVencimiento);
        }

        repositorio.guardar(articulo);

        System.out.println("Artículo ingresado correctamente");
        System.out.println("Objeto creado:");
        System.out.println(articulo);
    }


    /*
     * MÉTODO: listarArticulos
     */
    public static void listarArticulos(RepositorioInterfaz repositorio) {
        System.out.println("\n--- LISTADO DE ARTÍCULOS ---");

        ArrayList<Articulo> listaArticulos = repositorio.listarTodos();

        if (listaArticulos.isEmpty()) {
            System.out.println("No hay artículos cargados");
            return;
        }

        for (Articulo articulo : listaArticulos) {
            System.out.println(articulo);
            
            if (articulo instanceof ArticuloElectronico) {
                // lo castee para obtener el método específico
                ArticuloElectronico electronico = (ArticuloElectronico) articulo;
                System.out.println("Tel. Reclamos: " + electronico.nroTelMesaDeAyudaParaReclamos());
            }

            System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
            System.out.println("--------------------------------------------");
        }
    }


    /*
     * MÉTODO: consultarArticulo
     */
    public static void consultarArticulo(Scanner scanner, RepositorioInterfaz repositorio) {
        System.out.println("\n--- CONSULTAR ARTÍCULO ---");

        if (repositorio.listarTodos().isEmpty()) {
            System.out.println("No hay artículos cargados");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a consultar: ");

        Articulo articulo = repositorio.buscarPorCodigo(codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe");
            return;
        }

        System.out.println("Artículo encontrado:");
        System.out.println(articulo);
        System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
    }


    /*
     * MÉTODO: modificarArticulo
     */
    public static void modificarArticulo(Scanner scanner, RepositorioInterfaz repositorio, ArrayList<Categoria> categorias) {
        System.out.println("\n--- MODIFICAR ARTÍCULO ---");

        if (repositorio.listarTodos().isEmpty()) {
            System.out.println("No hay artículos cargados");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a modificar: ");

        Articulo articulo = repositorio.buscarPorCodigo(codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe");
            return;
        }

        String nuevoNombre;
        boolean nombreDuplicado;
        do {
            nombreDuplicado = false;
            nuevoNombre = leerTextoNoVacio(scanner, "Ingrese el nuevo nombre del artículo: ");

            for (Articulo otroArticulo : repositorio.listarTodos()) {
                if (otroArticulo.getNombre().equalsIgnoreCase(nuevoNombre) && otroArticulo.getCodigo() != articulo.getCodigo()) {
                    System.out.println("Error: Ya existe otro artículo registrado como " + otroArticulo.getNombre());
                    nombreDuplicado = true;
                    break;
                }
            }
        } while (nombreDuplicado);

        double nuevoPrecio = leerDoubleNoNegativo(scanner, "Ingrese el nuevo precio del artículo: ");

        listarCategorias(categorias);
        Categoria nuevaCategoria = pedirCategoriaExistente(scanner, categorias);

        articulo.setNombre(nuevoNombre);
        articulo.setPrecio(nuevoPrecio);
        articulo.setCategoria(nuevaCategoria);

        // si el artículo real es electrónico, se puede modificar la garantía
        if (articulo instanceof ArticuloElectronico) {
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;

            int nuevaGarantia = leerEnteroNoNegativo(scanner, "Ingrese la nueva garantía en meses: ");
            electronico.setGarantiaMeses(nuevaGarantia);
        }

        // si el artículo real es alimenticio, se pueden modificar los días para vencimiento
        if (articulo instanceof ArticuloAlimenticio) {
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;

            int nuevosDias = leerEnteroNoNegativo(scanner, "Ingrese los nuevos días para vencimiento: ");
            alimenticio.setDiasParaVencimiento(nuevosDias);
        }

        System.out.println("Artículo modificado correctamente");
    }


    /*
     * MÉTODO: eliminarArticulo
     */
    public static void eliminarArticulo(Scanner scanner, RepositorioInterfaz repositorio) {
        System.out.println("\n--- ELIMINAR ARTÍCULO ---");

        if (repositorio.listarTodos().isEmpty()) {
            System.out.println("No hay artículos cargados");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a eliminar: ");

        Articulo articulo = repositorio.buscarPorCodigo(codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe");
            return;
        }

        repositorio.eliminar(articulo);
        System.out.println("Artículo eliminado correctamente");
    }


    /*
     * MÉTODO: listarCategorias
     */
    public static void listarCategorias(ArrayList<Categoria> categorias) {
        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");

        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
    }


    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias) {
        while (true) {
            int codigoCategoria = leerEntero(scanner, "Ingrese el código de la categoría: ");

            Categoria categoria = buscarCategoriaPorCodigo(categorias, codigoCategoria);

            if (categoria != null) {
                return categoria;
            }

            System.out.println("Error: la categoría no existe");
        }
    }


    /*public static Articulo buscarArticuloPorCodigo(RepositorioInterfaz repositorio, int codigo) {
        for (Articulo articulo : repositorio.listarTodos()) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }*/


    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }


    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido");
            }
        }
    }

 
    public static int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            int valor = leerEntero(scanner, mensaje);

            if (valor < 0) {
                System.out.println("Error: el valor no puede ser negativo");
                continue;
            }

            return valor;
        }
    }

    
    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(scanner.nextLine());

                if (valor < 0) {
                    System.out.println("Error: el precio no puede ser negativo");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número decimal válido");
            }
        }
    }

    
    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            if (!texto.trim().isEmpty()) {
                return texto.trim();
            }

            System.out.println("Error: el texto no puede estar vacío");
        }
    }
}
