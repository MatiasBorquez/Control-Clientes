package web;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * @author Boros
 */

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        this.accionDefault(request, response);
        
    }
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            List<Cliente> clientes = new ClienteDaoJDBC().listar();
            request.setAttribute("clientes", clientes);
            request.setAttribute("totalClientes", clientes.size());
            request.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
            request.getRequestDispatcher("clientes.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    private double calcularSaldoTotal(List<Cliente> clientes){
        double saldoTotal=0;
        for(Cliente cliente: clientes){
            saldoTotal+= cliente.getSaldo();
        }
        return saldoTotal;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String accion = request.getParameter("accion");
        if (accion != null){
            switch (accion) {
                case "insertar":
                    this.insertarCliente(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
        
    }
    
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //recuperamos los valores del formulario agregarCliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo  =0;
        String saldoString = request.getParameter("saldo");
        if(saldoString != null && !"".equals(saldoString)){
            saldo = Double.parseDouble(saldoString);
        }
        
        //Creamos el objeto de cliente (modelo)
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        
        //Insertamos el nuevo objeto en la base de datos
        int registrosModificados = new ClienteDaoJDBC().insertar(cliente);
        
        //registramos los nuevos clientes
        this.accionDefault(request, response);
    }
}