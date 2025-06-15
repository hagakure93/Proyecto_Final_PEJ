package dao.impl;

import dao.BookDAO;
import model.Libro;
import util.ConexionJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCBookDAO implements BookDAO {

    private static final String NOMBRE_DB = "biblioteca";


    private Libro extractLibroFromResultSet(ResultSet rs) throws SQLException {

        Long id = rs.getLong("id");
        String isbn = rs.getString("isbn");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        String categoria = rs.getString("categoria");
        return new Libro(id, isbn, titulo, autor, categoria);
    }

    @Override
    public void addLibro(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (isbn, titulo, autor, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionJDBC.conectar(NOMBRE_DB);

             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getCategoria());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setId(generatedKeys.getLong(1)); // Asigna el ID al objeto Libro
                    }
                }
                System.out.println("JDBC: Libro '" + libro.getTitulo() + "' añadido con ID: " + libro.getId());
            } else {
                System.out.println("JDBC: No se pudo añadir el libro '" + libro.getTitulo() + "'.");
            }
        } catch (SQLException e) {
            System.err.println("Error JDBC al añadir libro: " + e.getMessage());

        }


    }

    @Override
    public Optional<Libro> getLibroByIsbn(String isbn) {
        String sql = "SELECT id, isbn, titulo, autor, categoria FROM libros WHERE isbn = ?";

        try (Connection conn = ConexionJDBC.conectar(NOMBRE_DB); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractLibroFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error JDBC al obtener libro por ISBN: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Libro> getAllLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, isbn, titulo, autor, categoria FROM libros";

        try (Connection conn = ConexionJDBC.conectar(NOMBRE_DB); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                libros.add(extractLibroFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error JDBC al obtener todos los libros: " + e.getMessage());

        }
        return libros;

    }

    @Override
    public List<Libro> searchLibrosByTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, isbn, titulo, autor, categoria FROM libros WHERE titulo LIKE ?";

        try (Connection conn = ConexionJDBC.conectar(NOMBRE_DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titulo + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(extractLibroFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error JDBC al buscar libros por título: " + e.getMessage());
        }
        return libros;
    }

    @Override
    public List<Libro> searchLibrosByAutor(String autor) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, isbn, titulo, autor, categoria FROM libros WHERE autor LIKE ?";

        try (Connection conn = ConexionJDBC.conectar(NOMBRE_DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Dejo % por si no te sabes los apellidos
            stmt.setString(1, "%" + autor + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(extractLibroFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error JDBC al buscar libros por autor: " + e.getMessage());
        }
        return libros;
    }

    @Override
    public void updateLibro(Libro libro) {

    }

    @Override
    public void deleteLibro(String isbn) {

    }

    @Override
    public boolean exists(String isbn) {
        return false;
    }
}
