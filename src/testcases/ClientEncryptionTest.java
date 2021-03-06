package testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;

import elfville.client.PublicKeyCipher;
import elfville.protocol.CentralBoardRequest;
import elfville.protocol.Request;
import elfville.protocol.SignInRequest;
import elfville.protocol.utils.SharedKeyCipher;

public class ClientEncryptionTest {
	PublicKeyCipher pk;
	SharedKeyCipher sk;

	@Before
	public void setUp() throws Exception {
		pk = new PublicKeyCipher(
				"/Users/caleb/Documents/workspace/ElfVille/src/elfville/client/elfville.pub.der");
		sk = new SharedKeyCipher();
	}

	@Test
	public void testGetNewSharedKey() throws Exception {
		SecretKey k1 = sk.getSharedKey();
		assertNotNull(k1);
		
		sk.reinitialize();
		SecretKey k2 = sk.getSharedKey();
		assertNotNull(k2);
		assertNotSame(k1, k2);
	}

	@Test
	public void testEncryptWithServerKey() throws Exception {
		SignInRequest req = new SignInRequest("homie5",
				"you are the man".toCharArray(), sk, 0);
		assertNotNull(pk.encrypt(req));
	}

	@Test
	public void testEncryptWithSharedKey() throws Exception {
		sk.reinitialize();
		Request req = new CentralBoardRequest();
		assertNotNull(sk.encrypt(req));
	}

}
