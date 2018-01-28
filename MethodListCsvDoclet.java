package customjavadoc;

import java.io.*;
import java.util.*;
import com.sun.javadoc.*;

public final class MethodListCsvDoclet extends Doclet{

	// ドックレット起動時のentry point。カスタムドックレットを作るときは必ずこのメソッドがentry pointになる。
	public static boolean start(RootDoc rootDoc) {
		
		File file = new File("doclet-methodlist.csv");

		try {
			PrintWriter out = new PrintWriter(file);
			try {
				// csvデータ作成
				writeTo(out, rootDoc);
				if (out.checkError()) {
					return false;
				}
			} finally {
				out.close();
			}

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return true;
	}

	// カスタムドックレットを作るときはこのメソッドとstartメソッドがあれば良い。
	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}

	// csvファイル作成処理
	private static void writeTo(PrintWriter out, RootDoc rootDoc) {

		// ヘッダー作成
		out.println(join(new String[]{
											"パッケージ",
											"完全修飾クラス名",
											"クラス修飾子",
											"クラス名", 
											"クラス概要",
											"メソッド修飾子", 
											"メソッド名", 
											"引数の型", 
											"戻り値の型", 
											"メソッド概要", 
											"引数(@param)", 
											"戻り値(@return)"
											}));

		// 一覧作成。クラス・メソッド情報を取り出す。
		for (ClassDoc classDoc : rootDoc.classes()) {
			for (MethodDoc methodDoc : classDoc.methods(true)) {
				String[] a = new String[13];
				Arrays.fill(a, "");
				int i = -1;

				//クラス関連情報出力
				a[++i] = classDoc.containingPackage().name();
				a[++i] = classDoc.qualifiedName();
				a[++i] = classDoc.modifiers();
				a[++i] = classDoc.name();
				a[++i] = getComment(classDoc.commentText());

				//メソッド関連情報出力
				a[++i] = methodDoc.modifiers();
				a[++i] = methodDoc.name();
				a[++i] = methodDoc.signature().replaceAll(",", "、"); // 引数が複数ある場合はカンマ区切りで返ってくるがcsvの区切りと見分けつかないから置き換える				
				a[++i] = String.valueOf(methodDoc.returnType());
				a[++i] = getComment(methodDoc.commentText());
				// tag情報
				a[++i] = getParamTag(methodDoc);
				a[++i] = getReturnTag(methodDoc);
				
				out.println(join(a));
			}
		}
	}
	
	
	
	private static String getComment(String doc) {

		StringBuilder s = new StringBuilder();
		
		if (doc == null || doc == "") {
			return "";
		}
		String[] parts = doc.split("[\r\n]");
		
		if(parts[0] ==null || parts[0] == ""){
			return "";
		}
		
		for(int h = 0; h < parts.length; h++){
			
			String part = "";
			
			part = parts[h].replaceAll(",", "、");

			part = part.trim();
			s.append(part);
			
		}
		return s.toString();
		
	}

	private static String getParamTag(MethodDoc m) {

		
		StringBuilder s = new StringBuilder();
		
		
		if (m == null || m.paramTags() == null) {
			return "";
		}
				
		ParamTag[] params = m.paramTags();
				
		if(params.length <1){
			
			return "";
		}
		
		
		for(int i = 0; i < params.length; i++) {
			
			String paramComment = "";
			
			paramComment =params[i].parameterComment().replaceAll("[\r\n]","").replaceAll(",",  "、");
			
			paramComment = paramComment.trim();
			
			if(i==0){
			
				if(paramComment != "") {
					s.append(params[i].parameterName()+ " - " + paramComment);
				} else {
					s.append(params[i].parameterName());
				}

			}else{
				
				if(paramComment != "") {
					s.append( "、").append(params[i].parameterName()+ " - " + paramComment);
				} else {
					s.append( "、").append(params[i].parameterName());
				}
			}
			
		}	
		
		return s.toString();
	}
	

	private static String getReturnTag(MethodDoc methodDoc) {
		
		StringBuilder s = new StringBuilder();
		
		if (methodDoc == null) {
			return "";
		}
		
		if(methodDoc.tags("return") == null){
			return "";
		}
		
		Tag[] returns = methodDoc.tags("return");
					
					for(int j = 0; j < returns.length; j++) {
						
						String returnComment = "";
						
						returnComment =returns[j].text().replaceAll("[\r\n]","").replaceAll(",",  "、");
						
						returnComment = returnComment.trim();
						
						if(j==0){
							s.append(returnComment);
						}else{
							
							s.append( "、").append(returnComment);
						}
					}

				return s.toString();
				
	}

	//カンマ区切りで結合
	private static <T> String join(T[] a) {

		if (a.length == 0) {
			return "";
		}
		
		StringBuilder s = new StringBuilder(String.valueOf(a[0]));
		for (int i = 1; i < a.length; i++) {
			s.append(",").append(a[i]);
		}
		return s.toString();
	}
	
}