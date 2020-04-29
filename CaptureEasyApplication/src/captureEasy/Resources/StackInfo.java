package captureEasy.Resources;

import java.io.File;
import java.util.ArrayList;

public class StackInfo
	{
	 String sourceProjectFolderPath=System.getProperty("user.dir")+"src";
		private String[] trace=new String[4];
		private int c;
		ArrayList<StackTraceElement> list=new ArrayList<StackTraceElement>();
		public StackInfo(StackTraceElement[] currentStackTrace)
		{
			for(int i=0;i<currentStackTrace.length;i++)
			{
				String folderPath=sourceProjectFolderPath+"/"+currentStackTrace[i].getClassName().substring(0, currentStackTrace[i].toString().indexOf("."));
				String filePath=sourceProjectFolderPath+"/"+currentStackTrace[i].getClassName().substring(0, currentStackTrace[i].toString().indexOf("."))+".java";
				if(c<4 && (new File(folderPath).exists() || new File(filePath).exists()))
				{
					trace[c++]=currentStackTrace[i].getClassName();
					trace[c++]=currentStackTrace[i].getMethodName();
				}
				list.add(currentStackTrace[i]);
			}
		}
		public String getCallSequence()
		{
			String str="CallSequence: ";
			for(int i=list.size()-1;i>=0;i--)
			{
				StackTraceElement s=list.get(i);
				String classname=s.getClassName();
				str=str +classname.substring(classname.lastIndexOf(".")+1, classname.length())+ "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")";
				if(i!=0)
				{
					str=str+"\n-> ";
				}
			}
			return str;
		}
		
		
		public String getCurrentClassName()
		{
			return trace[0];
		}
		public String getCurrentMethodName()
		{
			return trace[1];
		}
		public String getImmediateCallerClassName()
		{
			return trace[2];
		}

		public String getImmediateCallerMethodName()
		{
			return trace[3];
		}
	}