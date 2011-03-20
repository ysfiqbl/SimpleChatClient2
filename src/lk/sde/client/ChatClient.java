// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package lk.sde.client;

import java.io.IOException;
import lk.sde.common.ChatClientCommandFilter;
import lk.sde.common.ChatIF;
import lk.sde.ocsf.client.AbstractClient;

import static lk.sde.common.ChatClientCommandFilter.*;



/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  ChatClientCommandFilter chatClientCommandFilter;

  String loginId;
  private boolean isQuit = false;   // Variable used to determine whether the quit() or logoff() called closeConnection()
                                    // Used strickly to determine which message to write to console.
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.ˆ
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.chatClientCommandFilter =  new ChatClientCommandFilter();
    //openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

    public boolean isIsQuit() {
        return isQuit;
    }

    public void setIsQuit(boolean isQuit) {
        this.isQuit = isQuit;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

  

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
        String command="";
        try
        {
            if(chatClientCommandFilter.isCommand(message)){
                command = chatClientCommandFilter.getCommand(message);
                processCommand(command);
            }
            else{
                sendToServer(message);
            }
        }
        catch(IOException e)
        {
            if(LOGIN.equals(command)){
                clientUI.display("Cannot contact the server. Please try again later.");
            }
            else{
                clientUI.display("You need to be logged on to send a message to the server. Use the #login command to connect to the server.");
            }
        }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

    /**
     * Print a message to the client indicating that the server
     * has shutdown.
     *
     * [Client side (a) requirement for assignment]
     */
    @Override
    protected void connectionClosed(){
        if(!isQuit) System.out.println("Disconnecting from server.");
    }

    /**
     * This methods gets called when the server goes down.
     * Therefore the client is gracefully shutdown
     * [Client side (a) requirement for assignment]
     *
     * @param exception
     */
    @Override
    protected void connectionException(Exception exception) {
        System.out.println("The server has shutdown.");
        this.quit();
    }


    /**
     * This method handles the commands to the clients console.
     * That is anything beginning with '#'
     *
     * @param input
     * @throws IOException
     */
    private void processCommand(String input) throws IOException{
        String[] inputArray = input.split("\\s");
        String command = inputArray[0];
        
        if(LOGIN.equals(command)){
            if(!isConnected()){
                this.openConnection();
                this.sendToServer("#login " + loginId);
            }
            else{
              clientUI.display("You are already logged in.");
            }
            return;
        }        

        if(LOGOFF.equals(command)){
            if(isConnected()){
                this.closeConnection();
            }
            else{
               clientUI.display("You are already logged off.");
            }
            return;
        }

        if(GET_HOST.equals(command)){
            clientUI.display("HOST: "+this.getHost());
            return;
        }

        if(GET_PORT.equals(command)){
            clientUI.display("PORT: "+this.getPort());
            return;
        }

        if(SET_HOST.equals(command)){
            String parameter = chatClientCommandFilter.getSetHostParameter(inputArray);

            if(parameter==null){
                clientUI.display("Please specify the host. Usage:#sethost <host>");
            }
            else{
                if(!isConnected()){
                    this.setHost(parameter);
                }
                else{
                    clientUI.display("You need to log off in order to set the host.");
                }
            }

            return;
        }

        if(SET_PORT.equals(command)){
            int parameter = chatClientCommandFilter.getSetPortParameter(inputArray);

            if(parameter==-1){
                clientUI.display("Please specify the port.Usage:#setport <port>");
            }
            else if(parameter==-2){
                clientUI.display("Port should be a numeric value.");
            }
            else{
                if(!isConnected()){
                    this.setPort(parameter);
                }
                else{
                    clientUI.display("You need to log off in order to set the port.");
                }
            }

            return;
        }

        if(QUIT.equals(command)){
            clientUI.display("Disconnecting from the server and exiting the chat client.");
            isQuit=true;
            this.quit();            
            return;
        }
        //this.sendToServer("#"+input);
        this.sendToServer("#"+input);

    }

}
//End of ChatClient class
