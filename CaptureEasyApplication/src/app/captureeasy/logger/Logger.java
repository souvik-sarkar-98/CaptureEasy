package app.captureeasy.logger;

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

import app.captureeasy.annotations.NoLogging;
import app.captureeasy.resources.Library;


@Aspect
public class Logger extends Library{
	
	List<Long> startTime=new ArrayList<Long>();
	List<Long> startTimeKeyStroke=new ArrayList<Long>();
	List<Long> startTimeClear=new ArrayList<Long>();
	List<Long> startTimeUpdate=new ArrayList<Long>();

	

	
	@Before("execution(* app.captureEasy..*(..)) && !@annotation(app.captureeasy.annotations.NoLogging)")
	public void logAllBeforeMethod(JoinPoint joinpoint)
	{
		String annotationMarker=Arrays.toString(getAnnotations(joinpoint));
		if(annotationMarker.contains("KeyStroke")){}
		else if(annotationMarker.contains("Update"))
		{
			logProcess("Process_SoftwareUpdate",getLogMessage(joinpoint,startTimeUpdate));
		}
		else
		{
			logProcess("Process",getLogMessage(joinpoint,startTime));
		}
	}
	@AfterReturning(pointcut="execution(* app.captureEasy..*(..)) && !@annotation(app.captureeasy.annotations.NoLogging)",returning="result")
	public void logAllAfterMethod(JoinPoint joinpoint,Object result)
	{
		String annotationMarker=Arrays.toString(getAnnotations(joinpoint));
		if(annotationMarker.contains("KeyStroke")){}
		else if(annotationMarker.contains("Update"))
		{
			logProcess("Process_SoftwareUpdate",getLogMessage(joinpoint,startTimeUpdate,result));
		}
		else
		{
			logProcess("Process",getLogMessage(joinpoint,startTime,result));
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
