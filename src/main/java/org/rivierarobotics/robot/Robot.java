/*
 * This file is part of Swerve2019, licensed under the GNU General Public License (GPLv3).
 *
 * Copyright (c) Riviera Robotics <https://github.com/Team5818>
 * Copyright (c) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rivierarobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.rivierarobotics.driver.Controller;
import org.rivierarobotics.subsystems.DriveTrain;

public class Robot extends TimedRobot {
    public enum ControlMode {
        SWERVE, TANK, CRAB, AUTOMOBILE;
    }

    public static Robot runningRobot;
    public ControlMode currentControlMode;
    public DriveTrain driveTrain;
    public Controller controller;

    public Robot() {
        this.driveTrain = new DriveTrain();
        this.controller = new Controller();
        this.currentControlMode = ControlMode.SWERVE;
        runningRobot = this;
    }

    @Override
    public void robotInit() {
        driveTrain.resetGyro();
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        printShuffleboard();
    }

    @Override
    public void disabledPeriodic() {
        driveTrain.stop();
        printShuffleboard();
    }

    private void printShuffleboard() {
        SmartDashboard.putNumberArray("Drive Distances", runningRobot.driveTrain.getAllDistances());
        SmartDashboard.putNumberArray("Wheel Angles", runningRobot.driveTrain.getAllAngles());
        SmartDashboard.putNumberArray("Drive Powers", runningRobot.driveTrain.getAllPowers(true));
        SmartDashboard.putNumberArray("Steering Powers", runningRobot.driveTrain.getAllPowers(false));
        SmartDashboard.putNumberArray("Drive Encoder Ticks", runningRobot.driveTrain.getAllDistances());
        SmartDashboard.putNumberArray("Steering Encoder Ticks", runningRobot.driveTrain.getAllAngles());
    }
}
