// package frc.robot.utility.InfoTracker;

// import edu.wpi.first.wpilibj.TimedRobot;
// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;

// public class Logger extends TimedRobot {
// 	/**
// 	 *
// 	 */
    
//     Singleton singleton = Singleton.getInstance();

// 	File logf;
// 	BufferedWriter logbw;
// 	FileWriter logfw;

// 	public Logger(String fileName){
//         logf = new File(fileName);
// 		try {
//     		if(!logf.exists()){
//     			logf.createNewFile();
//     		}
// 			logfw = new FileWriter(logf);
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
// 		logbw = new BufferedWriter(logfw);
// 		try {
// 			logbw.write("leftDrivePos,rightDrivePos,leftDriveVel,rightDriveVel,gyroAngle,canPercent");
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
// 	}

// 	public void writeLog(){
// 		String logString = Double.toString(singleton.leftDrivePos)+','+Double.toString(singleton.rightDrivePos)+','+
// 			Double.toString(singleton.leftDriveVel)+','+Double.toString(singleton.rightDriveVel)+','+
// 			Double.toString(singleton.gyroAngle)+','+Double.toString(singleton.canPercent);
// 		try {
// 			logbw.newLine();
// 			logbw.write(logString);
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}

// 	}

// 	public void closeLog(){
// 		try {
// 			logbw.close();
// 			logfw.close();
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}

// 	}
    
// }