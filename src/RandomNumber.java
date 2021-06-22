import java.security.SecureRandom;
/**
 * This class is used for the purpose of generating random numbers during authentication
 */
public class RandomNumber 
{
    /**
     * This method is used to generate a secure random number of user specified number of bytes
     * @param num: number of bytes
     * @return: byte array form of random number
     */
    public static byte[] generateSecureNum(int num)
    {
        byte[] numberGenerated=new byte[num];
        new SecureRandom().nextBytes(numberGenerated);
        return numberGenerated;
    }


    public static String generateSecureNuminHex(int num)
    {
        byte[] numberGenerated=new byte[num];
        new SecureRandom().nextBytes(numberGenerated);
        return null;
    }



    
}
