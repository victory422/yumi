package com.yumikorea.common.utils;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SysTemInfoUtils {

	OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	
	public double getDiskSpace() {
		double totalSize = 0;
		double useableSize = 0;

		File[] drives = File.listRoots();

		// Sparrow 취약점 조치 :: null일 가능성이 있는 필드 체크
		if( drives != null ) {
			for(File drive : drives) {
				totalSize += drive.getTotalSpace() / Math.pow(1024, 3);
				useableSize += drive.getUsableSpace() / Math.pow(1024, 3);
			}
			
		} else {
			return (double) 0;
		}
		
		// Sparrow 취약점 조치 :: 모수가 0일 가능성이 있는 경우 처리
		if( totalSize == 0 ) totalSize = 1;
		double diskesUage = (double) ( (totalSize - useableSize) / totalSize * 100);
		

		return mathRound(diskesUage);
	}
	
	public double getCPUProcess() throws InterruptedException {
		
		double cpuUsage = (double)osBean.getSystemCpuLoad() * 100;
		
		while (cpuUsage <= 0.0) {
			cpuUsage = (double)osBean.getSystemCpuLoad() * 100;
		}
		
		return mathRound(cpuUsage);
	}
	
	
	public double getMemory() {
		long free = osBean.getFreePhysicalMemorySize();
		long total = osBean.getTotalPhysicalMemorySize();
		
		double memUsage = (double)((double)free / (double)total)  * 100L;
		return mathRound(memUsage);
	}

//	public void main(String[] args) throws InterruptedException {
//		System.out.println("Disk : " + getDiskSpace());
//		System.out.println("CPU : " + getCPUProcess());
//		System.out.println("Mem : " + getMemory());
//	}
	
	private double mathRound(double d) {
		
		return Math.round(d * 100) / 100.0;
		
	}
	
}
