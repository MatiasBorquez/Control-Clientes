/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import dominio.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Boros
 */
public interface IClienteDaoJDBC {
    public List<Cliente> listar() throws SQLException;
    public Cliente encontrar(Cliente cliente) throws SQLException;
    
    public int insertar(Cliente cliente) throws SQLException;
    
    public int actualizar(Cliente cliente) throws SQLException;
    
    public int eliminar(Cliente cliente) throws SQLException;
}
