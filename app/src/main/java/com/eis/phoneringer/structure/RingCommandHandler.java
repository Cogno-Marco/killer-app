package com.eis.phoneringer.structure;

import com.eis.smslibrary.SMSMessage;

/**
 * Class used to parse RingCommand to SMSMessage and back
 *
 * @author Alberto Ursino, Luca Crema, Marco Mariotto
 */
public class RingCommandHandler {

    public static final char COMMAND_IDENTIFIER = '_';

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static RingCommandHandler instance = null;

    /**
     * Private constructor
     */
    private RingCommandHandler() {
    }

    /**
     * @return the RingCommandHandler instance
     */
    public static RingCommandHandler getInstance() {
        if (instance == null)
            instance = new RingCommandHandler();
        return instance;
    }

    /**
     * Extracts the password from the message received and creates a RingCommand
     * A valid command is the following: "_password"
     *
     * @param smsMessage {@link SMSMessage} object received
     * @return a {@link RingCommand} object, null if the message is not a valid command
     */
    public RingCommand parseMessage(SMSMessage smsMessage) {
        String messageContent = smsMessage.getData();
        if (messageContent.charAt(0) == COMMAND_IDENTIFIER)
            return new RingCommand(smsMessage.getPeer(), messageContent.substring(1));
        return null;
    }

    /**
     * Extracts the password and the peer from the RingCommand and creates a SMSMessage object
     * The password is sent with the {@link #COMMAND_IDENTIFIER} in front
     *
     * @param ringCommand to parse, it must be a valid one
     * @return a SMSMessage object
     */
    public SMSMessage parseCommand(RingCommand ringCommand) {
        return new SMSMessage(ringCommand.getPeer(), COMMAND_IDENTIFIER + ringCommand.getPassword());
    }

}
