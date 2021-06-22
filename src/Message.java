import java.io.Serializable;

/**
 * This class is used for the purpose of the client sending messages
 * The format of the message is the messageTask indicates the task to be performed by the server
 * The Object array of length provided by user, contains information required to transmit to the server
 */
public class Message implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String messageTask;
    private Object[] content;

    /**
     * Constructor initialising size of content and task to be done
     * @param length: size of Object array that contains information to transmit to the server
     */
    public Message(int length)
    {
        messageTask="";
        content=new Object[length];
    }

    /**
     * set the content of the message
     * @param content: Object array containing content of the message
     */
    public void setContent(Object[] content)
    {
        this.content=content;
    }

    /**
     * set name of task to be performed on server side
     * @param messageTask
     */
    public void setTask(String messageTask)
    {
        this.messageTask=messageTask;
    }

    /**
     * returns message content
     * @return
     */
    public Object[] getContent()
    {
        return content;
    }

    /**
     * returns name of message task
     * @return
     */
    public String getTask()
    {
        return messageTask;
    }
    
}
