
/**
 * This class is developed for the purpose of creating the initial GUI window and client instance
 */
public class FrontEndDriver 
{
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args)
    {
        //creates instance of GUI Application
        GUIBuilder guiApplication = new GUIBuilder();
        guiApplication.loadWelcomeScreen();
        guiApplication.loadLogin();
    }
}
