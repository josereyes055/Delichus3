package geekgames.delichus2.customObjects;


public class SimpleRecipe {

    private int id;
    private String nombre;
    private String tipo;
    private String autor;
    private String fecha;
    private String puntuacion;


    public SimpleRecipe(int id, String nombre, String tipo, String autor, String fecha, String puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.autor = autor;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAutor() {
        return autor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPuntuacion() {
        return puntuacion;
    }


}
