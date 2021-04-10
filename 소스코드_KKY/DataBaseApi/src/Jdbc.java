import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Jdbc {
	public static Object JdbcExcute(String type, String query) throws SQLException, MalformedURLException, IOException, ParseException {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		Connection conn = null;
		// JDBC를 이용해 PostgreSQL 서버 및 데이터베이스 연결
		
		String dbUrl = "jdbc:postgresql://127.0.0.1:5432/KKY";
		String username = "postgres";
		String password = "vlftjs2";
		try {
			conn = DriverManager.getConnection(dbUrl, username, password);
		} catch(SQLException e) {
			System.err.println("JDBC/connect err: "+e.getErrorCode()+e.getMessage());
			e.printStackTrace();
		}
		
		try(Statement stmt = conn.createStatement()){
			if(type.equals("insertUserInfo")){
				stmt.executeUpdate(query);

			}
			else if(type.equals("getUserInfo")){
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() == false) {
					rs.close();
					return new UserInfo(null,null,null,null);
				}
				else{
					System.out.format("%-10s : %-5s |%-30s |%-5s\n", "ID","이름","상세 주소","간단 주소");
					UserInfo userInfo = null;
					while (rs.next()) {
						String tmp = "userId";
						System.out.format("%-10s : %-5s |%-30s |%-5s\n", rs.getString("userId"),rs.getString("userName"),rs.getString("address"),rs.getString("targetAdd"));
						userInfo = new UserInfo(rs.getString("userId"),rs.getString("userName"),rs.getString("address"),rs.getString("targetAdd"));
					}
					rs.close();
					return userInfo;
				}

			}
			else if(type.equals("initUserInfo")){
				initUserInfo(stmt);
			}
			else if(type.equals("initWorking")){
				initWorking(stmt);
			}
			else if(type.equals("initPolicySearch")) {
				initPolicySearch(stmt);
			}
			else if(type.equals("initHospital")) {
				initHospital(stmt, conn);
			}
			else if(type.equals("initNursingCenter")) {
				initNursingCenter(stmt,conn);
			}
			else if(type.equals("initSeniorClass")) {
				initSeniorClass(stmt,conn);
			}
			else if(type.equals("initSeniorCenter")) {
				initSeniorCenter(stmt,conn);
			}
			else if(type.equals("updateWorking")) {
				stmt.execute(query);
			}
			else if(type.equals("getWorking")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getWorking: 일자리 정보를 정상적으로 불러왔습니다.");
					printWk(rs);
					rs.close();
					return 0;
				}
				else {
					System.out.println("JDBC/query error: 해당하는 시의 검색 결과가 존재하지 않습니다.");
					rs.close();
					return -1;
				}
			}
			else if(type.equals("bookmarkWorking")){
				stmt.execute(query);
				System.out.println("JDBC/query bookmarkWorking: 일자리 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("updatePolicySearch")){
				stmt.execute(query);
			}
			else if(type.equals("getPolicySearch")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getPolicySearch: 정책 정보를 정상적으로 불러왔습니다.");
					printPs(rs);
					rs.close();
					return 0;
				}
				else {
					System.out.println("JDBC/query error: 정책 검색 결과가 존재하지 않습니다.");
					rs.close();
					return "-1";
				}
			}
			else if(type.equals("bookmarkPolicySearch")){
				stmt.execute(query);
				System.out.println("JDBC/query PolicySearch: 정책 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("updateSeniorClass")) {
				stmt.execute(query);
			}
			else if(type.equals("getSeniorClass")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getSeniorClass: 노인 교실 정보를 정상적으로 불러왔습니다.");
					printSc(rs);
					rs.close();
					return 0;
				}
				else {
					System.out.println("JDBC/query error: 해당하는 시의 검색 결과가 존재하지 않습니다.");
					rs.close();
					return -1;
				}
			}
			else if(type.equals("bookmarkSeniorClass")){
				stmt.execute(query);
				System.out.println("JDBC/query bookmarkSeniorClass: 노인 교실 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("updateSeniorCenter")) {
				stmt.execute(query);
			}
			else if(type.equals("getSeniorCenter")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getSeniorCenter: 경로당 정보를 정상적으로 불러왔습니다.");
					printScen(rs);
					rs.close();
				    return 0;
				}
				else {
					System.out.println("JDBC/query error: 해당하는 시의 검색 결과가 존재하지 않습니다.");
				    rs.close();
					return -1;
				}
			}
			else if(type.equals("bookmarkSeniorCenter")){
				stmt.execute(query);
				System.out.println("JDBC/query bookmarkSeniorCenter: 경로당 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("updateHospital")) {
				stmt.execute(query);
			}
			else if(type.equals("getHospital")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getWorking: 요양 병원 정보를 정상적으로 불러왔습니다.");
					printHos(rs);
					rs.close();
				    return 0;
				}
				else {
					System.out.println("JDBC/query error: 해당하는 시의 검색 결과가 존재하지 않습니다.");
				    rs.close();
					return -1;
				}
			}
			else if(type.equals("bookmarkHospital")){
				stmt.execute(query);
				System.out.println("JDBC/query bookmarkHospital: 요양병원 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("updateNursingCenter")) {
				stmt.execute(query);
			}
			else if(type.contentEquals("getNursingCenter")) {
				ResultSet rs = stmt.executeQuery(query);
				if(rs.isBeforeFirst() != false ) {
					System.out.println("JDBC/query getNursingCenter: 양로원 정보를 정상적으로 불러왔습니다.");
					printNc(rs);
					rs.close();
				    return 0;
				}
				else {
					System.out.println("JDBC/query error: 해당하는 시의 검색 결과가 존재하지 않습니다.");
				    rs.close();
					return -1;
				}
			}
			else if(type.equals("bookmarkNursingCenter")){
				stmt.execute(query);
				System.out.println("JDBC/query bookmarkNursingCenter: 양로원 정보를 정상적으로 저장했습니다.");
			}
			else if(type.equals("getBookmark")){
				int[] bookmark = new int[6];
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()){
					bookmark[0] = rs.getInt("workId");
					bookmark[1] = rs.getInt("policyId");
					bookmark[2] = rs.getInt("sclId");
					bookmark[3] = rs.getInt("scenId");
					bookmark[4] = rs.getInt("hosId");
					bookmark[5] = rs.getInt("ncId");
				}
				rs.close();
				return bookmark;
			}
			else if(type.equals("printWk")){
				ResultSet rs = stmt.executeQuery(query);
				printWk(rs);
				rs.close();
			}
			else if(type.equals("printPs")){
				ResultSet rs = stmt.executeQuery(query);
				printPs(rs);
				rs.close();
			}
			else if(type.equals("printSc")){
				ResultSet rs = stmt.executeQuery(query);
				printSc(rs);
				rs.close();
			}
			else if(type.equals("printScen")){
				ResultSet rs = stmt.executeQuery(query);
				printScen(rs);
				rs.close();
			}
			else if(type.equals("printHos")){
				ResultSet rs = stmt.executeQuery(query);
				printHos(rs);
				rs.close();
			}
			else if(type.equals("printNc")){
				ResultSet rs = stmt.executeQuery(query);
				printNc(rs);
				rs.close();
			}


		} catch (Exception e) {
			System.err.println("JDBC/query err: "+e.getMessage());
			e.printStackTrace();
        }

		//해제
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
			System.err.println("JDBC/discon err: "+e.getMessage());
			e.printStackTrace();
        }
		
		return "";
	}

	private static void printNc(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 양로원 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.format("%4s : |%.30s |%.30s |%.20s |%.5s |%.5s |%.5s \n", "ID","이름","주소","전화","정원", "현재 인원","종사자");
		while (rs.next()) {
			System.out.format(
					"%4s : |%.30s |%.30s |%.20s |%.5s |%.5s |%.5s  \n",
					rs.getString("ncID"),
							rs.getString("ncName"),
							rs.getString("address"),
							rs.getString("tel"),
							rs.getInt("peopleLim"),
							rs.getInt("people"),
							rs.getString("assist")

			);
		}
	}

	private static void printHos(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 요양병원 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.format("%4s : |%.30s |%.30s |%.4s |%.4s |%.20s \n", "ID","이름","주소","정원", "현재 인원", "전화");
		while (rs.next()) {
			System.out.format(
					"%4s : |%.30s |%.30s |%.4s |%.4s |%.20s \n",
					rs.getString("hosID"),
							rs.getString("hosName"),
							rs.getString("address"),
							rs.getInt("peopleLim"),
							rs.getInt("people"),
							rs.getString("tel")

			);
		}
	}

	private static void printScen(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 경로당 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.format("%4s : |%.30s |%.30s |%.5s |%.20s \n", "ID","이름","주소", "현재 인원", "전화");
		while (rs.next()) {
			System.out.format(
					"%4s : |%.30s |%.30s |%.5s |%.20s \n",
					rs.getString("scenID"),
							rs.getString("scenName"),
							rs.getString("address"),
							rs.getString("population"),
							rs.getString("tel")
			);
		}
	}

	private static void printSc(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 노인 교실 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.format("%4s : |%.30s |%.30s |%.4s |%.4s |%.5s \n", "ID","이름","주소","상태","정원", "현재 인원");
		while (rs.next()) {
			System.out.format(
					"%4s : |%.30s |%.30s |%.4s |%.4s |%.5s \n",
					rs.getString("sclID"),
							rs.getString("scName"),
							rs.getString("address"),
							rs.getString("status"),
							rs.getInt("peopleLim"),
							rs.getInt("people")
			);
		}
	}

	private static void printPs(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 정책 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.format("%4s : |%.50s |%.20s |%.100s \n", "ID","정책 이름","대상","시행 주체","상세 내용");
		while (rs.next()) {
			System.out.format(
					"%4s : |%.50s |%.20s |%.100s \n",
					rs.getString("policyID"),
							rs.getString("policyName"),
							rs.getString("target"),
							rs.getString("policywho"),
							rs.getString("contents")
			);
		}
	}

	private static void printWk(ResultSet rs) throws SQLException {
		System.out.println("\nJDBC/print: 일자리 출력 결과");
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.format("%4s : |%.15s |%.50s |%.5s |%.5s |%.10s |%.10s |%.5s \n", "ID","사업장 이름","상세 내용","정원","현재 인원","접수 시작", "접수 마감", "접수 상태");
		while (rs.next()) {

			System.out.format(
					"%4s : |%.15s |%.50s |%.5s |%.5s |%.10s |%.10s |%.5s \n",
					rs.getString("workID"),
							rs.getString("workname"),
							rs.getString("workinfo"),
							rs.getString("plannum"),
							rs.getString("curnum"),
							rs.getString("startdate"),
							rs.getString("enddate"),
							rs.getString("status")
			);
		}
	}

	public static void initUserInfo(Statement stmt) throws SQLException {
		System.out.println("JDBC/init: start init table 'UserInfo'");

		String existUserInfo = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'userinfo'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existUserInfo);
		if(rs.next()){
			if(!rs.getBoolean("exists")) {
				String createUserInfo = "create table userInfo(userid text, username text, address text, targetAdd text, primary key(userID));";
				stmt.execute(createUserInfo);
				//테이블 생성용 1개 추가
				String leisureMark = "create table leisure(userID text, sclID integer, scenID integer, primary key(userID), foreign key(userID) references userinfo(userID) on delete cascade, foreign key(sclID) references seniorClass(sclID), foreign key(scenID) references seniorCenter(scenID));";
				stmt.execute(leisureMark);
				String medicalMark = "create table medical(userID text, hosID integer, ncID integer, primary key(userID), foreign key(userID) references userinfo(userID) on delete cascade,foreign key(hosID) references hospital(hosID), foreign key(ncID) references nursingCenter(ncID));";
				stmt.execute(medicalMark);
				String economyMark = "create table economy(userID text, workID integer, policyID integer, primary key(userID),foreign key(userID) references userinfo(userID) on delete cascade, foreign key(workID) references working(workID), foreign key(policyID) references policysearch(policyID));";
				stmt.execute(economyMark);

				rs.close();
			}
		}
		//trigger
		String existTrigger = "SELECT EXISTS (\n" +
				"       SELECT  tgenabled\n" +
				"       FROM    pg_trigger\n" +
				"       WHERE   tgname='bookmark' AND\n" +
				"               tgenabled != 'D'\n" +
				");";
		rs = stmt.executeQuery(existTrigger);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				String trigger = "create function bm() returns trigger as $bm$\n" +
						"begin\n" +
						"\tinsert into leisure values(new.userID,NULL,NULL);\n" +
						"\tinsert into medical values(new.userID,NULL,NULL);\n" +
						"\tinsert into economy values(new.userID,NULL,NULL);\n" +
						"\treturn new;\n" +
						"\t\n" +
						"end;\n" +
						"$bm$LANGUAGE plpgsql;\n" +
						"\n" +
						"create trigger bookmark\n" +
						"after insert on userinfo \n" +
						"for each row\n" +
						"execute procedure bm();\n";
				stmt.execute(trigger);
				rs.close();
			}
		}
		System.out.println("JDBC/init: finish init table 'UserInfo'");
	}

	public static void initPolicySearch(Statement stmt) throws SQLException, FileNotFoundException, IOException {
		System.out.println("JDBC/init: start init table 'policySearch'");

		String existPolicySearch = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'policysearch'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existPolicySearch);
		if(rs.next()){
			if(!rs.getBoolean("exists")) {
				String sequencePolicySearch = "create sequence policyId";
				stmt.execute(sequencePolicySearch);
				String createPolicySearch = "create table policysearch(policyID integer, policyName text, target text, policywho text, contents text, primary key(policyID));";
				stmt.execute(createPolicySearch);
				String alter = "alter table policysearch alter column policyid set default nextval('policyid');";
				stmt.execute(alter);
				//csv 읽기
				BufferedReader br = null;
				br = new BufferedReader(new FileReader("C:\\Users\\kps\\Projects\\DataBaseApi\\src\\policysearch.csv"));
				String workingData = null;
				while ((workingData = br.readLine()) != null) {
					String[] words = workingData.trim().split(",");

					String policyName = words[0];
					String target = words[1];
					String policywho = words[2];
					String contents = words[3];


					String insertPolicySearch = "insert into policySearch values(nextval('policyID'),"
							+ "'" + policyName + "',"
							+ "'" + target + "',"
							+ "'" + policywho + "',"
							+ "'" + contents + "');";

					stmt.execute(insertPolicySearch);
				}
				br.close();
			}
		}
		System.out.println("JDBC/init: finish init table 'policySearch'");
	}


	public static void initWorking(Statement stmt) throws SQLException, FileNotFoundException, IOException {
		System.out.println("JDBC/init: start init table 'working'");

		String existWorking = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'working'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existWorking);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				{
					String sequenceWorking = "create sequence workId";
					stmt.execute(sequenceWorking);
					String createWorking = "create table working(workID integer, workName text, workInfo text, planNum int, curNum int, targetAdd text, startDate text, endDate text, status text, src text, primary key(workid));";
					stmt.execute(createWorking);
					String alter = "alter table Working alter column workID set default nextval('workID');";
					stmt.execute(alter);

					//csv 읽기
					BufferedReader br = null;
					br = new BufferedReader(new FileReader("C:\\Users\\kps\\Projects\\DataBaseApi\\src\\working.csv"));
					String workingData = null;
					while ((workingData = br.readLine()) != null) {
						String[] words = workingData.trim().split(",");

						String workName = words[0];
						String workInfo = words[1];
						int planNum = Integer.parseInt(words[2]);
						int curNum = Integer.parseInt(words[3]);
						String targetAdd = words[4];
						String strarDate = words[5];
						String endDate = words[6];
						String status = words[7];
						String src = words[8];

						String insertWorking = "insert into working values(nextval('workid'),"
								+ "'" + workName + "',"
								+ "'" + workInfo + "',"
								+ planNum + ","
								+ curNum + ","
								+ "'" + targetAdd + "',"
								+ "'" + strarDate + "',"
								+ "'" + endDate + "',"
								+ "'" + status + "',"
								+ "'" + src + "');";

						stmt.execute(insertWorking);
					}
					br.close();
				}
			}
		}
		System.out.println("JDBC/init: finish init table 'working'");
	}
	

	public static void initNursingCenter(Statement stmt, Connection conn) throws IOException, ParseException, SQLException {
		System.out.println("JDBC/init: start init table 'NursingCenter'");

		String existNursingCenter = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'nursingcenter'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existNursingCenter);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				{
					String sequenceNursingCenter = "create sequence ncID";
					stmt.execute(sequenceNursingCenter);
					String createNursingCenter = "create table nursingcenter(ncID integer, ncName text, address text, tel text, peoplelim int, people int, assist int, targetAdd text, primary key(ncID));";
					stmt.execute(createNursingCenter);
					String alter = "alter table NursingCenter alter column ncID set default nextval('ncID');";
					stmt.execute(alter);
					BufferedReader br = null;

					URL url = new URL(UrlNursingCenter.getUrl());
					URLConnection urlConn = url.openConnection();
					br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = (JSONObject) jsonParser.parse(br);
					JSONArray jsonArr = (JSONArray) jsonObj.get("OldpsnResideWelfaclt");
					JSONObject jsonObjRow = (JSONObject) jsonArr.get(1);
					JSONArray jsonArrRow = (JSONArray) jsonObjRow.get("row");

					String insertHospital = null;
					for (int i = 0; i < jsonArrRow.size(); i++) {
						JSONObject tempObj = (JSONObject) jsonArrRow.get(i);
						insertHospital = "insert into NursingCenter values("
								+ "nextval('ncid'),'"
								+ tempObj.get("FACLT_NM") + "','"
								+ tempObj.get("REFINE_LOTNO_ADDR") + "','"
								+ tempObj.get("DETAIL_TELNO") + "',"
								+ tempObj.get("ENTRNC_PSN_CAPA") + ","
								+ tempObj.get("ENTRNC_PSTPSN_SUM") + ","
								+ tempObj.get("ENFLPSN_PSTPSN_SUM") + ",'"
								+ tempObj.get("SIGUN_NM") + "');";
						//System.out.println("Init: "+pstmt.toString());
						stmt.execute(insertHospital);
					}
					br.close();
				}
			}
		}
		System.out.println("JDBC/init: finish init table 'NursingCenter'");
	}
	
	public static void initHospital(Statement stmt, Connection conn) throws MalformedURLException, IOException, ParseException, SQLException {
		System.out.println("JDBC/init: start init table 'hospital'");

		String existHospital = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'hospital'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existHospital);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				{
					String sequenceHospital = "create sequence hosID";
					stmt.execute(sequenceHospital);
					String createHospital = "create table Hospital(hosID int, hosName text, address text, peopleLim int, people int, tel text,  targetAdd text, primary key(hosID));";
					stmt.execute(createHospital);
					String alter = "alter table Hospital alter column hosID set default nextval('hosID');";
					stmt.execute(alter);

					BufferedReader br = null;

					for (int pageCnt = 1; pageCnt <= 2; pageCnt++) {
						URL url = new URL(UrlHospital.getUrl(pageCnt));
						URLConnection urlConn = url.openConnection();
						br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(br);
						JSONArray jsonArr = (JSONArray) jsonObj.get("OldpsnMedcareWelfac");
						JSONObject jsonObjRow = (JSONObject) jsonArr.get(1);
						JSONArray jsonArrRow = (JSONArray) jsonObjRow.get("row");

						String insertHospital = null;
						for (int i = 0; i < jsonArrRow.size(); i++) {
							JSONObject tempObj = (JSONObject) jsonArrRow.get(i);
							insertHospital = "insert into Hospital values("
									+ "nextval('hosid'),'"
									+ tempObj.get("FACLT_NM") + "','"
									+ tempObj.get("REFINE_LOTNO_ADDR") + "',"
									+ tempObj.get("ENTRNC_PSN_CAPA") + ","
									+ tempObj.get("ENTRNC_PSTPSN_SUM") + ",'"
									+ tempObj.get("DETAIL_TELNO") + "','"
									+ tempObj.get("SIGUN_NM") + "');";
							//System.out.println("Init: "+pstmt.toString());
							stmt.execute(insertHospital);
						}
					}
					br.close();
				}
			}
		}
		System.out.println("JDBC/init: finish init table 'hospital'");
	}
	
	public static void initSeniorClass(Statement stmt, Connection conn) throws IOException, ParseException, SQLException {
		System.out.println("JDBC/init: start init table 'seniorClass'");

		String existSeniorClass = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'seniorclass'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existSeniorClass);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				{
					String sequenceSeniorClass = "create sequence sclID";
					stmt.execute(sequenceSeniorClass);
					String createSeniorClass = "create table seniorClass(sclID integer, scName text, address text, status text, peopleLim integer, people integer,  targetAdd text, primary key(sclID));";
					stmt.execute(createSeniorClass);
					String alter = "alter table SeniorClass alter column sclID set default nextval('sclID');";
					stmt.execute(alter);

					BufferedReader br = null;

					URL url = new URL(UrlSeniorClass.getUrl());
					URLConnection urlConn = url.openConnection();
					br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = (JSONObject) jsonParser.parse(br);
					JSONArray jsonArr = (JSONArray) jsonObj.get("OldPersonClassroom");
					JSONObject jsonObjRow = (JSONObject) jsonArr.get(1);
					JSONArray jsonArrRow = (JSONArray) jsonObjRow.get("row");

					String insertSeniorClass = null;
					for (int i = 0; i < jsonArrRow.size(); i++) {
						JSONObject tempObj = (JSONObject) jsonArrRow.get(i);
						insertSeniorClass = "insert into SeniorClass values("
								+ "nextval('sclid'),'"
								+ tempObj.get("BIZPLC_NM") + "','"
								+ tempObj.get("REFINE_LOTNO_ADDR") + "','"
								+ tempObj.get("BSN_STATE_NM") + "',"
								+ tempObj.get("ENTRNC_PSN_CAPA") + ","
								+ tempObj.get("TOT_PSN_CNT") + ",'"
								+ tempObj.get("SIGUN_NM") + "');";
						//System.out.println("Init: "+pstmt.toString());
						stmt.execute(insertSeniorClass);
					}
					br.close();
				}
			}
		}
		System.out.println("JDBC/init: finish init table 'seniorClass'");
	}

	public static void initSeniorCenter(Statement stmt, Connection conn) throws IOException, ParseException, SQLException {
		System.out.println("JDBC/init: start init table 'SeniorCenter'");

		String existSeniorCenter = "SELECT EXISTS (\n" +
				"   SELECT FROM pg_tables\n" +
				"   WHERE  schemaname = 'public'\n" +
				"   AND    tablename  = 'seniorcenter'\n" +
				"   );";
		ResultSet rs = stmt.executeQuery(existSeniorCenter);
		if(rs.next()) {
			if (!rs.getBoolean("exists")) {
				{
					String sequenceSeniorCenter = "create sequence scenID";
					stmt.execute(sequenceSeniorCenter);
					String createSeniorCenter = "create table seniorCenter(scenID integer,scenName text, address text, population integer,tel text, targetAdd text, primary key(scenID));";
					stmt.execute(createSeniorCenter);
					String alter = "alter table SeniorCenter alter column scenID set default nextval('scenID');";
					stmt.execute(alter);

					BufferedReader br = null;

					for (int pageCnt = 1; pageCnt <= 10; pageCnt++) {
						URL url = new URL(UrlSeniorCenter.getUrl(pageCnt));
						URLConnection urlConn = url.openConnection();
						br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(br);
						JSONArray jsonArr = (JSONArray) jsonObj.get("SenircentFaclt");
						JSONObject jsonObjRow = (JSONObject) jsonArr.get(1);
						JSONArray jsonArrRow = (JSONArray) jsonObjRow.get("row");

						String insertSeniorCenter = null;
						for (int i = 0; i < jsonArrRow.size(); i++) {
							JSONObject tempObj = (JSONObject) jsonArrRow.get(i);
							insertSeniorCenter = "insert into seniorCenter values("
									+ "nextval('scenid'),'"
									+ tempObj.get("FACLT_NM") + "','"
									+ tempObj.get("REFINE_LOTNO_ADDR") + "',"
									+ tempObj.get("USE_MBER_CNT") + ",'"
									+ tempObj.get("DETAIL_TELNO") + "','"
									+ tempObj.get("SIGUN_NM") + "');";
							//System.out.println("Init: "+pstmt.toString());
							stmt.execute(insertSeniorCenter);
						}
					}
					br.close();
				}
			}
		}
		System.out.println("JDBC/init: finish init table 'SeniorCenter'");
	}
}
