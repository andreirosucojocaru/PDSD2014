package ro.pub.systems.pdsd.lab09.googlecloudmessagingclient;

public interface Configuration {

	// TO DO: replace this value with your own
	// the project number as it is generated from the Google Developer Console

	final public static String 	PROJECT_NUMBER 							= "418960676761";

	// the fragmentType value indicated whether the mobile device has sent or not
	// its registration id to the application server

	final public static int 	REGISTERED_FRAGMENT_TYPE				= 1;
	final public static int		UNREGISTERED_FRAGMENT_TYPE				= 2;

	final public static String	GOOGLECLOUDMESSAGING_WEBSERVER_URL		= "http://192.168.56.1:8080/GoogleCloudMessagingWebServer/GoogleCloudMessagingRegisterServlet";

	final public static String	USER_NAME								= "username";
	final public static String	EMAIL									= "email";
	final public static String	REGISTRATION_ID							= "registration_id";

	final public static int		SUCCESS									= 0;
	final public static int		FAILURE									= -1;

	final public static String 	REGISTRATION_ID_PROPERTY				= "registration_id";
	final public static String 	APPLICATION_VERSION_PROPERTY			= "application_version";
	final public static String 	APPLICATION_SERVER_REGISTRATION_STATUS	= "application_server_registration_status";

	final public static int		PLAY_SERVICES_RESOLUTION_REQUEST 		= 9000;
	final public static int 	NOTIFICATION_ID 						= 1;

	final public static int		NUMBER_OF_STEPS							= 5;
	final public static int		STEP_DURATION							= 3000;
}
