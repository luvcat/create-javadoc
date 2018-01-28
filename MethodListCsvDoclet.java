package customjavadoc;

import java.io.*;
import java.util.*;
import com.sun.javadoc.*;

public final class MethodListCsvDoclet extends Doclet{

	// �h�b�N���b�g�N������entry point�B�J�X�^���h�b�N���b�g�����Ƃ��͕K�����̃��\�b�h��entry point�ɂȂ�B
	public static boolean start(RootDoc rootDoc) {
		
		File file = new File("doclet-methodlist.csv");

		try {
			PrintWriter out = new PrintWriter(file);
			try {
				// csv�f�[�^�쐬
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

	// �J�X�^���h�b�N���b�g�����Ƃ��͂��̃��\�b�h��start���\�b�h������Ηǂ��B
	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}

	// csv�t�@�C���쐬����
	private static void writeTo(PrintWriter out, RootDoc rootDoc) {

		// �w�b�_�[�쐬
		out.println(join(new String[]{
											"�p�b�P�[�W",
											"���S�C���N���X��",
											"�N���X�C���q",
											"�N���X��", 
											"�N���X�T�v",
											"���\�b�h�C���q", 
											"���\�b�h��", 
											"�����̌^", 
											"�߂�l�̌^", 
											"���\�b�h�T�v", 
											"����(@param)", 
											"�߂�l(@return)"
											}));

		// �ꗗ�쐬�B�N���X�E���\�b�h�������o���B
		for (ClassDoc classDoc : rootDoc.classes()) {
			for (MethodDoc methodDoc : classDoc.methods(true)) {
				String[] a = new String[13];
				Arrays.fill(a, "");
				int i = -1;

				//�N���X�֘A���o��
				a[++i] = classDoc.containingPackage().name();
				a[++i] = classDoc.qualifiedName();
				a[++i] = classDoc.modifiers();
				a[++i] = classDoc.name();
				a[++i] = getComment(classDoc.commentText());

				//���\�b�h�֘A���o��
				a[++i] = methodDoc.modifiers();
				a[++i] = methodDoc.name();
				a[++i] = methodDoc.signature().replaceAll(",", "�A"); // ��������������ꍇ�̓J���}��؂�ŕԂ��Ă��邪csv�̋�؂�ƌ��������Ȃ�����u��������				
				a[++i] = String.valueOf(methodDoc.returnType());
				a[++i] = getComment(methodDoc.commentText());
				// tag���
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
			
			part = parts[h].replaceAll(",", "�A");

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
			
			paramComment =params[i].parameterComment().replaceAll("[\r\n]","").replaceAll(",",  "�A");
			
			paramComment = paramComment.trim();
			
			if(i==0){
			
				if(paramComment != "") {
					s.append(params[i].parameterName()+ " - " + paramComment);
				} else {
					s.append(params[i].parameterName());
				}

			}else{
				
				if(paramComment != "") {
					s.append( "�A").append(params[i].parameterName()+ " - " + paramComment);
				} else {
					s.append( "�A").append(params[i].parameterName());
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
						
						returnComment =returns[j].text().replaceAll("[\r\n]","").replaceAll(",",  "�A");
						
						returnComment = returnComment.trim();
						
						if(j==0){
							s.append(returnComment);
						}else{
							
							s.append( "�A").append(returnComment);
						}
					}

				return s.toString();
				
	}

	//�J���}��؂�Ō���
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