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

package org.rivierarobotics.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import org.rivierarobotics.util.MathUtil;

public class PigeonGyro extends PigeonIMU {
    private boolean needsOdometryReset = false;

    public PigeonGyro(int id) {
        super(id);
    }

    public double getAngle() {
        double[] ypr = new double[3];
        super.getYawPitchRoll(ypr);
        return ypr[0];
    }

    public double getWrappedAngle() {
        return MathUtil.fitToDegCircle(getAngle());
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getAngle());
    }

    public void doReset() {
        needsOdometryReset = true;
        super.setYaw(0);
    }

    public boolean requireOdometryReset() {
        if (needsOdometryReset) {
            needsOdometryReset = false;
            return true;
        }
        return false;
    }
}
