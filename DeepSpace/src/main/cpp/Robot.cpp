/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "Robot.h"

#include "commands/ElevatorSetPosition.h"

#include <frc/commands/Scheduler.h>
#include <frc/smartdashboard/SmartDashboard.h>

#include <iostream>


std::shared_ptr<OI> Robot::m_oi;
std::shared_ptr<BrushlessDrive> Robot::m_driveTrain;
std::shared_ptr<Elevator> Robot::m_elevator;
//std::shared_ptr<Claw> Robot::m_claw;

void Robot::RobotInit() {
  std::cout << "RobotInit Started" << std::endl;

  m_oi.reset(new OI());

  // Initialize Subsystems
  m_driveTrain.reset(new BrushlessDrive());
  m_elevator.reset(new Elevator());
  //m_claw.reset(new Claw());

  frc::SmartDashboard::PutData("Elevator Home", new ElevatorSetPosition(ElevatorSetPosition::Position::HomePosition));
  frc::SmartDashboard::PutData("Cargo Intake", new ElevatorSetPosition(ElevatorSetPosition::Position::CargoIntake));
  frc::SmartDashboard::PutData("Cargo Deposit Level 1", new ElevatorSetPosition(ElevatorSetPosition::Position::CargoDepositLevel1));
  frc::SmartDashboard::PutData("Cargo Deposit Level 2", new ElevatorSetPosition(ElevatorSetPosition::Position::CargoDepositLevel2));
  frc::SmartDashboard::PutData("Cargo Deposit Level 3", new ElevatorSetPosition(ElevatorSetPosition::Position::CargoDepositLevel3));
  frc::SmartDashboard::PutData("Hatch Panel Intake", new ElevatorSetPosition(ElevatorSetPosition::Position::HatchPanelIntake));
  frc::SmartDashboard::PutData("Hatch Deposit Level 1", new ElevatorSetPosition(ElevatorSetPosition::Position::HatchDepositLevel1));
  frc::SmartDashboard::PutData("Hatch Deposit Level 2", new ElevatorSetPosition(ElevatorSetPosition::Position::HatchDepositLevel2));
  frc::SmartDashboard::PutData("Hatch Deposit Level 3", new ElevatorSetPosition(ElevatorSetPosition::Position::HatchPanelIntake));
  frc::SmartDashboard::PutData("Climb Position", new ElevatorSetPosition(ElevatorSetPosition::Position::ClimbPosition));


}

/**
 * This function is called every robot packet, no matter the mode. Use
 * this for items like diagnostics that you want ran during disabled,
 * autonomous, teleoperated and test.
 *
 * <p> This runs after the mode specific periodic functions, but before
 * LiveWindow and SmartDashboard integrated updating.
 */
void Robot::RobotPeriodic() {}

/**
 * This function is called once each time the robot enters Disabled mode. You
 * can use it to reset any subsystem information you want to clear when the
 * robot is disabled.
 */
void Robot::DisabledInit() {
  std::cout << "DisabledInit Started" << std::endl;
}

void Robot::DisabledPeriodic() { frc::Scheduler::GetInstance()->Run(); }

/**
 * This autonomous (along with the chooser code above) shows how to select
 * between different autonomous modes using the dashboard. The sendable chooser
 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
 * remove all of the chooser code and uncomment the GetString code to get the
 * auto name from the text box below the Gyro.
 *
 * You can add additional auto modes by adding additional commands to the
 * chooser code above (like the commented example) or additional comparisons to
 * the if-else structure below with additional strings & commands.
 */
void Robot::AutonomousInit() {
  std::cout << "AutonomousInit Started" << std::endl;
}

void Robot::AutonomousPeriodic() { frc::Scheduler::GetInstance()->Run(); }

void Robot::TeleopInit() {
  std::cout << "TeleopInit Started" << std::endl;
}

void Robot::TeleopPeriodic() { 
  frc::Scheduler::GetInstance()->Run();
}

void Robot::TestPeriodic() {}

#ifndef RUNNING_FRC_TESTS
int main() { return frc::StartRobot<Robot>(); }
#endif