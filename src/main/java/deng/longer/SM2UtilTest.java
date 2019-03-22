package deng.longer;




import java.util.Arrays;

 

/**

 * 	 <B>说	明<B/>:SM2非对称加解密工具类测试

 * @author 作者名：冯龙淼

 * 		   E-mail：fenglongmiao@163.com

 * 

 * @version 版   本  号：1.0.<br/>

 *          创建时间：2018年3月6日 下午5:20:56

 */

public class SM2UtilTest {

 

	/** 元消息串 */

	private static String M = "哈哈哈，&*&…………&、、//\\!@#$%^&*()物品woyebuzhidaowozijiqiaodesha!@#$%^&*())))))ooooooooppppppppppppppppppplllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkffffffffffffffffffffffffffffffffffffff";

	

	public static void main(String[] args) {

		SM2Util sm2 = new SM2Util();

		SM2KeyPair keyPair = sm2.generateKeyPair();

		byte[] data = sm2.encrypt(M,keyPair.getPublicKey());

		System.out.println("data is:"+Arrays.toString(data));

		sm2.decrypt(data, keyPair.getPrivateKey());//71017045908707391874054405929626258767106914144911649587813342322113806533034

	}

	

}
