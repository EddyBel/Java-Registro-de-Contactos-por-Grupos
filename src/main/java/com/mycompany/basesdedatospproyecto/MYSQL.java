package com.mycompany.basesdedatospproyecto;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clase que gestiona las operaciones de conexión, inserción, consulta,
 * actualización y eliminación de registros en la base de datos MySQL para el
 * sistema de registro de usuarios. Los métodos aquí permiten manipular la tabla
 * 'contactos' y 'grupos' en la base de datos 'contactosdb'.
 */
public class MYSQL {

    // CONSTANTES DE LA CONEXIÓN A MYSQL
    // URL de conexión, usuario y contraseña de la base de datos
    // 1. jdbc:mysql: Indica que se utilizará el protocolo JDBC (Java Database Connectivity) para conectarse a una base de datos MySQL.
    // 2. localhost: El nombre del host o la dirección del servidor donde está ejecutándose la base de datos MySQL. "localhost" significa que la base de datos está en la misma máquina que el programa.
    // 3. 3306: Es el puerto por defecto en el que MySQL escucha las conexiones entrantes. Si se utiliza un puerto diferente, este valor debe ser modificado.
    // 4. contactosdb: Es el nombre de la base de datos a la que se desea conectar. En este caso, la base de datos llamada "contactosdb" es la que contiene la tabla "contactos" y "grupos".
    private static final String URL = "jdbc:mysql://localhost:3306/contactosdb";
    private static final String USER = "root"; // Usuario de la base de datos
    private static final String PASSWORD = "54628"; // Contraseña de la base de datos

    /**
     * Establece la conexión con la base de datos MySQL.
     *
     * @return La conexión a la base de datos o null si ocurre un error.
     */
    public Connection conectar() {
        Connection conexion = null;
        try {
            // Cargamos el driver de MySQL, necesario para establecer la conexión.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecemos la conexión usando los parámetros definidos anteriormente.
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver no encontrado: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     *
     * @param conexion La conexión que se desea cerrar.
     */
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Inserta un nuevo registro en la tabla `contactos` de la base de datos con
     * los valores proporcionados.
     *
     * Este método establece una conexión con la base de datos y utiliza una
     * sentencia SQL preparada para insertar los valores de nombre, apellido
     * paterno, apellido materno, teléfono e ID de grupo en la tabla
     * `contactos`. La conexión a la base de datos es cerrada automáticamente
     * después de la ejecución, independientemente de si la operación fue
     * exitosa o si ocurrió un error.
     *
     * El uso de un `PreparedStatement` previene posibles inyecciones SQL,
     * asegurando que los datos sean procesados de manera segura.
     *
     * @param nombre El nombre del contacto a insertar. No puede ser
     * {@code null}.
     * @param apellidoPaterno El apellido paterno del contacto. No puede ser
     * {@code null}.
     * @param apellidoMaterno El apellido materno del contacto. No puede ser
     * {@code null}.
     * @param telefono El número de teléfono del contacto. No puede ser
     * {@code null}.
     * @param IDGrupo El ID del grupo al que pertenece el contacto. Este valor
     * debe ser un número entero.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la sentencia SQL.
     */
    public void insertarValores(String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, int IDGrupo) throws SQLException {
        Connection conexion = conectar();

        if (conexion != null) {
            String sql = "INSERT INTO contactos (nombre, telefono, idGrupo, apellidoPaterno, apellidoMaterno) VALUES(?, ?, ?, ?, ?)";

            // Usamos un PreparedStatement para prevenir inyecciones SQL.
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos los valores a insertar
                statement.setString(1, nombre);
                statement.setString(2, telefono);
                statement.setInt(3, IDGrupo);
                statement.setString(4, apellidoPaterno);
                statement.setString(5, apellidoMaterno);

                // Ejecutamos la inserción
                int filasInsertadas = statement.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Se insertó correctamente el registro en la tabla personas.");
                }

            } catch (SQLException e) {
                System.out.println("Error al insertar registro: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
    }

    /**
     * Actualiza los detalles de un contacto existente en la tabla `contactos`
     * de la base de datos.
     *
     * Este método establece una conexión con la base de datos y utiliza una
     * sentencia SQL preparada para actualizar los valores de nombre, apellido
     * paterno, apellido materno, teléfono e ID de grupo de un contacto
     * específico, identificado por su ID en la tabla `contactos`.
     *
     * Si no se encuentra un contacto con el ID proporcionado, el método muestra
     * un mensaje indicando que no se actualizó ningún registro. La conexión a
     * la base de datos se cierra automáticamente después de la ejecución,
     * independientemente de si la operación fue exitosa o si ocurrió un error.
     *
     * @param idContacto El ID del contacto a actualizar. Este valor debe ser un
     * número entero que corresponde al registro del contacto en la base de
     * datos.
     * @param nombre El nuevo nombre del contacto. No puede ser {@code null}.
     * @param apellidoPaterno El nuevo apellido paterno del contacto. No puede
     * ser {@code null}.
     * @param apellidoMaterno El nuevo apellido materno del contacto. No puede
     * ser {@code null}.
     * @param telefono El nuevo número de teléfono del contacto. No puede ser
     * {@code null}.
     * @param idGrupo El nuevo ID del grupo al que pertenece el contacto. Este
     * valor debe ser un número entero.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la sentencia SQL.
     */
    public void actualizarContacto(int idContacto, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, int idGrupo) {
        Connection conexion = conectar();

        if (conexion != null) {
            String sql = "UPDATE contactos SET nombre = ?, telefono = ?, idGrupo = ?, apellidoPaterno = ?, apellidoMaterno = ? WHERE idContacto = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos los valores para la actualización
                statement.setString(1, nombre);
                statement.setString(2, telefono);
                statement.setInt(3, idGrupo);
                statement.setString(4, apellidoPaterno);
                statement.setString(5, apellidoMaterno);
                statement.setInt(6, idContacto);

                // Ejecutamos la actualización
                int filasActualizadas = statement.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("El registro fue actualizado correctamente.");
                } else {
                    System.out.println("No se encontró ningún registro con el id proporcionado.");
                }
            } catch (SQLException e) {
                System.out.println("Error al actualizar el registro: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
    }

    /**
     * Elimina un contacto de la tabla `contactos` en la base de datos
     * utilizando su ID.
     *
     * Este método establece una conexión con la base de datos y utiliza una
     * sentencia SQL preparada para eliminar un contacto específico de la tabla
     * `contactos`, identificado por su ID. Si no se encuentra un contacto con
     * el ID proporcionado, el método muestra un mensaje indicando que no se
     * eliminó ningún registro. La conexión a la base de datos se cierra
     * automáticamente después de la ejecución, independientemente de si la
     * operación fue exitosa o si ocurrió un error.
     *
     * @param idContacto El ID del contacto a eliminar. Este valor debe ser un
     * número entero que corresponde al registro del contacto en la base de
     * datos.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la sentencia SQL.
     */
    public void eliminarContacto(int idContacto) {
        Connection conexion = conectar();

        if (conexion != null) {
            String sql = "DELETE FROM contactos WHERE idContacto = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el valor del idContacto a eliminar
                statement.setInt(1, idContacto);

                // Ejecutamos la eliminación
                int filasEliminadas = statement.executeUpdate();
                if (filasEliminadas > 0) {
                    System.out.println("El contacto fue eliminado correctamente.");
                } else {
                    System.out.println("No se encontró ningún contacto con el id proporcionado.");
                }
            } catch (SQLException e) {
                System.out.println("Error al eliminar el contacto: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
    }

    /**
     * Obtiene una lista de contactos cuya coincidencia parcial en el nombre
     * coincide con el valor proporcionado.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL utilizando el operador `LIKE` para encontrar contactos cuyo
     * nombre contenga la cadena proporcionada, permitiendo coincidencias
     * parciales. Los resultados de la consulta, que incluyen el ID, nombre,
     * apellido paterno, apellido materno, teléfono e ID de grupo de cada
     * contacto, se almacenan en una lista de arreglos de cadenas, que luego se
     * retorna.
     *
     * @param nombre El nombre (o parte de él) del contacto a buscar. Este valor
     * se usa para realizar una búsqueda con coincidencias parciales en la base
     * de datos. No puede ser {@code null}.
     *
     * @return Una lista de arreglos de cadenas, donde cada arreglo contiene los
     * detalles de un contacto (ID, nombre, apellido paterno, apellido materno,
     * teléfono e ID de grupo). Si no se encuentran contactos, se retorna una
     * lista vacía.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la consulta SQL.
     */
    public List<String[]> obtenerContactosSimilaresPorNombre(String nombre) {
        List<String[]> contactosSimilares = new ArrayList<>();
        Connection conexion = conectar();

        if (conexion != null) {
            // Usamos el operador LIKE con % para permitir coincidencias parciales
            String sql = "SELECT * FROM contactos WHERE nombre LIKE ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el valor de nombre para la búsqueda, añadiendo '%' para permitir coincidencias parciales
                statement.setString(1, "%" + nombre + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Iteramos sobre los resultados y agregamos cada contacto a la lista
                    while (resultSet.next()) {
                        String id = String.valueOf(resultSet.getInt("idContacto"));
                        String nombreContacto = resultSet.getString("nombre");
                        String telefono = resultSet.getString("telefono");
                        String idGrupo = String.valueOf(resultSet.getInt("idGrupo"));
                        String apellidoPaterno = resultSet.getString("apellidoPaterno");
                        String apellidoMaterno = resultSet.getString("apellidoMaterno");

                        // Agregamos los datos de cada contacto a la lista
                        String[] contacto = new String[]{id, nombreContacto, apellidoPaterno, apellidoMaterno, telefono, idGrupo};
                        contactosSimilares.add(contacto);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener los contactos: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return contactosSimilares;
    }

    /**
     * Obtiene una lista de contactos que coinciden exactamente con el ID
     * proporcionado.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL para encontrar contactos cuyo ID coincida exactamente con el
     * valor proporcionado. Los resultados de la consulta, que incluyen el ID,
     * nombre, apellido paterno, apellido materno, teléfono e ID de grupo de
     * cada contacto, se almacenan en una lista de arreglos de cadenas, que
     * luego se retorna.
     *
     * @param idContacto El ID del contacto que se busca. Este valor debe ser un
     * número entero que corresponde al registro del contacto en la base de
     * datos.
     *
     * @return Una lista de arreglos de cadenas, donde cada arreglo contiene los
     * detalles de un contacto (ID, nombre, apellido paterno, apellido materno,
     * teléfono e ID de grupo). Si no se encuentran contactos con el ID
     * proporcionado, se retorna una lista vacía.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la consulta SQL.
     */
    public List<String[]> obtenerContactosSimilaresPorId(int idContacto) {
        List<String[]> contactosSimilares = new ArrayList<>();
        Connection conexion = conectar();

        if (conexion != null) {
            // Consulta para obtener contactos con idContacto similar
            String sql = "SELECT * FROM contactos WHERE idContacto = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el valor de idContacto para la búsqueda
                statement.setInt(1, idContacto);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Iteramos sobre los resultados y agregamos cada contacto a la lista
                    while (resultSet.next()) {
                        String id = String.valueOf(resultSet.getInt("idContacto"));
                        String nombre = resultSet.getString("nombre");
                        String telefono = resultSet.getString("telefono");
                        String idGrupo = String.valueOf(resultSet.getInt("idGrupo"));
                        String apellidoPaterno = resultSet.getString("apellidoPaterno");
                        String apellidoMaterno = resultSet.getString("apellidoMaterno");

                        // Agregamos los datos de cada contacto a la lista
                        String[] contacto = new String[]{id, nombre, apellidoPaterno, apellidoMaterno, telefono, idGrupo};
                        contactosSimilares.add(contacto);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener los contactos: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return contactosSimilares;
    }

    /**
     * Obtiene una lista de contactos que pertenecen al grupo con el ID
     * proporcionado.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL para encontrar los contactos que están asociados con el
     * grupo identificado por el `idGrupo` proporcionado. Los resultados de la
     * consulta, que incluyen el ID, nombre, apellido paterno, apellido materno,
     * teléfono e ID de grupo de cada contacto, se almacenan en una lista de
     * arreglos de cadenas, que luego se retorna.
     *
     * @param idGrupo El ID del grupo al que pertenecen los contactos que se
     * desean obtener. Este valor debe ser un número entero que corresponde a la
     * columna `idGrupo` en la base de datos.
     *
     * @return Una lista de arreglos de cadenas, donde cada arreglo contiene los
     * detalles de un contacto (ID, nombre, apellido paterno, apellido materno,
     * teléfono e ID de grupo). Si no se encuentran contactos con el `idGrupo`
     * proporcionado, se retorna una lista vacía.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o ejecutar la consulta SQL.
     */
    public List<String[]> obtenerContactosPorIdGrupo(int idGrupo) {
        List<String[]> contactosGrupo = new ArrayList<>();
        Connection conexion = conectar();

        if (conexion != null) {
            // Consulta para obtener contactos que pertenecen al mismo idGrupo
            String sql = "SELECT * FROM contactos WHERE idGrupo = ?";

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el valor de idGrupo para la búsqueda
                statement.setInt(1, idGrupo);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // Iteramos sobre los resultados y agregamos cada contacto a la lista
                    while (resultSet.next()) {
                        String id = String.valueOf(resultSet.getInt("idContacto"));
                        String nombre = resultSet.getString("nombre");
                        String telefono = resultSet.getString("telefono");
                        String idGrupoDb = String.valueOf(resultSet.getInt("idGrupo"));
                        String apellidoPaterno = resultSet.getString("apellidoPaterno");
                        String apellidoMaterno = resultSet.getString("apellidoMaterno");

                        // Agregamos los datos de cada contacto a la lista
                        String[] contacto = new String[]{id, nombre, apellidoPaterno, apellidoMaterno, telefono, idGrupoDb};
                        contactosGrupo.add(contacto);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener los contactos: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return contactosGrupo;
    }

    /**
     * Obtiene todos los registros de la tabla 'contactos'.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL para obtener todos los registros de la tabla 'contactos'.
     * Cada registro, que incluye los campos `idContacto`, `nombre`,
     * `apellidoPaterno`, `apellidoMaterno`, `telefono`, e `idGrupo`, se
     * almacena en un arreglo de cadenas y luego se agrega a una lista, que es
     * retornada al final.
     *
     * @return Una lista de arreglos de cadenas, donde cada arreglo contiene los
     * detalles de un contacto (ID, nombre, apellido paterno, apellido materno,
     * teléfono e ID de grupo). Si no existen registros en la tabla, se retorna
     * una lista vacía.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o al ejecutar la consulta SQL.
     */
    public List<String[]> obtenerContactos() {
        List<String[]> personas = new ArrayList<>();
        Connection conexion = conectar();
        if (conexion != null) {
            String sql = "SELECT * FROM contactos";
            try (PreparedStatement statement = conexion.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

                // Iteramos sobre los resultados y agregamos cada registro a la lista
                while (resultSet.next()) {
                    String id = String.valueOf(resultSet.getInt("idContacto"));
                    String nombre = resultSet.getString("nombre");
                    String telefono = resultSet.getString("telefono");
                    String idGrupo = String.valueOf(resultSet.getInt("idGrupo"));
                    String apellidoPaterno = resultSet.getString("apellidoPaterno");
                    String apellidoMaterno = resultSet.getString("apellidoMaterno");

                    // Agregamos los datos de cada persona a la lista
                    String[] persona = new String[]{id, nombre, apellidoPaterno, apellidoMaterno, telefono, idGrupo};
                    personas.add(persona);
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener los registros: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return personas;
    }

    /**
     * Obtiene los detalles de un contacto específico basado en su ID.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL para obtener los detalles del contacto cuya columna
     * `idContacto` coincide con el valor proporcionado. Si se encuentra un
     * contacto con el ID especificado, los datos (ID, nombre, apellido paterno,
     * apellido materno, teléfono e ID de grupo) se almacenan en un arreglo de
     * cadenas que luego se retorna. Si no se encuentra ningún contacto con ese
     * ID, se retorna `null`.
     *
     * @param idContacto El ID del contacto que se desea obtener. Este valor
     * corresponde al campo `idContacto` en la base de datos.
     *
     * @return Un arreglo de cadenas que contiene los detalles del contacto
     * solicitado (ID, nombre, apellido paterno, apellido materno, teléfono e ID
     * de grupo). Si no se encuentra ningún contacto con el `idContacto`
     * proporcionado, se retorna `null`.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o al ejecutar la consulta SQL.
     */
    public String[] obtenerContactoPorId(int idContacto) {
        String[] contacto = null;
        Connection conexion = conectar();
        if (conexion != null) {
            String sql = "SELECT * FROM contactos WHERE idContacto = ?";
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el parámetro idContacto en la consulta
                statement.setInt(1, idContacto);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String id = String.valueOf(resultSet.getInt("idContacto"));
                        String nombre = resultSet.getString("nombre");
                        String telefono = resultSet.getString("telefono");
                        String idGrupo = String.valueOf(resultSet.getInt("idGrupo"));
                        String apellidoPaterno = resultSet.getString("apellidoPaterno");
                        String apellidoMaterno = resultSet.getString("apellidoMaterno");

                        // Creamos el arreglo con los datos del contacto
                        contacto = new String[]{id, nombre, apellidoPaterno, apellidoMaterno, telefono, idGrupo};
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener el contacto: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return contacto;
    }

    /**
     * Obtiene el nombre de un grupo basado en su ID.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL para obtener el nombre del grupo cuya columna `idGrupo`
     * coincide con el valor proporcionado. Si se encuentra un grupo con el ID
     * especificado, el nombre del grupo se retorna como una cadena. Si no se
     * encuentra ningún grupo con ese ID, se retorna `null`.
     *
     * @param idGrupo El ID del grupo cuyo nombre se desea obtener. Este valor
     * corresponde al campo `idGrupo` en la base de datos.
     *
     * @return El nombre del grupo correspondiente al `idGrupo` proporcionado.
     * Si no se encuentra ningún grupo con ese ID, se retorna `null`.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o al ejecutar la consulta SQL.
     */
    public String obtenerNombreGrupo(int idGrupo) {
        String nombreGrupo = null;
        Connection conexion = conectar();
        if (conexion != null) {
            String sql = "SELECT nombreGrupo FROM grupos WHERE idGrupo = ?";
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el parámetro idGrupo en la consulta
                statement.setInt(1, idGrupo);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nombreGrupo = resultSet.getString("nombreGrupo");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener el nombre del grupo: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return nombreGrupo;
    }

    /**
     * Cuenta el número total de registros en la tabla 'contactos'.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL que cuenta todos los registros en la tabla `contactos`. El
     * resultado de la consulta, que es el número total de registros, se retorna
     * como un entero. Si ocurre un error en la consulta o la conexión, se
     * retorna 0.
     *
     * @return El número total de registros en la tabla `contactos`. Si ocurre
     * un error durante la ejecución, se retorna 0.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o al ejecutar la consulta SQL.
     */
    public int contarElementosContactos() {
        int totalElementos = 0;
        Connection conexion = conectar();
        if (conexion != null) {
            String sql = "SELECT COUNT(*) AS total FROM contactos";
            try (PreparedStatement statement = conexion.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    totalElementos = resultSet.getInt("total");
                }
            } catch (SQLException e) {
                System.out.println("Error al contar los elementos: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return totalElementos;
    }

    /**
     * Cuenta el número total de registros en la tabla 'contactos' para un grupo
     * específico.
     *
     * Este método establece una conexión con la base de datos y ejecuta una
     * consulta SQL que cuenta el número de registros en la tabla `contactos`
     * donde el campo `idGrupo` coincide con el valor proporcionado. El
     * resultado, que es el número total de registros en ese grupo, se retorna
     * como un entero. Si ocurre un error en la consulta o la conexión, se
     * retorna 0.
     *
     * @param idGrupo El ID del grupo cuyos contactos se desean contar. Este
     * valor corresponde al campo `idGrupo` en la tabla `contactos`.
     *
     * @return El número total de registros en la tabla `contactos` para el
     * grupo especificado. Si ocurre un error durante la ejecución, se retorna
     * 0.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos
     * o al ejecutar la consulta SQL.
     */
    public int contarElementosContactosPorGrupo(int idGrupo) {
        int totalElementos = 0;
        Connection conexion = conectar();
        if (conexion != null) {
            String sql = "SELECT COUNT(*) AS total FROM contactos WHERE idGrupo = ?";
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                // Establecemos el parámetro idGrupo en la consulta
                statement.setInt(1, idGrupo);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalElementos = resultSet.getInt("total");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al contar los elementos: " + e.getMessage());
            } finally {
                cerrarConexion(conexion);
            }
        }
        return totalElementos;
    }

}
