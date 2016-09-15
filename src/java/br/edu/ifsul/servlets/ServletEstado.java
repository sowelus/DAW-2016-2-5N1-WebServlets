package br.edu.ifsul.servlets;

import br.edu.ifsul.dao.EstadoDAO;
import br.edu.ifsul.modelo.Estado;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jorge
 */
@WebServlet(name = "ServletEstado", urlPatterns = {"/estado/ServletEstado"})
public class ServletEstado extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // capturando o dao da sessao
        EstadoDAO dao = (EstadoDAO) request.getSession().getAttribute("estadoDao");
        if (dao == null){ // caso o dao seja nulo (primeiro acesso) deve inicializa-lo
            dao = new EstadoDAO();
        }
        String tela = ""; // atributo que ira conter a tela que será direcionada
        String acao = request.getParameter("acao"); // ação solicitada
        if (acao == null){
            tela = "listar.jsp";
        } else if (acao.equals("incluir")){
            dao.setObjetoSelecionado(new Estado());
            tela = "formulario.jsp";
        } else if (acao.equals("alterar")){
            // carregar o objeto pelo id
            Integer id = Integer.parseInt(request.getParameter("id"));
            Estado obj = dao.localizar(id);
            if (obj != null){
                dao.setObjetoSelecionado(obj);
                dao.setMensagem("");
                tela = "formulario.jsp";
            }            
        } else if (acao.equals("excluir")){
            // carregar o objeto pelo id
            Integer id = Integer.parseInt(request.getParameter("id"));
            Estado obj = dao.localizar(id);
            if (obj != null){
                dao.remover(obj);
                tela = "listar.jsp";
            }             
        } else if (acao.equals("salvar")){
            Integer id = null;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch(Exception e){
                System.out.println("Erro ao converter o id");
            }
            Estado obj = new Estado();
            obj.setId(id);
            obj.setNome(request.getParameter("nome"));
            obj.setUf(request.getParameter("uf"));
            dao.setObjetoSelecionado(obj);
            if (dao.validaObjeto(obj)){
                dao.salvar(obj);
                tela = "listar.jsp";
            } else {
                tela = "formulario.jsp";
            }                        
        } else if (acao.equals("cancelar")){
            tela = "listar.jsp";
            dao.setMensagem("");
        }
        // atualizar o dao na sessão
        request.getSession().setAttribute("estadoDao", dao);
        // redireciona para a tela
        response.sendRedirect(tela);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
