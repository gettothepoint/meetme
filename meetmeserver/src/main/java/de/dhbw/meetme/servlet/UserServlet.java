package de.dhbw.meetme.servlet;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.database.dao.UserDao;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.logic.BasicLogic;
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

    @Inject BasicLogic basicLogic;




  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      log.debug("UserServlet get");

      String output1 = "<!doctype html>\n" +
              "<html>\n" +
              "<head>\n" +
              "<meta charset=\"utf-8\">\n" +
              "<title>GetToThePoint!</title>\n" +
              "<link rel=\"stylesheet\" href=\"style/style.css\">\n" +
              "\n" +
              "<!---VALIDATION JAVASCRIPT---->\n" +
              "\n" +
              "<script>\n" +
              "function validateForm() {\n" +
              "    var x = document.forms[\"registration\"][\"name\"][\"lastname\"][\"username\"][\"e-mail\"][\"confirm_mail\"][\"password\"][\"confirm_password\"].value;\n" +
              "\tif ( [\"username\"] != element.defaultValue) {\n" +
              "\t\t\n" +
              "\t} else if ( ([\"e-mail\"] != [\"confirm_mail\"]) || ([\"password\"] != [\"confirm_password\"])  ) {\n" +
              "\t \talert(\"E-Mail Address doesn't match!\");\n" +
              "\t }\n" +
              "\telse {\t\n" +
              "\t\tif (x == null || x == \"\")  {\n" +
              "        \talert(\"All fields must be filled out.\");\n" +
              "        \treturn false;\n" +
              "\t\t\t\t}\n" +
              "\t}\n" +
              "}\n" +
              "</script>\n" +
              "\n" +
              "</head>\n" +
              "<body>\n" +
              "<header><center><img src=\"img/header.gif\"></center></header>\n" +
              "<section id=\"content\">\n";
      String output2 =  "\n" +
              " \n" +
              " <a href=\"index.html\">Kehre zurück zur Startseite </a> </p>\n" +
              "\n" +
              "\n" +
              "\n" +
              "</section>\n" +
              "\n" +
              "\n" +
              "\n" +
              "\n" +
              "<footer>\n" +
              "<div class=\"innner\">\n" +
              "<p id=\"app\">Available for <br />\n" +
              "<a href=\"http://play.google.com\" target=\"new\"><img src=\"img/google.png\" width=\"55%\"></a>\n" +
              "</p>\n" +
              "<div id=\"social_icons\"><img style=\"float:right;\"src=\"img/social_media.PNG\"></div>\n" +
              "\n" +
              "<p id=\"legal\">Copyright (C) 2015</p>\n" +
              "</div>\n" +
              "\n" +
              "</footer>\n" +
              "\n" +
              "</body>\n" +
              "\n" +
              "</html>\n";

      //prüft, ob Username unique
      if (!basicLogic.usernameUnique(request.getParameter("username"))) {

          //gibt Error Page zurück
          response.setContentType("text/html");
          response.setBufferSize(8192);
          try (PrintWriter out = response.getWriter()) {
              out.println( output1 +
                      "<h1>Entschuldigung!</h1>\n" +
                      "<p>Es ist ein Fehler bei deiner Registrierung aufgetreten. <br />\n" +
                      "Wahrscheinlich ist dein Username bereits vergeben Bitte suche dir einen anderen Namen aus und versuche es noch einmal. <br />\n" +
                     output2);
          }
      } else if (!basicLogic.checkMail(request.getParameter("e-mail"))) {

          //gibt Error Page zurück
          response.setContentType("text/html");
          response.setBufferSize(8192);
          try (PrintWriter out = response.getWriter()) {
              out.println( output1 +
                      "<h1>Entschuldigung!</h1>\n" +
                      "<p>Es ist ein Fehler bei deiner Registrierung aufgetreten. <br />\n" +
                      "Wahrscheinlich ist deine E-Mailadresse ungültig. Bitte gib deine richtige E-Mail an und versuche es noch einmal. <br />\n" +
                      output2);
          }
      } /*else if (!basicLogic.checkTeam(request.getParameter("team"))) {

          //gibt Error Page zurück
          response.setContentType("text/html");
          response.setBufferSize(8192);
          try (PrintWriter out = response.getWriter()) {
              out.println( output1 +
                      "<h1>Entschuldigung!</h1>\n" +
                      "<p>Es ist ein Fehler bei deiner Registrierung aufgetreten. <br />\n" +
                      "Womöglich wolltest du Schlingel unsere Datenbank sabotieren - wir erlauben nur die Teameingaben blue, red und random!. <br />\n" +
                      output2);
          }
      } */else {
          String username = request.getParameter("username");
          String firstname = request.getParameter("name");
          String lastname = request.getParameter("lastname");
          String password = request.getParameter("password");
          String mail = request.getParameter("e-mail");
          String team = request.getParameter("teams");

          basicLogic.addUser(firstname, lastname, username, mail, password, team);

          //response.sendRedirect("http://localhost:63342/GetToThePoint/meetmeserver/src/main/webapp/confirmation.html");

          response.setContentType("text/html");
          response.setBufferSize(8192);
          try (PrintWriter out = response.getWriter()) {
              out.println("<html lang=\"en\"><head><title>Servlet Hello</title></head>");

              // then write the data of the response

              out.println(output1 +
                      "<h1>Gratulation!</h1>\n" +
                      "<p>Herzlichen Glückwunsch du hast dich erfolgreich registriert. <br />\n" +
                      "Verschwende nun keinen Moment! Schalte dein GPS-Signal an und es kann losgehen. <br />\n" +
                      output2);
      /*out.println("<body  bgcolor=\"#ffffff\">"
          + "<h1>Vielen Dank für die Registrierung, "+request.getParameter("username")+"!"
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
      out.println("</body></html>");*/

          }
      }
  }

}
