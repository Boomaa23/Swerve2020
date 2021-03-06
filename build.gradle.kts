import com.techshroom.inciseblue.commonLib

plugins {
    id("org.rivierarobotics.gradlerioredux") version "0.7.10"
}

gradleRioRedux {
    robotClass = "org.rivierarobotics.robot.Robot"
    teamNumber = 5818
}

tasks.register("windowsLaunchSim") {
	doLast {
		project.exec{
			workingDir = file("./build/")
			commandLine("cmd", "/C", "start", "gradlerio_simulateJava.bat")
		}
	}
}
if (edu.wpi.first.toolchain.NativePlatforms.desktopOS() == "windows") {
	tasks.getByName("simulateJava").finalizedBy(tasks.getByName("windowsLaunchSim"))
}

dependencies {
	implementation("org.rivierarobotics.apparjacktus:apparjacktus:0.1.1")
	commonLib("net.octyl.apt-creator", "apt-creator", "0.1.4") {
		compileOnly(lib("annotations"))
		annotationProcessor(lib("processor"))
	}
	commonLib("com.google.dagger", "dagger", "2.25.4") {
		implementation(lib())
		annotationProcessor(lib("compiler"))
	}
}

repositories {
	jcenter()
}
