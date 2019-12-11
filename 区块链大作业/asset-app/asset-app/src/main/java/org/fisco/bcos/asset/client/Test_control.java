package org.fisco.bcos.asset.client;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fisco.bcos.asset.contract.Asset;
import org.fisco.bcos.asset.contract.Asset.RegisterEventEventResponse;
import org.fisco.bcos.asset.contract.Asset.TransferEventEventResponse;
import org.fisco.bcos.asset.contract.Test_v1;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class Test_control {
	static Logger logger = LoggerFactory.getLogger(Test_control.class);

	private Web3j web3j;

	private Credentials credentials;

	public Web3j getWeb3j() {
		return web3j;
	}

	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public void recordAssetAddr(String address) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.setProperty("address", address);
		final Resource contractResource = new ClassPathResource("contract.properties");
		FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
		prop.store(fileOutputStream, "contract address");
	}

	public String loadAssetAddr() throws Exception {
		// load Asset contact address from contract.properties
		Properties prop = new Properties();
		final Resource contractResource = new ClassPathResource("contract.properties");
		prop.load(contractResource.getInputStream());

		String contractAddress = prop.getProperty("address");
		if (contractAddress == null || contractAddress.trim().equals("")) {
			throw new Exception(" load Asset contract address failed, please deploy it first. ");
		}
		logger.info(" load Asset address from contract.properties, address is {}", contractAddress);
		return contractAddress;
	}

	public void initialize() throws Exception {

		// init the Service
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Service service = context.getBean(Service.class);
		service.run();

		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		Web3j web3j = Web3j.build(channelEthereumService, 1);

		// init Credentials
		Credentials credentials = Credentials.create(Keys.createEcKeyPair());

		setCredentials(credentials);
		setWeb3j(web3j);

		logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
	}

	private static BigInteger gasPrice = new BigInteger("30000000");
	private static BigInteger gasLimit = new BigInteger("30000000");

	public void deployAssetAndRecordAddr() {

		try {
			Test_v1 asset = Test_v1.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
			System.out.println(" deploy Asset success, contract address is " + asset.getContractAddress());

			recordAssetAddr(asset.getContractAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(" deploy Asset contract failed, error message is  " + e.getMessage());
		}
	}

	public String formdata(String a) {
		if(a=="") return "";
		String s=new String();
		Boolean begin=false;
		for(int i=2;i<a.length();i++) {
			//System.out.print(" a [] "+a.charAt(i));
			if(begin==false) {
				if(a.charAt(i)!='0') {
					begin=true;
					s+=a.charAt(i);
				}
			} else {
				s+=a.charAt(i);
			}
		}
		//String data=new String();
		//System.out.println("format: "+s+" old: "+a);
		return s;
	}
	
	public String _16tostring(String a) {
		a=formdata(a);
		String str = "0123456789abcdef";
		char[] hexs = a.toCharArray();
		byte[] bytes = new byte[a.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}
	
	public void payback(String user) {
		try {
			String contractAddress = loadAssetAddr();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			asset.huanqian(user).send();
		} catch(Exception e) {
			System.out.println("error: "+e.getMessage());
		}
	}
	
	public String getUserBillMoney(String user) {
		try {
			String contractAddress = loadAssetAddr();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			Tuple2<BigInteger,BigInteger> res = asset.get_bills_money(user).send();
			System.out.println("get the money result: ");
			 //String s=formdata(res.getValue2());
			if(res.getValue1().compareTo(new BigInteger("0"))==0){
				return new String("-"+res.getValue2());
			}
			 System.out.println("outmoney: "+res.getValue2());
			 return res.getValue2().toString();
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
		}
		return "0";
	}
	
	public String getUserBillFromUser(String user) {
		try {
			String contractAddress = loadAssetAddr();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			 TransactionReceipt res = asset.get_bills_from(user).send();
			
			 //String s=formdata(res.getOutput());
			 System.out.println("outuser: "+_16tostring(res.getOutput()));
			 return _16tostring(res.getOutput());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	
	public void tranBills(String f_u,String t_u,BigInteger m) {
		try {
			String contractAddress = loadAssetAddr();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			TransactionReceipt receipt = asset.tran_bills(f_u, t_u, m).send();
			String res = receipt.getOutput();
			System.out.println("result: "+res);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		//List<TransferEventEventResponse> response = asset.getTransferEventEvents(receipt);
	}

}
