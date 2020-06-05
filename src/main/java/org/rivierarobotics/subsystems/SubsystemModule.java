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

import dagger.Module;
import dagger.Provides;
import org.rivierarobotics.inject.Corner;
import org.rivierarobotics.util.MotorGroup;

import javax.inject.Singleton;

@Module
public class SubsystemModule {
    private static final int PIGEON_ID = 20;

    private SubsystemModule() {
    }

    @Provides
    @Singleton
    @Corner(MotorGroup.FR)
    public static SwerveModule provideFrontRightSwerveModule() {
        return new SwerveModule(MotorGroup.FR);
    }

    @Provides
    @Singleton
    @Corner(MotorGroup.FL)
    public static SwerveModule provideFrontLeftSwerveModule() {
        return new SwerveModule(MotorGroup.FL);
    }

    @Provides
    @Singleton
    @Corner(MotorGroup.BL)
    public static SwerveModule provideBackLeftSwerveModule() {
        return new SwerveModule(MotorGroup.BL);
    }

    @Provides
    @Singleton
    @Corner(MotorGroup.BR)
    public static SwerveModule provideBackRightSwerveModule() {
        return new SwerveModule(MotorGroup.BR);
    }

    @Provides
    @Singleton
    public static PigeonGyro providePigeonGyro() {
        return new PigeonGyro(PIGEON_ID);
    }
}
