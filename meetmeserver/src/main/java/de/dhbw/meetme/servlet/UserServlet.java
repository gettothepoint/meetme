package de.dhbw.meetme.servlet;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.database.dao.UserDao;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

  @Inject UserClassicDao userClassicDao;
  @Inject UserDao userDao;
  @Inject Transaction transaction;


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    log.debug("UserServlet get");

    transaction.begin();
    Collection<User> users = userClassicDao.list();

    User user = new User();
    user.setName(request.getParameter("username"));
    user.setFirstname(request.getParameter("name"));
    user.setLastname(request.getParameter ("lastname"));
    user.setEmail(request.getParameter("e-mail"));
    user.setPassword(request.getParameter("password"));
    //user.chooseTeam();
    user.setTeam(request.getParameter("teams"));

    user.setLatitude("");
    user.setLongitude("");

    userClassicDao.persist(user);
    transaction.commit();

    users = new ArrayList<>(users); // cloning the read-only list so that we can add something
    users.add(user);


    response.setContentType("text/html");
    response.setBufferSize(8192);
    try (PrintWriter out = response.getWriter()) {
      out.println("<html lang=\"en\"><head><title>Servlet Hello</title></head>");

      // then write the data of the response
      out.println("<body  bgcolor=\"#ffffff\">"
          + "<h2>Known users:</h2>");

      for(User u: users) {
        out.println(u + "<br/>");
      }

      String username = request.getParameter("username");
      if (username != null && username.length() > 0) {
        RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher("/response");

        if (dispatcher != null) {
          dispatcher.include(request, response);
        }
      }
      out.println("</body></html>");
    }
  }

}
