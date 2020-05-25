package app.captureEasy.Logger;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Resources.Library;


@Aspect
public class Logger extends Library{
	
	List<Long> startTime=new ArrayList<Long>();
	List<Long> startTimeKeyStroke=new ArrayList<Long>();
	List<Long> startTimeClear=new ArrayList<Long>();
	List<Long> startTimeUpdate=new ArrayList<Long>();

	

	
	@Before("execution(* app.captureEasy..*(..)) && !@annotation(app.captureEasy.Annotations.NoLogging)")
	public void logAllBeforeMethod(JoinPoint joinpoint)
	{
		logProcess("Process",getLogMessage(joinpoint,startTime));
		String annotationMarker=Arrays.toString(getAnnotations(joinpoint));
		if(annotationMarker.contains("KeyStroke"))
		{
			logProcess("Process_Keypress",getLogMessage(joinpoint,startTimeKeyStroke));
		}
		
		else if(annotationMarker.contains("Update"))
		{
			logProcess("Process_SoftwareUpdate","%m%n%n",getLogMessage(joinpoint,startTimeUpdate));
		}
	}
	@AfterReturning(pointcut="execution(* app.captureEasy..*(..)) && !@annotation(app.captureEasy.Annotations.NoLogging)",returning="result")
	public void logAllAfterMethod(JoinPoint joinpoint,Object result)
	{
		logProcess("Process",getLogMessage(joinpoint,startTime,result));
		String annotationMarker=Arrays.toString(getAnnotations(joinpoint));
		if(annotationMarker.contains("KeyStroke"))
		{
			logProcess("Process_Keypress",getLogMessage(joinpoint,startTimeKeyStroke,result));
		}
		else if(annotationMarker.contains("Update"))
		{
			logProcess("Process_Update",getLogMessage(joinpoint,startTimeUpdate,result));
		}
	}	
	
	
	
	
	@NoLogging
	public Annotation[] getAnnotations(JoinPoint joinpoint) 
	{
		return MethodSignature.class.cast(joinpoint.getSignature()).getMethod().getAnnotations();
	}
	@NoLogging
	public String getLogMessage(JoinPoint joinpoint,List<Long> startTime)
	{
		return getLogMessage(joinpoint,startTime,"");
	}
	@NoLogging
	public String getLogMessage(JoinPoint joinpoint,List<Long> startTime,Object result)
	{
		String depthTab="",ret;
		MethodSignature sign=((MethodSignature) joinpoint.getSignature());
		sign.getMethod().getParameterAnnotations();
		String returnType=(sign.getReturnType()).getSimpleName();
		Class<?>[] parTypes=sign.getParameterTypes();
		String[] paraName=sign.getParameterNames();
		String parameters="(";	
		for(int i=0;i<parTypes.length;i++)
		{
			parameters=parameters+parTypes[i].getSimpleName()+" "+paraName[i];
			if(!(i==parTypes.length-1))
				parameters=parameters+",";
		}
		parameters=parameters+")";
		if("".equals(result))
		{
			for(int i=0;i<startTime.size();i++)
				depthTab=depthTab+"\t ";
			ret= depthTab+returnType+" "+joinpoint.getSignature().getName()+parameters+" Method from "+joinpoint.getSourceLocation()+" called with "+Arrays.toString(joinpoint.getArgs())+" at "+new Timestamp(new Date().getTime());
			startTime.add(System.currentTimeMillis());
		}
		else if(returnType.equalsIgnoreCase("void"))
		{
			for(int i=0;i<startTime.size()-1;i++)
				depthTab=depthTab+"\t ";
			ret= depthTab+returnType+" " +joinpoint.getSignature().getName()+parameters+" Method successfully executed within "+(System.currentTimeMillis()-startTime.get(startTime.size()-1))+" milisecond.";
			startTime.remove(startTime.size()-1);
		}
		else 
		{
			for(int i=0;i<startTime.size()-1;i++)
				depthTab=depthTab+"\t ";
			ret= depthTab+""+returnType+" " +joinpoint.getSignature().getName()+parameters+" Method successfully executed within "+(System.currentTimeMillis()-startTime.get(startTime.size()-1))+" milisecond and returned  "+result+".";
			startTime.remove(startTime.size()-1);
		}
		return ret;
		
	}

}
