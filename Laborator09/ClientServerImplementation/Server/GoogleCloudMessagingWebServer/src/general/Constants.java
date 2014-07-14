package general;

public interface Constants {

    final public static String      DATABASE_CONNECTION         = "jdbc:mysql://localhost:3306/googlecloudmessaging";
    final public static String      DATABASE_NAME               = "googlecloudmessaging";
    final public static String      DATABASE_USER               = "root";
    final public static String      DATABASE_PASSWORD           = "******"; 
    
	// the address of the Google Cloud Messaging HTTP connection server 
	// to which the application server sends a POST request in order to be further transmitted to the mobile device
	
	final public static String 		REQUEST_SERVER_URL 			= "https://android.googleapis.com/gcm/send";
	
	// TO DO: replace these value with your own
	
	// HTTP header must contain the Authorization field which includes the API key
	// provided from the Google Developer Console
	// Authorization: key=...
	
    final public static String 		API_KEY 					= "AIzaSyASz6lZCK1PPOITYbBM1Mtju50OMles9qs";    
        
    final public static boolean     DEBUG                       = false;
	
    final public static String		ID							= "id";
    final public static String		REGISTRATION_ID				= "registration_id";
    final public static String		USER_NAME					= "username";
    final public static String		EMAIL						= "email";
    final public static String		CREATION_TIME				= "creation_time";
    
    final public static int			ID_INDEX					= 0;
    final public static int			REGISTRATION_ID_INDEX		= 1;
    final public static int			USER_NAME_INDEX				= 2;
    final public static int			EMAIL_INDEX					= 3;
    final public static int			CREATION_TIME_INDEX			= 4;
    
    final public static String		ID_TEXT						= "Identificator";
    final public static String		REGISTRATION_ID_TEXT		= "Identificator de Inregistrare";
    final public static String		USER_NAME_TEXT				= "Nume de Utilizator";
    final public static String		EMAIL_TEXT					= "Email";
    final public static String		CREATION_TIME_TEXT			= "Data Crearii";
    
    final public static String		TABLE_NAME					= "registered_devices";
    
    final public static String		MESSAGE_TEXT				= "Mesaj";
    
    final public static String		SEND_MESSAGE				= "send_message";
    final public static String		SEND_MESSAGE_TEXT			= "Transmite Notificare Dispozitiv Mobil";
    
    final public static String[]	SYSTEM_FUNCTION	            = {"CURRENT_TIMESTAMP()"};
    
    final public static String		WEB_PAGE_TITLE				= "Google Cloud Messaging Registered Devices";
    
    final public static String		REGISTRATION_IDS			= "registration_ids";
    final public static String		DATA						= "data";
    final public static String		MESSAGE						= "message";
}