package graphicuserinterface;

import general.Constants;

import java.io.PrintWriter;
import java.util.ArrayList;

public class GoogleCloudMessagingGraphicUserInterface {
    
    public GoogleCloudMessagingGraphicUserInterface() { }
    
    public static void displayGoogleCloudMessagingGraphicUserInterface(ArrayList<ArrayList<Object>> records, PrintWriter printWriter) {
        String content = new String();
        content += "<html>\n";
        content += "<head>\n";
        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" /><title>"+Constants.WEB_PAGE_TITLE+"</title>\n";
        content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/googlecloudmessaging.css\" />\n";  
        content += "</head>\n";
        content += "<body>\n";
        content += "<center>\n";
        content += "<h2>"+Constants.WEB_PAGE_TITLE+"</h2>\n";
        content += "<br>\n";
        content += "<form name=\"formular\" action=\"GoogleCloudMessagingPushNotificationServlet\" method=\"POST\">\n";        
        content += "<table bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\">\n";
        for (ArrayList<Object> record:records) {
        	int id = Integer.parseInt(record.get(Constants.ID_INDEX).toString());
        	content += "<tr bgcolor=\"#ebebeb\"><td>"+Constants.USER_NAME_TEXT+"</td><td>"+record.get(Constants.USER_NAME_INDEX)+"</td></tr>\n";
        	content += "<tr bgcolor=\"#ebebeb\"><td>"+Constants.EMAIL_TEXT+"</td><td>"+record.get(Constants.EMAIL_INDEX)+"</td></tr>\n";
        	content += "<tr bgcolor=\"#ebebeb\"><td>"+Constants.CREATION_TIME_TEXT+"</td><td>"+record.get(Constants.CREATION_TIME_INDEX)+"</td></tr>\n";
	        content += "<tr bgcolor=\"#ebebeb\"><td>"+Constants.MESSAGE_TEXT+"</td><td><textarea name=\""+Constants.MESSAGE_TEXT+"#"+id+"\" cols=\"25\" rows=\"5\"></textarea></td></tr>\n";
	        content += "<tr><td colspan=\"2\" align=\"center\"><input type=\"submit\" name=\""+Constants.SEND_MESSAGE+"#"+id+"\" value=\""+Constants.SEND_MESSAGE_TEXT+"\"></td></tr>\n";
	        content += "<tr><td colspan=\"2\" align=\"center\"><input type=\"hidden\" name=\""+Constants.REGISTRATION_ID+"#"+id+"\" value=\""+record.get(Constants.REGISTRATION_ID_INDEX)+"\"></td></tr>\n";
        }
        content += "</table>\n";                
        content += "</form>\n";
        content += "<p>\n";        
        content += "</center>\n";
        content += "</body>\n";
        content += "</html>";
        printWriter.println(content);
    }
}
