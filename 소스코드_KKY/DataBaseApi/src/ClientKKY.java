import java.util.Scanner;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class ClientKKY {
	public static UserInfo userInfo;
	public static int[] bookmark = new int[6]; //일자리, 정책, 노인 교실, 경로당, 요양병원, 양로원
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws MalformedURLException, SQLException, IOException, ParseException {
		System.out.println("---------------");
		System.out.println("노인을 위한 나라는 있다");
		System.out.println("---------------\n\n");

		//TODO DB 초기화
		initDatabase();

		while(true){
			if (initUserInfo()) break;
		}

	}

	private static void initDatabase() throws SQLException, IOException, ParseException {
		Jdbc.JdbcExcute("initWorking","initWorking");
		Jdbc.JdbcExcute("initPolicySearch","initPolicySearch");
		Jdbc.JdbcExcute("initHospital", "initHospital");
		Jdbc.JdbcExcute("initNursingCenter", "initNursingCenter");
		Jdbc.JdbcExcute("initSeniorClass", "initSeniorClass");
		Jdbc.JdbcExcute("initSeniorCenter", "initSeniorCenter");
		Jdbc.JdbcExcute("initUserInfo","initUserInfo");
	}

	private static boolean initUserInfo() throws SQLException, IOException, ParseException {
		int input = 0;
		System.out.println("\n안녕하세요. 사용자 정보를 새로 등록하거나, 기존에 등록한 정보를 불러와주세요.");
		System.out.println("1. 새로운 사용자 정보를 등록합니다.");
		System.out.print("2. 등록한 사용자 정보를 불러옵니다.\n>");
		input = Integer.parseInt(scan.nextLine());
		switch (input) {
			case 1:
				System.out.println("정보를 입력해주세요.");
				System.out.print("ID(불러올 때 사용) : ");
				String userId = scan.nextLine();

				System.out.print("이름 : ");
				String userName = scan.nextLine();

				System.out.print("시 혹은 군 이름 (예시 - 수원시, 포천군 등): ");
				String targetAdd = scan.nextLine();

				System.out.print("상세 주소: ");
				String address = scan.nextLine();

				String insertUserInfo = "Insert into userInfo values"+"('"+userId+"','"+userName+"','"+address+"','"+targetAdd+"');";
				Jdbc.JdbcExcute("insertUserInfo",insertUserInfo);
				userInfo = new UserInfo(userId,userName,address,targetAdd);
				System.out.println("\nJDBC/query insertUserInfo: 사용자 정보를 정상적으로 저장했습니다.");
				System.out.format("%-10s : %-5s |%-30s |%-5s\n", "ID","이름","상세 주소","간단 주소");
				System.out.format("%-10s : %-5s |%-30s |%-5s\n", userId,userName,address,targetAdd);
				selectMenu();
				break;
			case 2:
				System.out.print("ID를 입력해주세요.\n>");
				userId = scan.nextLine();

				String getUserInfo = "select * from userInfo where userinfo.userId = '"+userId+"';";
				UserInfo tmpUserInfo = (UserInfo) Jdbc.JdbcExcute("getUserInfo",getUserInfo);
				System.out.println(tmpUserInfo.getUserName());
				if(tmpUserInfo.getUserID() == null){
					System.out.println("아이디가 존재하지 않습니다. 아이디를 새로 생성하거나, 다시 입력해주세요.");
				}
				else{
					System.out.println("\nJDBC/query getUserInfo: 사용자 정보를 정상적으로 로드했습니다.");
					userInfo = new UserInfo(tmpUserInfo.getUserID(),tmpUserInfo.getUserName(),tmpUserInfo.getAddress(),tmpUserInfo.getTargetAdd());
					selectMenu();
				}
				break;
			default:
				break;

		}
		if(userInfo != null)
			return true;

		return false;
	}

	private static void selectMenu() throws SQLException, IOException, ParseException {
		int menuInput = 0;
		while (menuInput != 6) {
			System.out.println("\n기능 선택\n");
			System.out.println("1. 경제");
			System.out.println("2. 여가");
			System.out.println("3. 의료");
			System.out.println("4. 아이디 선택");
			System.out.println("5. 즐겨찾기");
			System.out.println("6. 종료");

			System.out.print("> ");
			menuInput = Integer.parseInt(scan.nextLine());

			switch (menuInput) {
			case 1:
				selectEco();
				break;

			case 2:
				selectLeis();
				break;

			case 3:
				selectMedi();
				break;
			case 4:
				initUserInfo();
				break;
			case 5:
				getBookmark();
				break;
			case 6:
				System.out.println("프로그램을 종료합니다.");

				break;
			default:
				break;
			}
		}
	}

	public static void getBookmark() throws ParseException, SQLException, IOException {
		System.out.println("\nJDBC/query getBookmark: 즐겨찾기를 정상적으로 로드했습니다.");
		String getBookmark = "select medical.hosid, medical.ncid, leisure.sclid, leisure.scenid, economy.workid, economy.policyid\n" +
				"from medical, leisure, economy\n" +
				"where medical.userid = '"+userInfo.getUserID()+"' and leisure.userid = '"+userInfo.getUserID()+"' and economy.userid = '"+userInfo.getUserID()+"';";
		bookmark = (int[]) Jdbc.JdbcExcute("getBookmark",getBookmark);
		String printWk = "select * from economy, working where economy.workid = working.workid and economy.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printWk",printWk);
		String printPs = "select * from economy, policySearch where economy.policyid = policySearch.policyid and economy.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printPs",printPs);
		String printSc = "select * from leisure, seniorclass where leisure.sclid = seniorclass.sclid and leisure.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printSc",printSc);
		String printScen = "select * from leisure, seniorcenter where leisure.scenid = seniorcenter.scenid and leisure.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printScen",printScen);
		String printHos = "select * from medical, hospital where medical.hosid = hospital.hosid and medical.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printHos",printHos);
		String printNc = "select * from medical, nursingcenter where medical.ncid = nursingcenter.ncid and medical.userid = '"+userInfo.getUserID()+"';";
		Jdbc.JdbcExcute("printNc",printNc);
	}

	public static void selectEco() throws MalformedURLException, SQLException, IOException, ParseException {

			int num;

			System.out.println("\n1. 일자리");
			System.out.println("2. 복지 정책");
			
			System.out.print("> ");

			Scanner scan = new Scanner(System.in);

			num = Integer.parseInt(scan.nextLine());

			switch (num) {
			case 1:
                String updateWorking = "create or replace view workingview as " +
						"select * from working where working.targetadd = '"+userInfo.getTargetAdd()+"';";
				String getWorking = "select * from workingview";

                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updateWorking",updateWorking);
                int wkrt = (int) Jdbc.JdbcExcute("getWorking", getWorking);

                if(wkrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 일자리: " + num);
						bookmark[0] = num;

						String bookmarkWorking = "update economy set workId = "+num+" where economy.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkWorking",bookmarkWorking);
					}
				}

				break;

			case 2:
                String updatePolicySearch = "create or replace view policysearchview as select * from policysearch;";
                String getPolicySearch = "select * from policysearchview;";
                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updatePolicySearch", updatePolicySearch);
                int psrt = (int) Jdbc.JdbcExcute("getPolicySearch",getPolicySearch);
				if(psrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 정책: " + num);
						bookmark[1] = num;

						String bookmarkPolicySearch = "update economy set policyid = "+num+" where economy.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkPolicySearch",bookmarkPolicySearch);
					}
				}
				break;

			default:
				break;
			}
		}

		public static void selectLeis() throws MalformedURLException, SQLException, IOException, ParseException {

			int num;

			System.out.println("1. 노인 교실");
			System.out.println("2. 경로당");
			System.out.print("> ");

			Scanner scan = new Scanner(System.in);

			num = Integer.parseInt(scan.nextLine());

			switch (num) {
			case 1:
                String updateSeniorClass = "create or replace view SeniorClassview as select * from SeniorClass where SeniorClass.address like '%"+userInfo.getTargetAdd()+"%';";
				String getSeniorClass =	"select * from SeniorClassview";
                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updateSeniorClass", updateSeniorClass);
                int lsrt = (int) Jdbc.JdbcExcute("getSeniorClass", getSeniorClass);
				if(lsrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 노인 교실: " + num);
						bookmark[2] = num;

						String bookmarkSeniorClass = "update leisure set sclid = "+num+" where leisure.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkSeniorClass",bookmarkSeniorClass);
					}
				}
				break;
			case 2:
                String updateSeniorCenter = "create or replace view SeniorCenterview as select * from SeniorCenter where SeniorCenter.address like '%"+userInfo.getTargetAdd()+"%';";
				String getSeniorCenter = "select * from SeniorCenterview;";
                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updateSeniorCenter", updateSeniorCenter);
                int scrt = (int) Jdbc.JdbcExcute("getSeniorCenter", getSeniorCenter);

				if(scrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 경로당: " + num);
						bookmark[3] = num;

						String bookmarkSeniorCenter = "update leisure set scenid = "+num+" where leisure.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkSeniorCenter",bookmarkSeniorCenter);
					}
				}
				break;
			default:
				break;
			}
		}

		public static void selectMedi() throws MalformedURLException, SQLException, IOException, ParseException {
			int num;

			System.out.println("\n1. 병원");
			System.out.println("2. 요양원");

			System.out.print("> ");
			Scanner scan = new Scanner(System.in);

			num = Integer.parseInt(scan.nextLine());

			switch (num) {
			case 1:
                String updateHospital = "create or replace view hospitalview as select * from hospital where hospital.address like '%"+userInfo.getTargetAdd()+"%'";
                String getHospital = "select * from hospitalview;";
                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updateHospital",updateHospital);
                int hsrt = (int)Jdbc.JdbcExcute("getHospital", getHospital);

				if(hsrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 요양병원: " + num);
						bookmark[4] = num;

						String bookmarkHospital = "update medical set hosid = "+num+" where medical.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkSeniorCenter",bookmarkHospital);
					}
				}
				break;

			case 2:
                String updateNursingCenter = "create or replace view nursingcenterview as select * from nursingcenter where nursingcenter.address like '%"+userInfo.getTargetAdd()+"%';";
				String getNursingCenter = "select * from nursingcenterview;";
                System.out.println(userInfo.getTargetAdd());
                Jdbc.JdbcExcute("updateNursingCenter", updateNursingCenter);
				int ncrt = (int) Jdbc.JdbcExcute("getNursingCenter", getNursingCenter);

				if(ncrt == 0){
					System.out.println("\n즐겨찾기를 등록하시겠습니까?");
					System.out.println("1. 네");
					System.out.print("2. 아니오\n>");
					int bm = Integer.parseInt(scan.nextLine());
					if(bm == 1){
						System.out.println("즐겨찾기로 등록하려면 원하는 항목의 ID를 입력하세요.");
						System.out.print("ID 입력 :");
						num = Integer.parseInt(scan.nextLine());
						System.out.println("선택한 양로원: " + num);
						bookmark[5] = num;

						String bookmarkNursingCenter = "update medical set ncid = "+num+" where medical.userID = '"+userInfo.getUserID()+"';";
						Jdbc.JdbcExcute("bookmarkNursingCenter",bookmarkNursingCenter);
					}
				}
				break;

			default:
				break;
			}

	}

}
