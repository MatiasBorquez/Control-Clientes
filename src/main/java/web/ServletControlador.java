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
        String accion = request.getParameter("accion");
        if (accion != null){
            switch (accion) {
                case "editar":
                {
                    try {
                        this.editarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
                    break;
                case "eliminar":
                {
                    try {
                        this.eliminarCliente(request,response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
                    break;

                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
        
    }
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            List<Cliente> clientes = new ClienteDaoJDBC().listar();
            HttpSession sesion = request.getSession();
            sesion.setAttribute("clientes", clientes);
            sesion.setAttribute("totalClientes", clientes.size());
            sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
            //el url no cambia
            //request.getRequestDispatcher("clientes.jsp").forward(request, response);
            response.sendRedirect("clientes.jsp");
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
    
    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
        //recuperar el id cliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String accion = request.getParameter("accion");
        if (accion != null){
            switch (accion) {
                case "insertar":
                    try {
                        this.insertarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                    break;
                case "modificar":
                {
                    try {
                        this.modificarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);

                    }
                }
                break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
        
    }
    
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
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
        System.out.println(registrosModificados);
        
        //registramos los nuevos clientes
        this.accionDefault(request, response);
    }
    
    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
        //recuperamos los valores del formulario editarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo  =0;
        String saldoString = request.getParameter("saldo");
        if(saldoString != null && !"".equals(saldoString)){
            saldo = Double.parseDouble(saldoString);
        }
        
        //modificamos el objeto de cliente (modelo)
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        
        //modificamos en la base de datos
        int registrosModificados = new ClienteDaoJDBC().actualizar(cliente);
        System.out.println(registrosModificados);
        
        //registramos la modificacion
        this.accionDefault(request, response);
    }
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
        //recuperamos los valores del formulario eliminarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        //Creamos el objeto de cliente (modelo)
        Cliente cliente = new Cliente(idCliente);
        
        //Eiminamos el objeto en la base de datos
        int registrosModificados = new ClienteDaoJDBC().eliminar(cliente);
        System.out.println(registrosModificados);
        
        //registramos los nuevos clientes
        this.accionDefault(request, response);
    }
    
}
