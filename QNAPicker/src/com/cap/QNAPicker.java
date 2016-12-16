package com.cap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;  
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;  
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute; 
import javax.naming.directory.BasicAttributes; 
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem; 
import javax.naming.directory.SearchControls;  
import javax.naming.directory.SearchResult;  
import javax.naming.ldap.Control;  
import javax.naming.ldap.InitialLdapContext;  
import javax.naming.ldap.LdapContext;  
import javax.naming.ldap.PagedResultsControl;  
import javax.naming.ldap.PagedResultsResponseControl;
import javax.security.auth.login.LoginException;

import com.ast.logger.Cacher;
import com.ast.logger.UtilityLogger;

import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

//import oracle.iam.identity.usermgmt.api.UserManager;
//import oracle.iam.platform.OIMClient;  


public class QNAPicker { 

	static UtilityLogger logger = null;
	public static String OID_HOST = null;
	public static String OID_PORT = null;
	public static String OIM_URL1 = null;
	public static String xlHomeDir = null;
	public static String CONTEXT_FACTORY = null;
	public static String OID_USERNAME = null;
	public static String OID_PASSWORD = null;
	public static String OIM_USER_ID1 = null;
	public static String OIM_USER_PASSWORD1 = null;
	public static String XL_LOGIN_CONFIG = null;
	public static String Query_Users_OIM1 = null;
	public static String CSV_FILE_PATH = null;
	public static String OID_SEARCH_BASE = null;
	public static String OID_SEARCH_FILTER = null;
	public static String OID_COUNT_LIMIT = null;




	public static void main (String[] args) {
		long startTime = System.currentTimeMillis();

		OID_HOST = Cacher.getproperty("OID_HOST");
		OID_PORT = Cacher.getproperty("OID_PORT");
		OIM_URL1 = Cacher.getproperty("OIM_URL1");
		XL_LOGIN_CONFIG = Cacher.getproperty("XL_LOGIN_CONFIG");
		xlHomeDir = Cacher.getproperty("XL_HOME_DIR");
		OID_USERNAME = Cacher.getproperty("OID_USERNAME");
		OID_PASSWORD = Cacher.getproperty("OID_PASSWORD");
		OIM_USER_ID1 = Cacher.getproperty("OIM_USER_ID1");
		OIM_USER_PASSWORD1 = Cacher.getproperty("OIM_USER_PASSWORD1");
		Query_Users_OIM1 = Cacher.getproperty("Query_Users_OIM1");
		logger =UtilityLogger.getUtilityLogger(Cacher.getproperty("LOGGER_INSTANCE_NAME"));
		CSV_FILE_PATH =Cacher.getproperty("CSV_FILE_PATH");
		OID_SEARCH_BASE =Cacher.getproperty("OID_SEARCH_BASE");
		OID_SEARCH_FILTER =Cacher.getproperty("OID_SEARCH_FILTER");
		OID_COUNT_LIMIT =Cacher.getproperty("OID_COUNT_LIMIT");

		//List<UserRecord> listUserRecord=null;
		QNAPicker oim = new QNAPicker();
		//DirContext ldapContext =oim.OIDConnection();
		//listUserRecord=oim.fetchUserRecords(ldapContext);
		OIMClient oimClient=oim.OIMServerConnection();
		
		//manual list created with below sample data
		//2016-12-14 22:55:47 INFO  ASTCustomLogger:39 - uid: afletche::securityquestions: Question3=Last 4 digits of SSN, Question2=City of birth, Question1=Mother's maiden name::securityanswers: Answer1=brown, Answer3=7168, Answer2=montego bay
		//2016-12-14 22:55:47 INFO  ASTCustomLogger:39 - uid: alexabbud::securityquestions: Question2=Last 4 digits of SSN, Question1=City of birth, Question3=Mother's maiden name::securityanswers: Answer2=0676, Answer1=Juarez, Answer3=Mendez
		List<UserRecord> listUserRecord=new ArrayList<UserRecord>();
		UserRecord usrRecord=new UserRecord();
		usrRecord.setMemberID("CAPTESTUSER25");//get data always in uppercase from OID
		usrRecord.setSecQuestion("securityquestions: Question3=Last 4 digits of SSN, Question2=City of birth, Question1=Mother's maiden name");
		usrRecord.setSecAnswer("securityanswers: Answer1=brown, Answer3=7168, Answer2=montego bay");
		
		UserRecord usrRecord1=new UserRecord();
		usrRecord1.setMemberID("aschaad");
		usrRecord1.setSecQuestion("securityquestions: Question1=Undergraduate major, Question2=First car, Question3=Mother's maiden name");
		usrRecord1.setSecAnswer("securityanswers: Answer3=manlove, Answer2=malibu, Answer1=biology");
		
		UserRecord usrRecord11=new UserRecord();
		usrRecord11.setMemberID("CAPTESTUSER26");
		usrRecord11.setSecQuestion("securityquestions: Question2=City of birth, Question1=Best childhood friend's first name, Question3=First car");
		usrRecord11.setSecAnswer("securityanswers: Answer3=Honda CRX, Answer2=Rome, Answer1=Casey");
		
		listUserRecord.add(usrRecord);
		listUserRecord.add(usrRecord1);
		listUserRecord.add(usrRecord11);
		
		if (!listUserRecord.isEmpty()){
			oim.UpdateQnADetail(oimClient,listUserRecord);
		}
		
		else{
			logger.info("Nothing to update!!listUserRecord  empty......");
			System.out.println("Nothing to update!!listUserRecord  empty......");
		}
		//oim.OIMServer1UserSearch();
		//oim.readCSVData();
		//oim.OIMServer1Connection();
		//oim.changePassword();
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		logger.info("Total time taken::"+totalTime);

		logger.info(" Utility Exit......");
		System.out.println(" Utility Exit......");
	}


	private void printRecord() {
		// TODO Auto-generated method stub
		
	}


	private void UpdateQnADetail(OIMClient oimClient, List<UserRecord> listUserRecord) {

		logger.info("UpdateQnADetail Method Executing......");
		System.out.println("UpdateQnADetail Method Executing......");

		int usrCountFound=0;
		int usrCountNotFound=0;

		UserManager usrMgr = oimClient.getService(UserManager.class);

		for (UserRecord temp : listUserRecord) {
			String[] kvPairs = temp.getSecQuestion().substring(temp.getSecQuestion().indexOf(":")+1).split(",");
			Map<String,String> mapQuestion = new HashMap<String,String>();

			for (String entry : kvPairs) {
				String[] keyValue = entry.split("=");
				/*if(keyValue[1].trim().equalsIgnoreCase("Mother's maiden name")){
					keyValue[1]="What is your mother's maiden name?";
				}else if (keyValue[1].trim().equalsIgnoreCase("City of birth")){
					keyValue[1]="What is the city of your birth?";
				} else if (keyValue[1].trim().equalsIgnoreCase("Last 4 digits of SSN")){
					keyValue[1]="What is last 4 digits of SSN?";
				}*/
				mapQuestion.put(keyValue[0].trim(),keyValue[1].trim());

			}
			kvPairs=null;

			//System.out.println("Updated mapQuestion :: "+mapQuestion);
			//logger.info("Updated mapQuestion :: "+mapQuestion.toString());

			String[] kvPairs1 = temp.getSecAnswer().substring(temp.getSecAnswer().indexOf(":")+1).split(",");
			Map<String,String> mapAnswer = new HashMap<String,String>();

			for (String entry : kvPairs1) {
				String[] keyValue = entry.split("=");
				mapAnswer.put(keyValue[0].trim(),keyValue[1].trim());

			}
			kvPairs1=null;
			//System.out.println("mapAnswer :: "+mapAnswer);
			//logger.info("mapAnswer :: "+mapAnswer.toString());

			HashMap<String,Object> tempQnAMap = new HashMap<String,Object>(); 


			for (Map.Entry<String, String> e : mapQuestion.entrySet()) {
				if(e.getKey().equalsIgnoreCase("Question1")){
					if(mapQuestion.get("Question1") != null){
						tempQnAMap.put(mapQuestion.get("Question1"), mapAnswer.get("Answer1"));
					}
					//tempQnAMap.put(mapQuestion.get("Question1"), mapAnswer.get("Answer1"));
				} else if (e.getKey().equalsIgnoreCase("Question2")){
					if(mapQuestion.get("Question2") != null){
						tempQnAMap.put(mapQuestion.get("Question2"), mapAnswer.get("Answer2"));
					}
					//tempQnAMap.put(mapQuestion.get("Question2"), mapAnswer.get("Answer2"));
				} else if (e.getKey().equalsIgnoreCase("Question3")){
					if(mapQuestion.get("Question3") != null){
						tempQnAMap.put(mapQuestion.get("Question3"), mapAnswer.get("Answer3"));
					}
					//tempQnAMap.put(mapQuestion.get("Question3"), mapAnswer.get("Answer3"));
				}

			}
			System.out.println("Processed  Map with QnA:: "+tempQnAMap);
			logger.info("Processed  Map with QnA:: "+tempQnAMap);

			boolean flag=false;

			try {
				SearchCriteria criteria = new SearchCriteria("User Login",temp.getMemberID().toUpperCase(), SearchCriteria.Operator.EQUAL);
				Set retSet = new HashSet();
				retSet.add("usr_key");
				retSet.add("User Login");

				List<User> users = usrMgr.search(criteria, retSet, null);

				for(User user : users) {
					Long usrKey = (Long)user.getAttribute("usr_key");
					String usrLogin = (String)user.getAttribute("User Login");

					System.out.println(usrKey + " " + usrLogin);
					logger.info("User found in OIM :: usr_key :: "+usrKey+":: usrLogin :: "+usrLogin);
					System.out.println("User found in OIM :: usr_key :: "+usrKey+":: usrLogin :: "+usrLogin);
					flag=true;

				}


				if (flag){

					System.out.println("Updating QnA for user:: "+temp.getMemberID()+" :: "+tempQnAMap);
					logger.info("Updating QnA for user:: "+temp.getMemberID()+" :: "+tempQnAMap);

					usrMgr.setUserChallengeValues(temp.getMemberID(),true,tempQnAMap);

					System.out.println("Updated user successfully");
					logger.info("Updated user successfully");

					usrCountFound++;
				}

				else {
					logger.info("User not found in OIM :: "+temp.getMemberID());
					System.out.println("User not found in OIM  :: "+temp.getMemberID());
					usrCountNotFound++;
				}

			} catch (UserManagerException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				logger.info("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				e1.printStackTrace();
			} catch (AccessDeniedException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				logger.info("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				e1.printStackTrace();
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				logger.info("Error occurred in usrMgr.setUserChallengeValues method : " + e1);
				e1.printStackTrace();
			}

			mapQuestion=null;
			mapAnswer=null;
			tempQnAMap=null;


		}

		logger.info("User's QnA Updated successfully, Total count :: "+usrCountFound);
		System.out.println("User's QnA Updated successfully, Total count :: "+usrCountFound);

		logger.info("User's QnA Not Updated successfully, Total count :: "+usrCountNotFound);
		System.out.println("User's QnA Not Updated successfully, Total count :: "+usrCountNotFound);
		//StringBuffer temp.getSecQuestion()=new StringBuffer("securityquestions: Question3=Last 4 digits of SSN, Question2=City of birth, Question1=Mother's maiden name");
		//StringBuffer temp.getSecAnswer()=new StringBuffer("securityanswers: Answer1=brown, Answer3=7168, Answer2=montego bay");


		/*HashMap<String,Object> m11 = new HashMap<String,Object>(); 
	      m11.put(mapQuestion., "Tidwell");
	      m11.put("What is the city of your birth?", "Haskell");
	      m11.put("What is your favorite color?", "4228");

		 */  

		//System.out.println(temBuffer.substring(temBuffer.lastIndexOf("Question1")+10,temBuffer.lastIndexOf(",")));
		//System.out.println(temBuffer.substring(temBuffer.lastIndexOf("Answer1"), temBuffer.lastIndexOf("Answer1")+13));

		logger.info("UpdateQnADetail Method Exit......");
		System.out.println("UpdateQnADetail Method Exit......");

	}


	public OIMClient OIMServerConnection() {
		
		
		Hashtable<Object, Object> env = new Hashtable();
		env.put("java.naming.factory.initial",
				"weblogic.jndi.WLInitialContextFactory");
		env.put("java.naming.provider.url", OIM_URL1);
		System.setProperty("XL.HomeDir", xlHomeDir);
		System.setProperty("java.security.auth.login.config", XL_LOGIN_CONFIG);
		System.setProperty("OIM.AppServerType", "wls");
		System.setProperty("APPSERVER_TYPE", "wls");
		System.out.println("OIM Connection Detail::"+env);
		logger.info("OIM Connection Detail::"+env);
		OIMClient oimClient = new OIMClient(env);
		try {
			oimClient.login(OIM_USER_ID1, OIM_USER_PASSWORD1.toCharArray());
			System.out.println("Successfully Connected with OIMServer1Connection ");
			logger.info("Successfully Connected with OIMServer1Connection ");
		} catch (LoginException e) {
			System.out.println("Successfully Connected with OIMServer1Connection ");
			logger.error("Error in OIMServer1Connection method : " + e);
		} catch (Exception e) {
			System.out.println("Successfully Connected with OIMServer1Connection ");
			logger.error("Error in OIMServer1Connection method : " + e);
		} finally {
			env = null;
			OIM_URL1 = null;
			OIM_USER_ID1 = null;
			OIM_USER_PASSWORD1 = null;
		}
		return oimClient;
	 }


	private List<UserRecord> fetchUserRecords(DirContext ldapContext) {
		// TODO Auto-generated method stub
		// Create the search controls  

		logger.info(" Started Fetching user detail......");
		System.out.println(" Started Fetching user detail......");

		List<UserRecord> listUserRecord=new ArrayList<UserRecord>();

		SearchControls searchCtls = new SearchControls();

		String returnedAtts[]={"uid","SecurityQuestions","SecurityAnswers"};
		searchCtls.setReturningAttributes(returnedAtts);

		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchCtls.setCountLimit(Integer.parseInt(OID_COUNT_LIMIT));

		String searchFilter = OID_SEARCH_FILTER;

		String searchBase = OID_SEARCH_BASE;

		try {
			NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

			int memberIDNotFoundCount=0;
			while (answer.hasMoreElements()) {

				UserRecord usrRecord=new UserRecord();
				SearchResult sr = (SearchResult)answer.next();

				System.out.println(">>>" + sr.getName());
				Attributes attrs = sr.getAttributes();
				if ((attrs.get("uid")!=null)&&(attrs.get("SecurityQuestions")!=null)){
				System.out.println(">>>>>>" + attrs.get("uid"));
				usrRecord.setMemberID(attrs.get("uid")!=null?attrs.get("uid").toString():"");
				System.out.println(">>>>>>" + attrs.get("SecurityQuestions"));
				usrRecord.setSecQuestion(attrs.get("SecurityQuestions")!=null?attrs.get("SecurityQuestions").toString():"");
				System.out.println(">>>>>>" + attrs.get("SecurityAnswers"));
				usrRecord.setSecAnswer(attrs.get("SecurityAnswers")!=null?attrs.get("SecurityAnswers").toString():"");
				logger.info(usrRecord.toString());
				listUserRecord.add(usrRecord);

				System.out.println(usrRecord.getSecQuestion());
				System.out.println(usrRecord.getSecAnswer());
				usrRecord=null;
				}else{
					memberIDNotFoundCount++;
					System.out.println("SecurityQuestions is not found for user ::"+sr.getName());
					logger.info("SecurityQuestions is not found for user ::"+sr.getName());
				}
			}
			
			System.out.println("Total user without SecurityQuestions::"+memberIDNotFoundCount);
			logger.info("Total user without SecurityQuestions::"+memberIDNotFoundCount);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			logger.error("Error: " + e);
			e.printStackTrace();
		} finally {
			try {
				ldapContext.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: " + e);
				logger.error("Error: " + e);
				e.printStackTrace();
			}
		}

		System.out.println("Total results: " + listUserRecord.size());
		logger.info("Total results: " + listUserRecord.size());
		return listUserRecord;




	}


	private DirContext OIDConnection() {
		// TODO Auto-generated method stub

		DirContext ldapContext=null;
		try {

			logger.info("Utility Started Execution......");
			System.out.println("Utility Started Execution......");


			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			//ldapEnv.put(Context.PROVIDER_URL,  "ldap://societe.fr:389");
			ldapEnv.put(Context.PROVIDER_URL,  "ldap://"+OID_HOST+":"+OID_PORT);
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL, OID_USERNAME);
			ldapEnv.put(Context.SECURITY_CREDENTIALS, OID_PASSWORD);
			//ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
			//ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
			ldapContext  = new InitialDirContext(ldapEnv);

			logger.info("Connection details ......"+OID_HOST+":"+OID_PORT+":"+OID_USERNAME);
			System.out.println("Connection details ......"+OID_HOST+":"+OID_PORT+":"+OID_USERNAME);

			System.out.println("ldapContext"+ldapContext);
			logger.info("ldapContext"+ldapContext);


		}
		catch (Exception e) {
			System.out.println("Error: " + e);
			logger.error("Error: " + e);;
			e.printStackTrace();
		}
		return ldapContext;
	}

}
		    
