import java.io.UnsupportedEncodingException;
import java.security.*;
import javax.crypto.*;
import java.util.*;

public class RSAEncryption{
	
	 public KeyPairGenerator generator=null;	
	 public KeyPair keyPair=null;
	 public PublicKey publicKey=null;
	 public PrivateKey privateKey = null;
	 public Scanner s = null;

	
	public static void main(String args[]) {
		
	}
    public KeyPair GenerateKey() {//method of generating key
    		try {
				generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(1024); 
		        keyPair = generator.generateKeyPair();
		       
		        
		        
		      
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        return keyPair;  //if i call the method generateKey(), i will get keyPair.
    }
   
    public byte[] Encryption(PublicKey publickey,  byte[] text) {//method of encryption
    				Cipher cipher;
    				byte[] b0 =null;
				try {
					cipher = Cipher.getInstance("RSA");
					cipher.init(Cipher.ENCRYPT_MODE, publickey);
					b0 = cipher.doFinal(text);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
        return b0;
        
        
       
    		
    }
    public byte[] Decryption(PrivateKey privateKey,  byte[] Encrypttext){ //method of decryption.
    		
    		byte[] b1 = null;
    		Cipher cipher2;
    		try {
    			
    			cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.DECRYPT_MODE, privateKey);
				
			
	    		b1 = cipher2.doFinal(Encrypttext);
	    		
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		return b1;
    }

    
}
