package dao;

import model.Libro;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    void addLibro(Libro libro) throws SQLException;

    Optional<Libro> getLibroByIsbn(String isbn);

    List<Libro> getAllLibros();

    List<Libro> searchLibrosByTitulo(String titulo);

    List<Libro> searchLibrosByAutor(String autor);

    void updateLibro(Libro libro);

    void deleteLibro(String isbn);

    boolean exists(String isbn);
}
