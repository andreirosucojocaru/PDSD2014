import dataaccess.DataBaseConnection;
import general.Constants;
import graphicuserinterface.GoogleCloudMessagingGraphicUserInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleCloudMessagingRegisterServlet extends HttpServlet {
    final public static long    serialVersionUID = 1024L;
    
    public String               registrationId, userName, email;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DataBaseConnection.openConnection();
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            DataBaseConnection.closeConnection();
        } catch (SQLException exception) {
            System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> parameters = request.getParameterNames();
        boolean found = false;
        while(parameters.hasMoreElements()) {
            String parameter = (String)parameters.nextElement();
            if (parameter.equals(Constants.REGISTRATION_ID)) {
            	found = true;
                registrationId = request.getParameter(parameter);
            }
            if (parameter.equals(Constants.USER_NAME)) {
                userName = request.getParameter(parameter);
            }
            if (parameter.equals(Constants.EMAIL)) {
            	email = request.getParameter(parameter);
            }
        }
        if (found) {
            ArrayList<String> values = new ArrayList<String>();
            values.add(registrationId);
            values.add(userName);
            values.add(email);
            values.add(general.Constants.SYSTEM_FUNCTION[0]);
            try {
            	DataBaseConnection.insertValuesIntoTable(Constants.TABLE_NAME, null, values, true);
            } catch (Exception exception) {
            	System.out.println("exceptie: "+exception.getMessage());
                if (Constants.DEBUG)
                    exception.printStackTrace();
            }
        }
        response.setContentType("text/html");
        try {
        	PrintWriter printWriter = new PrintWriter(response.getWriter());
        	ArrayList<ArrayList<Object>> records = DataBaseConnection.getTableContent(Constants.TABLE_NAME, null, null, null, null);
            GoogleCloudMessagingGraphicUserInterface.displayGoogleCloudMessagingGraphicUserInterface(records, printWriter);
        } catch (Exception exception) {
        	System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try {
        	PrintWriter printWriter = new PrintWriter(response.getWriter());
        	ArrayList<ArrayList<Object>> records = DataBaseConnection.getTableContent(Constants.TABLE_NAME, null, null, null, null);
            GoogleCloudMessagingGraphicUserInterface.displayGoogleCloudMessagingGraphicUserInterface(records, printWriter);
        } catch (Exception exception) {
        	System.out.println("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }      	 
}
