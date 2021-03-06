/*
 * This file is part of Swerve2020, licensed under the GNU General Public License (GPLv3).
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

package org.rivierarobotics.util;

import org.rivierarobotics.driver.CompositeJoystick;
import org.rivierarobotics.subsystems.NavXGyro;

public class DriveCalculation {
    @FunctionalInterface
    public interface DriveCalculator {
        MotorMapped<ControlDirective> calculate(CompositeJoystick composite, NavXGyro gyro, MotorGroup... groups);
    }

    public static class Swerve {
        private static double A = 0;
        private static double B = 0;
        private static double C = 0;
        private static double D = 0;

        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, NavXGyro gyro, MotorGroup... groups) -> {
            control(composite, gyro.getWrappedAngle());
            MotorMapped<ControlDirective> calcs = new MotorMapped<>();
            double max = -1;
            for (MotorGroup group : groups) {
                double[] calcIndexes = getCalcIndexes(group);
                double speed = Math.sqrt((calcIndexes[0] * calcIndexes[0]) + (calcIndexes[1] * calcIndexes[1]));
                double angle = MathUtil.wrapToCircle(Math.toDegrees(Math.atan2(calcIndexes[0], calcIndexes[1])));
                calcs.put(group, new ControlDirective(angle, speed));
                if (speed > max) {
                    max = speed;
                }
            }
            if (max > 1) {
                for (ControlDirective directive : calcs.values()) {
                    directive.setPower(directive.getPower() / max);
                }
            }
            return calcs;
        };

        private static void control(CompositeJoystick composite, double robotAngle) {
            robotAngle = Math.toRadians(robotAngle);
            double fwd = composite.getY() * Math.cos(robotAngle) + composite.getX() * Math.sin(robotAngle);
            double str = composite.getX() * Math.sin(robotAngle) + composite.getY() * Math.cos(robotAngle);
            double r = Math.sqrt((Dimensions.WHEELBASE * Dimensions.WHEELBASE) + (Dimensions.TRACKWIDTH * Dimensions.TRACKWIDTH));
            A = str - composite.getZ() * (Dimensions.WHEELBASE / r);
            B = str + composite.getZ() * (Dimensions.WHEELBASE / r);
            C = fwd - composite.getZ() * (Dimensions.TRACKWIDTH / r);
            D = fwd + composite.getZ() * (Dimensions.TRACKWIDTH / r);
        }

        private static double[] getCalcIndexes(MotorGroup group) {
            switch (group) {
                case FR:
                    return new double[]{ B, C };
                case FL:
                    return new double[]{ B, D };
                case BL:
                    return new double[]{ A, D };
                case BR:
                    return new double[]{ A, C };
                default:
                    return new double[]{ 0, 0 };
            }
        }
    }

    public static class Tank {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, NavXGyro gyro, MotorGroup... groups) -> {
            MotorMapped<ControlDirective> calcs = new MotorMapped<>();
            for (MotorGroup group : groups) {
                calcs.put(group, new ControlDirective(0.0,
                        calcWheelSpeed(group.lrSide, composite.getX(), composite.getY())));
            }
            return calcs;
        };

        private static double calcWheelSpeed(MotorGroup.Side motorSide, double rotate, double power) {
            double max = Math.max(Math.abs(rotate), Math.abs(power));
            double diff = power - rotate;
            double sum = power + rotate;

            double left;
            double right;

            if (power > 0) {
                if (rotate > 0) {
                    left = max;
                    right = diff;
                } else {
                    left = sum;
                    right = max;
                }
            } else {
                if (rotate > 0) {
                    left = sum;
                    right = -max;
                } else {
                    right = diff;
                    left = -max;
                }
            }

            return motorSide.equals(MotorGroup.Side.LEFT) ? left : right;
        }
    }

    public static class Automobile {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, NavXGyro gyro, MotorGroup... groups) -> {
            MotorMapped<ControlDirective> baseCalc = Crab.CALCULATOR.calculate(composite, gyro, groups);
            baseCalc.get(MotorGroup.BL).setAngle(90);
            baseCalc.get(MotorGroup.BR).setAngle(90);
            return baseCalc;
        };
    }

    public static class Crab {
        public static final DriveCalculator CALCULATOR = (CompositeJoystick composite, NavXGyro gyro, MotorGroup... groups) -> {
            MotorMapped<ControlDirective> calcs = new MotorMapped<>();
            for (MotorGroup group : groups) {
                calcs.put(group, new ControlDirective(calcWheelAngle(group, composite), composite.getY()));
            }
            return calcs;
        };

        public static double calcWheelAngle(MotorGroup group, CompositeJoystick composite) {
            double angle = new Vector2D(composite.getX(), composite.getY()).getAngle();
            if (group.fbSide.equals(MotorGroup.Side.BACK)) {
                angle += 90;
            }
            return angle;
        }
    }
}
