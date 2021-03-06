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

package org.rivierarobotics.driver;

import edu.wpi.first.wpilibj.Joystick;
import org.rivierarobotics.util.MathUtil;

import java.util.HashMap;
import java.util.Map;

public class BoundedJoystick extends Joystick {
    public BoundedJoystick(int port) {
        super(port);
    }

    public double getDimension(char dim) {
        return getXYZMap().get(dim);
    }

    public Map<Character, Double> getXYZMap() {
        Map<Character, Double> xyz = new HashMap<>();
        xyz.put('X', MathUtil.fitDeadband(super.getX()));
        xyz.put('Y', MathUtil.fitDeadband(super.getY()));
        xyz.put('Z', MathUtil.fitDeadband(super.getTwist()));
        return xyz;
    }
}