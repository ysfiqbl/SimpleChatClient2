/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lk.sde.common;

/**
 * The <code>ChatClientCommandFilter</code> class handles
 * has the definitions and the verification methods to deal with
 * commands entered to the server console. That is any input beginning
 * with '#'
 *
 * @author Yusuf
 */
public class ChatClientCommandFilter {
    /**
     * To recognize an input as a command, the input needs to be
     * prefixed by the COMMAND_SYMBOL. Here it is '#'
     *
     */
    public final static String COMMAND_SYMBOL = "#";

    /**
     * List of all the commands that the server can perform.
     */
    public final static String QUIT = "quit";
    public final static String LOGOFF = "logoff";
    public final static String SET_HOST = "sethost";
    public final static String SET_PORT = "setport";
    public final static String LOGIN = "login";
    public final static String GET_HOST = "gethost";
    public final static String GET_PORT = "getport";
    public final static String MSG = "msg";
    public final static String CHANNEL = "channel";
    public final static String MONITOR = "monitor";


    /**
     * Determine whether an input is a command or not.
     *
     * @param command
     * @return
     */
    public boolean isCommand(String command){
        return command.startsWith(COMMAND_SYMBOL) ? true : false;
    }

    /**
     * Remove the COMMAND_SYMBOL from the input, lower case it
     * and remove both trailing and leading whitespace characters.
     * return the resulting String.
     *
     * @param command
     * @return
     */
    public String getCommand(String command){
        return command.trim().substring(1,command.length()).toLowerCase();
    }

    /**
     * Check whether the client passed a parameter for the host
     *
     * @param inputArray
     * @return
     */
    public String getSetHostParameter(String[] inputArray){
        String host = null;

        try{
            host = inputArray[1];
        }
        catch(ArrayIndexOutOfBoundsException ex){
            
        }
        finally{
            return host;
        }
    }

    /**
     * Validates a user entered port number.
     *
     * Returns -1 if no parameter was give.
     * Returns -2 if the parameter is not a numeric
     * Else returns the integer representation of the port number.
     *
     * @param inputArray
     * @return
     */
    public int getSetPortParameter(String[] inputArray){
        int port = 0;

        try{
            port = Integer.parseInt(inputArray[1]);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            port = -1;
        }
        catch(NumberFormatException ex){
            port = -2;
        }
        finally{
            return port;
        }
    }

}
